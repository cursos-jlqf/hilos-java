import java.util.Arrays;
import java.util.Random;

/**
 * Calcula la suma de los elementos de un arreglo de forma secuencial.
 * Es la versión base para comparar con la versión paralela.
 */
public class SumarArregloSecuencial {

    /**
     * Llena el arreglo con valores aleatorios entre 0 y maxValor - 1.
     *
     * @param arreglo  referencia al arreglo a inicializar
     * @param maxValor valor máximo (exclusivo)
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
     * Calcula la suma total de un arreglo de forma secuencial.
     *
     * @param arreglo arreglo de números enteros
     * @return suma total
     */
    static int sumarArreglo(int[] arreglo) {
        int suma = 0;
        for (int valor : arreglo) {
            suma += valor;
        }
        return suma;
    }

    /**
     * Método principal. Genera el arreglo, lo imprime y calcula la suma total.
     *
     * @param args args[0] = tamaño del arreglo (opcional)
     */
    public static void main(String[] args) {
        int N = 10;
        if (args.length > 0) {
            N = Integer.parseInt(args[0]);
        }

        int[] arreglo = new int[N];
        inicializarArreglo(arreglo, 100);
        imprimir(arreglo);

        int suma = sumarArreglo(arreglo);
        System.out.println("Suma total (secuencial): " + suma);
    }
}
