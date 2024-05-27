package com.semenbazanov.program;

import com.semenbazanov.service.ParkingService;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Количество парковочных мест");
        int capacity = scanner.nextInt();
        System.out.println("Максимальную длину очереди автомобилей ожидающих въезда на парковку");
        int carLimit = scanner.nextInt();
        System.out.println("Интервал генерации входящих автомобилей в секундах");
        int inTime = scanner.nextInt();
        System.out.println("Интервал генерации выходящих автомобилей в секундах");
        int outTime = scanner.nextInt();

        ParkingService parking = new ParkingService(capacity, carLimit, inTime, outTime);
        parking.service();
    }
}