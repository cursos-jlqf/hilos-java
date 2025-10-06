import java.util.Arrays;
import java.util.Random;

/**
 * Calcula la suma de los elementos de un arreglo usando múltiples hilos.
 *
 * El arreglo se divide en partes iguales entre los hilos. Cada hilo calcula la suma
 * parcial de su bloque y, en caso de existir elementos sobrantes, algunos hilos
 * suman un elemento adicional desde el final del arreglo. El hilo principal espera
 * a que todos terminen y acumula el resultado total.
 *
 * Uso: java SumarArregloThread <N> <HILOS>
 *   N: tamaño del arreglo (entero positivo)
 *   HILOS: número de hilos (entero positivo)
 */
public class SumarArregloThread extends Thread {

    /** Tamaño del arreglo a sumar. Se establece desde los argumentos. */
    static int N = 1;

    /** Número de hilos a utilizar. Se establece desde los argumentos. */
    static int HILOS = 1;

    /** Arreglo con las sumas parciales por hilo (índice = id del hilo). */
    static int[] resultado = null;

    /** Arreglo de datos a sumar. */
    static int[] arreglo = null;

    /** Identificador lógico del hilo (0..HILOS-1). */
    private final int id;

    /**
     * Crea un hilo trabajador para sumar una porción del arreglo.
     *
     * @param id identificador lógico del hilo (0..HILOS-1)
     */
    public SumarArregloThread(int id) {
        this.id = id;
        setName("Trabajador-" + id);
    }

    /**
     * Ejecuta la tarea del hilo.
     * Calcula la suma parcial de una porción del arreglo, considerando
     * un bloque base y un posible elemento extra si hay resto.
     * El resultado parcial se almacena en el arreglo global resultado.
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

        // Reparto del resto: los primeros "resto" hilos toman un elemento adicional desde el final
        if (id < resto) {
            int idxExtra = (arreglo.length - 1) - id;
            suma += arreglo[idxExtra];
        }

        System.out.println("Hilo " + id
                + " (ThreadId=" + Thread.currentThread().threadId()
                + ", Nombre=" + getName()
                + ") Suma Local: " + suma);

        resultado[id] = suma;
    }

    /**
     * Llena el arreglo con valores aleatorios entre 0 y maxValor - 1.
     *
     * @param arreglo  referencia al arreglo a inicializar
     * @param maxValor valor máximo (exclusivo) para los números aleatorios
     */
    static void inicializarArreglo(int[] arreglo, int maxValor) {
        Random azar = new Random();
        for (int i = 0; i < arreglo.length; i++) {
            arreglo[i] = azar.nextInt(Math.max(1, maxValor));
        }
    }

    /**
     * Imprime el contenido del arreglo en consola.
     *
     * @param arreglo arreglo a imprimir
     */
    static void imprimir(int[] arreglo) {
        System.out.println(Arrays.toString(arreglo));
    }

    /**
     * Procesa y valida los parámetros de línea de comandos.
     * Asigna los valores a N y HILOS, y termina el programa si hay error.
     *
     * @param args argumentos del programa: args[0]=N, args[1]=HILOS
     */
    static void parsearParametros(String[] args) {
        if (args == null || args.length < 2) {
            System.out.println("Uso: java SumarArregloThread <N> <HILOS>");
            System.exit(1);
        }
        try {
            N = Integer.parseInt(args[0]);
            HILOS = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.out.println("Error: no es posible convertir a entero.");
            System.exit(2);
        }
        if (N <= 0 || HILOS <= 0) {
            System.out.println("Error: N y HILOS deben ser enteros positivos.");
            System.exit(3);
        }
    }

    /**
     * Método principal del programa.
     * 1. Lee los parámetros N y HILOS.
     * 2. Inicializa los arreglos y lanza los hilos.
     * 3. Espera a que todos terminen y muestra la suma total.
     *
     * @param args argumentos del programa: args[0]=N, args[1]=HILOS
     */
    public static void main(String[] args) {
        parsearParametros(args);

        arreglo = new int[N];
        resultado = new int[HILOS];
        Thread[] trabajadores = new Thread[HILOS];

        inicializarArreglo(arreglo, 100);
        imprimir(arreglo);

        for (int i = 0; i < HILOS; i++) {
            trabajadores[i] = new SumarArregloThread(i);
            trabajadores[i].start();
        }

        int sumaFinal = 0;
        for (int i = 0; i < HILOS; i++) {
            try {
                trabajadores[i].join();
                sumaFinal += resultado[i];
            } catch (InterruptedException e) {
                System.out.println("Error: interrupción durante la espera del hilo " + i);
            }
        }

        System.out.println("Suma total: " + sumaFinal);
    }
}
