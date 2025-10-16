import java.util.Arrays;
import java.util.Random;

/**
 * Calcula la suma de matrices usando múltiples hilos.
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
public class SumaMatrizThread extends Thread {

    /** Tamaño del arreglo a sumar. Se establece desde los argumentos. */
    static int N = 1;

    /** Número de hilos a utilizar. Se establece desde los argumentos. */
    static int HILOS = 1;

    /** Arreglo de datos a sumar. */
    static int[][] a = null;
    static int[][] b = null;
    static int[][] c = null;


    /** Identificador lógico del hilo (0..HILOS-1). */
    private final int id;

    /**
     * Crea un hilo trabajador para sumar una porción del arreglo.
     *
     * @param id identificador lógico del hilo (0..HILOS-1)
     */
    public SumaMatrizThread(int id) {
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


        for (int i = id; i < c.length; i=i+HILOS) {
            for (int j = 0; j < c.length; j++) {
                c[i][j]= a[i][j]+b[i][j];
            }
        }



        System.out.println("Hilo " + id
                + " (ThreadId=" + Thread.currentThread().threadId()
                + ", Nombre=" + getName());


    }
    

    /**
     * Llena el arreglo con valores aleatorios entre 0 y maxValor - 1.
     *
     * @param arreglo  referencia al arreglo a inicializar
     * @param maxValor valor máximo (exclusivo) para los números aleatorios
     */
    static void inicializarMatriz(int[][] m, int maxValor) {
        Random azar = new Random();
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m.length; j++) {
                m[i][j] = azar.nextInt(Math.max(1, maxValor));
           }
        }
    }

    /**
     * Imprime el contenido del arreglo en consola.
     *
     * @param arreglo arreglo a imprimir
     */
    static void imprimir(int[][] m) {
        for (int i = 0; i < m.length; i++) {
            System.out.println(Arrays.toString(m[i]));
        }
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

        a = new int[N][N];
        b = new int[N][N];
        c = new int[N][N];
        

        Thread[] trabajadores = new Thread[HILOS];

        inicializarMatriz(a, 100);
        inicializarMatriz(b, 100);
        //imprimir(a);
        System.out.println("");
	//imprimir(b);
        for (int i = 0; i < HILOS; i++) {
            trabajadores[i] = new SumaMatrizThread(i);
            trabajadores[i].start();
        }

        for (int i = 0; i < HILOS; i++) {
            try {
                trabajadores[i].join();
            } catch (InterruptedException e) {
                System.out.println("Error: interrupción durante la espera del hilo " + i);
            }
        }
	System.out.println("Fin!");
        //imprimir(c);
    }
}
