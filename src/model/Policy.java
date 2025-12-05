package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Complete Policy class
 * Represents an insurance policy with all details and calculations
 */
public class Policy implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    // Policy Types
    public enum PolicyType {
        INDIVIDUAL_HMO("Individual HMO"),
        INDIVIDUAL_PPO("Individual PPO"),
        FAMILY_HMO("Family HMO"),
        FAMILY_PPO("Family PPO"),
        GROUP("Group"),
        MEDICARE("Medicare"),
        MEDICAID("Medicaid");
        
        private final String displayName;
        
        PolicyType(String displayName) {
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
    
    // Policy Status
    public enum PolicyStatus {
        ACTIVE("Active"),
        EXPIRED("Expired"),
        CANCELLED("Cancelled"),
        SUSPENDED("Suspended"),
        PENDING("Pending");
        
        private final String displayName;
        
        PolicyStatus(String displayName) {
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
    private String policyNumber;
    private String patientId; // ID of the policyholder
    private PolicyType policyType;
    private PolicyStatus policyStatus;
    private double coverageAmount;
    private double deductible;
    private double copayment;
    private double monthlyPremium;
    private LocalDate startDate;
    private LocalDate expiryDate;
    private String insuranceProviderId; // ID of insurance provider enterprise
    private List<String> beneficiaries; // Names of beneficiaries
    private List<String> claimIds; // List of claim IDs for this policy
    private LocalDate createdDate;
    
    /**
     * Complete constructor
     */
    public Policy(String patientId, PolicyType policyType, double coverageAmount, 
                  double deductible, double copayment, String insuranceProviderId,
                  LocalDate startDate, int durationYears) {
        this.policyNumber = generatePolicyNumber();
        this.patientId = patientId;
        this.policyType = policyType;
        this.policyStatus = PolicyStatus.ACTIVE;
        this.coverageAmount = coverageAmount;
        this.deductible = deductible;
        this.copayment = copayment;
        this.monthlyPremium = 0.0; // Will be calculated
        this.startDate = startDate;
        this.expiryDate = startDate.plusYears(durationYears);
        this.insuranceProviderId = insuranceProviderId;
        this.beneficiaries = new ArrayList<>();
        this.claimIds = new ArrayList<>();
        this.createdDate = LocalDate.now();
    }
    
    /**
     * Simple constructor
     */
    public Policy(String patientId, PolicyType policyType, double coverageAmount) {
        this.policyNumber = generatePolicyNumber();
        this.patientId = patientId;
        this.policyType = policyType;
        this.policyStatus = PolicyStatus.ACTIVE;
        this.coverageAmount = coverageAmount;
        this.deductible = 2000.0;
        this.copayment = 50.0;
        this.monthlyPremium = 0.0;
        this.startDate = LocalDate.now();
        this.expiryDate = LocalDate.now().plusYears(1);
        this.insuranceProviderId = "";
        this.beneficiaries = new ArrayList<>();
        this.claimIds = new ArrayList<>();
        this.createdDate = LocalDate.now();
    }
    
    /**
     * Default constructor
     */
    public Policy() {
        this.policyNumber = generatePolicyNumber();
        this.patientId = "";
        this.policyType = PolicyType.INDIVIDUAL_HMO;
        this.policyStatus = PolicyStatus.PENDING;
        this.coverageAmount = 100000.0;
        this.deductible = 1000.0;
        this.copayment = 30.0;
        this.monthlyPremium = 0.0;
        this.startDate = LocalDate.now();
        this.expiryDate = LocalDate.now().plusYears(1);
        this.insuranceProviderId = "";
        this.beneficiaries = new ArrayList<>();
        this.claimIds = new ArrayList<>();
        this.createdDate = LocalDate.now();
    }
    
    /**
     * Generate unique policy number
     */
    private String generatePolicyNumber() {
        int year = LocalDate.now().getYear();
        int random = (int)(Math.random() * 100000);
        return String.format("POL-%d-%05d", year, random);
    }
    
    // Getters
    public String getPolicyNumber() {
        return policyNumber;
    }
    
    public String getPatientId() {
        return patientId;
    }
    
    public PolicyType getPolicyType() {
        return policyType;
    }
    
    public PolicyStatus getPolicyStatus() {
        return policyStatus;
    }
    
    public double getCoverageAmount() {
        return coverageAmount;
    }
    
    public double getDeductible() {
        return deductible;
    }
    
    public double getCopayment() {
        return copayment;
    }
    
    public double getMonthlyPremium() {
        return monthlyPremium;
    }
    
    public LocalDate getStartDate() {
        return startDate;
    }
    
    public LocalDate getExpiryDate() {
        return expiryDate;
    }
    
    public String getInsuranceProviderId() {
        return insuranceProviderId;
    }
    
    public List<String> getBeneficiaries() {
        return new ArrayList<>(beneficiaries);
    }
    
    public List<String> getClaimIds() {
        return new ArrayList<>(claimIds);
    }
    
    public LocalDate getCreatedDate() {
        return createdDate;
    }
    
    // Setters
    public void setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
    }
    
    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }
    
    public void setPolicyType(PolicyType policyType) {
        this.policyType = policyType;
    }
    
    public void setPolicyStatus(PolicyStatus policyStatus) {
        this.policyStatus = policyStatus;
    }
    
    public void setCoverageAmount(double coverageAmount) {
        this.coverageAmount = coverageAmount;
    }
    
    public void setDeductible(double deductible) {
        this.deductible = deductible;
    }
    
    public void setCopayment(double copayment) {
        this.copayment = copayment;
    }
    
    public void setMonthlyPremium(double monthlyPremium) {
        this.monthlyPremium = monthlyPremium;
    }
    
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
    
    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }
    
    public void setInsuranceProviderId(String insuranceProviderId) {
        this.insuranceProviderId = insuranceProviderId;
    }
    
    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }
    
    /**
     * Add beneficiary
     */
    public void addBeneficiary(String beneficiaryName) {
        if (!beneficiaries.contains(beneficiaryName)) {
            beneficiaries.add(beneficiaryName);
        }
    }
    
    /**
     * Remove beneficiary
     */
    public void removeBeneficiary(String beneficiaryName) {
        beneficiaries.remove(beneficiaryName);
    }
    
    /**
     * Add claim to policy
     */
    public void addClaim(String claimId) {
        if (!claimIds.contains(claimId)) {
            claimIds.add(claimId);
        }
    }
    
    /**
     * Get beneficiaries as formatted string
     */
    public String getBeneficiariesString() {
        if (beneficiaries.isEmpty()) {
            return "None";
        }
        return String.join(", ", beneficiaries);
    }
    
    /**
     * Check if policy is currently active
     */
    public boolean isCurrentlyActive() {
        LocalDate today = LocalDate.now();
        return policyStatus == PolicyStatus.ACTIVE && 
               !today.isBefore(startDate) && 
               !today.isAfter(expiryDate);
    }
    
    /**
     * Check if policy is expired
     */
    public boolean isExpired() {
        return LocalDate.now().isAfter(expiryDate) || policyStatus == PolicyStatus.EXPIRED;
    }
    
    /**
     * Renew policy
     */
    public void renewPolicy(int years) {
        this.startDate = this.expiryDate.plusDays(1);
        this.expiryDate = this.startDate.plusYears(years);
        this.policyStatus = PolicyStatus.ACTIVE;
    }
    
    /**
     * Cancel policy
     */
    public void cancelPolicy() {
        this.policyStatus = PolicyStatus.CANCELLED;
    }
    
    /**
     * Suspend policy
     */
    public void suspendPolicy() {
        this.policyStatus = PolicyStatus.SUSPENDED;
    }
    
    /**
     * Activate policy
     */
    public void activatePolicy() {
        if (isExpired()) {
            throw new IllegalStateException("Cannot activate an expired policy. Please renew it first.");
        }
        this.policyStatus = PolicyStatus.ACTIVE;
    }
    
    /**
     * Get formatted coverage amount
     */
    public String getFormattedCoverageAmount() {
        return String.format("$%,.2f", coverageAmount);
    }
    
    /**
     * Get formatted deductible
     */
    public String getFormattedDeductible() {
        return String.format("$%,.2f", deductible);
    }
    
    /**
     * Get formatted copayment
     */
    public String getFormattedCopayment() {
        return String.format("$%.2f", copayment);
    }
    
    /**
     * Get formatted monthly premium
     */
    public String getFormattedMonthlyPremium() {
        return String.format("$%.2f/month", monthlyPremium);
    }
    
    /**
     * Get formatted start date
     */
    public String getFormattedStartDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        return startDate.format(formatter);
    }
    
    /**
     * Get formatted expiry date
     */
    public String getFormattedExpiryDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        return expiryDate.format(formatter);
    }
    
    /**
     * Get number of claims filed
     */
    public int getClaimCount() {
        return claimIds.size();
    }
    
    /**
     * Calculate annual premium
     */
    public double getAnnualPremium() {
        return monthlyPremium * 12;
    }
    
    /**
     * Get formatted annual premium
     */
    public String getFormattedAnnualPremium() {
        return String.format("$%,.2f/year", getAnnualPremium());
    }
    
    /**
     * Validate policy data
     */
    public boolean isValid() {
        return !policyNumber.isEmpty() && 
               !patientId.isEmpty() && 
               policyType != null && 
               coverageAmount > 0 && 
               deductible >= 0 && 
               copayment >= 0 && 
               startDate != null && 
               expiryDate != null &&
               expiryDate.isAfter(startDate);
    }
    
    @Override
    public String toString() {
        return policyNumber + " - " + policyType.getDisplayName() + " (" + policyStatus.getDisplayName() + ")";
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Policy policy = (Policy) obj;
        return policyNumber.equals(policy.policyNumber);
    }
    
    @Override
    public int hashCode() {
        return policyNumber.hashCode();
    }
}