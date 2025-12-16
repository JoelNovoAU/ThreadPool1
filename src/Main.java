/**
 * EJERCICIO 1: Conversor de temperaturas paralelo
 *
 * OBJETIVO:
 * Convertir una lista de temperaturas de Celsius a Fahrenheit usando
 * un FixedThreadPool de 2 hilos. Cada conversión debe ser un Callable<Double>.
 *
 * FÓRMULA: F = C * 9/5 + 32
 *
 * LISTA DE ENTRADA: [0, 10, 20, 30, 40, 50, 100]
 *
 * REQUISITOS:
 * 1. Crear un Callable<Double> que convierta una temperatura
 * 2. Usar ExecutorService con FixedThreadPool de 2 hilos
 * 3. Usar Future para recoger los resultados
 * 4. Mostrar tabla con Celsius -> Fahrenheit
 * 5. Cerrar correctamente el executor
 */

import java.util.*;
import java.util.concurrent.*;

public class Main {

    static class ConversorTemperatura implements Callable<Double> {
        private double celsius;

        public ConversorTemperatura(double celsius) {
            this.celsius = celsius;
        }

        @Override
        public Double call() throws Exception {
            Thread.sleep(100);

            return celsius * 9 / 5 + 32;
        }
    }

    public static void main(String[] args) {
        System.out.println("\n=== CONVERSOR DE TEMPERATURAS PARALELO ===\n");

        List<Double> temperaturasCelsius =
                Arrays.asList(0.0, 10.0, 20.0, 30.0, 40.0, 50.0, 100.0);

        ExecutorService executor = Executors.newFixedThreadPool(2);

        List<Future<Double>> futuros = new ArrayList<>();

        for (Double celsius : temperaturasCelsius) {
            ConversorTemperatura tarea = new ConversorTemperatura(celsius);
            Future<Double> future = executor.submit(tarea);
            futuros.add(future);
        }

        System.out.println("╔═══════════╦═════════════╗");
        System.out.println("║  Celsius  ║  Fahrenheit ║");
        System.out.println("╠═══════════╬═════════════╣");

        for (int i = 0; i < futuros.size(); i++) {
            try {
                double celsius = temperaturasCelsius.get(i);
                double fahrenheit = futuros.get(i).get();
                System.out.printf("║   %6.1f  ║    %6.1f   ║%n",
                        celsius, fahrenheit);
            } catch (InterruptedException | ExecutionException e) {
                System.err.println("Error en la conversión: " + e.getMessage());
            }
        }

        System.out.println("╚═══════════╩═════════════╝\n");

        executor.shutdown();

        System.out.println("✅ Conversión completada\n");
    }
}
