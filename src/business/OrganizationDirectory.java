package business;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import model.Organization;
import model.Organization.OrganizationType;

/**
 * Complete OrganizationDirectory class
 * Manages all organizations in the system
 */
public class OrganizationDirectory implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    // Properties
    private List<Organization> organizations;
    
    /**
     * Constructor
     */
    public OrganizationDirectory() {
        this.organizations = new ArrayList<>();
    }
    
    /**
     * Get all organizations
     */
    public List<Organization> getAllOrganizations() {
        return new ArrayList<>(organizations);
    }
    
    /**
     * Create and add new organization
     */
    public Organization createOrganization(String name, OrganizationType type, String enterpriseId) {
        Organization organization = new Organization(name, type, enterpriseId);
        organizations.add(organization);
        return organization;
    }
    
    /**
     * Add existing organization
     */
    public void addOrganization(Organization organization) {
        if (!organizations.contains(organization)) {
            organizations.add(organization);
        }
    }
    
    /**
     * Remove organization
     */
    public boolean removeOrganization(String organizationId) {
        return organizations.removeIf(o -> o.getOrganizationId().equals(organizationId));
    }
    
    /**
     * Find organization by ID
     */
    public Organization findOrganizationById(String organizationId) {
        for (Organization organization : organizations) {
            if (organization.getOrganizationId().equals(organizationId)) {
                return organization;
            }
        }
        return null;
    }
    
    /**
     * Find organization by name
     */
    public Organization findOrganizationByName(String name) {
        for (Organization organization : organizations) {
            if (organization.getOrganizationName().equalsIgnoreCase(name)) {
                return organization;
            }
        }
        return null;
    }
    
    /**
     * Get all organizations for a specific enterprise
     */
    public List<Organization> getOrganizationsByEnterprise(String enterpriseId) {
        return organizations.stream()
                .filter(o -> o.getEnterpriseId().equals(enterpriseId))
                .collect(Collectors.toList());
    }
    
    /**
     * Get organizations by type
     */
    public List<Organization> getOrganizationsByType(OrganizationType type) {
        return organizations.stream()
                .filter(o -> o.getOrganizationType() == type)
                .collect(Collectors.toList());
    }
    
    /**
     * Get all active organizations
     */
    public List<Organization> getActiveOrganizations() {
        return organizations.stream()
                .filter(Organization::isActive)
                .collect(Collectors.toList());
    }
    
    /**
     * Get organization count
     */
    public int getOrganizationCount() {
        return organizations.size();
    }
    
    /**
     * Get organization count for enterprise
     */
    public int getOrganizationCountForEnterprise(String enterpriseId) {
        return (int) organizations.stream()
                .filter(o -> o.getEnterpriseId().equals(enterpriseId))
                .count();
    }
    
    /**
     * Check if organization name exists within an enterprise
     */
    public boolean isOrganizationNameExistsInEnterprise(String name, String enterpriseId) {
        return organizations.stream()
                .anyMatch(o -> o.getOrganizationName().equalsIgnoreCase(name) && 
                              o.getEnterpriseId().equals(enterpriseId));
    }
    
    /**
     * Deactivate organization
     */
    public boolean deactivateOrganization(String organizationId) {
        Organization organization = findOrganizationById(organizationId);
        if (organization != null) {
            organization.deactivate();
            return true;
        }
        return false;
    }
    
    /**
     * Activate organization
     */
    public boolean activateOrganization(String organizationId) {
        Organization organization = findOrganizationById(organizationId);
        if (organization != null) {
            organization.activate();
            return true;
        }
        return false;
    }
    
    /**
     * Update organization name
     */
    public boolean updateOrganizationName(String organizationId, String newName) {
        Organization organization = findOrganizationById(organizationId);
        if (organization != null) {
            organization.setOrganizationName(newName);
            return true;
        }
        return false;
    }
    
    /**
     * Get organization names for dropdown (UI helper)
     */
    public List<String> getOrganizationNamesForDropdown() {
        return organizations.stream()
                .map(Organization::getOrganizationName)
                .collect(Collectors.toList());
    }
    
    /**
     * Get organization names for specific enterprise
     */
    public List<String> getOrganizationNamesByEnterprise(String enterpriseId) {
        return organizations.stream()
                .filter(o -> o.getEnterpriseId().equals(enterpriseId))
                .map(Organization::getOrganizationName)
                .collect(Collectors.toList());
    }
    
    /**
     * Search organizations by name (partial match)
     */
    public List<Organization> searchOrganizationsByName(String searchTerm) {
        String lowerSearch = searchTerm.toLowerCase();
        return organizations.stream()
                .filter(o -> o.getOrganizationName().toLowerCase().contains(lowerSearch))
                .collect(Collectors.toList());
    }
    
    /**
     * Clear all organizations (for testing)
     */
    public void clearAll() {
        organizations.clear();
    }
    
    /**
     * Check if directory is empty
     */
    public boolean isEmpty() {
        return organizations.isEmpty();
    }
}