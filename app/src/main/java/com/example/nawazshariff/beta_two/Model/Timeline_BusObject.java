package com.example.nawazshariff.beta_two.Model;

/**
 * Created by nawazshariff on 27-09-2017.
 */

public class Timeline_BusObject {
    public int cost;
    public String name;
    public float stars;

    public Timeline_BusObject() {

    }

    public Timeline_BusObject(int cost, String name, float stars) {
        this.cost = cost;
        this.name = name;
        this.stars = stars;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getStars() {
        return stars;
    }

    public void setStars(float stars) {
        this.stars = stars;
    }
}
