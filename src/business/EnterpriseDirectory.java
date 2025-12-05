package business;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import model.Enterprise;
import model.Enterprise.EnterpriseType;

/**
 * Complete EnterpriseDirectory class
 * Manages all enterprises in the system
 */
public class EnterpriseDirectory implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    // Properties
    private List<Enterprise> enterprises;
    
    /**
     * Constructor
     */
    public EnterpriseDirectory() {
        this.enterprises = new ArrayList<>();
    }
    
    /**
     * Get all enterprises
     */
    public List<Enterprise> getAllEnterprises() {
        return new ArrayList<>(enterprises);
    }
    
    /**
     * Create and add new enterprise
     */
    public Enterprise createEnterprise(String name, EnterpriseType type) {
        Enterprise enterprise = new Enterprise(name, type);
        enterprises.add(enterprise);
        return enterprise;
    }
    
    /**
     * Add existing enterprise
     */
    public void addEnterprise(Enterprise enterprise) {
        if (!enterprises.contains(enterprise)) {
            enterprises.add(enterprise);
        }
    }
    
    /**
     * Remove enterprise
     */
    public boolean removeEnterprise(String enterpriseId) {
        return enterprises.removeIf(e -> e.getEnterpriseId().equals(enterpriseId));
    }
    
    /**
     * Find enterprise by ID
     */
    public Enterprise findEnterpriseById(String enterpriseId) {
        for (Enterprise enterprise : enterprises) {
            if (enterprise.getEnterpriseId().equals(enterpriseId)) {
                return enterprise;
            }
        }
        return null;
    }
    
    /**
     * Find enterprise by name
     */
    public Enterprise findEnterpriseByName(String name) {
        for (Enterprise enterprise : enterprises) {
            if (enterprise.getEnterpriseName().equalsIgnoreCase(name)) {
                return enterprise;
            }
        }
        return null;
    }
    
    /**
     * Get all enterprises of a specific type
     */
    public List<Enterprise> getEnterprisesByType(EnterpriseType type) {
        return enterprises.stream()
                .filter(e -> e.getEnterpriseType() == type)
                .collect(Collectors.toList());
    }
    
    /**
     * Get all hospitals
     */
    public List<Enterprise> getAllHospitals() {
        return getEnterprisesByType(EnterpriseType.HOSPITAL);
    }
    
    /**
     * Get all insurance providers
     */
    public List<Enterprise> getAllInsuranceProviders() {
        return getEnterprisesByType(EnterpriseType.INSURANCE_PROVIDER);
    }
    
    /**
     * Get all pharmacy chains
     */
    public List<Enterprise> getAllPharmacyChains() {
        return getEnterprisesByType(EnterpriseType.PHARMACY_CHAIN);
    }
    
    /**
     * Get all pharmaceutical suppliers
     */
    public List<Enterprise> getAllPharmaceuticalSuppliers() {
        return getEnterprisesByType(EnterpriseType.PHARMACEUTICAL_SUPPLIER);
    }
    
    /**
     * Get all active enterprises
     */
    public List<Enterprise> getActiveEnterprises() {
        return enterprises.stream()
                .filter(Enterprise::isActive)
                .collect(Collectors.toList());
    }
    
    /**
     * Get enterprise count
     */
    public int getEnterpriseCount() {
        return enterprises.size();
    }
    
    /**
     * Get enterprise count by type
     */
    public int getEnterpriseCountByType(EnterpriseType type) {
        return (int) enterprises.stream()
                .filter(e -> e.getEnterpriseType() == type)
                .count();
    }
    
    /**
     * Check if enterprise name already exists
     */
    public boolean isEnterpriseNameExists(String name) {
        return enterprises.stream()
                .anyMatch(e -> e.getEnterpriseName().equalsIgnoreCase(name));
    }
    
    /**
     * Deactivate enterprise
     */
    public boolean deactivateEnterprise(String enterpriseId) {
        Enterprise enterprise = findEnterpriseById(enterpriseId);
        if (enterprise != null) {
            enterprise.deactivate();
            return true;
        }
        return false;
    }
    
    /**
     * Activate enterprise
     */
    public boolean activateEnterprise(String enterpriseId) {
        Enterprise enterprise = findEnterpriseById(enterpriseId);
        if (enterprise != null) {
            enterprise.activate();
            return true;
        }
        return false;
    }
    
    /**
     * Update enterprise name
     */
    public boolean updateEnterpriseName(String enterpriseId, String newName) {
        Enterprise enterprise = findEnterpriseById(enterpriseId);
        if (enterprise != null) {
            enterprise.setEnterpriseName(newName);
            return true;
        }
        return false;
    }
    
    /**
     * Get enterprise names for dropdown (UI helper)
     */
    public List<String> getEnterpriseNamesForDropdown() {
        return enterprises.stream()
                .map(Enterprise::getEnterpriseName)
                .collect(Collectors.toList());
    }
    
    /**
     * Get enterprise names by type for dropdown
     */
    public List<String> getEnterpriseNamesByType(EnterpriseType type) {
        return enterprises.stream()
                .filter(e -> e.getEnterpriseType() == type)
                .map(Enterprise::getEnterpriseName)
                .collect(Collectors.toList());
    }
    
    /**
     * Search enterprises by name (partial match)
     */
    public List<Enterprise> searchEnterprisesByName(String searchTerm) {
        String lowerSearch = searchTerm.toLowerCase();
        return enterprises.stream()
                .filter(e -> e.getEnterpriseName().toLowerCase().contains(lowerSearch))
                .collect(Collectors.toList());
    }
    
    /**
     * Clear all enterprises (for testing)
     */
    public void clearAll() {
        enterprises.clear();
    }
    
    /**
     * Check if directory is empty
     */
    public boolean isEmpty() {
        return enterprises.isEmpty();
    }
}