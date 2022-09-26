package Algoritmo;

public class Bono {

    public static final Integer PAGA_DESDE_PREFERENCIA = 1;
    private static Integer TOT_PREFERENCIAS;
    private static int matrizBonos[][];

    private Bono() {}
    
    public static Integer valor(int gf, int preferencia) {
        return matrizBonos[gf][preferencia];
    }

    public static void inicializarBonos(int gfMin, int gfMax, int preferencias) {
        /* Matriz con los bonos a pagar */
        TOT_PREFERENCIAS = preferencias;
        matrizBonos = new int[gfMax+1][TOT_PREFERENCIAS];

        // inicializo bonos 0
        for (int i = 0; i < gfMin; i++) {
            for (int k = 0; k < PAGA_DESDE_PREFERENCIA; k++) {
                matrizBonos[i][k] = 0;
            }
        }
        // inicializo bonos con valor
        for (int i = gfMin; i <= gfMax; i++) { // calculo todos los bonos pagables
            for (int k = PAGA_DESDE_PREFERENCIA; k < TOT_PREFERENCIAS; k++) {
                int bono = 25 + (10 * i) + (5 * k);
                matrizBonos[i][k] = bono;
            }
        }
    }
}