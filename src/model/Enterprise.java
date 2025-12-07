package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Complete Enterprise class
 * Represents Hospital, Insurance Provider, Pharmacy Chain, or Pharmaceutical
 * Supplier
 */
public class Enterprise implements Serializable {

    private static final long serialVersionUID = 1L;

    // Enterprise Types
    public enum EnterpriseType {
        HOSPITAL("Hospital"),
        INSURANCE_PROVIDER("Insurance Provider"),
        PHARMACY_CHAIN("Pharmacy Chain"),
        PHARMACEUTICAL_SUPPLIER("Pharmaceutical Supplier");

        private final String displayName;

        EnterpriseType(String displayName) {
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
    private String enterpriseId;
    private String enterpriseName;
    private EnterpriseType enterpriseType;
    private Address address;
    private String phoneNumber;
    private String email;
    private LocalDate createdDate;
    private List<String> organizationIds; // List of organization IDs belonging to this enterprise
    private List<String> employeeIds; // List of enterprise-level employee IDs (e.g., Claims Processors)
    private boolean isActive;

    /**
     * Complete constructor
     */
    public Enterprise(String enterpriseName, EnterpriseType enterpriseType,
            Address address, String phoneNumber, String email) {
        this.enterpriseId = generateEnterpriseId();
        this.enterpriseName = enterpriseName;
        this.enterpriseType = enterpriseType;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.createdDate = LocalDate.now();
        this.organizationIds = new ArrayList<>();
        this.employeeIds = new ArrayList<>();
        this.isActive = true;
    }

    /**
     * Simple constructor (for quick creation)
     */
    public Enterprise(String enterpriseName, EnterpriseType enterpriseType) {
        this.enterpriseId = generateEnterpriseId();
        this.enterpriseName = enterpriseName;
        this.enterpriseType = enterpriseType;
        this.address = new Address();
        this.phoneNumber = "";
        this.email = "";
        this.createdDate = LocalDate.now();
        this.organizationIds = new ArrayList<>();
        this.employeeIds = new ArrayList<>();
        this.isActive = true;
    }

    /**
     * Default constructor
     */
    public Enterprise() {
        this.enterpriseId = generateEnterpriseId();
        this.enterpriseName = "";
        this.enterpriseType = EnterpriseType.HOSPITAL;
        this.address = new Address();
        this.phoneNumber = "";
        this.email = "";
        this.createdDate = LocalDate.now();
        this.organizationIds = new ArrayList<>();
        this.isActive = true;
    }

    /**
     * Generate unique enterprise ID
     */
    private String generateEnterpriseId() {
        return "ENT-" + java.util.UUID.randomUUID().toString();
    }

    // Getters
    public String getEnterpriseId() {
        return enterpriseId;
    }

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public EnterpriseType getEnterpriseType() {
        return enterpriseType;
    }

    public Address getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public List<String> getOrganizationIds() {
        return new ArrayList<>(organizationIds);
    }

    public boolean isActive() {
        return isActive;
    }

    // Setters
    public void setEnterpriseId(String enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }

    public void setEnterpriseType(EnterpriseType enterpriseType) {
        this.enterpriseType = enterpriseType;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    /**
     * Add organization to this enterprise
     */
    public void addOrganization(String organizationId) {
        if (!organizationIds.contains(organizationId)) {
            organizationIds.add(organizationId);
        }
    }

    /**
     * Remove organization from this enterprise
     */
    public void removeOrganization(String organizationId) {
        organizationIds.remove(organizationId);
    }

    /**
     * Get number of organizations
     */
    public int getOrganizationCount() {
        return organizationIds.size();
    }

    /**
     * Check if enterprise has organizations
     */
    public boolean hasOrganizations() {
        return !organizationIds.isEmpty();
    }

    /**
     * Add employee to enterprise (for enterprise-level roles)
     */
    public void addEmployee(String employeeId) {
        if (!employeeIds.contains(employeeId)) {
            employeeIds.add(employeeId);
        }
    }

    /**
     * Remove employee from enterprise
     */
    public void removeEmployee(String employeeId) {
        employeeIds.remove(employeeId);
    }

    /**
     * Get employee IDs
     */
    public List<String> getEmployeeIds() {
        return new ArrayList<>(employeeIds);
    }

    /**
     * Get employee count
     */
    public int getEmployeeCount() {
        return employeeIds.size();
    }

    /**
     * Deactivate enterprise
     */
    public void deactivate() {
        this.isActive = false;
    }

    /**
     * Activate enterprise
     */
    public void activate() {
        this.isActive = true;
    }

    /**
     * Get formatted created date
     */
    public String getFormattedCreatedDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        return createdDate.format(formatter);
    }

    /**
     * Validate enterprise data
     */
    public boolean isValid() {
        return !enterpriseName.isEmpty() &&
                enterpriseType != null;
    }

    @Override
    public String toString() {
        return enterpriseName + " (" + enterpriseType.getDisplayName() + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Enterprise enterprise = (Enterprise) obj;
        return enterpriseId.equals(enterprise.enterpriseId);
    }

    @Override
    public int hashCode() {
        return enterpriseId.hashCode();
    }
}