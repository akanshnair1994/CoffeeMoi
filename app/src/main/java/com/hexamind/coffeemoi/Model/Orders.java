package com.hexamind.coffeemoi.Model;

import com.google.firebase.database.Exclude;

import androidx.annotation.NonNull;

public class Orders extends Object {
    @Exclude
    private String orderId;
    private String username;
    private String coffeeType;
    private String coffeeSize;
    private int cremeInCoffee;
    private int milkInCoffee;
    private int sugarInCoffee;
    private String orderDate;
    private Double price;
    private boolean isDeleted;

    public Orders() {

    }

    public Orders(String orderId, String username, String coffeeType, String coffeeSize, int cremeInCoffee, int milkInCoffee, int sugarInCoffee, String orderDate, Double price, boolean isDeleted) {
        this.orderId = orderId;
        this.username = username;
        this.coffeeType = coffeeType;
        this.coffeeSize = coffeeSize;
        this.cremeInCoffee = cremeInCoffee;
        this.milkInCoffee = milkInCoffee;
        this.sugarInCoffee = sugarInCoffee;
        this.orderDate = orderDate;
        this.price = price;
        this.isDeleted = isDeleted;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCoffeeType() {
        return coffeeType;
    }

    public void setCoffeeType(String coffeeType) {
        this.coffeeType = coffeeType;
    }

    public String getCoffeeSize() {
        return coffeeSize;
    }

    public void setCoffeeSize(String coffeeSize) {
        this.coffeeSize = coffeeSize;
    }

    public int getCremeInCoffee() {
        return cremeInCoffee;
    }

    public void setCremeInCoffee(int cremeInCoffee) {
        this.cremeInCoffee = cremeInCoffee;
    }

    public int getMilkInCoffee() {
        return milkInCoffee;
    }

    public void setMilkInCoffee(int milkInCoffee) {
        this.milkInCoffee = milkInCoffee;
    }

    public int getSugarInCoffee() {
        return sugarInCoffee;
    }

    public void setSugarInCoffee(int sugarInCoffee) {
        this.sugarInCoffee = sugarInCoffee;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    @NonNull
    @Override
    public String toString() {
        return getCoffeeType() + "\n" +
                getCoffeeSize() + "\n" +
                getMilkInCoffee() + "\n" +
                getCremeInCoffee() + "\n" +
                getSugarInCoffee() + "\n" +
                getPrice();
    }
}
