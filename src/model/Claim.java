package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Complete Claim class with workflow state machine
 * Represents an insurance claim with full lifecycle management
 */
public class Claim implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    // Claim Status (Workflow States)
    public enum ClaimStatus {
        SUBMITTED("Submitted"),
        UNDER_REVIEW("Under Review"),
        APPROVED("Approved"),
        DENIED("Denied"),
        PAID("Paid"),
        WITHDRAWN("Withdrawn");
        
        private final String displayName;
        
        ClaimStatus(String displayName) {
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
    
    // Service Types
    public enum ServiceType {
        EMERGENCY_ROOM("Emergency Room Visit"),
        HOSPITAL_STAY("Hospital Stay"),
        SURGERY("Surgery"),
        DOCTOR_VISIT("Doctor Visit"),
        DIAGNOSTIC_TEST("Diagnostic Test"),
        PRESCRIPTION_MEDICATION("Prescription Medication"),
        PHYSICAL_THERAPY("Physical Therapy"),
        DENTAL("Dental"),
        VISION("Vision"),
        OTHER("Other");
        
        private final String displayName;
        
        ServiceType(String displayName) {
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
    private String claimNumber;
    private String policyNumber; // Policy this claim is for
    private String patientId; // Patient who filed the claim
    private LocalDate serviceDate;
    private String providerName;
    private String diagnosis;
    private ServiceType serviceType;
    private double claimAmount;
    private double approvedAmount;
    private ClaimStatus claimStatus;
    private String processorId; // Employee ID of claims processor
    private String reviewNotes;
    private List<String> documentPaths; // Paths to supporting documents
    private LocalDate submittedDate;
    private LocalDate lastUpdatedDate;
    private LocalDate processedDate;
    
    /**
     * Complete constructor
     */
    public Claim(String policyNumber, String patientId, LocalDate serviceDate, 
                 String providerName, String diagnosis, ServiceType serviceType, double claimAmount) {
        this.claimNumber = generateClaimNumber();
        this.policyNumber = policyNumber;
        this.patientId = patientId;
        this.serviceDate = serviceDate;
        this.providerName = providerName;
        this.diagnosis = diagnosis;
        this.serviceType = serviceType;
        this.claimAmount = claimAmount;
        this.approvedAmount = 0.0;
        this.claimStatus = ClaimStatus.SUBMITTED;
        this.processorId = "";
        this.reviewNotes = "";
        this.documentPaths = new ArrayList<>();
        this.submittedDate = LocalDate.now();
        this.lastUpdatedDate = LocalDate.now();
        this.processedDate = null;
    }
    
    /**
     * Default constructor
     */
    public Claim() {
        this.claimNumber = generateClaimNumber();
        this.policyNumber = "";
        this.patientId = "";
        this.serviceDate = LocalDate.now();
        this.providerName = "";
        this.diagnosis = "";
        this.serviceType = ServiceType.DOCTOR_VISIT;
        this.claimAmount = 0.0;
        this.approvedAmount = 0.0;
        this.claimStatus = ClaimStatus.SUBMITTED;
        this.processorId = "";
        this.reviewNotes = "";
        this.documentPaths = new ArrayList<>();
        this.submittedDate = LocalDate.now();
        this.lastUpdatedDate = LocalDate.now();
        this.processedDate = null;
    }
    
    /**
     * Generate unique claim number
     */
    private String generateClaimNumber() {
        int year = LocalDate.now().getYear();
        int random = (int)(Math.random() * 100000);
        return String.format("CLM-%d-%05d", year, random);
    }
    
    // Getters
    public String getClaimNumber() {
        return claimNumber;
    }
    
    public String getPolicyNumber() {
        return policyNumber;
    }
    
    public String getPatientId() {
        return patientId;
    }
    
    public LocalDate getServiceDate() {
        return serviceDate;
    }
    
    public String getProviderName() {
        return providerName;
    }
    
    public String getDiagnosis() {
        return diagnosis;
    }
    
    public ServiceType getServiceType() {
        return serviceType;
    }
    
    public double getClaimAmount() {
        return claimAmount;
    }
    
    public double getApprovedAmount() {
        return approvedAmount;
    }
    
    public ClaimStatus getClaimStatus() {
        return claimStatus;
    }
    
    public String getProcessorId() {
        return processorId;
    }
    
    public String getReviewNotes() {
        return reviewNotes;
    }
    
    public List<String> getDocumentPaths() {
        return new ArrayList<>(documentPaths);
    }
    
    public LocalDate getSubmittedDate() {
        return submittedDate;
    }
    
    public LocalDate getLastUpdatedDate() {
        return lastUpdatedDate;
    }
    
    public LocalDate getProcessedDate() {
        return processedDate;
    }
    
    // Setters
    public void setClaimNumber(String claimNumber) {
        this.claimNumber = claimNumber;
    }
    
    public void setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
    }
    
    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }
    
    public void setServiceDate(LocalDate serviceDate) {
        this.serviceDate = serviceDate;
    }
    
    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }
    
    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }
    
    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }
    
    public void setClaimAmount(double claimAmount) {
        this.claimAmount = claimAmount;
    }
    
    public void setApprovedAmount(double approvedAmount) {
        this.approvedAmount = approvedAmount;
    }
    
    public void setClaimStatus(ClaimStatus claimStatus) {
        this.claimStatus = claimStatus;
        this.lastUpdatedDate = LocalDate.now();
    }
    
    public void setProcessorId(String processorId) {
        this.processorId = processorId;
    }
    
    public void setReviewNotes(String reviewNotes) {
        this.reviewNotes = reviewNotes;
    }
    
    public void setSubmittedDate(LocalDate submittedDate) {
        this.submittedDate = submittedDate;
    }
    
    public void setLastUpdatedDate(LocalDate lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }
    
    public void setProcessedDate(LocalDate processedDate) {
        this.processedDate = processedDate;
    }
    
    /**
     * Add supporting document
     */
    public void addDocument(String documentPath) {
        if (!documentPaths.contains(documentPath)) {
            documentPaths.add(documentPath);
        }
    }
    
    /**
     * Remove document
     */
    public void removeDocument(String documentPath) {
        documentPaths.remove(documentPath);
    }
    
    /**
     * Move claim to Under Review status
     */
    public void moveToUnderReview(String processorId) {
        if (claimStatus != ClaimStatus.SUBMITTED) {
            throw new IllegalStateException("Can only review submitted claims");
        }
        this.claimStatus = ClaimStatus.UNDER_REVIEW;
        this.processorId = processorId;
        this.lastUpdatedDate = LocalDate.now();
    }
    
    /**
     * Approve claim
     */
    public void approveClaim(double approvedAmount, String processorId, String notes) {
        if (claimStatus != ClaimStatus.UNDER_REVIEW && claimStatus != ClaimStatus.SUBMITTED) {
            throw new IllegalStateException("Can only approve claims under review");
        }
        this.approvedAmount = approvedAmount;
        this.claimStatus = ClaimStatus.APPROVED;
        this.processorId = processorId;
        this.reviewNotes = notes;
        this.processedDate = LocalDate.now();
        this.lastUpdatedDate = LocalDate.now();
    }
    
    /**
     * Deny claim
     */
    public void denyClaim(String processorId, String reasonForDenial) {
        if (claimStatus != ClaimStatus.UNDER_REVIEW && claimStatus != ClaimStatus.SUBMITTED) {
            throw new IllegalStateException("Can only deny claims under review");
        }
        this.approvedAmount = 0.0;
        this.claimStatus = ClaimStatus.DENIED;
        this.processorId = processorId;
        this.reviewNotes = reasonForDenial;
        this.processedDate = LocalDate.now();
        this.lastUpdatedDate = LocalDate.now();
    }
    
    /**
     * Mark claim as paid
     */
    public void markAsPaid() {
        if (claimStatus != ClaimStatus.APPROVED) {
            throw new IllegalStateException("Can only pay approved claims");
        }
        this.claimStatus = ClaimStatus.PAID;
        this.lastUpdatedDate = LocalDate.now();
    }
    
    /**
     * Withdraw claim
     */
    public void withdrawClaim() {
        if (claimStatus == ClaimStatus.APPROVED || claimStatus == ClaimStatus.PAID || claimStatus == ClaimStatus.DENIED) {
            throw new IllegalStateException("Cannot withdraw a claim that has been processed");
        }
        this.claimStatus = ClaimStatus.WITHDRAWN;
        this.lastUpdatedDate = LocalDate.now();
    }
    
    /**
     * Get formatted claim amount
     */
    public String getFormattedClaimAmount() {
        return String.format("$%.2f", claimAmount);
    }
    
    /**
     * Get formatted approved amount
     */
    public String getFormattedApprovedAmount() {
        return String.format("$%.2f", approvedAmount);
    }
    
    /**
     * Get formatted service date
     */
    public String getFormattedServiceDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        return serviceDate.format(formatter);
    }
    
    /**
     * Get formatted submitted date
     */
    public String getFormattedSubmittedDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        return submittedDate.format(formatter);
    }
    
    /**
     * Get formatted last updated date
     */
    public String getFormattedLastUpdatedDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        return lastUpdatedDate.format(formatter);
    }
    
    /**
     * Check if claim can be withdrawn
     */
    public boolean canBeWithdrawn() {
        return claimStatus == ClaimStatus.SUBMITTED || claimStatus == ClaimStatus.UNDER_REVIEW;
    }
    
    /**
     * Validate claim data
     */
    public boolean isValid() {
        return !claimNumber.isEmpty() && 
               !policyNumber.isEmpty() && 
               !patientId.isEmpty() && 
               serviceDate != null && 
               !providerName.isEmpty() && 
               !diagnosis.isEmpty() && 
               serviceType != null && 
               claimAmount > 0;
    }
    
    @Override
    public String toString() {
        return claimNumber + " - " + providerName + " ($" + String.format("%.2f", claimAmount) + ") - " + claimStatus.getDisplayName();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Claim claim = (Claim) obj;
        return claimNumber.equals(claim.claimNumber);
    }
    
    @Override
    public int hashCode() {
        return claimNumber.hashCode();
    }
}