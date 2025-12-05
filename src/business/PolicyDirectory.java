package business;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import model.Policy;
import model.Policy.PolicyStatus;
import model.Policy.PolicyType;

/**
 * Complete PolicyDirectory class
 * Manages all insurance policies in the system
 */
public class PolicyDirectory implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    // Properties
    private List<Policy> policies;
    
    /**
     * Constructor
     */
    public PolicyDirectory() {
        this.policies = new ArrayList<>();
        createSamplePolicies(); // Create sample data for testing
    }
    
    /**
     * Create sample policies for demonstration
     */
    /**
     * Create sample policies for demonstration
     * NOTE: This should be called AFTER a patient logs in
     */
    private void createSamplePolicies() {
        // Don't create sample policies in constructor
        // We'll create them when we know the patient ID
    }
    
    /**
     * Create sample policies for a specific patient
     */
    public void createSamplePoliciesForPatient(String patientId) {
        // Clear existing policies first
        policies.clear();
        
        // Sample policy 1 - Active Family PPO
        Policy policy1 = new Policy(patientId, PolicyType.FAMILY_PPO, 500000);
        policy1.setPolicyNumber("POL-2024-00123");
        policy1.setDeductible(2000);
        policy1.setCopayment(50);
        policy1.setMonthlyPremium(450);
        policy1.setStartDate(LocalDate.of(2024, 1, 15));
        policy1.setExpiryDate(LocalDate.of(2026, 1, 14));
        policy1.setPolicyStatus(PolicyStatus.ACTIVE);
        policy1.addBeneficiary("Self");
        policy1.addBeneficiary("Spouse");
        policy1.addBeneficiary("2 Children");
        policies.add(policy1);
        
        // Sample policy 2 - Active Individual HMO
        Policy policy2 = new Policy(patientId, PolicyType.INDIVIDUAL_HMO, 250000);
        policy2.setPolicyNumber("POL-2023-00456");
        policy2.setDeductible(1500);
        policy2.setCopayment(30);
        policy2.setMonthlyPremium(250);
        policy2.setStartDate(LocalDate.of(2023, 6, 1));
        policy2.setExpiryDate(LocalDate.of(2025, 5, 31));
        policy2.setPolicyStatus(PolicyStatus.ACTIVE);
        policy2.addBeneficiary("Self");
        policies.add(policy2);
        
        // Sample policy 3 - Expired Medicare
        Policy policy3 = new Policy(patientId, PolicyType.MEDICARE, 100000);
        policy3.setPolicyNumber("POL-2022-00789");
        policy3.setDeductible(1000);
        policy3.setCopayment(20);
        policy3.setMonthlyPremium(180);
        policy3.setStartDate(LocalDate.of(2022, 3, 10));
        policy3.setExpiryDate(LocalDate.of(2024, 3, 9));
        policy3.setPolicyStatus(PolicyStatus.EXPIRED);
        policy3.addBeneficiary("Self");
        policies.add(policy3);
    }
    
    /**
     * Get all policies
     */
    public List<Policy> getAllPolicies() {
        return new ArrayList<>(policies);
    }
    
    /**
     * Create and add new policy
     */
    public Policy createPolicy(String patientId, PolicyType policyType, double coverageAmount,
                              double deductible, double copayment, double monthlyPremium,
                              String insuranceProviderId, LocalDate startDate, int durationYears) {
        Policy policy = new Policy(patientId, policyType, coverageAmount, deductible, copayment, 
                                  insuranceProviderId, startDate, durationYears);
        policy.setMonthlyPremium(monthlyPremium);
        policies.add(policy);
        return policy;
    }
    
    /**
     * Add existing policy
     */
    public void addPolicy(Policy policy) {
        if (!policies.contains(policy)) {
            policies.add(policy);
        }
    }
    
    /**
     * Remove policy
     */
    public boolean removePolicy(String policyNumber) {
        return policies.removeIf(p -> p.getPolicyNumber().equals(policyNumber));
    }
    
    /**
     * Find policy by policy number
     */
    public Policy findPolicyByNumber(String policyNumber) {
        for (Policy policy : policies) {
            if (policy.getPolicyNumber().equals(policyNumber)) {
                return policy;
            }
        }
        return null;
    }
    
    /**
     * Get all policies for a patient
     */
    public List<Policy> getPoliciesByPatient(String patientId) {
        return policies.stream()
                .filter(p -> p.getPatientId().equals(patientId))
                .collect(Collectors.toList());
    }
    
    /**
     * Get active policies for a patient
     */
    public List<Policy> getActivePoliciesByPatient(String patientId) {
        return policies.stream()
                .filter(p -> p.getPatientId().equals(patientId))
                .filter(Policy::isCurrentlyActive)
                .collect(Collectors.toList());
    }
    
    /**
     * Get policies by status
     */
    public List<Policy> getPoliciesByStatus(PolicyStatus status) {
        return policies.stream()
                .filter(p -> p.getPolicyStatus() == status)
                .collect(Collectors.toList());
    }
    
    /**
     * Get policies by type
     */
    public List<Policy> getPoliciesByType(PolicyType type) {
        return policies.stream()
                .filter(p -> p.getPolicyType() == type)
                .collect(Collectors.toList());
    }
    
    /**
     * Get all active policies
     */
    public List<Policy> getAllActivePolicies() {
        return policies.stream()
                .filter(Policy::isCurrentlyActive)
                .collect(Collectors.toList());
    }
    
    /**
     * Get all expired policies
     */
    public List<Policy> getAllExpiredPolicies() {
        return policies.stream()
                .filter(Policy::isExpired)
                .collect(Collectors.toList());
    }
    
    /**
     * Get policy count
     */
    public int getPolicyCount() {
        return policies.size();
    }
    
    /**
     * Get active policy count
     */
    public int getActivePolicyCount() {
        return (int) policies.stream().filter(Policy::isCurrentlyActive).count();
    }
    
    /**
     * Get active policy count for patient
     */
    public int getActivePolicyCountForPatient(String patientId) {
        return (int) policies.stream()
                .filter(p -> p.getPatientId().equals(patientId))
                .filter(Policy::isCurrentlyActive)
                .count();
    }
    
    /**
     * Renew policy
     */
    public boolean renewPolicy(String policyNumber, int years) {
        Policy policy = findPolicyByNumber(policyNumber);
        if (policy != null) {
            policy.renewPolicy(years);
            return true;
        }
        return false;
    }
    
    /**
     * Cancel policy
     */
    public boolean cancelPolicy(String policyNumber) {
        Policy policy = findPolicyByNumber(policyNumber);
        if (policy != null) {
            policy.cancelPolicy();
            return true;
        }
        return false;
    }
    
    /**
     * Get policy numbers for dropdown (UI helper)
     */
    public List<String> getPolicyNumbersForDropdown(String patientId) {
        return policies.stream()
                .filter(p -> p.getPatientId().equals(patientId))
                .filter(Policy::isCurrentlyActive)
                .map(p -> p.getPolicyNumber() + " - " + p.getPolicyType().getDisplayName())
                .collect(Collectors.toList());
    }
    
    /**
     * Calculate total premium revenue
     */
    public double getTotalMonthlyPremiumRevenue() {
        return policies.stream()
                .filter(Policy::isCurrentlyActive)
                .mapToDouble(Policy::getMonthlyPremium)
                .sum();
    }
    
    /**
     * Calculate total annual premium revenue
     */
    public double getTotalAnnualPremiumRevenue() {
        return getTotalMonthlyPremiumRevenue() * 12;
    }
    
    /**
     * Get policies expiring soon (within 30 days)
     */
    public List<Policy> getPoliciesExpiringSoon() {
        LocalDate thirtyDaysFromNow = LocalDate.now().plusDays(30);
        return policies.stream()
                .filter(p -> p.getPolicyStatus() == PolicyStatus.ACTIVE)
                .filter(p -> p.getExpiryDate().isBefore(thirtyDaysFromNow))
                .collect(Collectors.toList());
    }
    
    /**
     * Search policies by patient name (requires access to patient data)
     */
    public List<Policy> searchPoliciesByPolicyNumber(String searchTerm) {
        String lowerSearch = searchTerm.toLowerCase();
        return policies.stream()
                .filter(p -> p.getPolicyNumber().toLowerCase().contains(lowerSearch))
                .collect(Collectors.toList());
    }
    
    /**
     * Clear all policies (for testing)
     */
    public void clearAll() {
        policies.clear();
    }
    
    /**
     * Clear and reload sample data
     */
    public void reloadSampleData() {
        policies.clear();
        createSamplePolicies();
    }
    
    /**
     * Check if directory is empty
     */
    public boolean isEmpty() {
        return policies.isEmpty();
    }
}