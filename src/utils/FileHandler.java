package utils;

import business.SystemData;
import java.io.*;

/**
 * Complete FileHandler class
 * Handles saving and loading SystemData to/from file
 */
public class FileHandler {
    
    // File path for storing data
    private static final String DATA_FILE_PATH = "healthguard360_data.dat";
    
    /**
     * Save SystemData to file
     */
    public static boolean saveSystemData(SystemData systemData) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE_PATH))) {
            oos.writeObject(systemData);
            System.out.println("✅ Data saved successfully to " + DATA_FILE_PATH);
            return true;
        } catch (IOException e) {
            System.err.println("❌ Error saving data: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Load SystemData from file
     */
    public static SystemData loadSystemData() {
        File file = new File(DATA_FILE_PATH);
        
        // Check if file exists
        if (!file.exists()) {
            System.out.println("ℹ️ No saved data file found. Starting with fresh data.");
            return null;
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DATA_FILE_PATH))) {
            SystemData loadedData = (SystemData) ois.readObject();
            System.out.println("✅ Data loaded successfully from " + DATA_FILE_PATH);
            return loadedData;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("❌ Error loading data: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Check if data file exists
     */
    public static boolean dataFileExists() {
        return new File(DATA_FILE_PATH).exists();
    }
    
    /**
     * Delete data file (for testing/reset)
     */
    public static boolean deleteDataFile() {
        File file = new File(DATA_FILE_PATH);
        if (file.exists()) {
            boolean deleted = file.delete();
            if (deleted) {
                System.out.println("✅ Data file deleted successfully");
            }
            return deleted;
        }
        return false;
    }
    
    /**
     * Get data file size
     */
    public static long getDataFileSize() {
        File file = new File(DATA_FILE_PATH);
        if (file.exists()) {
            return file.length();
        }
        return 0;
    }
    
    /**
     * Get formatted file size
     */
    public static String getFormattedFileSize() {
        long bytes = getDataFileSize();
        if (bytes == 0) {
            return "No data file";
        }
        if (bytes < 1024) {
            return bytes + " bytes";
        }
        if (bytes < 1024 * 1024) {
            return String.format("%.2f KB", bytes / 1024.0);
        }
        return String.format("%.2f MB", bytes / (1024.0 * 1024.0));
    }
    
    /**
     * Backup current data file
     */
    public static boolean backupDataFile() {
        File sourceFile = new File(DATA_FILE_PATH);
        if (!sourceFile.exists()) {
            return false;
        }
        
        String backupPath = "healthguard360_data_backup_" + System.currentTimeMillis() + ".dat";
        File backupFile = new File(backupPath);
        
        try (FileInputStream fis = new FileInputStream(sourceFile);
             FileOutputStream fos = new FileOutputStream(backupFile)) {
            
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                fos.write(buffer, 0, length);
            }
            
            System.out.println("✅ Backup created: " + backupPath);
            return true;
            
        } catch (IOException e) {
            System.err.println("❌ Error creating backup: " + e.getMessage());
            return false;
        }
    }
}