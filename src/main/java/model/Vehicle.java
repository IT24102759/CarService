package model;

public class Vehicle {
    private int vehicleId;
    private int userId;
    private String make;
    private String model;
    private int year;
    private String licensePlate;
    private int mileage;

    public Vehicle(int vehicleId, int userId, String make, String model, int year, String licensePlate, int mileage) {
        this.vehicleId = vehicleId;
        this.userId = userId;
        this.make = make;
        this.model = model;
        this.year = year;
        this.licensePlate = licensePlate;
        this.mileage = mileage;
    }

    // Getters
    public int getVehicleId() {
        return vehicleId;
    }

    public int getUserId() {
        return userId;
    }

    public String getMake() {
        return make;
    }

    public String getModel() {
        return model;
    }

    public int getYear() {
        return year;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public int getMileage() {
        return mileage;
    }

    // Setters
    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public void setMileage(int mileage) {
        if (mileage < 0) {
            throw new IllegalArgumentException("Mileage cannot be negative");
        }
        this.mileage = mileage;
    }

    @Override
    public String toString() {
        return String.format("%d,%d,%s,%s,%d,%s,%d", 
            vehicleId, userId, make, model, year, licensePlate, mileage);
    }
}