package com.example.nawazshariff.beta_two.Model;

import java.io.Serializable;

/**
 * Created by nawazshariff on 27-09-2017.
 */

public class Timeline_RecyclerObject implements Serializable {
    public double  cost;
    public String name;
    public float stars;
    public String date;

    public Timeline_RecyclerObject()
    {}

    public Timeline_RecyclerObject(double cost, String date,String name, float stars) {
        this.cost = cost;
        this.date=date;
        this.name = name;
        this.stars = stars;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
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
