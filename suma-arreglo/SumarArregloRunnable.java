import java.util.Arrays;
import java.util.Random;

/**
 * Calcula la suma de los elementos de un arreglo usando múltiples hilos
 * implementando la interfaz Runnable.
 */
public class SumarArregloRunnable implements Runnable {

    static int N = 1;
    static int HILOS = 1;
    static int[] resultado = null;
    static int[] arreglo = null;
    private final int id;

    /**
     * Constructor del trabajador.
     *
     * @param id identificador lógico del hilo
     */
    public SumarArregloRunnable(int id) {
        this.id = id;
    }

    /**
     * Calcula la suma parcial correspondiente a este hilo.
     */
    @Override
    public void run() {
        int suma = 0;
        int tam = arreglo.length / HILOS;
        int resto = arreglo.length % HILOS;
        int ini = id * tam;
        int fin = ini + tam;

        for (int i = ini; i < fin; i++) {
            suma += arreglo[i];
        }
        if (id < resto) {
            int idxExtra = (arreglo.length - 1) - id;
            suma += arreglo[idxExtra];
        }

        resultado[id] = suma;
        System.out.println("Hilo " + id + " suma local: " + suma);
    }

    static void inicializarArreglo(int[] arreglo, int maxValor) {
        Random azar = new Random();
        for (int i = 0; i < arreglo.length; i++) {
            arreglo[i] = azar.nextInt(Math.max(1, maxValor));
        }
    }

    static void imprimir(int[] arreglo) {
        System.out.println(Arrays.toString(arreglo));
    }

    /**
     * Método principal. Crea los hilos y ejecuta la suma concurrente.
     *
     * @param args args[0]=N, args[1]=HILOS
     */
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Uso: java SumarArregloRunnable <N> <HILOS>");
            System.exit(1);
        }

        N = Integer.parseInt(args[0]);
        HILOS = Integer.parseInt(args[1]);
        arreglo = new int[N];
        resultado = new int[HILOS];

        inicializarArreglo(arreglo, 100);
        imprimir(arreglo);

        Thread[] trabajadores = new Thread[HILOS];
        for (int i = 0; i < HILOS; i++) {
            Runnable tarea = new SumarArregloRunnable(i);
            trabajadores[i] = new Thread(tarea, "Trabajador-" + i);
            trabajadores[i].start();
        }

        int sumaFinal = 0;
        for (int i = 0; i < HILOS; i++) {
            try {
                trabajadores[i].join();
                sumaFinal += resultado[i];
            } catch (InterruptedException e) {
                System.out.println("Error en la espera del hilo " + i);
            }
        }

        System.out.println("Suma total (Runnable): " + sumaFinal);
    }
}
