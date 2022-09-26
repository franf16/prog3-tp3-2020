package Algoritmo;

public class Asignacion {

    private Integer dia;
    private Integer valorBono;

    public Asignacion() {
        this.dia = -1;
        this.valorBono = -1;
    }
    public Asignacion(Integer d, Integer v) {
        this.dia = d;
        this.valorBono = v;
    }

    public Integer dia() { return this.dia; }
    public Integer bono() { return this.valorBono; }

}