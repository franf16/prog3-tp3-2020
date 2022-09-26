package Algoritmo;

import java.util.Arrays;

/* Una familia, con su cantidad de dias, y una arreglo con el top de 4 dias preferidos */
public class Familia implements Comparable<Familia> {

    private int id;
    private int miembros;
    private int[] diasPreferidos;
    private Asignacion asignacion;

    public Familia(int id, int miembros, int... diasPreferidos) {
        this.id = id;
        this.miembros = miembros;
        this.diasPreferidos = diasPreferidos;
        this.asignacion = new Asignacion();
    }

    public void asignar(Asignacion a) { this.asignacion = a; }
    public Integer preferenciaAsignada() { return this.indiceDePreferencia(this.asignacion.dia()); }
    public Integer bonoAsignado() { return this.asignacion.bono(); }

    public int totPreferencias() { return this.diasPreferidos.length; }

    /* Id de la familia */
    public int getId() {
        return id;
    }

    /* Retorna la cantidad de miembros de la familia. */
    public int miembros() {
        return miembros;
    }

    /*
     * Dado un indice entre 0 y 4, retorna el d�a preferido por la familia para ese
     * indice.
     */
    public int preferenciaEn(int indice) {
        return this.diasPreferidos[indice];
    }

    /* Retorna el d�a preferido de la familia */
    public int diaPreferido() {
        return preferenciaEn(0);
    }

    /*
     * Dado un dia pasado por parametro, indica el orden de ese dia en el top 5 de
     * preferencias. Si el dia no forma parte de las preferencias del usuario, se
     * retorna -1.
     */
    public int indiceDePreferencia(int dia) {
        for (int indice = 0; indice < diasPreferidos.length; indice++)
            if (dia == diasPreferidos[indice])
                return indice;
        return -1;
    }

    @Override
    public String toString() {
        return "Familia: id=" + id + ", miembros=" + miembros + ", preferencias=" + Arrays.toString(diasPreferidos);
    }

    @Override
    public int compareTo(Familia f) {
        return this.miembros() == f.miembros() ? 0 : this.miembros() > f.miembros() ? 1 : -1; // miembros ascendente
    }
}
