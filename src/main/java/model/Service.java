package model;

import java.util.Date;

public class Service {
    private int serviceId;
    private int vehicleId;
    private String name;
    private String description;
    private double cost;
    private Date serviceDate;

    // Default constructor for database operations
    public Service() {
    }

    public Service(int serviceId, String name, String description, double cost) {
        this.serviceId = serviceId;
        this.name = name;
        this.description = description;
        this.cost = cost;
        this.serviceDate = new Date();
    }

    // Getters
    public int getServiceId() {
        return serviceId;
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getCost() {
        return cost;
    }

    public Date getServiceDate() {
        return serviceDate;
    }

    // Setters
    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public void setServiceDate(Date serviceDate) {
        this.serviceDate = serviceDate;
    }

    @Override
    public String toString() {
        return serviceId + "," + name + "," + description + "," + cost;
    }
}