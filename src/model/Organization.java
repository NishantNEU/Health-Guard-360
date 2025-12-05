package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Complete Organization class
 * Represents departments, divisions, branches, or units within an enterprise
 */
public class Organization implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    // Organization Types
    public enum OrganizationType {
        DEPARTMENT("Department"),
        DIVISION("Division"),
        BRANCH("Branch"),
        UNIT("Unit");
        
        private final String displayName;
        
        OrganizationType(String displayName) {
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
    private String organizationId;
    private String organizationName;
    private OrganizationType organizationType;
    private String enterpriseId; // ID of parent enterprise
    private LocalDate createdDate;
    private List<String> employeeIds; // List of employee IDs in this organization
    private boolean isActive;
    
    /**
     * Complete constructor
     */
    public Organization(String organizationName, OrganizationType organizationType, String enterpriseId) {
        this.organizationId = generateOrganizationId();
        this.organizationName = organizationName;
        this.organizationType = organizationType;
        this.enterpriseId = enterpriseId;
        this.createdDate = LocalDate.now();
        this.employeeIds = new ArrayList<>();
        this.isActive = true;
    }
    
    /**
     * Default constructor
     */
    public Organization() {
        this.organizationId = generateOrganizationId();
        this.organizationName = "";
        this.organizationType = OrganizationType.DEPARTMENT;
        this.enterpriseId = "";
        this.createdDate = LocalDate.now();
        this.employeeIds = new ArrayList<>();
        this.isActive = true;
    }
    
    /**
     * Generate unique organization ID
     */
    private String generateOrganizationId() {
        return "ORG-" + System.currentTimeMillis();
    }
    
    // Getters
    public String getOrganizationId() {
        return organizationId;
    }
    
    public String getOrganizationName() {
        return organizationName;
    }
    
    public OrganizationType getOrganizationType() {
        return organizationType;
    }
    
    public String getEnterpriseId() {
        return enterpriseId;
    }
    
    public LocalDate getCreatedDate() {
        return createdDate;
    }
    
    public List<String> getEmployeeIds() {
        return new ArrayList<>(employeeIds);
    }
    
    public boolean isActive() {
        return isActive;
    }
    
    // Setters
    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }
    
    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }
    
    public void setOrganizationType(OrganizationType organizationType) {
        this.organizationType = organizationType;
    }
    
    public void setEnterpriseId(String enterpriseId) {
        this.enterpriseId = enterpriseId;
    }
    
    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }
    
    public void setActive(boolean active) {
        isActive = active;
    }
    
    /**
     * Add employee to organization
     */
    public void addEmployee(String employeeId) {
        if (!employeeIds.contains(employeeId)) {
            employeeIds.add(employeeId);
        }
    }
    
    /**
     * Remove employee from organization
     */
    public void removeEmployee(String employeeId) {
        employeeIds.remove(employeeId);
    }
    
    /**
     * Get employee count
     */
    public int getEmployeeCount() {
        return employeeIds.size();
    }
    
    /**
     * Check if organization has employees
     */
    public boolean hasEmployees() {
        return !employeeIds.isEmpty();
    }
    
    /**
     * Deactivate organization
     */
    public void deactivate() {
        this.isActive = false;
    }
    
    /**
     * Activate organization
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
     * Validate organization data
     */
    public boolean isValid() {
        return !organizationName.isEmpty() && 
               organizationType != null && 
               !enterpriseId.isEmpty();
    }
    
    @Override
    public String toString() {
        return organizationName + " (" + organizationType.getDisplayName() + ")";
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Organization org = (Organization) obj;
        return organizationId.equals(org.organizationId);
    }
    
    @Override
    public int hashCode() {
        return organizationId.hashCode();
    }
}