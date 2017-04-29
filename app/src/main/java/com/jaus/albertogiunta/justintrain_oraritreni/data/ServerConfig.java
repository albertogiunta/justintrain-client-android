package com.jaus.albertogiunta.justintrain_oraritreni.data;

public class ServerConfig {

    String address;
    boolean isAvailable;
    float weight;

    public ServerConfig(String address, boolean isAvailable, float weight) {
        this.address = address;
        this.isAvailable = isAvailable;
        this.weight = weight;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "ServerConfig{" +
                "address='" + address + '\'' +
                ", isAvailable=" + isAvailable +
                ", weight=" + weight +
                '}';
    }
}
