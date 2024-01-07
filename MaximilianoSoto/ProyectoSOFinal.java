import java.util.LinkedList;
import java.util.Queue;

public class ProyectoSOIndividual implements Runnable{
    Queue<String> colaHilos;
    int quantum;
    
    public ProyectoSOIndividual(int quantum){
        this.colaHilos= new LinkedList<>();
        this.quantum=quantum;
    }
    public void agregarHilo(String hilo) {
        colaHilos.add(hilo);
    }
    public void run(){
        while(!colaHilos.isEmpty()){
            String hiloActual= colaHilos.poll();
            int tiempoDeEjecución= Math.min(quantum, hiloActual.length());
            System.out.println("Ejecutando el Hilo: " + Thread.currentThread().getName()+ " por "+ tiempoDeEjecución+" unidades de tiempo.");
            hiloActual=hiloActual.substring(tiempoDeEjecución);

            if (!hiloActual.isEmpty()) {
                colaHilos.add(hiloActual);
            }
        }
    }
    public static void main(String[] args) {
        ProyectoSOIndividual hilos= new ProyectoSOIndividual(4);
        Thread h1 = new Thread(hilos,"Hilo 1");
        Thread h2 = new Thread(hilos,"Hilo 2");
        Thread h3= new Thread(hilos,"Hilo 3");
        hilos.agregarHilo("Hilo 1");
        hilos.agregarHilo("Hilo 2");
        hilos.agregarHilo("Hilo 3");
        h1.start();
        h2.start();
        h3.start();
        
    }
}
