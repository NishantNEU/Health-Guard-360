package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Complete Patient class
 * Extends Person, adds medical and insurance information
 */
public class Patient extends Person implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    // Properties
    private String patientId;
    private String bloodType;
    private List<String> allergies;
    private List<String> chronicConditions;
    private String emergencyContactName;
    private String emergencyContactPhone;
    private String insuranceProvider;
    private List<String> policyNumbers; // List of policy IDs
    private List<String> claimNumbers; // List of claim IDs
    private List<String> prescriptionNumbers; // List of prescription IDs
    
    /**
     * Complete constructor
     */
    public Patient(String firstName, String lastName, LocalDate dateOfBirth, 
                   String gender, String email, String phoneNumber, Address address,
                   String bloodType, String emergencyContactName, String emergencyContactPhone) {
        super(firstName, lastName, dateOfBirth, gender, email, phoneNumber, address);
        this.patientId = generatePatientId();
        this.bloodType = bloodType;
        this.emergencyContactName = emergencyContactName;
        this.emergencyContactPhone = emergencyContactPhone;
        this.allergies = new ArrayList<>();
        this.chronicConditions = new ArrayList<>();
        this.insuranceProvider = "";
        this.policyNumbers = new ArrayList<>();
        this.claimNumbers = new ArrayList<>();
        this.prescriptionNumbers = new ArrayList<>();
    }
    
    /**
     * Default constructor
     */
    public Patient() {
        super();
        this.patientId = generatePatientId();
        this.bloodType = "Unknown";
        this.allergies = new ArrayList<>();
        this.chronicConditions = new ArrayList<>();
        this.emergencyContactName = "";
        this.emergencyContactPhone = "";
        this.insuranceProvider = "";
        this.policyNumbers = new ArrayList<>();
        this.claimNumbers = new ArrayList<>();
        this.prescriptionNumbers = new ArrayList<>();
    }
    
    /**
     * Generate unique patient ID
     */
    private String generatePatientId() {
        return "PAT-" + System.currentTimeMillis();
    }
    
    // Getters
    public String getPatientId() {
        return patientId;
    }
    
    public String getBloodType() {
        return bloodType;
    }
    
    public List<String> getAllergies() {
        return new ArrayList<>(allergies);
    }
    
    public List<String> getChronicConditions() {
        return new ArrayList<>(chronicConditions);
    }
    
    public String getEmergencyContactName() {
        return emergencyContactName;
    }
    
    public String getEmergencyContactPhone() {
        return emergencyContactPhone;
    }
    
    public String getInsuranceProvider() {
        return insuranceProvider;
    }
    
    public List<String> getPolicyNumbers() {
        return new ArrayList<>(policyNumbers);
    }
    
    public List<String> getClaimNumbers() {
        return new ArrayList<>(claimNumbers);
    }
    
    public List<String> getPrescriptionNumbers() {
        return new ArrayList<>(prescriptionNumbers);
    }
    
    // Setters
    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }
    
    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }
    
    public void setEmergencyContactName(String emergencyContactName) {
        this.emergencyContactName = emergencyContactName;
    }
    
    public void setEmergencyContactPhone(String emergencyContactPhone) {
        this.emergencyContactPhone = emergencyContactPhone;
    }
    
    public void setInsuranceProvider(String insuranceProvider) {
        this.insuranceProvider = insuranceProvider;
    }
    
    /**
     * Add allergy
     */
    public void addAllergy(String allergy) {
        if (!allergies.contains(allergy)) {
            allergies.add(allergy);
        }
    }
    
    /**
     * Remove allergy
     */
    public void removeAllergy(String allergy) {
        allergies.remove(allergy);
    }
    
    /**
     * Add chronic condition
     */
    public void addChronicCondition(String condition) {
        if (!chronicConditions.contains(condition)) {
            chronicConditions.add(condition);
        }
    }
    
    /**
     * Remove chronic condition
     */
    public void removeChronicCondition(String condition) {
        chronicConditions.remove(condition);
    }
    
    /**
     * Add policy to patient
     */
    public void addPolicy(String policyNumber) {
        if (!policyNumbers.contains(policyNumber)) {
            policyNumbers.add(policyNumber);
        }
    }
    
    /**
     * Remove policy
     */
    public void removePolicy(String policyNumber) {
        policyNumbers.remove(policyNumber);
    }
    
    /**
     * Add claim to patient
     */
    public void addClaim(String claimNumber) {
        if (!claimNumbers.contains(claimNumber)) {
            claimNumbers.add(claimNumber);
        }
    }
    
    /**
     * Add prescription to patient
     */
    public void addPrescription(String prescriptionNumber) {
        if (!prescriptionNumbers.contains(prescriptionNumber)) {
            prescriptionNumbers.add(prescriptionNumber);
        }
    }
    
    /**
     * Get total number of active policies
     */
    public int getActivePolicyCount() {
        return policyNumbers.size();
    }
    
    /**
     * Get total number of claims
     */
    public int getTotalClaimCount() {
        return claimNumbers.size();
    }
    
    /**
     * Get total number of prescriptions
     */
    public int getTotalPrescriptionCount() {
        return prescriptionNumbers.size();
    }
    
    /**
     * Get medical summary
     */
    public String getMedicalSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append("Blood Type: ").append(bloodType).append("\n");
        summary.append("Allergies: ").append(allergies.isEmpty() ? "None" : String.join(", ", allergies)).append("\n");
        summary.append("Chronic Conditions: ").append(chronicConditions.isEmpty() ? "None" : String.join(", ", chronicConditions));
        return summary.toString();
    }
    
    @Override
    public String toString() {
        return getFullName() + " - " + patientId;
    }
}