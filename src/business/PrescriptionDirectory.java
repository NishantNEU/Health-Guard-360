package business;

import model.Prescription;
import model.Prescription.PrescriptionStatus;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Complete PrescriptionDirectory class
 * Manages all prescriptions in the system
 */
public class PrescriptionDirectory implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    // Properties
    private List<Prescription> prescriptions;
    
    /**
     * Constructor
     */
    public PrescriptionDirectory() {
        this.prescriptions = new ArrayList<>();
        createSamplePrescriptions(); // Create sample data
    }
    
    /**
     * Create sample prescriptions for demonstration
     */
    /**
     * Create sample prescriptions for demonstration
     */
    private void createSamplePrescriptions() {
        // Don't create in constructor
    }
    
    /**
     * Create sample prescriptions for a specific patient
     */
    public void createSamplePrescriptionsForPatient(String patientId) {
        // Sample 1 - Active Lisinopril
        Prescription rx1 = new Prescription(patientId, "EMP-DOC-001", "MED-001",
                                           "10mg, once daily", 30, 3, 
                                           "Take with food", "ENT-PHARM-001", "POL-2024-00123");
        rx1.setPrescriptionNumber("RX-2025-001234");
        rx1.setPrescribedDate(LocalDate.of(2025, 10, 15));
        prescriptions.add(rx1);
        
        // Sample 2 - Active Metformin
        Prescription rx2 = new Prescription(patientId, "EMP-DOC-001", "MED-002",
                                           "500mg, twice daily", 60, 2,
                                           "Take with meals", "ENT-PHARM-002", "POL-2024-00123");
        rx2.setPrescriptionNumber("RX-2025-001567");
        rx2.setPrescribedDate(LocalDate.of(2025, 9, 20));
        prescriptions.add(rx2);
        
        // Sample 3 - Active Atorvastatin
        Prescription rx3 = new Prescription(patientId, "EMP-DOC-002", "MED-003",
                                           "20mg, once at bedtime", 30, 5,
                                           "Monitor cholesterol", "ENT-PHARM-001", "POL-2024-00123");
        rx3.setPrescriptionNumber("RX-2025-001890");
        rx3.setPrescribedDate(LocalDate.of(2025, 11, 1));
        prescriptions.add(rx3);
        
        // Sample 4 - Completed Amoxicillin
        Prescription rx4 = new Prescription(patientId, "EMP-DOC-001", "MED-004",
                                           "500mg, three times daily", 21, 0,
                                           "Complete full course", "ENT-PHARM-003", "POL-2023-00456");
        rx4.setPrescriptionNumber("RX-2024-008765");
        rx4.setPrescribedDate(LocalDate.of(2024, 8, 10));
        rx4.setStatus(PrescriptionStatus.COMPLETED);
        prescriptions.add(rx4);
    }
    
    /**
     * Get all prescriptions
     */
    public List<Prescription> getAllPrescriptions() {
        return new ArrayList<>(prescriptions);
    }
    
    /**
     * Create and add new prescription
     */
    public Prescription createPrescription(String patientId, String doctorId, String medicationId,
                                          String dosage, int quantity, int refillsAuthorized,
                                          String instructions, String pharmacyId, String policyNumber) {
        Prescription prescription = new Prescription(patientId, doctorId, medicationId, dosage,
                                                    quantity, refillsAuthorized, instructions,
                                                    pharmacyId, policyNumber);
        prescriptions.add(prescription);
        return prescription;
    }
    
    /**
     * Add existing prescription
     */
    public void addPrescription(Prescription prescription) {
        if (!prescriptions.contains(prescription)) {
            prescriptions.add(prescription);
        }
    }
    
    /**
     * Remove prescription
     */
    public boolean removePrescription(String prescriptionNumber) {
        return prescriptions.removeIf(p -> p.getPrescriptionNumber().equals(prescriptionNumber));
    }
    
    /**
     * Find prescription by number
     */
    public Prescription findPrescriptionByNumber(String prescriptionNumber) {
        for (Prescription prescription : prescriptions) {
            if (prescription.getPrescriptionNumber().equals(prescriptionNumber)) {
                return prescription;
            }
        }
        return null;
    }
    
    /**
     * Get all prescriptions for a patient
     */
    public List<Prescription> getPrescriptionsByPatient(String patientId) {
        return prescriptions.stream()
                .filter(p -> p.getPatientId().equals(patientId))
                .collect(Collectors.toList());
    }
    
    /**
     * Get active prescriptions for a patient
     */
    public List<Prescription> getActivePrescriptionsByPatient(String patientId) {
        return prescriptions.stream()
                .filter(p -> p.getPatientId().equals(patientId))
                .filter(p -> p.getStatus() == PrescriptionStatus.ACTIVE)
                .filter(p -> !p.isExpired())
                .collect(Collectors.toList());
    }
    
    /**
     * Get prescriptions by status
     */
    public List<Prescription> getPrescriptionsByStatus(PrescriptionStatus status) {
        return prescriptions.stream()
                .filter(p -> p.getStatus() == status)
                .collect(Collectors.toList());
    }
    
    /**
     * Get prescriptions ready for refill for a patient
     */
    public List<Prescription> getPrescriptionsReadyForRefill(String patientId) {
        return prescriptions.stream()
                .filter(p -> p.getPatientId().equals(patientId))
                .filter(Prescription::isReadyForRefill)
                .collect(Collectors.toList());
    }
    
    /**
     * Get prescription count for patient
     */
    public int getPrescriptionCountForPatient(String patientId) {
        return (int) prescriptions.stream()
                .filter(p -> p.getPatientId().equals(patientId))
                .count();
    }
    
    /**
     * Get active prescription count for patient
     */
    public int getActivePrescriptionCountForPatient(String patientId) {
        return (int) prescriptions.stream()
                .filter(p -> p.getPatientId().equals(patientId))
                .filter(p -> p.getStatus() == PrescriptionStatus.ACTIVE)
                .filter(p -> !p.isExpired())
                .count();
    }
    
    /**
     * Get ready for refill count for patient
     */
    public int getReadyForRefillCountForPatient(String patientId) {
        return (int) prescriptions.stream()
                .filter(p -> p.getPatientId().equals(patientId))
                .filter(Prescription::isReadyForRefill)
                .count();
    }
    
    /**
     * Process refill request
     */
    public boolean processRefill(String prescriptionNumber) {
        Prescription prescription = findPrescriptionByNumber(prescriptionNumber);
        if (prescription != null) {
            return prescription.processRefill();
        }
        return false;
    }
    
    /**
     * Cancel prescription
     */
    public boolean cancelPrescription(String prescriptionNumber) {
        Prescription prescription = findPrescriptionByNumber(prescriptionNumber);
        if (prescription != null) {
            prescription.cancel();
            return true;
        }
        return false;
    }
    
    /**
     * Get expiring prescriptions (within 30 days)
     */
    public List<Prescription> getExpiringPrescriptions() {
        LocalDate thirtyDaysFromNow = LocalDate.now().plusDays(30);
        return prescriptions.stream()
                .filter(p -> p.getStatus() == PrescriptionStatus.ACTIVE)
                .filter(p -> p.getExpiryDate().isBefore(thirtyDaysFromNow))
                .collect(Collectors.toList());
    }
    
    /**
     * Clear all prescriptions (for testing)
     */
    public void clearAll() {
        prescriptions.clear();
    }
    
    /**
     * Reload sample data
     */
    public void reloadSampleData() {
        prescriptions.clear();
        createSamplePrescriptions();
    }
    
    /**
     * Check if directory is empty
     */
    public boolean isEmpty() {
        return prescriptions.isEmpty();
    }
}