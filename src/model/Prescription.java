package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Complete Prescription class
 * Represents a medication prescription with refill tracking
 */
public class Prescription implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    // Prescription Status
    public enum PrescriptionStatus {
        ACTIVE("Active"),
        FILLED("Filled"),
        EXPIRED("Expired"),
        CANCELLED("Cancelled"),
        COMPLETED("Completed");
        
        private final String displayName;
        
        PrescriptionStatus(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
        
        @Override
        public String toString() {
            return displayName;
        }
    }
    
    // Properties
    private String prescriptionNumber;
    private String patientId;
    private String doctorId; // Employee ID of prescribing doctor
    private String medicationId;
    private String dosage; // e.g., "10mg, once daily"
    private int quantity; // Number of pills/doses
    private int refillsRemaining;
    private int refillsAuthorized; // Total refills allowed
    private String instructions; // Special instructions
    private LocalDate prescribedDate;
    private LocalDate expiryDate;
    private String pharmacyId; // ID of pharmacy enterprise
    private PrescriptionStatus status;
    private List<LocalDate> refillDates; // Track when refills were filled
    private String policyNumber; // Insurance policy covering this prescription
    
    /**
     * Complete constructor
     */
    public Prescription(String patientId, String doctorId, String medicationId, 
                       String dosage, int quantity, int refillsAuthorized, 
                       String instructions, String pharmacyId, String policyNumber) {
        this.prescriptionNumber = generatePrescriptionNumber();
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.medicationId = medicationId;
        this.dosage = dosage;
        this.quantity = quantity;
        this.refillsRemaining = refillsAuthorized;
        this.refillsAuthorized = refillsAuthorized;
        this.instructions = instructions;
        this.prescribedDate = LocalDate.now();
        this.expiryDate = LocalDate.now().plusYears(1); // Prescriptions valid for 1 year
        this.pharmacyId = pharmacyId;
        this.status = PrescriptionStatus.ACTIVE;
        this.refillDates = new ArrayList<>();
        this.policyNumber = policyNumber;
    }
    
    /**
     * Simple constructor
     */
    public Prescription(String patientId, String medicationId, String dosage, int quantity) {
        this.prescriptionNumber = generatePrescriptionNumber();
        this.patientId = patientId;
        this.doctorId = "";
        this.medicationId = medicationId;
        this.dosage = dosage;
        this.quantity = quantity;
        this.refillsRemaining = 3;
        this.refillsAuthorized = 3;
        this.instructions = "";
        this.prescribedDate = LocalDate.now();
        this.expiryDate = LocalDate.now().plusYears(1);
        this.pharmacyId = "";
        this.status = PrescriptionStatus.ACTIVE;
        this.refillDates = new ArrayList<>();
        this.policyNumber = "";
    }
    
    /**
     * Default constructor
     */
    public Prescription() {
        this.prescriptionNumber = generatePrescriptionNumber();
        this.patientId = "";
        this.doctorId = "";
        this.medicationId = "";
        this.dosage = "";
        this.quantity = 0;
        this.refillsRemaining = 0;
        this.refillsAuthorized = 0;
        this.instructions = "";
        this.prescribedDate = LocalDate.now();
        this.expiryDate = LocalDate.now().plusYears(1);
        this.pharmacyId = "";
        this.status = PrescriptionStatus.ACTIVE;
        this.refillDates = new ArrayList<>();
        this.policyNumber = "";
    }
    
    /**
     * Generate unique prescription number
     */
    private String generatePrescriptionNumber() {
        int year = LocalDate.now().getYear();
        int random = (int)(Math.random() * 1000000);
        return String.format("RX-%d-%06d", year, random);
    }
    
    // Getters
    public String getPrescriptionNumber() {
        return prescriptionNumber;
    }
    
    public String getPatientId() {
        return patientId;
    }
    
    public String getDoctorId() {
        return doctorId;
    }
    
    public String getMedicationId() {
        return medicationId;
    }
    
    public String getDosage() {
        return dosage;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public int getRefillsRemaining() {
        return refillsRemaining;
    }
    
    public int getRefillsAuthorized() {
        return refillsAuthorized;
    }
    
    public String getInstructions() {
        return instructions;
    }
    
    public LocalDate getPrescribedDate() {
        return prescribedDate;
    }
    
    public LocalDate getExpiryDate() {
        return expiryDate;
    }
    
    public String getPharmacyId() {
        return pharmacyId;
    }
    
    public PrescriptionStatus getStatus() {
        return status;
    }
    
    public List<LocalDate> getRefillDates() {
        return new ArrayList<>(refillDates);
    }
    
    public String getPolicyNumber() {
        return policyNumber;
    }
    
    // Setters
    public void setPrescriptionNumber(String prescriptionNumber) {
        this.prescriptionNumber = prescriptionNumber;
    }
    
    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }
    
    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }
    
    public void setMedicationId(String medicationId) {
        this.medicationId = medicationId;
    }
    
    public void setDosage(String dosage) {
        this.dosage = dosage;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    public void setRefillsRemaining(int refillsRemaining) {
        this.refillsRemaining = refillsRemaining;
    }
    
    public void setRefillsAuthorized(int refillsAuthorized) {
        this.refillsAuthorized = refillsAuthorized;
    }
    
    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }
    
    public void setPrescribedDate(LocalDate prescribedDate) {
        this.prescribedDate = prescribedDate;
    }
    
    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }
    
    public void setPharmacyId(String pharmacyId) {
        this.pharmacyId = pharmacyId;
    }
    
    public void setStatus(PrescriptionStatus status) {
        this.status = status;
    }
    
    public void setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
    }
    
    /**
     * Process a refill
     */
    public boolean processRefill() {
        if (!canRefill()) {
            return false;
        }
        
        refillsRemaining--;
        refillDates.add(LocalDate.now());
        
        // If no refills left, mark as completed
        if (refillsRemaining == 0) {
            status = PrescriptionStatus.COMPLETED;
        }
        
        return true;
    }
    
    /**
     * Check if prescription can be refilled
     */
    public boolean canRefill() {
        return refillsRemaining > 0 && 
               !isExpired() && 
               status == PrescriptionStatus.ACTIVE;
    }
    
    /**
     * Check if prescription is expired
     */
    public boolean isExpired() {
        return LocalDate.now().isAfter(expiryDate) || status == PrescriptionStatus.EXPIRED;
    }
    
    /**
     * Cancel prescription
     */
    public void cancel() {
        this.status = PrescriptionStatus.CANCELLED;
    }
    
    /**
     * Get total number of refills processed
     */
    public int getRefillsProcessed() {
        return refillsAuthorized - refillsRemaining;
    }
    
    /**
     * Get formatted prescribed date
     */
    public String getFormattedPrescribedDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        return prescribedDate.format(formatter);
    }
    
    /**
     * Get formatted expiry date
     */
    public String getFormattedExpiryDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        return expiryDate.format(formatter);
    }
    
    /**
     * Get refill status message
     */
    public String getRefillStatusMessage() {
        if (refillsRemaining == 0) {
            return "No refills remaining - Contact your doctor";
        } else if (refillsRemaining == 1) {
            return "1 refill remaining";
        } else {
            return refillsRemaining + " refills remaining";
        }
    }
    
    /**
     * Check if ready for refill (less than 2 refills left)
     */
    public boolean isReadyForRefill() {
        return refillsRemaining > 0 && refillsRemaining <= 2 && status == PrescriptionStatus.ACTIVE;
    }
    
    /**
     * Validate prescription data
     */
    public boolean isValid() {
        return !prescriptionNumber.isEmpty() && 
               !patientId.isEmpty() && 
               !medicationId.isEmpty() && 
               !dosage.isEmpty() && 
               quantity > 0 && 
               refillsAuthorized >= 0 &&
               prescribedDate != null && 
               expiryDate != null;
    }
    
    @Override
    public String toString() {
        return prescriptionNumber + " - " + dosage + " (" + refillsRemaining + " refills)";
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Prescription rx = (Prescription) obj;
        return prescriptionNumber.equals(rx.prescriptionNumber);
    }
    
    @Override
    public int hashCode() {
        return prescriptionNumber.hashCode();
    }
}
