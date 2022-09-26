package Algoritmo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NavigableMap;
import java.util.TreeMap;

public class Candidatos implements Iterable<Familia> {
    private TreeMap<Integer, ArrayList<Familia>> familias;
    private int size;

    public Candidatos() {
        this.familias = new TreeMap<>();
        this.size = 0;
    }

    public int size() { return this.size; }
    public boolean isEmpty() { return this.size == 0; }

    public void add(Familia f) {
        if (!familias.containsKey(f.miembros())) {
            familias.put(f.miembros(), new ArrayList<>());
        }
        familias.get(f.miembros()).add(f);
        this.size++;
    }
    public void remove(Familia f) {
        if (familias.get(f.miembros()).remove(f))
            this.size--;
        else System.out.println("¿!¿! NO SE PUDO ELIMINAR ?!?!");
    }

    public NavigableMap<Integer, ArrayList<Familia>> familiasPorGfAscendente() {
        return this.familias;
    }
    // public NavigableMap<Integer, ArrayList<Familia>> familiasPorGfDescendente() {
    //     return this.familias.descendingMap();
    // }

    public int gfMayor() {
        return familias.lastKey();
    }

    public int gfMenor() {
        return familias.firstKey();
    }

    public Familia get() {
        return this.familias.get(this.familias.firstKey()).get(0);
    }

    @Override
    public Iterator<Familia> iterator() {
        HashSet<Familia> todas = new HashSet<>();

        for (ArrayList<Familia> fs : familias.values())
            todas.addAll(fs);

        return todas.iterator();
    }

}