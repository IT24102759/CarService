package model;

import java.time.LocalDate;

public class MaintenanceReminder {
    private int userId;
    private int vehicleId;
    private int serviceId;
    private String title;
    private String description;
    private LocalDate dueDate;

    public MaintenanceReminder(int userId, int vehicleId, int serviceId, String title, String description, LocalDate dueDate) {
        this.userId = userId;
        this.vehicleId = vehicleId;
        this.serviceId = serviceId;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
    }

    public int getUserId() {
        return userId;
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public int getServiceId() {
        return serviceId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }
} 