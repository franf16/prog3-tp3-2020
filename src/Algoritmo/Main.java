package Algoritmo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.NavigableMap;
import java.util.Map.Entry;

public class Main {

    final static int DIAS = 100;
    final static int CAPACIDAD = 340;
    final static int TOT_PREFERENCIAS = 8;
    final static int dias[] = new int[DIAS + 1];
    public static void main(String... args) {

        CSVReader reader = new CSVReader("./data/familias.csv");

        Candidatos candidatos = reader.read();

        // System.out.println(candidatos.size());
        
        final int GF_MAYOR = candidatos.gfMayor();
        final int GF_MENOR = candidatos.gfMenor();

        Bono.inicializarBonos(GF_MENOR, GF_MAYOR, TOT_PREFERENCIAS);

        Asignaciones A = new Asignaciones(DIAS, CAPACIDAD);

        System.out.println("Iniciando primer asignacion...");
        /* Asigno las familias cuyas primeras preferencias no sobrepasan la capacidad
         * i) encuentro que dias no se sobrepasan */
        for (Familia f : candidatos) {
            dias[f.diaPreferido()] += f.miembros();
        }
        /* ii) agrego las familias con primer preferencia en los dias que no se sobrepasan */
        for (Familia f : candidatos) {
            if (dias[f.diaPreferido()] <= CAPACIDAD) {
                A.asignar(f.diaPreferido(), f);
                candidatos.remove(f);
            }
        }
        System.out.printf("#1 asignadas %d familias\n", A.totFamilias());
        /* iii) reseteo los dias sobrepasados, en los que no se asignaron familias */
        for (int i = 1; i < DIAS+1; i++) {
            if (dias[i] > CAPACIDAD) dias[i] = 0;
        }
        // System.out.println("Personas asignadas por dia:");
        // for (int i = 1; i < DIAS+1; i++) System.out.printf("| %2d | %3d |\n", i, dias[i]);

        System.out.println("#2 asignando familias faltantes por orden de preferencia");
        /**
         * Asigno las demás familias por orden de preferencia.
         * Recorro las familias por grupo familiar ascendente, para asignar menos bonos posibles 
         * A cada iteración ordeno las familias segun que tan ocupado tienen su dia de preferencia siguiente */
        int preferenciaAsignada = 0;
        while (!candidatos.isEmpty() && preferenciaAsignada < TOT_PREFERENCIAS) {

            NavigableMap<Integer, ArrayList<Familia>> familiasPorGf = candidatos.familiasPorGfAscendente();

            // int contadorAsignadas = 0, contadorIteraciones = 0;

            for (ArrayList<Familia> familias : familiasPorGf.values()) { // contadorIteraciones++;

                if (preferenciaAsignada < TOT_PREFERENCIAS-1)
                    Collections.sort(familias, new OrdenadorFamilias(preferenciaAsignada, dias));

                HashSet<Familia> asignadas = new HashSet<>();
                
                for (Familia f : familias) { // contadorIteraciones++;

                    Integer diaAsignado = f.preferenciaEn(preferenciaAsignada);
                    if (A.asignar(diaAsignado, f)) {
                        asignadas.add(f);
                        dias[diaAsignado] += f.miembros();
                    }
                }

                // contadorAsignadas += asignadas.size();

                for (Familia f : asignadas) candidatos.remove(f);
            }
            
            // System.out.printf("-- Preferencia %d, asignadas %d familias. Quedan %d familias. Tot costo: %d\n",
            //                   preferenciaAsignada, contadorAsignadas, candidatos.size(), A.totCosto());
            preferenciaAsignada++;
        }
        System.out.printf("Fin primera asignacion: tot costo: %d, tot familias: %d\n", A.totCosto(), A.totFamilias());

        // Integer contador = 0;
        // Integer intercambios = 0;
        System.out.printf("\nIniciando reasignacion\n");
        /**
         * Recorro las familias que pagan bono y les asigno un dia de preferencia menor, 
         * si encuentro una familia en esos dias que pague un bono menor
         */
        // Integer encontrados = 0;
        //          DIA       asignaciones
        for (Entry<Integer, FamiliasAsignadas> asignacion : A) {

            // iterar sobre cada familia con una preferencia asignada > 0
            HashSet<Familia> familiasQuePaganBono = asignacion.getValue().familiasAsignadasDesde(Bono.PAGA_DESDE_PREFERENCIA);

            for (Familia familiaAIntercambiar : familiasQuePaganBono) {

                Integer diaAsignado = familiaAIntercambiar.preferenciaEn(familiaAIntercambiar.preferenciaAsignada());
                Integer costoFamiliaAIntercambiar = familiaAIntercambiar.bonoAsignado();

                Integer diaAIntercambiar = -1; // dia al que asignar familiaAIntercambiar
                boolean encontrado = false; // si fue encontrado un sustituto
                Familia familiaMenor = null; // la familia a intercambiar
                Integer diaMenor = -1; // el dia a asignar a la familia a intercamiar
                Integer diferenciaMayor = 0; // ahorro intercambiando

                /* por cada dia de preferencia menor, recorrer las familias desde las que les fueron asignadas preferencias mayores (pagan bono),
                 * y encontrar el mayor ahorro si se pasa una de esas familias a alguno de sus dias de preferencia mayor */
                for (Integer desdePreferencia = familiaAIntercambiar.preferenciaAsignada()-1; desdePreferencia >= 0; desdePreferencia--) { // contador++;

                    Integer diaIntercambiable = familiaAIntercambiar.preferenciaEn(desdePreferencia);
                    Integer desdeGf = Math.abs(CAPACIDAD - dias[diaIntercambiable] - familiaAIntercambiar.miembros()); /* desde que grupo familiar buscar sustitutos */
                    ArrayList<Familia> familiasIntercambiables = A.asignacionEn(diaIntercambiable).familiasConPrefDescendenteDesde(desdeGf);

                    boolean terminado = false; /* para cortar si llegase a encontrar una familia con pref 0 y diferencia mayor en dia de preferencia 1 (no hace falta mirar grupos familiares mayores) */
                    for (Familia intercambiable : familiasIntercambiables) { // contador++;
                        if (terminado) break;
                        Integer nuevaPreferencia = intercambiable.preferenciaAsignada()+1;
                        Integer costoFamiliaIntercambiable = intercambiable.bonoAsignado();
                        Integer sumaCostos = costoFamiliaAIntercambiar + costoFamiliaIntercambiable;
                        Integer nuevaDiferencia = sumaCostos 
                                                - Bono.valor(intercambiable.miembros(), nuevaPreferencia) 
                                                - Bono.valor(familiaAIntercambiar.miembros(), desdePreferencia);
                        // recorro las preferencias mayores de la familia intercambiable para ver si moviendola a otro dia pagaria un bono menor
                        while (nuevaDiferencia > diferenciaMayor) { // contador++;

                            int nuevoDia = intercambiable.preferenciaEn(nuevaPreferencia);
                            Integer cantidadIntercambiando = dias[nuevoDia] + desdeGf;

                            if (nuevoDia == diaAsignado) cantidadIntercambiando -= familiaAIntercambiar.miembros();
                            
                            if (cantidadIntercambiando <= CAPACIDAD) {
                                /* se encontro una familia que pagaria un bono menor */
                                // encontrados++;
                                diaAIntercambiar = familiaAIntercambiar.preferenciaEn(desdePreferencia);
                                encontrado = true;
                                familiaMenor = intercambiable;
                                diaMenor = nuevoDia;
                                diferenciaMayor = nuevaDiferencia;

                                if (familiaMenor.preferenciaAsignada() == 0
                                        && nuevaPreferencia < Bono.PAGA_DESDE_PREFERENCIA // si hubiese mas dias que no pagan bono
                                        || nuevaPreferencia == Bono.PAGA_DESDE_PREFERENCIA
                                                && Bono.PAGA_DESDE_PREFERENCIA == 1)
                                    terminado = true;
                                break; /* no hay dia que pague un menor bono entre sus preferencias siguientes */
                            }
                            nuevaPreferencia++;
                            nuevaDiferencia = sumaCostos
                                            - Bono.valor(intercambiable.miembros(), nuevaPreferencia)
                                            - Bono.valor(familiaAIntercambiar.miembros(), desdePreferencia);
                        }
                    }
                }

                if (encontrado) {
                    // System.out.printf("Intercambiando pref %d por %d\n", familiaAIntercambiar.preferenciaAsignada(), familiaMenor.preferenciaAsignada());
                    // System.out.printf("Bono viejo: %d , Bono nuevo: %d\n", 
                    //                   familiaAIntercambiar.bonoAsignado(), 
                    //                   Bono.valor(familiaMenor.miembros(), familiaMenor.indiceDePreferencia(nuevoDiaMenor)));

                    A.desasignar(familiaAIntercambiar);
                    dias[diaAsignado] -= familiaAIntercambiar.miembros();
                    A.desasignar(familiaMenor);
                    dias[diaAIntercambiar] -= familiaMenor.miembros();
                    A.asignar(diaAIntercambiar, familiaAIntercambiar);
                    dias[diaAIntercambiar] += familiaAIntercambiar.miembros();

                    A.asignar(diaMenor, familiaMenor);
                    dias[diaMenor] += familiaMenor.miembros();
                    // intercambios++;
                }
            }
        }

        // System.out.printf("Terminado. %d iteraciones, %d intercambios\n", contador, intercambios);
        System.out.printf("Tot costo: %d, Tot familias: %d\n", A.totCosto(), A.totFamilias());

        // A.totPersonasPorDia();
    }
}