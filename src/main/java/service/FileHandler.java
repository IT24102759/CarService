package service;

import jakarta.servlet.ServletContext;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {
    private final String usersFilePath;
    private final String vehiclesFilePath;
    private final String servicesFilePath;
    private final String serviceRecordsFilePath;
    private final String sourceDataDir;
    private final String targetDataDir;

    public FileHandler(ServletContext context) {
        // Get both source and target directories
        sourceDataDir = context.getRealPath("/WEB-INF/data");
        targetDataDir = context.getRealPath("/data");
        
        if (sourceDataDir == null || targetDataDir == null) {
            throw new RuntimeException("Could not resolve data directories. Make sure /WEB-INF/data and /data exist in your webapp.");
        }

        // Initialize directories
        initializeDirectories(sourceDataDir);
        initializeDirectories(targetDataDir);

        // Set file paths for target directory (where we'll read/write)
        usersFilePath = targetDataDir + File.separator + "users.txt";
        vehiclesFilePath = targetDataDir + File.separator + "vehicles.txt";
        servicesFilePath = targetDataDir + File.separator + "services.txt";
        serviceRecordsFilePath = targetDataDir + File.separator + "service_records.txt";

        // Initialize files and sync with source
        initializeFiles();
    }

    private void initializeDirectories(String dirPath) {
        File dir = new File(dirPath);
        if (!dir.exists()) {
            boolean created = dir.mkdirs();
            if (!created) {
                throw new RuntimeException("Failed to create directory: " + dirPath);
            }
        }
        if (!dir.canWrite()) {
            throw new RuntimeException("Directory is not writable: " + dirPath);
        }
    }

    private void initializeFiles() {
        // Initialize all files
        createFileIfNotExists(usersFilePath);
        createFileIfNotExists(vehiclesFilePath);
        createFileIfNotExists(servicesFilePath);
        createFileIfNotExists(serviceRecordsFilePath);

        // Sync users file from source if target is empty
        syncUsersFile();
    }

    private void syncUsersFile() {
        File targetFile = new File(usersFilePath);
        if (targetFile.length() == 0) {
            String sourceUsersPath = sourceDataDir + File.separator + "users.txt";
            File sourceFile = new File(sourceUsersPath);
            if (sourceFile.exists() && sourceFile.length() > 0) {
                List<String> lines = readFile(sourceUsersPath);
                writeToFile(usersFilePath, String.join("\n", lines), false);
            }
        }
    }

    private void createFileIfNotExists(String filePath) {
        File file = new File(filePath);
        try {
            if (!file.exists()) {
                if (!file.createNewFile()) {
                    throw new IOException("Failed to create file: " + filePath);
                }
            }
            if (!file.canWrite()) {
                throw new IOException("File is not writable: " + filePath);
            }
        } catch (IOException e) {
            throw new RuntimeException("File initialization failed: " + filePath, e);
        }
    }

    public void writeUser(String line, boolean append) {
        writeToFile(usersFilePath, line, append);
    }

    public void writeVehicle(String line, boolean append) {
        writeToFile(vehiclesFilePath, line, append);
    }

    public void writeService(String line, boolean append) {
        writeToFile(servicesFilePath, line, append);
    }

    public void writeServiceRecord(String line, boolean append) {
        writeToFile(serviceRecordsFilePath, line, append);
    }

    private void writeToFile(String filePath, String data, boolean append) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, append))) {
            if (append) {
                writer.newLine();
            }
            writer.write(data);
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException("Failed to write to file: " + filePath, e);
        }
    }

    public List<String> readUsers() {
        return readFile(usersFilePath);
    }

    public List<String> readVehicles() {
        return readFile(vehiclesFilePath);
    }

    public List<String> readServices() {
        return readFile(servicesFilePath);
    }

    public List<String> readServiceRecords() {
        return readFile(serviceRecordsFilePath);
    }

    private List<String> readFile(String filePath) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file: " + filePath, e);
        }
        return lines;
    }

    public void updateFile(String fileName, List<String> lines) {
        String filePath = getFilePath(fileName);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to update file: " + filePath, e);
        }
    }

    private String getFilePath(String fileName) {
        return targetDataDir + File.separator + fileName;
    }
}