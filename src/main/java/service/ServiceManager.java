package service;

import jakarta.servlet.ServletContext;
import model.Service;
import model.ServiceRecord;
import model.Vehicle;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.time.format.DateTimeParseException;

public class ServiceManager {
    private final FileHandler fileHandler;
    private List<Service> services;
    private List<ServiceRecord> serviceRecords;

    public ServiceManager(ServletContext context) {
        this.fileHandler = new FileHandler(context);
        this.services = new ArrayList<>();
        this.serviceRecords = new ArrayList<>();
        loadServices();
        loadServiceRecords();
    }

    private void loadServices() {
        List<String> lines = fileHandler.readServices();
        for (String line : lines) {
            String[] parts = line.split(",");
            if (parts.length >= 3) {
                int serviceId = Integer.parseInt(parts[0]);
                String name = parts[1];
                // Handle both old format (id,name,cost) and new format (id,name,description,cost)
                String description = parts.length >= 4 ? parts[2] : name; // Use name as description if not provided
                double cost = Double.parseDouble(parts[parts.length - 1]); // Cost is always the last field
                services.add(new Service(serviceId, name, description, cost));
            }
        }
    }

    private void loadServiceRecords() {
        List<String> lines = fileHandler.readServiceRecords();
        System.out.println("Loading service records from file. Total lines: " + lines.size());
        for (String line : lines) {
            if (line.trim().isEmpty()) {
                continue;
                }
            String[] parts = line.split(",");
            System.out.println("Processing line: [" + line + "]");
            if (parts.length >= 5) {
                try {
                    int recordId = Integer.parseInt(parts[0]);
                    int userId = Integer.parseInt(parts[1]);
                    int vehicleId = Integer.parseInt(parts[2]);
                    int serviceId = Integer.parseInt(parts[3]);
                    LocalDate serviceDate = LocalDate.parse(parts[4]);
                    double cost = Double.parseDouble(parts[5]);
                    serviceRecords.add(new ServiceRecord(recordId, userId, vehicleId, serviceId, serviceDate, cost));
                    System.out.println("Successfully added service record: " + recordId);
                } catch (NumberFormatException | DateTimeParseException e) {
                    System.err.println("Error parsing service record line: [" + line + "]");
                    System.err.println("Error: " + e.getMessage());
                }
            } else {
                System.err.println("Invalid service record format: [" + line + "]");
            }
        }
        System.out.println("Finished loading service records. Total records: " + serviceRecords.size());
    }

    public void addService(String name, String description, double cost) {
        // Generate new service ID
        int newId = services.isEmpty() ? 1 : services.get(services.size() - 1).getServiceId() + 1;

        // Create new service
        Service newService = new Service(newId, name, description, cost);
        services.add(newService);

        // Save to file
        fileHandler.writeService(newService.toString(), true);
    }

    public List<Service> getAllServices() {
        return new ArrayList<>(services);
    }

    public Service getServiceById(int serviceId) {
        return services.stream()
            .filter(s -> s.getServiceId() == serviceId)
            .findFirst()
            .orElse(null);
    }

    public void updateService(Service service) {
        Service existingService = getServiceById(service.getServiceId());
        if (existingService != null) {
            services.remove(existingService);
            services.add(service);
            
            // Update file
            List<String> lines = fileHandler.readServices();
            List<String> updatedLines = lines.stream()
                .map(line -> {
                    String[] parts = line.split(",");
                    if (parts.length >= 1 && Integer.parseInt(parts[0]) == service.getServiceId()) {
                        return service.toString();
                    }
                    return line;
                })
                .collect(Collectors.toList());
            fileHandler.writeService(String.join("\n", updatedLines), false);
        }
    }

    public void deleteService(int serviceId) {
        services.removeIf(s -> s.getServiceId() == serviceId);
        
        // Update file
        List<String> lines = fileHandler.readServices();
        List<String> updatedLines = lines.stream()
            .filter(line -> {
                String[] parts = line.split(",");
                return parts.length >= 1 && Integer.parseInt(parts[0]) != serviceId;
            })
            .collect(Collectors.toList());
        fileHandler.writeService(String.join("\n", updatedLines), false);
    }

    public void addServiceRecord(int userId, int vehicleId, int serviceId, LocalDate serviceDate, double cost) {
        // Generate new record ID
        int newId = serviceRecords.isEmpty() ? 1 : serviceRecords.get(serviceRecords.size() - 1).getRecordId() + 1;

        // Create new service record
        ServiceRecord newRecord = new ServiceRecord(newId, userId, vehicleId, serviceId, serviceDate, cost);
        serviceRecords.add(newRecord);

        // Save to file
        fileHandler.writeServiceRecord(newRecord.toString(), true);
    }

    public List<ServiceRecord> getUserServiceHistory(int userId) {
        return serviceRecords.stream()
            .filter(r -> r.getUserId() == userId)
            .collect(Collectors.toList());
    }

    public List<ServiceRecord> getAllServiceRecords() {
        return new ArrayList<>(serviceRecords);
    }

    public int getTotalServices() {
        return services.size();
    }

    public double getTotalRevenue() {
        return serviceRecords.stream()
            .mapToDouble(ServiceRecord::getCost)
            .sum();
    }

    public int getUserServiceCount(int userId) {
        return (int) serviceRecords.stream()
            .filter(r -> r.getUserId() == userId)
            .count();
    }
}