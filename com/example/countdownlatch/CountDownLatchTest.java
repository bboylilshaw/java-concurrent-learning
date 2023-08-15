package com.example.countdownlatch;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class CountDownLatchTest {
    public static void main(String[] args) {
        ExecutorService executorService = new ThreadPoolExecutor(2, 10,
                                 1, TimeUnit.MINUTES, new ArrayBlockingQueue<>(5, true),
                                 Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());

        CountDownLatch latch = new CountDownLatch(2);

        System.out.println(Thread.currentThread().getName() + ": start execute tasks");

        executorService.submit(() -> {
            try {
                Thread.sleep(10000L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            System.out.println(Thread.currentThread().getName() + ": task done");
            latch.countDown();
        });

        executorService.submit(() -> {
            try {
                Thread.sleep(5000L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            System.out.println(Thread.currentThread().getName() + ": task done");
            latch.countDown();
        });

        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println(Thread.currentThread().getName() + ": end execute tasks");

        executorService.shutdown();

    }
}
