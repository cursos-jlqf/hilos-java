import java.util.Arrays;
import java.util.Random;

/**
 * La clase Matriz proporciona métodos para inicializar, sumar y multiplicar matrices.
 * También permite la multiplicación de matrices utilizando múltiples hilos para 
 * mejorar el rendimiento en matrices grandes.
 */
public class Matriz {

    static final int MAX = 10000; // Tamaño máximo por defecto de las matrices
    static int N = 2; // Tamaño por defecto de la matriz
    static int HILOS = 2; // Número de hilos por defecto
    double[][] a = null, b = null, c = null; // Matrices de entrada y resultado

    /**
     * Constructor que inicializa la clase con matrices dadas.
     * 
     * @param a Matriz A
     * @param b Matriz B
     * @param c Matriz C (resultado)
     */
    public Matriz(double[][] a, double[][] b, double[][] c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    /**
     * Constructor por defecto que inicializa matrices con tamaño máximo.
     */
    public Matriz() {
        this(new double[MAX][MAX], new double[MAX][MAX], new double[MAX][MAX]);
    }

    /**
     * Inicializa una matriz con un valor dado.
     * 
     * @param m Matriz a inicializar
     * @param n Valor con el que se llenará la matriz
     */
    public void inicializarMatriz(double[][] m, double n) {
        Random azar = new Random();
        int i, j;
        for (i = 0; i < m.length; i++) {
            for (j = 0; j < m[0].length; j++) {
                m[i][j] = n; // Se puede modificar para inicializar con valores aleatorios
            }
        }
    }

    /**
     * Imprime una matriz en la consola.
     * 
     * @param m Matriz a imprimir
     */
    public void imprimir(double[][] m) {
        System.out.println("");
        for (int i = 0; i < m.length; i++)
            System.out.println(Arrays.toString(m[i]));
    }

    /**
     * Realiza la suma de las matrices A y B, almacenando el resultado en la matriz C.
     */
    public void sumar() {
        int i, j;
        for (i = 0; i < c.length; i++)
            for (j = 0; j < c[0].length; j++)
                c[i][j] = a[i][j] + b[i][j];
    }

    /**
     * Multiplica matrices utilizando múltiples hilos.
     * 
     * @param runnables Arreglo de tareas que realizan la multiplicación
     */
    public void multiplicar(Runnable[] runnables) {
        Thread[] threads = new Thread[runnables.length];

        for (int i = 0; i < runnables.length; i++) {
            threads[i] = new Thread(runnables[i]);
            threads[i].start();
        }

        // Esperar a que todos los hilos terminen
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Realiza la multiplicación parcial de matrices utilizando un hilo específico.
     * 
     * @param id Identificador del hilo
     */
    public void multiplicar(int id) {
        int i, j, k;
        for (i = id; i < c.length; i = i + HILOS) {
            for (j = 0; j < c[0].length; j++) {
                c[i][j] = 0; // Inicializa el valor en la matriz de resultado
                for (k = 0; k < a[0].length; k++) {
                    c[i][j] += a[i][k] * b[k][j];
                }
            }
        }
    }

    /**
     * Realiza la multiplicación de las matrices A y B de manera secuencial,
     * almacenando el resultado en la matriz C.
     */
    public void multiplicar() {
        int i, j, k;
        for (i = 0; i < c.length; i++)
            for (j = 0; j < c[0].length; j++)
                for (k = 0; k < a[0].length; k++)
                    c[i][j] += a[i][k] * b[k][j];
    }

    /**
     * Método principal que inicializa las matrices, ejecuta la multiplicación 
     * secuencial y paralela, y mide el tiempo de ejecución de cada una.
     * 
     * @param args Argumentos de línea de comandos: tamaño de la matriz y número de hilos
     */
    public static void main(String[] args) {

        double[][] a, b, c;
        Matriz matriz;
        long tinicio = 0, tfin = 0;

        try {
            N = Integer.parseInt(args[0]);
            HILOS = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.out.println("Error: No es posible convertir a entero");
            System.exit(0);
        }

        a = new double[N][N];
        b = new double[N][N];
        c = new double[N][N];
        Runnable[] tareas = new Runnable[HILOS];

        matriz = new Matriz(a, b, c);

        matriz.inicializarMatriz(a, 1.0);
        matriz.inicializarMatriz(b, 1.0);

        // Multiplicación secuencial
        tinicio = System.currentTimeMillis();
        matriz.multiplicar();
        tfin = System.currentTimeMillis();
        System.out.println("\nTiempo Secuencial: " + (tfin - tinicio) + " ms, Matriz " + c.length + "x" + c[0].length);

        // Preparar y ejecutar la multiplicación paralela
        for (int i = 0; i < HILOS; i++) {
            final int id = i;
            tareas[i] = () -> matriz.multiplicar(id);
        }

        tinicio = System.currentTimeMillis();
        matriz.multiplicar(tareas);
        tfin = System.currentTimeMillis();
        System.out.println("\nTiempo Paralelo: " + (tfin - tinicio) + " ms, Matriz " + c.length + "x" + c[0].length);
    }
}
