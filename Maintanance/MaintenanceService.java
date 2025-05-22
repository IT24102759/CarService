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
import java.util.HashSet;
import java.util.Set;
import java.io.File;

public class MaintenanceService {
    private List<MaintenanceReminder> reminders;
    private VehicleService vehicleService;
    private final ServiceManager serviceManager;
    private static final int OIL_CHANGE_INTERVAL = 5000; // miles
    private static final int TIRE_ROTATION_INTERVAL = 7500; // miles
    private static final int ENGINE_REPAIR_INTERVAL = 30000; // miles
    private Set<String> dismissedReminders;
    private FileHandler fileHandler;

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
        this.fileHandler = new FileHandler(context);
        this.dismissedReminders = new HashSet<>(fileHandler.readDismissedReminders());
    }

    private String getDismissedKey(int userId, int vehicleId, int serviceId) {
        return userId + "," + vehicleId + "," + serviceId;
    }

    public void removeMaintenanceReminder(int userId, int vehicleId, int serviceId) {
        String key = getDismissedKey(userId, vehicleId, serviceId);
        if (dismissedReminders.contains(key)) {
            dismissedReminders.remove(key);
            // To update the file, we need to rewrite it without the removed key.
            // Reading all, filtering, and writing back is simplest for this file structure.
            List<String> updatedLines = new ArrayList<>();
            for (String dismissedKey : dismissedReminders) {
                updatedLines.add(dismissedKey);
            }
            fileHandler.updateFile("dismissed_reminders.txt", updatedLines);
        }
    }

    // New method to mark a reminder as dismissed
    public void dismissMaintenanceReminder(int userId, int vehicleId, int serviceId) {
        String key = getDismissedKey(userId, vehicleId, serviceId);
        if (!dismissedReminders.contains(key)) {
            dismissedReminders.add(key);
            fileHandler.writeDismissedReminder(key, true);
        }
    }

    public List<MaintenanceReminder> getMaintenanceReminders(int userId) {
        if (vehicleService == null) {
            throw new IllegalStateException("VehicleService is not initialized");
        }
        List<MaintenanceReminder> activeReminders = new ArrayList<>();
        List<Vehicle> vehicles = vehicleService.getUserVehicles(userId);
        List<ServiceRecord> allServiceRecords = serviceManager.getUserServiceHistory(userId);

        for (Vehicle vehicle : vehicles) {
            // Check oil change
            int oilChangeServiceId = 1; // Assuming 1 is oil change service ID
            int lastOilChangeMileage = getLastServiceMileage(userId, oilChangeServiceId);
            boolean noOilChangeHistory = lastOilChangeMileage == 0;
            boolean isOilChangeDismissed = dismissedReminders.contains(getDismissedKey(userId, vehicle.getVehicleId(), oilChangeServiceId));

            // Check for upcoming scheduled oil change
            boolean isOilChangeScheduled = allServiceRecords.stream()
                .anyMatch(record -> record.getServiceId() == oilChangeServiceId && 
                             record.getVehicleId() == vehicle.getVehicleId() &&
                             record.getServiceDate().isAfter(LocalDate.now()));

            if ((noOilChangeHistory || vehicle.getMileage() - lastOilChangeMileage >= OIL_CHANGE_INTERVAL || isOilChangeScheduled) && !isOilChangeDismissed) {
                 // If scheduled, use scheduled date for reminder due date
                 LocalDate dueDate = LocalDate.now().plusDays(7); // Default due date
                 if (isOilChangeScheduled) {
                     dueDate = allServiceRecords.stream()
                         .filter(record -> record.getServiceId() == oilChangeServiceId && 
                                      record.getVehicleId() == vehicle.getVehicleId() &&
                                      record.getServiceDate().isAfter(LocalDate.now()))
                         .map(ServiceRecord::getServiceDate)
                         .min(Comparator.naturalOrder())
                         .orElse(dueDate); // Use default if somehow no future date found
                 }

                MaintenanceReminder reminder = new MaintenanceReminder(
                    userId,
                    vehicle.getVehicleId(),
                    oilChangeServiceId,
                    "Oil Change Due",
                    String.format("Oil change needed for %s %s (Vehicle #%s) at %d miles",
                        vehicle.getMake(), vehicle.getModel(), vehicle.getLicensePlate(), vehicle.getMileage()),
                    dueDate
                );
                activeReminders.add(reminder);
            }

            // Check tire rotation
            int tireRotationServiceId = 2; // Assuming 2 is tire rotation service ID
            int lastTireRotationMileage = getLastServiceMileage(userId, tireRotationServiceId); // Assuming 2 is tire rotation service ID
            boolean noTireRotationHistory = lastTireRotationMileage == 0;
            boolean isTireRotationDismissed = dismissedReminders.contains(getDismissedKey(userId, vehicle.getVehicleId(), tireRotationServiceId));

             // Check for upcoming scheduled tire rotation
            boolean isTireRotationScheduled = allServiceRecords.stream()
                .anyMatch(record -> record.getServiceId() == tireRotationServiceId && 
                             record.getVehicleId() == vehicle.getVehicleId() &&
                             record.getServiceDate().isAfter(LocalDate.now()));

            if ((noTireRotationHistory || vehicle.getMileage() - lastTireRotationMileage >= TIRE_ROTATION_INTERVAL || isTireRotationScheduled) && !isTireRotationDismissed) {
                 // If scheduled, use scheduled date for reminder due date
                 LocalDate dueDate = LocalDate.now().plusDays(7); // Default due date
                 if (isTireRotationScheduled) {
                     dueDate = allServiceRecords.stream()
                         .filter(record -> record.getServiceId() == tireRotationServiceId && 
                                      record.getVehicleId() == vehicle.getVehicleId() &&
                                      record.getServiceDate().isAfter(LocalDate.now()))
                         .map(ServiceRecord::getServiceDate)
                         .min(Comparator.naturalOrder())
                         .orElse(dueDate); // Use default if somehow no future date found
                 }

                MaintenanceReminder reminder = new MaintenanceReminder(
                    userId,
                    vehicle.getVehicleId(),
                    tireRotationServiceId,
                    "Tire Rotation Due",
                    String.format("Tire rotation needed for %s %s (Vehicle #%s) at %d miles",
                        vehicle.getMake(), vehicle.getModel(), vehicle.getLicensePlate(), vehicle.getMileage()),
                    dueDate
                );
                 activeReminders.add(reminder);
            }

            // Check engine repair (lookup by name)
            Service engineRepairService = serviceManager.getAllServices().stream()
                .filter(s -> s.getName().equalsIgnoreCase("Engine Repair"))
                .findFirst().orElse(null);
            if (engineRepairService != null) {
                int engineRepairServiceId = engineRepairService.getServiceId();
                int lastEngineRepairMileage = getLastServiceMileage(userId, engineRepairServiceId);
                boolean noEngineRepairHistory = lastEngineRepairMileage == 0;
                boolean isEngineRepairDismissed = dismissedReminders.contains(getDismissedKey(userId, vehicle.getVehicleId(), engineRepairServiceId));

                 // Check for upcoming scheduled engine repair
                boolean isEngineRepairScheduled = allServiceRecords.stream()
                    .anyMatch(record -> record.getServiceId() == engineRepairServiceId && 
                                 record.getVehicleId() == vehicle.getVehicleId() &&
                                 record.getServiceDate().isAfter(LocalDate.now()));

                if ((noEngineRepairHistory || vehicle.getMileage() - lastEngineRepairMileage >= ENGINE_REPAIR_INTERVAL || isEngineRepairScheduled) && !isEngineRepairDismissed) {
                     // If scheduled, use scheduled date for reminder due date
                     LocalDate dueDate = LocalDate.now().plusDays(7); // Default due date
                     if (isEngineRepairScheduled) {
                         dueDate = allServiceRecords.stream()
                             .filter(record -> record.getServiceId() == engineRepairServiceId && 
                                          record.getVehicleId() == vehicle.getVehicleId() &&
                                          record.getServiceDate().isAfter(LocalDate.now()))
                             .map(ServiceRecord::getServiceDate)
                             .min(Comparator.naturalOrder())
                             .orElse(dueDate); // Use default if somehow no future date found
                     }

                    MaintenanceReminder reminder = new MaintenanceReminder(
                        userId,
                        vehicle.getVehicleId(),
                        engineRepairServiceId,
                        "Engine Maintenance Due",
                        String.format("Engine maintenance needed for %s %s (Vehicle #%s) at %d miles",
                            vehicle.getMake(), vehicle.getModel(), vehicle.getLicensePlate(), vehicle.getMileage()),
                        dueDate
                    );
                     activeReminders.add(reminder);
                }
            }
        }
        return activeReminders;
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
            .mapToInt(ServiceRecord::getMileage)
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
            // Get the vehicle to get its current mileage
            Vehicle vehicle = vehicleService.getVehicleById(vehicleId);
            if (vehicle != null) {
                // Create service record with the vehicle's current mileage
                serviceManager.addServiceRecord(userId, vehicleId, serviceId, scheduledDate, vehicle.getMileage());
                
                // Create maintenance reminder
                MaintenanceReminder reminder = new MaintenanceReminder(
                    userId,
                    vehicleId,
                    serviceId,
                    service.getName() + " Scheduled",
                    String.format("%s scheduled for %s %s (Vehicle #%s)",
                        service.getName(), vehicle.getMake(), vehicle.getModel(), vehicle.getLicensePlate()),
                    scheduledDate
                );
                reminders.add(reminder);
            }
        }
    }
}