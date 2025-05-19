package service;

import model.MaintenanceReminder;
import model.ServiceRecord;
import model.Vehicle;
import model.Service;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;
import java.util.stream.Collectors;
import jakarta.servlet.ServletContext;

public class MaintenanceService {
    private List<MaintenanceReminder> reminders;
    private VehicleService vehicleService;
    private final ServiceManager serviceManager;
    private static final int OIL_CHANGE_INTERVAL = 5000; // miles
    private static final int TIRE_ROTATION_INTERVAL = 7500; // miles

    public MaintenanceService() {
        throw new IllegalStateException("MaintenanceService must be initialized with ServletContext");
    }

    public MaintenanceService(ServletContext context) {
        if (context == null) {
            throw new IllegalArgumentException("ServletContext cannot be null");
        }
        this.reminders = new ArrayList<>();
        this.vehicleService = new VehicleService(context);
        this.serviceManager = new ServiceManager(context);
    }

    public List<MaintenanceReminder> getMaintenanceReminders(int userId) {
        if (vehicleService == null) {
            throw new IllegalStateException("VehicleService is not initialized");
        }
        List<MaintenanceReminder> reminders = new ArrayList<>();
        List<Vehicle> vehicles = vehicleService.getUserVehicles(userId);

        for (Vehicle vehicle : vehicles) {
            // Check oil change
            int lastOilChangeMileage = getLastServiceMileage(userId, 1); // Assuming 1 is oil change service ID
            if (vehicle.getMileage() - lastOilChangeMileage >= OIL_CHANGE_INTERVAL) {
                reminders.add(new MaintenanceReminder(
                    userId,
                    vehicle.getVehicleId(),
                    1, // Oil change service ID
                    "Oil Change Due",
                    String.format("Oil change needed for %s %s (Vehicle #%s) at %d miles",
                        vehicle.getMake(), vehicle.getModel(), vehicle.getLicensePlate(), vehicle.getMileage()),
                    LocalDate.now().plusDays(7)
                ));
            }

            // Check tire rotation
            int lastTireRotationMileage = getLastServiceMileage(userId, 2); // Assuming 2 is tire rotation service ID
            if (vehicle.getMileage() - lastTireRotationMileage >= TIRE_ROTATION_INTERVAL) {
                reminders.add(new MaintenanceReminder(
                    userId,
                    vehicle.getVehicleId(),
                    2, // Tire rotation service ID
                    "Tire Rotation Due",
                    String.format("Tire rotation needed for %s %s (Vehicle #%s) at %d miles",
                        vehicle.getMake(), vehicle.getModel(), vehicle.getLicensePlate(), vehicle.getMileage()),
                    LocalDate.now().plusDays(7)
                ));
            }
        }

        return reminders;
    }

    public List<String> getMaintenanceReminderStrings(int userId) {
        if (vehicleService == null) {
            throw new IllegalStateException("VehicleService is not initialized");
        }
        return getMaintenanceReminders(userId).stream()
            .map(MaintenanceReminder::getDescription)
            .collect(Collectors.toList());
    }

    private int getLastServiceMileage(int userId, int serviceId) {
        return serviceManager.getUserServiceHistory(userId).stream()
            .filter(record -> record.getServiceId() == serviceId)
            .mapToInt(record -> (int)record.getCost())  // Using cost as mileage since there's no mileage field
            .max()
            .orElse(0);
    }

    private LocalDate getLastServiceDate(int userId, int serviceId) {
        return serviceManager.getUserServiceHistory(userId).stream()
            .filter(record -> record.getServiceId() == serviceId)
            .map(ServiceRecord::getServiceDate)
            .max(Comparator.naturalOrder())
            .orElse(LocalDate.now().minusYears(1));
    }

    public void scheduleMaintenance(int userId, int vehicleId, int serviceId) {
        if (vehicleService == null) {
            throw new IllegalStateException("VehicleService is not initialized");
        }
        scheduleMaintenance(userId, vehicleId, serviceId, LocalDate.now());
    }

    public void scheduleMaintenance(int userId, int vehicleId, int serviceId, LocalDate scheduledDate) {
        if (vehicleService == null) {
            throw new IllegalStateException("VehicleService is not initialized");
        }
        // Implementation for scheduling maintenance
        Service service = serviceManager.getServiceById(serviceId);
        if (service != null) {
            serviceManager.addServiceRecord(userId, vehicleId, serviceId, scheduledDate, service.getCost());
        }
    }
}