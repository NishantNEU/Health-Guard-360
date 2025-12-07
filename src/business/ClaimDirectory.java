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
        // Diverse sample claims for dashboard demonstration
        // These are linked to dummy patient IDs to simulate system activity

        // 1. Recent Submitted Claims (Pending Review)
        createClaim("POL-2024-1001", "PAT-101", LocalDate.now().minusDays(1), "City General", "Flu Symptoms",
                ServiceType.DOCTOR_VISIT, 150.0)
                .setClaimNumber("CLM-2025-1001");

        createClaim("POL-2024-1002", "PAT-102", LocalDate.now().minusDays(2), "Metro Pharmacy", "Antibiotics",
                ServiceType.PRESCRIPTION_MEDICATION, 45.0)
                .setClaimNumber("CLM-2025-1002");

        createClaim("POL-2024-1003", "PAT-103", LocalDate.now().minusDays(3), "Valley Hospital", "X-Ray Leg",
                ServiceType.DIAGNOSTIC_TEST, 350.0)
                .setClaimNumber("CLM-2025-1003");

        // 2. Under Review Claims
        Claim c4 = createClaim("POL-2024-1004", "PAT-101", LocalDate.now().minusDays(5), "Dental Care Plus",
                "Root Canal", ServiceType.DENTAL, 1200.0);
        c4.setClaimNumber("CLM-2025-1004");
        c4.moveToUnderReview("EMP-PROC-001");

        Claim c5 = createClaim("POL-2024-1005", "PAT-104", LocalDate.now().minusDays(6), "Eye Vision Center", "Glasses",
                ServiceType.VISION, 400.0);
        c5.setClaimNumber("CLM-2025-1005");
        c5.moveToUnderReview("EMP-PROC-002");

        // 3. Approved Claims (Processed Today/Recently)
        Claim c6 = createClaim("POL-2024-1006", "PAT-102", LocalDate.now().minusDays(10), "City General", "Checkup",
                ServiceType.DOCTOR_VISIT, 200.0);
        c6.setClaimNumber("CLM-2025-1006");
        c6.approveClaim(180.0, "EMP-PROC-001", "Standard coverage");

        Claim c7 = createClaim("POL-2024-1007", "PAT-105", LocalDate.now().minusDays(12), "Ortho Clinic", "Knee Brace",
                ServiceType.PHYSICAL_THERAPY, 150.0);
        c7.setClaimNumber("CLM-2025-1007");
        c7.approveClaim(120.0, "EMP-PROC-003", "80% covered");

        // 4. Paid Claims (High Value)
        Claim c8 = createClaim("POL-2023-2001", "PAT-106", LocalDate.now().minusMonths(1), "Regional Medical Center",
                "Appendectomy", ServiceType.SURGERY, 15000.0);
        c8.setClaimNumber("CLM-2025-2001");
        c8.approveClaim(14000.0, "EMP-PROC-001", "Surgery covered");
        c8.markAsPaid();

        Claim c9 = createClaim("POL-2023-2002", "PAT-107", LocalDate.now().minusMonths(2), "Emergency Care", "ER Visit",
                ServiceType.EMERGENCY_ROOM, 2500.0);
        c9.setClaimNumber("CLM-2025-2002");
        c9.approveClaim(2000.0, "EMP-PROC-002", "ER Co-pay applied");
        c9.markAsPaid();

        // 5. Denied Claims
        Claim c10 = createClaim("POL-2024-1008", "PAT-108", LocalDate.now().minusDays(15), "Luxury Spa", "Massage",
                ServiceType.OTHER, 300.0);
        c10.setClaimNumber("CLM-2025-3001");
        c10.denyClaim("EMP-PROC-001", "Service not covered by policy");

        // 6. More Recent Activity (for visual volume)
        createClaim("POL-2024-1009", "PAT-109", LocalDate.now(), "Quick Care", "Stitches", ServiceType.EMERGENCY_ROOM,
                800.0)
                .setClaimNumber("CLM-2025-4001");

        createClaim("POL-2024-1010", "PAT-110", LocalDate.now(), "City Pharmacy", "Prescription",
                ServiceType.PRESCRIPTION_MEDICATION, 120.0)
                .setClaimNumber("CLM-2025-4002");
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