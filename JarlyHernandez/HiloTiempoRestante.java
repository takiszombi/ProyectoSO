public class HiloTiempoRestante {

    public static void main(String[] args) {
        Hilo[] hilos = new Hilo[5];

        for (int i = 0; i < 5; i++) {
            hilos[i] = new Hilo("Hilo-" + (i + 1), hilos);
            hilos[i].start();
        }

        try {                                     //Esperar 4 segundo para que busque el hilo con menor tiempo
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Hilo hiloConMenorTiempo = encontrarHiloConMenorTiempo(hilos); //Lo busca y completa
        if (hiloConMenorTiempo != null) {
            hiloConMenorTiempo.interrupt();
        }
    }

    private static Hilo encontrarHiloConMenorTiempo(Hilo[] hilos) {
        Hilo hiloConMenorTiempo = null;
        long menorTiempo = Long.MAX_VALUE;

        for (Hilo hilo : hilos) {
            long tiempoRestante = hilo.getTiempoRestante();
            if (tiempoRestante < menorTiempo) {
                menorTiempo = tiempoRestante;
                hiloConMenorTiempo = hilo;
            }
        }

        return hiloConMenorTiempo;
    }
}

class Hilo extends Thread {
    private long tiempoInicial = 5000; // 5 segundos
    private long tiempoRestante = tiempoInicial;
    private String nombre;
    private Hilo[] hilos;

    public Hilo(String nombre, Hilo[] hilos) {
        this.nombre = nombre;
        this.hilos = hilos;
    }

    @Override
    public void run() {
        long inicio = System.currentTimeMillis();

        while (tiempoRestante > 0 && !isInterrupted()) {
            System.out.println(nombre + " Tiempo restante: " + tiempoRestante); //Se imprime el tiempo restante

            try {                       //Simula tarea de hilo
                sleep(500);
            } catch (InterruptedException e) {
                System.out.println(nombre + " ha sido completado por ser el de menor tiempo");
                return;
            }

            tiempoRestante = tiempoInicial - (System.currentTimeMillis() - inicio); //Actualiza tiempo
        }

        System.out.println(nombre + " ha concluido.");
    }

    public long getTiempoRestante() {
        return tiempoRestante;
    }
}
