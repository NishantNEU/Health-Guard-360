package model;

import java.io.Serializable;

/**
 * Complete Medication class
 * Represents a pharmaceutical drug with all details
 */
public class Medication implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    // Medication Categories
    public enum MedicationCategory {
        ANTIBIOTIC("Antibiotic"),
        ANTIHYPERTENSIVE("Antihypertensive"),
        ANTIDIABETIC("Antidiabetic"),
        ANALGESIC("Analgesic/Pain Reliever"),
        ANTICOAGULANT("Anticoagulant"),
        ANTIDEPRESSANT("Antidepressant"),
        CHOLESTEROL("Cholesterol Medication"),
        CARDIOVASCULAR("Cardiovascular"),
        RESPIRATORY("Respiratory"),
        GASTROINTESTINAL("Gastrointestinal"),
        HORMONE("Hormone"),
        VITAMIN("Vitamin/Supplement"),
        OTHER("Other");
        
        private final String displayName;
        
        MedicationCategory(String displayName) {
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
    
    // Insurance Tier
    public enum InsuranceTier {
        TIER_1("Tier 1 - Generic", 10.0),
        TIER_2("Tier 2 - Preferred Brand", 25.0),
        TIER_3("Tier 3 - Non-Preferred Brand", 50.0),
        TIER_4("Tier 4 - Specialty", 100.0);
        
        private final String displayName;
        private final double copayAmount;
        
        InsuranceTier(String displayName, double copayAmount) {
            this.displayName = displayName;
            this.copayAmount = copayAmount;
        }
        
        public String getDisplayName() {
            return displayName;
        }
        
        public double getCopayAmount() {
            return copayAmount;
        }
        
        @Override
        public String toString() {
            return displayName;
        }
    }
    
    // Properties
    private String medicationId;
    private String genericName;
    private String brandName;
    private String manufacturer;
    private MedicationCategory category;
    private String strength; // e.g., "10mg", "500mg"
    private String form; // e.g., "Tablet", "Capsule", "Liquid"
    private InsuranceTier insuranceTier;
    private double wholesalePrice;
    private double retailPrice;
    private boolean requiresPrescription;
    private String[] sideEffects;
    private String[] interactions;
    private String dosageInstructions;
    
    /**
     * Complete constructor
     */
    public Medication(String genericName, String brandName, String manufacturer, 
                     MedicationCategory category, String strength, String form,
                     InsuranceTier insuranceTier, double wholesalePrice, double retailPrice) {
        this.medicationId = generateMedicationId();
        this.genericName = genericName;
        this.brandName = brandName;
        this.manufacturer = manufacturer;
        this.category = category;
        this.strength = strength;
        this.form = form;
        this.insuranceTier = insuranceTier;
        this.wholesalePrice = wholesalePrice;
        this.retailPrice = retailPrice;
        this.requiresPrescription = true;
        this.sideEffects = new String[0];
        this.interactions = new String[0];
        this.dosageInstructions = "";
    }
    
    /**
     * Simple constructor
     */
    public Medication(String genericName, String strength, String form) {
        this.medicationId = generateMedicationId();
        this.genericName = genericName;
        this.brandName = genericName;
        this.manufacturer = "Generic";
        this.category = MedicationCategory.OTHER;
        this.strength = strength;
        this.form = form;
        this.insuranceTier = InsuranceTier.TIER_1;
        this.wholesalePrice = 10.0;
        this.retailPrice = 20.0;
        this.requiresPrescription = true;
        this.sideEffects = new String[0];
        this.interactions = new String[0];
        this.dosageInstructions = "";
    }
    
    /**
     * Default constructor
     */
    public Medication() {
        this.medicationId = generateMedicationId();
        this.genericName = "";
        this.brandName = "";
        this.manufacturer = "";
        this.category = MedicationCategory.OTHER;
        this.strength = "";
        this.form = "Tablet";
        this.insuranceTier = InsuranceTier.TIER_1;
        this.wholesalePrice = 0.0;
        this.retailPrice = 0.0;
        this.requiresPrescription = true;
        this.sideEffects = new String[0];
        this.interactions = new String[0];
        this.dosageInstructions = "";
    }
    
    /**
     * Generate unique medication ID
     */
    private String generateMedicationId() {
        return "MED-" + System.currentTimeMillis();
    }
    
    // Getters
    public String getMedicationId() {
        return medicationId;
    }
    
    public String getGenericName() {
        return genericName;
    }
    
    public String getBrandName() {
        return brandName;
    }
    
    public String getManufacturer() {
        return manufacturer;
    }
    
    public MedicationCategory getCategory() {
        return category;
    }
    
    public String getStrength() {
        return strength;
    }
    
    public String getForm() {
        return form;
    }
    
    public InsuranceTier getInsuranceTier() {
        return insuranceTier;
    }
    
    public double getWholesalePrice() {
        return wholesalePrice;
    }
    
    public double getRetailPrice() {
        return retailPrice;
    }
    
    public boolean isRequiresPrescription() {
        return requiresPrescription;
    }
    
    public String[] getSideEffects() {
        return sideEffects;
    }
    
    public String[] getInteractions() {
        return interactions;
    }
    
    public String getDosageInstructions() {
        return dosageInstructions;
    }
    
    // Setters
    public void setMedicationId(String medicationId) {
        this.medicationId = medicationId;
    }
    
    public void setGenericName(String genericName) {
        this.genericName = genericName;
    }
    
    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }
    
    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }
    
    public void setCategory(MedicationCategory category) {
        this.category = category;
    }
    
    public void setStrength(String strength) {
        this.strength = strength;
    }
    
    public void setForm(String form) {
        this.form = form;
    }
    
    public void setInsuranceTier(InsuranceTier insuranceTier) {
        this.insuranceTier = insuranceTier;
    }
    
    public void setWholesalePrice(double wholesalePrice) {
        this.wholesalePrice = wholesalePrice;
    }
    
    public void setRetailPrice(double retailPrice) {
        this.retailPrice = retailPrice;
    }
    
    public void setRequiresPrescription(boolean requiresPrescription) {
        this.requiresPrescription = requiresPrescription;
    }
    
    public void setSideEffects(String[] sideEffects) {
        this.sideEffects = sideEffects;
    }
    
    public void setInteractions(String[] interactions) {
        this.interactions = interactions;
    }
    
    public void setDosageInstructions(String dosageInstructions) {
        this.dosageInstructions = dosageInstructions;
    }
    
    /**
     * Get full medication name
     */
    public String getFullName() {
        if (brandName.isEmpty() || brandName.equals(genericName)) {
            return genericName + " " + strength;
        }
        return brandName + " (" + genericName + ") " + strength;
    }
    
    /**
     * Get patient copay amount based on insurance tier
     */
    public double getPatientCopay() {
        return insuranceTier.getCopayAmount();
    }
    
    /**
     * Get formatted patient copay
     */
    public String getFormattedPatientCopay() {
        return String.format("$%.2f", getPatientCopay());
    }
    
    /**
     * Calculate insurance coverage percentage
     */
    public double getInsuranceCoveragePercentage() {
        if (retailPrice <= 0) {
            return 0.0;
        }
        double coverage = ((retailPrice - getPatientCopay()) / retailPrice) * 100;
        return Math.max(0, Math.min(100, coverage)); // Between 0-100%
    }
    
    /**
     * Get formatted coverage percentage
     */
    public String getFormattedCoveragePercentage() {
        return String.format("%.0f%% covered", getInsuranceCoveragePercentage());
    }
    
    /**
     * Get formatted retail price
     */
    public String getFormattedRetailPrice() {
        return String.format("$%.2f", retailPrice);
    }
    
    /**
     * Validate medication data
     */
    public boolean isValid() {
        return !genericName.isEmpty() && 
               !strength.isEmpty() && 
               !form.isEmpty() && 
               category != null && 
               insuranceTier != null;
    }
    
    @Override
    public String toString() {
        return getFullName();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Medication med = (Medication) obj;
        return medicationId.equals(med.medicationId);
    }
    
    @Override
    public int hashCode() {
        return medicationId.hashCode();
    }
}
