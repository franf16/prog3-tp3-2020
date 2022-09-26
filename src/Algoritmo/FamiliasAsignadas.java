package Algoritmo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeMap;

public class FamiliasAsignadas {
    
    //           Preferencia         Grupo
    //             asignada         familiar
    private TreeMap<Integer, TreeMap<Integer, HashSet<Familia>>> familias;
    Integer personas;
    Integer costo;
    Integer dia;

    public FamiliasAsignadas(int d) {
        this.familias = new TreeMap<>();
        this.personas = 0;
        this.costo = 0;
        this.dia = d;
    }

    public boolean asignar(Familia f) {
        Integer preferenciaAsignada = f.indiceDePreferencia(this.dia);

        if (!this.familias.containsKey(preferenciaAsignada)) 
            this.familias.put(preferenciaAsignada, new TreeMap<>());

        if (!this.familias.get(preferenciaAsignada).containsKey(f.miembros())) 
            this.familias.get(preferenciaAsignada).put(f.miembros(), new HashSet<>());

        boolean asignada = this.familias.get(preferenciaAsignada).get(f.miembros()).add(f);
        if (asignada) {
            Integer valorBono = Bono.valor(f.miembros(), f.indiceDePreferencia(this.dia));
            f.asignar(new Asignacion(this.dia, valorBono));
            this.personas += f.miembros();
            this.costo += valorBono;
        }
        else {
            System.out.println("FAMILIA DUPLICADA");
        }

        return asignada;
    }

    public void desasignar(Familia f) {
        Integer preferenciaAsignada = f.indiceDePreferencia(this.dia);
        this.familias.get(preferenciaAsignada).get(f.miembros()).remove(f);
        if (this.familias.get(preferenciaAsignada).get(f.miembros()).size() == 0) 
            this.familias.get(preferenciaAsignada).remove(f.miembros());
        if (this.familias.get(preferenciaAsignada).size() == 0)
            this.familias.remove(preferenciaAsignada);
        this.personas -= f.miembros();
        this.costo -= f.bonoAsignado();
        f.asignar(new Asignacion());
    }

    public TreeMap<Integer, TreeMap<Integer, HashSet<Familia>>> familiasAsignadas() {
        return this.familias;
    }

    public HashSet<Familia> familiasAsignadasDesde(Integer pref) {
        HashSet<Familia> todas = new HashSet<>();
         for (TreeMap<Integer, HashSet<Familia>> familiasConPref : this.familias.tailMap(pref).values()) {
            for (HashSet<Familia> fs : familiasConPref.values())
                todas.addAll(fs);
         }
         return todas;
    }
    public ArrayList<Familia> familiasConPrefDescendenteDesde(Integer desdeGf) {
        ArrayList<Familia> todas = new ArrayList<>();
        for (Integer preferencia : this.familias.descendingKeySet()) {
            for (HashSet<Familia> familiasConPreferencia : this.familias.get(preferencia).tailMap(desdeGf).values()) {
                todas.addAll(familiasConPreferencia);
            }
        }
        return todas;
    }
    
    public Integer totPersonas() { return this.personas; }
    public Integer totCosto() { return this.costo; }
    public Integer totFamilias() {
        Integer totFamilias = 0;
        for (Integer pref : this.familias.keySet()) {
            for (HashSet<Familia> familiasPorGF : this.familias.get(pref).values()) {
                totFamilias += familiasPorGF.size();
            }
        }
        return totFamilias;
    }

    @Override
    public String toString() {
        String res = "";
        for (Integer pref : this.familias.keySet()) {
            res += String.format("| %d | ", pref);
            for (HashSet<Familia> familiasPorGF : this.familias.get(pref).values()) {
                res += String.format("%d | ", familiasPorGF.size());
            }
            res += "\n";
        }
        return res;
    }
}