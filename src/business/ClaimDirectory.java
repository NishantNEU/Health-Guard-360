package business;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import model.Claim;
import model.Claim.ClaimStatus;
import model.Claim.ServiceType;

/**
 * Complete ClaimDirectory class
 * Manages all insurance claims in the system
 */
public class ClaimDirectory implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    // Properties
    private List<Claim> claims;
    
    /**
     * Constructor
     */
    public ClaimDirectory() {
        this.claims = new ArrayList<>();
        createSampleClaims(); // Create sample data for testing
    }
    
    /**
     * Create sample claims for demonstration
     */
    /**
     * Create sample claims for demonstration
     */
    private void createSampleClaims() {
        // Don't create in constructor - wait for patient login
    }
    
    /**
     * Create sample claims for a specific patient
     */
    public void createSampleClaimsForPatient(String patientId) {
        // Sample claim 1 - Approved
        Claim claim1 = new Claim("POL-2024-00123", patientId, LocalDate.of(2025, 11, 15),
                                "City General Hospital", "Annual Physical Examination", 
                                ServiceType.DOCTOR_VISIT, 350.0);
        claim1.setClaimNumber("CLM-2025-00123");
        claim1.approveClaim(280.0, "EMP-PROC-001", "Standard checkup - Approved");
        claim1.setSubmittedDate(LocalDate.of(2025, 11, 16));
        claim1.setLastUpdatedDate(LocalDate.of(2025, 11, 20));
        claims.add(claim1);
        
        // Sample claim 2 - Under Review
        Claim claim2 = new Claim("POL-2024-00123", patientId, LocalDate.of(2025, 11, 20),
                                "Downtown Clinic", "Blood Work", 
                                ServiceType.DIAGNOSTIC_TEST, 125.0);
        claim2.setClaimNumber("CLM-2025-00456");
        claim2.moveToUnderReview("EMP-PROC-002");
        claim2.setSubmittedDate(LocalDate.of(2025, 11, 21));
        claim2.setLastUpdatedDate(LocalDate.of(2025, 11, 22));
        claims.add(claim2);
        
        // Sample claim 3 - Paid
        Claim claim3 = new Claim("POL-2023-00456", patientId, LocalDate.of(2025, 10, 28),
                                "Metro Hospital", "Emergency Room Visit", 
                                ServiceType.EMERGENCY_ROOM, 1200.0);
        claim3.setClaimNumber("CLM-2025-00234");
        claim3.approveClaim(960.0, "EMP-PROC-001", "Emergency treatment - Approved 80%");
        claim3.markAsPaid();
        claim3.setSubmittedDate(LocalDate.of(2025, 10, 29));
        claim3.setLastUpdatedDate(LocalDate.of(2025, 11, 10));
        claims.add(claim3);
        
        // Sample claim 4 - Denied
        Claim claim4 = new Claim("POL-2024-00123", patientId, LocalDate.of(2025, 10, 15),
                                "City Pharmacy", "Over-the-counter medication", 
                                ServiceType.PRESCRIPTION_MEDICATION, 75.0);
        claim4.setClaimNumber("CLM-2025-00567");
        claim4.denyClaim("EMP-PROC-001", "OTC medications not covered under policy");
        claim4.setSubmittedDate(LocalDate.of(2025, 10, 16));
        claim4.setLastUpdatedDate(LocalDate.of(2025, 10, 20));
        claims.add(claim4);
        
        // Sample claim 5 - Submitted
        Claim claim5 = new Claim("POL-2023-00456", patientId, LocalDate.of(2025, 11, 25),
                                "Wellness Center", "Physical Therapy Session", 
                                ServiceType.PHYSICAL_THERAPY, 450.0);
        claim5.setClaimNumber("CLM-2025-00789");
        claim5.setSubmittedDate(LocalDate.of(2025, 11, 26));
        claims.add(claim5);
    }
    
    /**
     * Get all claims
     */
    public List<Claim> getAllClaims() {
        return new ArrayList<>(claims);
    }
    
    /**
     * Create and add new claim
     */
    public Claim createClaim(String policyNumber, String patientId, LocalDate serviceDate,
                            String providerName, String diagnosis, ServiceType serviceType, 
                            double claimAmount) {
        Claim claim = new Claim(policyNumber, patientId, serviceDate, providerName, 
                               diagnosis, serviceType, claimAmount);
        claims.add(claim);
        return claim;
    }
    
    /**
     * Add existing claim
     */
    public void addClaim(Claim claim) {
        if (!claims.contains(claim)) {
            claims.add(claim);
        }
    }
    
    /**
     * Remove claim
     */
    public boolean removeClaim(String claimNumber) {
        return claims.removeIf(c -> c.getClaimNumber().equals(claimNumber));
    }
    
    /**
     * Find claim by claim number
     */
    public Claim findClaimByNumber(String claimNumber) {
        for (Claim claim : claims) {
            if (claim.getClaimNumber().equals(claimNumber)) {
                return claim;
            }
        }
        return null;
    }
    
    /**
     * Get all claims for a patient
     */
    public List<Claim> getClaimsByPatient(String patientId) {
        return claims.stream()
                .filter(c -> c.getPatientId().equals(patientId))
                .collect(Collectors.toList());
    }
    
    /**
     * Get claims by policy
     */
    public List<Claim> getClaimsByPolicy(String policyNumber) {
        return claims.stream()
                .filter(c -> c.getPolicyNumber().equals(policyNumber))
                .collect(Collectors.toList());
    }
    
    /**
     * Get claims by status
     */
    public List<Claim> getClaimsByStatus(ClaimStatus status) {
        return claims.stream()
                .filter(c -> c.getClaimStatus() == status)
                .collect(Collectors.toList());
    }
    
    /**
     * Get claims by patient and status
     */
    public List<Claim> getClaimsByPatientAndStatus(String patientId, ClaimStatus status) {
        return claims.stream()
                .filter(c -> c.getPatientId().equals(patientId))
                .filter(c -> c.getClaimStatus() == status)
                .collect(Collectors.toList());
    }
    
    /**
     * Get pending claims for review
     */
    public List<Claim> getPendingClaims() {
        return claims.stream()
                .filter(c -> c.getClaimStatus() == ClaimStatus.SUBMITTED || 
                            c.getClaimStatus() == ClaimStatus.UNDER_REVIEW)
                .collect(Collectors.toList());
    }
    
    /**
     * Get approved claims
     */
    public List<Claim> getApprovedClaims() {
        return getClaimsByStatus(ClaimStatus.APPROVED);
    }
    
    /**
     * Get denied claims
     */
    public List<Claim> getDeniedClaims() {
        return getClaimsByStatus(ClaimStatus.DENIED);
    }
    
    /**
     * Get paid claims
     */
    public List<Claim> getPaidClaims() {
        return getClaimsByStatus(ClaimStatus.PAID);
    }
    
    /**
     * Get claim count
     */
    public int getClaimCount() {
        return claims.size();
    }
    
    /**
     * Get claim count for patient
     */
    public int getClaimCountForPatient(String patientId) {
        return (int) claims.stream()
                .filter(c -> c.getPatientId().equals(patientId))
                .count();
    }
    
    /**
     * Get claim count by status
     */
    public int getClaimCountByStatus(ClaimStatus status) {
        return (int) claims.stream()
                .filter(c -> c.getClaimStatus() == status)
                .count();
    }
    
    /**
     * Get pending claim count for patient
     */
    public int getPendingClaimCountForPatient(String patientId) {
        return (int) claims.stream()
                .filter(c -> c.getPatientId().equals(patientId))
                .filter(c -> c.getClaimStatus() == ClaimStatus.SUBMITTED || 
                            c.getClaimStatus() == ClaimStatus.UNDER_REVIEW)
                .count();
    }
    
    /**
     * Get approved claim count for patient
     */
    public int getApprovedClaimCountForPatient(String patientId) {
        return (int) claims.stream()
                .filter(c -> c.getPatientId().equals(patientId))
                .filter(c -> c.getClaimStatus() == ClaimStatus.APPROVED || 
                            c.getClaimStatus() == ClaimStatus.PAID)
                .count();
    }
    
    /**
     * Get denied claim count for patient
     */
    public int getDeniedClaimCountForPatient(String patientId) {
        return (int) claims.stream()
                .filter(c -> c.getPatientId().equals(patientId))
                .filter(c -> c.getClaimStatus() == ClaimStatus.DENIED)
                .count();
    }
    
    /**
     * Calculate total claim amount for patient
     */
    public double getTotalClaimAmountForPatient(String patientId) {
        return claims.stream()
                .filter(c -> c.getPatientId().equals(patientId))
                .mapToDouble(Claim::getClaimAmount)
                .sum();
    }
    
    /**
     * Calculate total approved amount
     */
    public double getTotalApprovedAmount() {
        return claims.stream()
                .filter(c -> c.getClaimStatus() == ClaimStatus.APPROVED || 
                            c.getClaimStatus() == ClaimStatus.PAID)
                .mapToDouble(Claim::getApprovedAmount)
                .sum();
    }
    
    /**
     * Withdraw claim
     */
    public boolean withdrawClaim(String claimNumber) {
        Claim claim = findClaimByNumber(claimNumber);
        if (claim != null && claim.canBeWithdrawn()) {
            claim.withdrawClaim();
            return true;
        }
        return false;
    }
    
    /**
     * Approve claim
     */
    public boolean approveClaim(String claimNumber, double approvedAmount, String processorId, String notes) {
        Claim claim = findClaimByNumber(claimNumber);
        if (claim != null) {
            try {
                claim.approveClaim(approvedAmount, processorId, notes);
                return true;
            } catch (IllegalStateException e) {
                return false;
            }
        }
        return false;
    }
    
    /**
     * Deny claim
     */
    public boolean denyClaim(String claimNumber, String processorId, String reason) {
        Claim claim = findClaimByNumber(claimNumber);
        if (claim != null) {
            try {
                claim.denyClaim(processorId, reason);
                return true;
            } catch (IllegalStateException e) {
                return false;
            }
        }
        return false;
    }
    
    /**
     * Clear all claims (for testing)
     */
    public void clearAll() {
        claims.clear();
    }
    
    /**
     * Reload sample data
     */
    public void reloadSampleData() {
        claims.clear();
        createSampleClaims();
    }
    
    /**
     * Check if directory is empty
     */
    public boolean isEmpty() {
        return claims.isEmpty();
    }
}