package business;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import model.*;

/**
 * Complete SystemData class - THE CENTRAL HUB
 * This is a Singleton that holds ALL data directories
 * All panels access data through this single instance
 */
public class SystemData implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    // Singleton instance
    private static SystemData instance;
    
    // All Directory Objects (shared across entire application)
    private EnterpriseDirectory enterpriseDirectory;
    private OrganizationDirectory organizationDirectory;
    private UserDirectory userDirectory;
    private PolicyDirectory policyDirectory;
    private ClaimDirectory claimDirectory;
    private PrescriptionDirectory prescriptionDirectory;
    
    // Current logged-in user
    private User currentUser;
    
    /**
     * Private constructor (Singleton pattern)
     */
    private SystemData() {
        // Initialize all directories
        this.enterpriseDirectory = new EnterpriseDirectory();
        this.organizationDirectory = new OrganizationDirectory();
        this.userDirectory = new UserDirectory();
        this.policyDirectory = new PolicyDirectory();
        this.claimDirectory = new ClaimDirectory();
        this.prescriptionDirectory = new PrescriptionDirectory();
        this.currentUser = null;
    }
    
    /**
     * Get the single instance (Singleton)
     */
    public static SystemData getInstance() {
        if (instance == null) {
            instance = new SystemData();
        }
        return instance;
    }
    
    /**
     * Reset instance (for testing/logout)
     */
    public static void resetInstance() {
        instance = new SystemData();
    }
    
    // ==================== GETTERS FOR ALL DIRECTORIES ====================
    
    public EnterpriseDirectory getEnterpriseDirectory() {
        return enterpriseDirectory;
    }
    
    public OrganizationDirectory getOrganizationDirectory() {
        return organizationDirectory;
    }
    
    public UserDirectory getUserDirectory() {
        return userDirectory;
    }
    
    public PolicyDirectory getPolicyDirectory() {
        return policyDirectory;
    }
    
    public ClaimDirectory getClaimDirectory() {
        return claimDirectory;
    }
    
    public PrescriptionDirectory getPrescriptionDirectory() {
        return prescriptionDirectory;
    }
    
    // ==================== CURRENT USER MANAGEMENT ====================
    
    /**
     * Get current logged-in user
     */
    public User getCurrentUser() {
        return currentUser;
    }
    
    /**
     * Set current user (after login)
     */
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }
    
    /**
     * Logout current user
     */
    public void logout() {
        this.currentUser = null;
    }
    
    /**
     * Check if user is logged in
     */
    public boolean isUserLoggedIn() {
        return currentUser != null;
    }
    
    /**
     * Get current user's full name
     */
    public String getCurrentUserFullName() {
        if (currentUser != null && currentUser.getPerson() != null) {
            return currentUser.getPerson().getFullName();
        }
        return "Guest";
    }
    
    /**
     * Get current user's role
     */
    public Role getCurrentUserRole() {
        if (currentUser != null) {
            return currentUser.getRole();
        }
        return null;
    }
    
    /**
     * Check if current user is admin
     */
    public boolean isCurrentUserAdmin() {
        return currentUser != null && currentUser.getRole().isAdminRole();
    }
    
    /**
     * Check if current user is patient
     */
    public boolean isCurrentUserPatient() {
        return currentUser != null && currentUser.getRole().isPatient();
    }
    
    /**
     * Get current patient ID (if current user is patient)
     */
    public String getCurrentPatientId() {
        if (isCurrentUserPatient() && currentUser.getPerson() != null) {
            return currentUser.getPerson().getPersonId();
        }
        return null;
    }
    
    // ==================== ENTERPRISE OPERATIONS ====================
    
    /**
     * Create new enterprise
     */
    // ==================== ENTERPRISE OPERATIONS ====================
    
    /**
     * Create new enterprise
     */
    public Enterprise createEnterprise(String name, Enterprise.EnterpriseType type) {
        return enterpriseDirectory.createEnterprise(name, type);
    }
    
    /**
     * Get all enterprises
     */
    public List<Enterprise> getAllEnterprises() {
        return enterpriseDirectory.getAllEnterprises();
    }
    
    /**
     * Find enterprise by ID
     */
    public Enterprise findEnterpriseById(String id) {
        return enterpriseDirectory.findEnterpriseById(id);
    }
    
    // ==================== ORGANIZATION OPERATIONS ====================
    
    /**
     * Create new organization
     */
    public Organization createOrganization(String name, Organization.OrganizationType type, String enterpriseId) {
        Organization org = organizationDirectory.createOrganization(name, type, enterpriseId);
        
        // Also add to enterprise
        Enterprise enterprise = enterpriseDirectory.findEnterpriseById(enterpriseId);
        if (enterprise != null) {
            enterprise.addOrganization(org.getOrganizationId());
        }
        
        return org;
    }
    
    /**
     * Get all organizations
     */
    public List<Organization> getAllOrganizations() {
        return organizationDirectory.getAllOrganizations();
    }
    
    /**
     * Get organizations by enterprise
     */
    public List<Organization> getOrganizationsByEnterprise(String enterpriseId) {
        return organizationDirectory.getOrganizationsByEnterprise(enterpriseId);
    }
    
    // ==================== POLICY OPERATIONS ====================
    
    /**
     * Get policies for current patient
     */
    public java.util.List<Policy> getCurrentPatientPolicies() {
        String patientId = getCurrentPatientId();
        if (patientId != null) {
            return policyDirectory.getPoliciesByPatient(patientId);
        }
        return new java.util.ArrayList<>();
    }
    
    /**
     * Get active policies for current patient
     */
    public java.util.List<Policy> getCurrentPatientActivePolicies() {
        String patientId = getCurrentPatientId();
        if (patientId != null) {
            return policyDirectory.getActivePoliciesByPatient(patientId);
        }
        return new java.util.ArrayList<>();
    }
    
    /**
     * Get active policy count for current patient
     */
    public int getCurrentPatientActivePolicyCount() {
        String patientId = getCurrentPatientId();
        if (patientId != null) {
            return policyDirectory.getActivePolicyCountForPatient(patientId);
        }
        return 0;
    }
    
    // ==================== CLAIM OPERATIONS ====================
    
    /**
     * Submit new claim for current patient
     */
    public Claim submitClaim(String policyNumber, LocalDate serviceDate, String providerName,
                            String diagnosis, Claim.ServiceType serviceType, double claimAmount) {
        String patientId = getCurrentPatientId();
        if (patientId != null) {
            return claimDirectory.createClaim(policyNumber, patientId, serviceDate, 
                                             providerName, diagnosis, serviceType, claimAmount);
        }
        return null;
    }
    
    /**
     * Get claims for current patient
     */
    public java.util.List<Claim> getCurrentPatientClaims() {
        String patientId = getCurrentPatientId();
        if (patientId != null) {
            return claimDirectory.getClaimsByPatient(patientId);
        }
        return new java.util.ArrayList<>();
    }
    
    /**
     * Get pending claim count for current patient
     */
    public int getCurrentPatientPendingClaimCount() {
        String patientId = getCurrentPatientId();
        if (patientId != null) {
            return claimDirectory.getPendingClaimCountForPatient(patientId);
        }
        return 0;
    }
    
    /**
     * Get approved claim count for current patient
     */
    public int getCurrentPatientApprovedClaimCount() {
        String patientId = getCurrentPatientId();
        if (patientId != null) {
            return claimDirectory.getApprovedClaimCountForPatient(patientId);
        }
        return 0;
    }
    
    /**
     * Get denied claim count for current patient
     */
    public int getCurrentPatientDeniedClaimCount() {
        String patientId = getCurrentPatientId();
        if (patientId != null) {
            return claimDirectory.getDeniedClaimCountForPatient(patientId);
        }
        return 0;
    }
    
    /**
     * Withdraw claim
     */
    public boolean withdrawClaim(String claimNumber) {
        return claimDirectory.withdrawClaim(claimNumber);
    }
    
    // ==================== PRESCRIPTION OPERATIONS ====================
    
    /**
     * Get prescriptions for current patient
     */
    public java.util.List<Prescription> getCurrentPatientPrescriptions() {
        String patientId = getCurrentPatientId();
        if (patientId != null) {
            return prescriptionDirectory.getPrescriptionsByPatient(patientId);
        }
        return new java.util.ArrayList<>();
    }
    
    /**
     * Get active prescriptions for current patient
     */
    public java.util.List<Prescription> getCurrentPatientActivePrescriptions() {
        String patientId = getCurrentPatientId();
        if (patientId != null) {
            return prescriptionDirectory.getActivePrescriptionsByPatient(patientId);
        }
        return new java.util.ArrayList<>();
    }
    
    /**
     * Get active prescription count for current patient
     */
    public int getCurrentPatientActivePrescriptionCount() {
        String patientId = getCurrentPatientId();
        if (patientId != null) {
            return prescriptionDirectory.getActivePrescriptionCountForPatient(patientId);
        }
        return 0;
    }
    
    /**
     * Get ready for refill count for current patient
     */
    public int getCurrentPatientReadyForRefillCount() {
        String patientId = getCurrentPatientId();
        if (patientId != null) {
            return prescriptionDirectory.getReadyForRefillCountForPatient(patientId);
        }
        return 0;
    }
    
    /**
     * Get total medications for current patient
     */
    public int getCurrentPatientTotalMedicationCount() {
        String patientId = getCurrentPatientId();
        if (patientId != null) {
            return prescriptionDirectory.getPrescriptionCountForPatient(patientId);
        }
        return 0;
    }
    
    /**
     * Request prescription refill
     */
    public boolean requestRefill(String prescriptionNumber) {
        return prescriptionDirectory.processRefill(prescriptionNumber);
    }
    
    // ==================== SYSTEM STATISTICS ====================
    
    /**
     * Get total enterprise count
     */
    public int getTotalEnterpriseCount() {
        return enterpriseDirectory.getEnterpriseCount();
    }
    
    /**
     * Get total organization count
     */
    public int getTotalOrganizationCount() {
        return organizationDirectory.getOrganizationCount();
    }
    
    /**
     * Get total user count
     */
    public int getTotalUserCount() {
        return userDirectory.getUserCount();
    }
    
    /**
     * Get total policy count
     */
    public int getTotalPolicyCount() {
        return policyDirectory.getPolicyCount();
    }
    
    /**
     * Get total claim count
     */
    public int getTotalClaimCount() {
        return claimDirectory.getClaimCount();
    }
    
    /**
     * Get total prescription count
     */
    public int getTotalPrescriptionCount() {
        return prescriptionDirectory.getAllPrescriptions().size();
    }
    
    /**
     * Reset all data (for testing)
     */
    public void resetAllData() {
        enterpriseDirectory.clearAll();
        organizationDirectory.clearAll();
        userDirectory.clearAll();
        policyDirectory.reloadSampleData();
        claimDirectory.reloadSampleData();
        prescriptionDirectory.reloadSampleData();
        currentUser = null;
    }

    /**
     * Initialize sample data for a patient (called after login)
     */
    public void initializeSampleDataForPatient(String patientId) {
        // Create sample policies for this patient
        policyDirectory.createSamplePoliciesForPatient(patientId);
        
        // Create sample claims for this patient
        claimDirectory.createSampleClaimsForPatient(patientId);
        
        // Create sample prescriptions for this patient
        prescriptionDirectory.createSamplePrescriptionsForPatient(patientId);
    }
    
    
    /**
     * Save current system data to file
     */
    public boolean saveToFile() {
        return utils.FileHandler.saveSystemData(this);
    }
    
    /**
     * Load system data from file
     */
    public static SystemData loadFromFile() {
        SystemData loadedData = utils.FileHandler.loadSystemData();
        if (loadedData != null) {
            instance = loadedData;
            return instance;
        }
        return null;
    }
    
    /**
     * Check if saved data exists
     */
    public static boolean hasSavedData() {
        return utils.FileHandler.dataFileExists();
    }
}
