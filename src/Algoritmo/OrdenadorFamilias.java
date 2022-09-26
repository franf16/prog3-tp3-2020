package Algoritmo;

import java.util.Comparator;

public class OrdenadorFamilias implements Comparator<Familia> {

    private Integer preferenciaAsignada;
    private int capacidadDias[];

    public OrdenadorFamilias(int p, int d[]) {
        this.preferenciaAsignada = p;
        this.capacidadDias = d;
    }

    @Override
    public int compare(Familia f1, Familia f2) {
        int f1_siguienteDia = f1.preferenciaEn(preferenciaAsignada+1);
        int f2_siguienteDia = f2.preferenciaEn(preferenciaAsignada+1);

        return capacidadDias[f1_siguienteDia] == capacidadDias[f2_siguienteDia] ? 0
                : capacidadDias[f1_siguienteDia] < capacidadDias[f2_siguienteDia] ? 1 : -1;
    }
}