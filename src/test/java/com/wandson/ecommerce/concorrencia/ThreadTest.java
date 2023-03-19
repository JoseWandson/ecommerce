package com.wandson.ecommerce.concorrencia;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ThreadTest {

    @Test
    void entenderThreads() {
        Assertions.assertDoesNotThrow(() -> {
            Runnable runnable1 = () -> {
                log("Runnable 01 vai esperar 3 segundos.");
                esperar(3);
                log("Runnable 01 concluído.");
            };

            Runnable runnable2 = () -> {
                log("Runnable 02 vai esperar 1 segundo.");
                esperar(1);
                log("Runnable 02 concluído.");
            };

            var thread1 = new Thread(runnable1);
            var thread2 = new Thread(runnable2);

            thread1.start();
            thread2.start();

            try {
                thread1.join();
                thread2.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            log("Encerrando método de teste.");
        });
    }

    private static void log(Object obj, Object... args) {
        System.out.printf("[LOG " + System.currentTimeMillis() + "] " + obj + "%n", args);
    }

    private static void esperar(int segundos) {
        try {
            Thread.sleep(segundos * 1000L);
        } catch (InterruptedException ignored) {
        }
    }
}