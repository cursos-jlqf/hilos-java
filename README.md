# ¿Qué es un hilo en Java?

Un **hilo** (*thread*) es una unidad de ejecución independiente dentro de un programa.  
Permite que diferentes tareas se realicen **de forma concurrente**, aprovechando mejor los recursos del sistema.

En Java, cada programa inicia con **al menos un hilo**, llamado **hilo principal** (`main`), pero puedes crear más para ejecutar tareas en paralelo.

---

# Cómo crear un hilo

Existen dos formas principales de crear un hilo en Java:

### 1. Extendiendo la clase `Thread`
```java
class MiHilo extends Thread {
    public void run() {
        System.out.println("Hola desde el hilo " + getName());
    }
}

public class EjemploThread {
    public static void main(String[] args) {
        MiHilo hilo1 = new MiHilo();
        hilo1.start(); // inicia el hilo

        System.out.println("Hola desde el hilo principal");
    }
}
```
### 2. Implementando la interfaz Runnable
```java
class MiTarea implements Runnable {
    public void run() {
        System.out.println("Hola desde un hilo Runnable");
    }
}

public class EjemploRunnable {
    public static void main(String[] args) {
        Thread hilo = new Thread(new MiTarea());
        hilo.start(); // inicia el hilo

        System.out.println("Hola desde el hilo principal");
    }
}
```
# Cómo ejecutar el programa
Guarda el primer código  en un archivo, por ejemplo EjemploThread.java, y compílalo:
```bash
javac EjemploThread.java
```

Ejecuta el programa:
```bash
java EjemploThread
```

La salida puede variar, por ejemplo:
```bash
Hola desde el hilo principal
Hola desde el hilo Thread-0
```

Esto ocurre porque los hilos se ejecutan de forma concurrente, y el orden de ejecución no está garantizado.


