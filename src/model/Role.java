package model;

import java.io.Serializable;

/**
 * Complete Role enum for all user types in the system
 */
public enum Role implements Serializable {
    
    // System Level
    SYSTEM_ADMIN("System Administrator", "Full system access and configuration"),
    
    // Enterprise Level - Hospital
    HOSPITAL_ADMIN("Hospital Administrator", "Manages hospital operations"),
    DOCTOR("Doctor/Healthcare Provider", "Creates prescriptions and treats patients"),
    NURSE("Nurse", "Assists in patient care"),
    
    // Enterprise Level - Insurance Provider
    INSURANCE_ADMIN("Insurance Administrator", "Manages insurance company operations"),
    CLAIMS_PROCESSOR("Claims Processor", "Reviews and processes insurance claims"),
    UNDERWRITER("Underwriter", "Evaluates and approves policies"),
    
    // Enterprise Level - Pharmacy
    PHARMACY_ADMIN("Pharmacy Administrator", "Manages pharmacy operations"),
    PHARMACIST("Pharmacist", "Dispenses medications and manages prescriptions"),
    PHARMACY_TECHNICIAN("Pharmacy Technician", "Assists pharmacists"),
    
    // Enterprise Level - Pharmaceutical Supplier
    SUPPLIER_ADMIN("Supplier Administrator", "Manages pharmaceutical supply operations"),
    SUPPLIER_MANAGER("Supply Chain Manager", "Manages inventory and orders"),
    
    // Customer/Patient
    PATIENT("Patient", "Healthcare customer with insurance coverage");
    
    // Properties
    private final String displayName;
    private final String description;
    
    /**
     * Constructor
     */
    Role(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }
    
    /**
     * Get display name
     */
    public String getDisplayName() {
        return displayName;
    }
    
    /**
     * Get description
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Check if role is admin level
     */
    public boolean isAdminRole() {
        return this == SYSTEM_ADMIN || 
               this == HOSPITAL_ADMIN || 
               this == INSURANCE_ADMIN || 
               this == PHARMACY_ADMIN || 
               this == SUPPLIER_ADMIN;
    }
    
    /**
     * Check if role is patient
     */
    public boolean isPatient() {
        return this == PATIENT;
    }
    
    /**
     * Check if role can process claims
     */
    public boolean canProcessClaims() {
        return this == CLAIMS_PROCESSOR || this == INSURANCE_ADMIN || this == SYSTEM_ADMIN;
    }
    
    /**
     * Check if role can prescribe medications
     */
    public boolean canPrescribe() {
        return this == DOCTOR || this == NURSE;
    }
    
    /**
     * Check if role can dispense medications
     */
    public boolean canDispense() {
        return this == PHARMACIST || this == PHARMACY_TECHNICIAN || this == PHARMACY_ADMIN;
    }
    
    @Override
    public String toString() {
        return displayName;
    }
    
    /**
     * Get role by display name
     */
    public static Role fromDisplayName(String displayName) {
        for (Role role : Role.values()) {
            if (role.displayName.equalsIgnoreCase(displayName)) {
                return role;
            }
        }
        return null;
    }
}