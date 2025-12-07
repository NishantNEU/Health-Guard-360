package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class to parse claim data from uploaded documents
 * Supports text-based documents with labeled fields
 */
public class ClaimDocumentParser {

    /**
     * Parse claim data from a text file
     * Expected format:
     * Service Date: MM/DD/YYYY
     * Provider Name: [name]
     * Diagnosis: [diagnosis]
     * Service Type: [type]
     * Claim Amount: $XXX.XX
     */
    public static Map<String, String> parseClaimDocument(File file) throws IOException {
        Map<String, String> claimData = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                parseLine(line, claimData);
            }
        }

        return claimData;
    }

    /**
     * Parse a single line and extract claim data
     */
    private static void parseLine(String line, Map<String, String> claimData) {
        line = line.trim();

        // Service Date pattern: "Service Date: MM/DD/YYYY"
        if (line.toLowerCase().contains("service date:")) {
            String date = extractAfterColon(line);
            if (isValidDate(date)) {
                claimData.put("serviceDate", date);
            }
        }

        // Provider Name pattern: "Provider Name:" or "Provider:"
        else if (line.toLowerCase().contains("provider name:") ||
                (line.toLowerCase().contains("provider:") && !line.toLowerCase().contains("id"))) {
            String provider = extractAfterColon(line);
            if (!provider.isEmpty()) {
                claimData.put("providerName", provider);
            }
        }

        // Diagnosis pattern: "Diagnosis:"
        else if (line.toLowerCase().contains("diagnosis:")) {
            String diagnosis = extractAfterColon(line);
            if (!diagnosis.isEmpty()) {
                claimData.put("diagnosis", diagnosis);
            }
        }

        // Service Type pattern: "Service Type:"
        else if (line.toLowerCase().contains("service type:")) {
            String serviceType = extractAfterColon(line);
            if (!serviceType.isEmpty()) {
                claimData.put("serviceType", mapServiceType(serviceType));
            }
        }

        // Claim Amount pattern: "Claim Amount: $XXX.XX" or "Amount: $XXX.XX"
        else if (line.toLowerCase().contains("claim amount:") ||
                (line.toLowerCase().contains("amount:") && !line.toLowerCase().contains("approved"))) {
            String amount = extractAmount(line);
            if (amount != null) {
                claimData.put("claimAmount", amount);
            }
        }

        // Patient Name pattern: "Patient Name:" or "Patient:"
        else if (line.toLowerCase().contains("patient name:") ||
                line.toLowerCase().contains("patient:")) {
            String patientName = extractAfterColon(line);
            if (!patientName.isEmpty()) {
                claimData.put("patientName", patientName);
            }
        }
    }

    /**
     * Extract text after colon
     */
    private static String extractAfterColon(String line) {
        int colonIndex = line.indexOf(':');
        if (colonIndex >= 0 && colonIndex < line.length() - 1) {
            return line.substring(colonIndex + 1).trim();
        }
        return "";
    }

    /**
     * Extract amount from string (handles $XXX.XX format)
     */
    private static String extractAmount(String line) {
        // Pattern to match currency amounts like $500.00 or 500.00
        Pattern pattern = Pattern.compile("\\$?\\s*(\\d+\\.\\d{2})");
        Matcher matcher = pattern.matcher(line);

        if (matcher.find()) {
            return matcher.group(1); // Return just the number
        }
        return null;
    }

    /**
     * Validate date format MM/DD/YYYY
     */
    private static boolean isValidDate(String date) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            LocalDate.parse(date, formatter);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Map service type string to standard values
     */
    private static String mapServiceType(String serviceType) {
        String lower = serviceType.toLowerCase();

        if (lower.contains("doctor") || lower.contains("physician")) {
            return "Doctor Visit";
        } else if (lower.contains("emergency") || lower.contains("er")) {
            return "Emergency Room Visit";
        } else if (lower.contains("hospital") && lower.contains("stay")) {
            return "Hospital Stay";
        } else if (lower.contains("surgery") || lower.contains("operation")) {
            return "Surgery";
        } else if (lower.contains("diagnostic") || lower.contains("test") || lower.contains("lab")) {
            return "Diagnostic Test";
        } else if (lower.contains("prescription") || lower.contains("medication") || lower.contains("pharmacy")) {
            return "Prescription Medication";
        } else if (lower.contains("therapy") || lower.contains("physical")) {
            return "Physical Therapy";
        }

        return "Doctor Visit"; // Default
    }

    /**
     * Validate parsed claim data
     */
    public static boolean isValidClaimData(Map<String, String> claimData) {
        return claimData.containsKey("serviceDate") ||
                claimData.containsKey("providerName") ||
                claimData.containsKey("claimAmount");
    }
}
