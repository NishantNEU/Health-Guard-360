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
        // Diverse sample policies for dashboard demonstration
        // These correspond to the dummy patients used in ClaimDirectory

        // 1. PAT-101: Family PPO (Active)
        Policy p1 = createPolicy("PAT-101", PolicyType.FAMILY_PPO, 500000.0, 2000.0, 50.0, 450.0, "INS-001",
                LocalDate.now().minusMonths(6), 2);
        p1.setPolicyNumber("POL-2024-1001");
        p1.setPolicyStatus(PolicyStatus.ACTIVE);
        p1.addBeneficiary("Spouse");
        p1.addBeneficiary("Child 1");

        // 2. PAT-102: Individual HMO (Active)
        Policy p2 = createPolicy("PAT-102", PolicyType.INDIVIDUAL_HMO, 250000.0, 1500.0, 30.0, 250.0, "INS-001",
                LocalDate.now().minusMonths(3), 1);
        p2.setPolicyNumber("POL-2024-1002");
        p2.setPolicyStatus(PolicyStatus.ACTIVE);

        // 3. PAT-103: Medicare (Active)
        Policy p3 = createPolicy("PAT-103", PolicyType.MEDICARE, 100000.0, 1000.0, 20.0, 180.0, "INS-001",
                LocalDate.now().minusYears(1), 5);
        p3.setPolicyNumber("POL-2024-1003");
        p3.setPolicyStatus(PolicyStatus.ACTIVE);

        // 4. PAT-104: Dental Application (Active)
        Policy p4 = createPolicy("PAT-104", PolicyType.INDIVIDUAL_PPO, 50000.0, 500.0, 25.0, 80.0, "INS-001",
                LocalDate.now().minusMonths(1), 1);
        p4.setPolicyNumber("POL-2024-1005");
        p4.setPolicyStatus(PolicyStatus.ACTIVE);

        // 5. PAT-105: Expired Policy
        Policy p5 = createPolicy("PAT-105", PolicyType.FAMILY_HMO, 300000.0, 2500.0, 40.0, 350.0, "INS-001",
                LocalDate.now().minusYears(2), 1);
        p5.setPolicyNumber("POL-2022-8001");
        p5.setPolicyStatus(PolicyStatus.EXPIRED);
        p5.setExpiryDate(LocalDate.now().minusDays(30));

        // 6. PAT-106: High Value Policy
        Policy p6 = createPolicy("PAT-106", PolicyType.FAMILY_PPO, 1000000.0, 5000.0, 100.0, 1200.0, "INS-001",
                LocalDate.now().minusMonths(8), 3);
        p6.setPolicyNumber("POL-2023-2001");
        p6.setPolicyStatus(PolicyStatus.ACTIVE);
        p6.addBeneficiary("Spouse");
        p6.addBeneficiary("3 Children");

        // 7. PAT-107: New Policy (Just Started)
        Policy p7 = createPolicy("PAT-107", PolicyType.INDIVIDUAL_PPO, 200000.0, 1200.0, 35.0, 220.0, "INS-001",
                LocalDate.now().minusDays(5), 1);
        p7.setPolicyNumber("POL-2023-2002");
        p7.setPolicyStatus(PolicyStatus.ACTIVE);

        // 8. PAT-108: Another Active Policy
        Policy p8 = createPolicy("PAT-108", PolicyType.INDIVIDUAL_HMO, 150000.0, 1000.0, 25.0, 190.0, "INS-001",
                LocalDate.now().minusMonths(11), 1);
        p8.setPolicyNumber("POL-2024-1008");
        p8.setPolicyStatus(PolicyStatus.ACTIVE);

        // 9. PAT-109: Recent
        Policy p9 = createPolicy("PAT-109", PolicyType.FAMILY_PPO, 400000.0, 2200.0, 45.0, 420.0, "INS-001",
                LocalDate.now().minusMonths(2), 1);
        p9.setPolicyNumber("POL-2024-1009");
        p9.setPolicyStatus(PolicyStatus.ACTIVE);

        // 10. PAT-110: Recent
        Policy p10 = createPolicy("PAT-110", PolicyType.MEDICARE, 120000.0, 800.0, 15.0, 160.0, "INS-001",
                LocalDate.now().minusMonths(4), 10);
        p10.setPolicyNumber("POL-2024-1010");
        p10.setPolicyStatus(PolicyStatus.ACTIVE);
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
        policy2.setExpiryDate(LocalDate.of(2026, 5, 31)); // Extended to 2026
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
     * Update existing policy
     * The policy list already contains references, so changes to policy objects
     * are automatically reflected. This method ensures consistency.
     */
    public void updatePolicy(Policy policy) {
        if (policy != null) {
            // Find and replace the policy
            for (int i = 0; i < policies.size(); i++) {
                if (policies.get(i).getPolicyNumber().equals(policy.getPolicyNumber())) {
                    policies.set(i, policy);
                    return;
                }
            }
        }
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