package com.semenbazanov.model;

import java.util.Objects;

public class Car {
    private int id;

    private int type;

    public Car() {
    }

    public Car(int id, int type) {
        this.id = id;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return id == car.id && type == car.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type);
    }
}
