package com.semenbazanov.service;


import com.semenbazanov.model.Car;

import java.util.List;
import java.util.concurrent.*;

public class ParkingService {
    private static int id = 1;
    private volatile int capacity;
    private volatile int occupiedPlaces = 0;
    private int carLimit;
    private int inTime;
    private int outTime;
    private volatile int type1 = 0;
    private volatile int type2 = 0;
    private List<Car> list = new CopyOnWriteArrayList<>();
    private BlockingQueue<Car> queue = new LinkedBlockingQueue<>();

    private ExecutorService executor1 = Executors.newSingleThreadExecutor();
    private ExecutorService executor2 = Executors.newSingleThreadExecutor();
    private ExecutorService executor3 = Executors.newSingleThreadExecutor();

    public ParkingService(int capacity, int queueLength, int inTime, int outTime) {
        this.capacity = capacity;
        this.carLimit = queueLength;
        this.inTime = inTime;
        this.outTime = outTime;
    }

    public void service() {
        Runnable runnable1 = () -> {
            try {
                while (true) {
                    Thread.sleep(inTime * 1000L);
                    Car car = new Car(id, generateNumber(1, 2));
                    id++;
                    queue.add(car);
                    if (car.getType() == 1) {
                        System.out.println("Легковой автомобиль с id " + car.getId() +
                                " встал в очередь на въезд.");
                    } else {
                        System.out.println("Грузовой автомобиль с id " + car.getId() +
                                " встал в очередь на въезд.");
                    }
                    park();
                }
            } catch (InterruptedException ignored) {
            }
        };

        Runnable runnable2 = () -> {
            try {
                while (true) {
                    Thread.sleep(outTime * 1000L);
                    if (!list.isEmpty()) {
                        Car car = list.get(generateNumber(0, list.size() - 1));
                        list.remove(car);
                        if (car.getType() == 1) {
                            type1--;
                            occupiedPlaces--;
                            System.out.println("Легковой автомобиль с id " + car.getId()
                                    + " покинул парковку.");
                        } else {
                            type2--;
                            occupiedPlaces -= 2;
                            System.out.println("Грузовой автомобиль с id " + car.getId()
                                    + " покинул парковку.");
                        }
                    }
                    park();
                }
            } catch (InterruptedException ignored) {
            }
        };

        Runnable runnable3 = () -> {
            try {
                while (true) {
                    Thread.sleep(5000);
                    System.out.println("Свободных мест: " + (capacity - occupiedPlaces));
                    System.out.println("Занято мест: " + type1 + " легковых и " + type2 + " грузовых");
                    System.out.println("Автомобилей, ожидающих в очереди: " + queue.size());
                }
            } catch (InterruptedException ignored) {
            }
        };
        executor1.execute(runnable1);
        executor2.execute(runnable2);
        executor3.execute(runnable3);
    }

    /**
     * Добавление на парковку
     */
    private synchronized void park() {
        if (!this.queue.isEmpty() && this.capacity > this.occupiedPlaces) {
            try {
                Car car = this.queue.take();
                if (this.occupiedPlaces + car.getType() <= this.capacity) {
                    if (car.getType() == 1) {
                        this.list.add(car);
                        type1++;
                        this.occupiedPlaces++;
                        System.out.println("Легковой автомобиль с id " + car.getId() + " припарковался");
                    } else {
                        this.list.add(car);
                        type2++;
                        this.occupiedPlaces += 2;
                        System.out.println("Грузовой автомобиль с id " + car.getId() + " припарковался");
                    }
                }
            } catch (InterruptedException ignored) {
            }
        } else if (this.queue.size() == this.carLimit) {
            executor1.shutdownNow();
            executor2.shutdownNow();
            executor3.shutdownNow();
            System.out.println("Длина очереди достигла максимальной");
        }
    }

    public int generateNumber(int a, int b) {
        return a + (int) (Math.random() * (b - a + 1));
    }
}
