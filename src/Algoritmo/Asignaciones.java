package Algoritmo;

import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.Map.Entry;

public class Asignaciones implements Iterable<Entry<Integer, FamiliasAsignadas>> {

    //                Dia
    private TreeMap<Integer, FamiliasAsignadas> asignaciones;
    private HashMap<Familia, FamiliasAsignadas> map_familias;
    final Integer capacidadMaxima;

    public Asignaciones(Integer totDias, Integer capacidad) {
        this.capacidadMaxima = capacidad;
        this.asignaciones = new TreeMap<>();
        this.map_familias = new HashMap<>();
        for (int i = 1; i <= totDias; i++)
            this.asignaciones.put(i, new FamiliasAsignadas(i));
    }

    public boolean asignar(Integer dia, Familia f) {
        FamiliasAsignadas familias = this.asignaciones.get(dia);
        map_familias.put(f, familias);
        return familias.totPersonas() + f.miembros() <= this.capacidadMaxima ? familias.asignar(f) : false;
    }

    public void desasignar(Familia f) {
        FamiliasAsignadas asignada = map_familias.put(f, null);
        asignada.desasignar(f);
    }

    public FamiliasAsignadas asignacionEn(int dia) {
        return this.asignaciones.get(dia);
    }

    public Integer totCosto() {
        Integer totCosto = 0;
        for (FamiliasAsignadas a : this.asignaciones.values()) {
            totCosto += a.totCosto();
        }
        return totCosto;
    }

    public Integer totFamilias() {
        Integer totFamilias = 0;
        for (FamiliasAsignadas a : this.asignaciones.values()) {
            totFamilias += a.totFamilias();
        }
        return totFamilias;
    }

    public void totPersonasPorDia() {
        for (Integer dia : this.asignaciones.keySet()) {
            System.out.printf("| %3d | %3d |\n", dia, this.asignaciones.get(dia).totPersonas());
        }
    }

    @Override
    public Iterator<Entry<Integer, FamiliasAsignadas>> iterator() {
        return this.asignaciones.entrySet().iterator();
    }
}