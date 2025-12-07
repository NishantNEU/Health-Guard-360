/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package ui;

import business.SystemData;
import model.*;
import javax.swing.table.DefaultTableModel;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author Sandeep Patil
 */
public class ClaimsProcessorDashboard extends javax.swing.JPanel {

    /**
     * Creates new form ClaimsProcessor
     */
    private SystemData systemData;
    private model.Claim selectedClaim;

    public ClaimsProcessorDashboard() {
        initComponents();
        systemData = SystemData.getInstance();
        customInit();
    }

    private void customInit() {
        // Set all detail fields to non-editable
        claimNumberField.setEditable(false);
        patientNameField.setEditable(false);
        policyNumberField.setEditable(false);
        serviceDateField.setEditable(false);
        providerField.setEditable(false);
        diagnosisField.setEditable(false);
        serviceTypeField.setEditable(false);
        claimAmountField.setEditable(false);

        // Add ActionListener to refresh button
        refreshButton.addActionListener(e -> loadClaimsTable());

        // Add ActionListener to status filter combo box
        statusFilterComboBox.addActionListener(e -> loadClaimsTable());

        // Load claims
        loadClaimsTable();

        // Add table selection listener
        claimsTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadSelectedClaimDetails();
            }
        });
    }

    private void loadClaimsTable() {
        DefaultTableModel model = (DefaultTableModel) claimsTable.getModel();
        model.setRowCount(0);

        String filterStatus = (String) statusFilterComboBox.getSelectedItem();

        for (model.Claim claim : systemData.getClaimDirectory().getAllClaims()) {
            // Filter by status
            if (!filterStatus.equals("All Claims")) {
                if (!claim.getClaimStatus().getDisplayName().equals(filterStatus)) {
                    continue;
                }
            }

            // Get patient name
            String patientName = "Unknown";
            model.User user = systemData.getUserDirectory().findUserByPersonId(claim.getPatientId());
            if (user != null) {
                patientName = user.getPerson().getFullName();
            }

            Object[] row = new Object[6];
            row[0] = claim.getClaimNumber();
            row[1] = patientName;
            row[2] = claim.getServiceDate().format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
            row[3] = String.format("$%.2f", claim.getClaimAmount());
            row[4] = claim.getClaimStatus().getDisplayName();
            row[5] = "Review";
            model.addRow(row);
        }
    }

    private void loadSelectedClaimDetails() {
        int selectedRow = claimsTable.getSelectedRow();
        if (selectedRow < 0) {
            return;
        }

        String claimNumber = (String) claimsTable.getValueAt(selectedRow, 0);
        selectedClaim = systemData.getClaimDirectory().findClaimByNumber(claimNumber);

        if (selectedClaim != null) {
            claimNumberField.setText(selectedClaim.getClaimNumber());
            policyNumberField.setText(selectedClaim.getPolicyNumber());
            serviceDateField.setText(selectedClaim.getServiceDate().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
            providerField.setText(selectedClaim.getProviderName());
            diagnosisField.setText(selectedClaim.getDiagnosis());
            serviceTypeField.setText(selectedClaim.getServiceType().getDisplayName());
            claimAmountField.setText(String.format("$%.2f", selectedClaim.getClaimAmount()));

            // Get patient name
            model.User user = systemData.getUserDirectory().findUserByPersonId(selectedClaim.getPatientId());
            if (user != null) {
                patientNameField.setText(user.getPerson().getFullName());
            }

            // Load existing review notes
            reviewNotesTextArea.setText(selectedClaim.getReviewNotes());

            // Set approved amount if already set
            if (selectedClaim.getApprovedAmount() > 0) {
                approvedAmountField.setText(String.format("%.2f", selectedClaim.getApprovedAmount()));
            } else {
                approvedAmountField.setText("");
            }
        }
    }

    private void approveClaim() {
        if (selectedClaim == null) {
            javax.swing.JOptionPane.showMessageDialog(this, "Please select a claim to approve", "No Selection",
                    javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        String approvedAmountText = approvedAmountField.getText().trim();
        if (approvedAmountText.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, "Please enter approved amount", "Validation Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            double approvedAmount = Double.parseDouble(approvedAmountText);

            if (approvedAmount <= 0) {
                javax.swing.JOptionPane.showMessageDialog(this, "Approved amount must be greater than 0",
                        "Validation Error", javax.swing.JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (approvedAmount > selectedClaim.getClaimAmount()) {
                int confirm = javax.swing.JOptionPane.showConfirmDialog(this,
                        "Approved amount is greater than claim amount. Continue?",
                        "Confirm",
                        javax.swing.JOptionPane.YES_NO_OPTION);
                if (confirm != javax.swing.JOptionPane.YES_OPTION) {
                    return;
                }
            }

            // Update claim
            selectedClaim.setApprovedAmount(approvedAmount);
            selectedClaim.setClaimStatus(model.Claim.ClaimStatus.APPROVED);
            selectedClaim.setReviewNotes(reviewNotesTextArea.getText());
            selectedClaim.setProcessorId(systemData.getCurrentUser().getPerson().getPersonId());
            selectedClaim.setProcessedDate(java.time.LocalDate.now());

            javax.swing.JOptionPane.showMessageDialog(this,
                    "Claim approved successfully!\nClaim Number: " + selectedClaim.getClaimNumber()
                            + "\nApproved Amount: $" + String.format("%.2f", approvedAmount),
                    "Success",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE);

            // Refresh table
            loadClaimsTable();
            clearDetails();

        } catch (NumberFormatException ex) {
            javax.swing.JOptionPane.showMessageDialog(this, "Invalid approved amount", "Validation Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void denyClaim() {
        if (selectedClaim == null) {
            javax.swing.JOptionPane.showMessageDialog(this, "Please select a claim to deny", "No Selection",
                    javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        String reviewNotes = reviewNotesTextArea.getText().trim();
        if (reviewNotes.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, "Please enter review notes for denial reason",
                    "Validation Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = javax.swing.JOptionPane.showConfirmDialog(this,
                "Are you sure you want to deny this claim?",
                "Confirm Denial",
                javax.swing.JOptionPane.YES_NO_OPTION);

        if (confirm == javax.swing.JOptionPane.YES_OPTION) {
            // Update claim
            selectedClaim.setClaimStatus(model.Claim.ClaimStatus.DENIED);
            selectedClaim.setReviewNotes(reviewNotes);
            selectedClaim.setProcessorId(systemData.getCurrentUser().getPerson().getPersonId());
            selectedClaim.setProcessedDate(java.time.LocalDate.now());
            selectedClaim.setApprovedAmount(0.0);

            javax.swing.JOptionPane.showMessageDialog(this,
                    "Claim denied.\nClaim Number: " + selectedClaim.getClaimNumber(),
                    "Claim Denied",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE);

            // Refresh table
            loadClaimsTable();
            clearDetails();
        }
    }

    private void refreshTable() {
        loadClaimsTable();
        clearDetails();
    }

    private void clearDetails() {
        selectedClaim = null;
        claimNumberField.setText("");
        patientNameField.setText("");
        policyNumberField.setText("");
        serviceDateField.setText("");
        providerField.setText("");
        diagnosisField.setText("");
        serviceTypeField.setText("");
        claimAmountField.setText("");
        approvedAmountField.setText("");
        reviewNotesTextArea.setText("");
        claimsTable.clearSelection();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * 
     * /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        queuePanel = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        statusFilterComboBox = new javax.swing.JComboBox<>();
        refreshButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        claimsTable = new javax.swing.JTable();
        detailsPanel = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        claimNumberField = new javax.swing.JTextField();
        policyNumberField = new javax.swing.JTextField();
        patientNameField = new javax.swing.JTextField();
        serviceDateField = new javax.swing.JTextField();
        providerField = new javax.swing.JTextField();
        diagnosisField = new javax.swing.JTextField();
        serviceTypeField = new javax.swing.JTextField();
        claimAmountField = new javax.swing.JTextField();
        approvedAmountField = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        reviewNotesScrollPane = new javax.swing.JScrollPane();
        reviewNotesTextArea = new javax.swing.JTextArea();
        approveButton = new javax.swing.JButton();
        denyButton = new javax.swing.JButton();
        clearButton = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(1150, 720));

        jLabel1.setText("Claims Processor Dashboard");

        jLabel2.setText("Review and process insurance claims");

        jLabel3.setText("Pending Claims Queue");

        jLabel4.setText("Filter by Status:");

        statusFilterComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(
                new String[] { "All Claims", "Submitted", "Under Review", "Approved", "Denied" }));
        statusFilterComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                statusFilterComboBoxActionPerformed(evt);
            }
        });

        refreshButton.setText("Refresh");
        refreshButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshButtonActionPerformed(evt);
            }
        });

        claimsTable.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][] {
                        { null, null, null, null, null, null },
                        { null, null, null, null, null, null },
                        { null, null, null, null, null, null },
                        { null, null, null, null, null, null }
                },
                new String[] {
                        "Claim#", "Patient", "Service Date", "Amount", "Status", "Action"
                }) {
            boolean[] canEdit = new boolean[] {
                    false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        jScrollPane1.setViewportView(claimsTable);

        javax.swing.GroupLayout queuePanelLayout = new javax.swing.GroupLayout(queuePanel);
        queuePanel.setLayout(queuePanelLayout);
        queuePanelLayout.setHorizontalGroup(
                queuePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(queuePanelLayout.createSequentialGroup()
                                .addGroup(queuePanelLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(queuePanelLayout.createSequentialGroup()
                                                .addContainerGap()
                                                .addGroup(queuePanelLayout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel3)
                                                        .addGroup(queuePanelLayout.createSequentialGroup()
                                                                .addComponent(jLabel4)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(statusFilterComboBox,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(
                                                                        javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addComponent(refreshButton))))
                                        .addGroup(queuePanelLayout.createSequentialGroup()
                                                .addGap(12, 12, 12)
                                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 734,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(150, Short.MAX_VALUE)));
        queuePanelLayout.setVerticalGroup(
                queuePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(queuePanelLayout.createSequentialGroup()
                                .addGap(16, 16, 16)
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(queuePanelLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel4)
                                        .addComponent(statusFilterComboBox, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(refreshButton))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 185, Short.MAX_VALUE)
                                .addContainerGap()));

        jLabel5.setText("Claim Review Details");

        jLabel6.setText("Claim Number:");

        jLabel7.setText("Patient Name:");

        jLabel8.setText("Policy Number:");

        jLabel9.setText("Service Date:");

        jLabel10.setText("Provider: ");

        jLabel11.setText("Diagnosis:");

        jLabel12.setText("Service Type:");

        jLabel13.setText("Claim Amount:");

        jLabel14.setText("Approved Amount:");

        claimNumberField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                claimNumberFieldActionPerformed(evt);
            }
        });

        patientNameField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                patientNameFieldActionPerformed(evt);
            }
        });

        serviceDateField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                serviceDateFieldActionPerformed(evt);
            }
        });

        providerField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                providerFieldActionPerformed(evt);
            }
        });

        diagnosisField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                diagnosisFieldActionPerformed(evt);
            }
        });

        serviceTypeField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                serviceTypeFieldActionPerformed(evt);
            }
        });

        claimAmountField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                claimAmountFieldActionPerformed(evt);
            }
        });

        approvedAmountField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                approvedAmountFieldActionPerformed(evt);
            }
        });

        jLabel15.setText("Review Notes:");

        reviewNotesTextArea.setColumns(20);
        reviewNotesTextArea.setRows(5);
        reviewNotesScrollPane.setViewportView(reviewNotesTextArea);

        approveButton.setText("Approve Claim");
        approveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                approveButtonActionPerformed(evt);
            }
        });

        denyButton.setText("Deny Claim");
        denyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                denyButtonActionPerformed(evt);
            }
        });

        clearButton.setText("Clear");
        clearButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout detailsPanelLayout = new javax.swing.GroupLayout(detailsPanel);
        detailsPanel.setLayout(detailsPanelLayout);
        detailsPanelLayout.setHorizontalGroup(
                detailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(detailsPanelLayout.createSequentialGroup()
                                .addGap(22, 22, 22)
                                .addGroup(detailsPanelLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, detailsPanelLayout
                                                .createSequentialGroup()
                                                .addGroup(detailsPanelLayout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(detailsPanelLayout.createSequentialGroup()
                                                                .addComponent(jLabel8)
                                                                .addGap(27, 27, 27)
                                                                .addComponent(policyNumberField,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 159,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addGroup(detailsPanelLayout.createSequentialGroup()
                                                                .addComponent(jLabel9)
                                                                .addGap(38, 38, 38)
                                                                .addComponent(serviceDateField,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 159,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addGroup(detailsPanelLayout.createSequentialGroup()
                                                                .addComponent(jLabel10)
                                                                .addGap(57, 57, 57)
                                                                .addComponent(providerField,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 159,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addGroup(detailsPanelLayout.createSequentialGroup()
                                                                .addComponent(jLabel11)
                                                                .addGap(53, 53, 53)
                                                                .addComponent(diagnosisField,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 159,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addGroup(detailsPanelLayout.createSequentialGroup()
                                                                .addComponent(jLabel7)
                                                                .addGap(33, 33, 33)
                                                                .addGroup(detailsPanelLayout.createParallelGroup(
                                                                        javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addComponent(claimNumberField,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                159,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(patientNameField,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                159,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))))
                                                .addGap(130, 130, 130)
                                                .addGroup(detailsPanelLayout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(reviewNotesScrollPane,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE, 291,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addGroup(detailsPanelLayout.createSequentialGroup()
                                                                .addGroup(detailsPanelLayout.createParallelGroup(
                                                                        javax.swing.GroupLayout.Alignment.TRAILING,
                                                                        false)
                                                                        .addComponent(clearButton,
                                                                                javax.swing.GroupLayout.Alignment.LEADING,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)
                                                                        .addComponent(approveButton,
                                                                                javax.swing.GroupLayout.Alignment.LEADING,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE))
                                                                .addGap(18, 18, 18)
                                                                .addComponent(denyButton,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 109,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE))))
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, detailsPanelLayout
                                                .createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, detailsPanelLayout
                                                        .createSequentialGroup()
                                                        .addComponent(jLabel14)
                                                        .addPreferredGap(
                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(approvedAmountField))
                                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING,
                                                        detailsPanelLayout.createSequentialGroup()
                                                                .addComponent(jLabel12)
                                                                .addGap(38, 38, 38)
                                                                .addComponent(serviceTypeField,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 159,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING,
                                                detailsPanelLayout.createSequentialGroup()
                                                        .addComponent(jLabel13)
                                                        .addGap(29, 29, 29)
                                                        .addComponent(claimAmountField,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE, 159,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, detailsPanelLayout
                                                .createSequentialGroup()
                                                .addComponent(jLabel6)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jLabel15)
                                                .addGap(297, 297, 297)))
                                .addContainerGap(12, Short.MAX_VALUE)));
        detailsPanelLayout.setVerticalGroup(
                detailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(detailsPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(detailsPanelLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(detailsPanelLayout.createSequentialGroup()
                                                .addGroup(detailsPanelLayout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(jLabel6)
                                                        .addComponent(claimNumberField,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jLabel15))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(detailsPanelLayout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(jLabel7)
                                                        .addComponent(patientNameField,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(detailsPanelLayout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(jLabel8)
                                                        .addComponent(policyNumberField,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addComponent(reviewNotesScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 72,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(detailsPanelLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(detailsPanelLayout.createSequentialGroup()
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(detailsPanelLayout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(jLabel9)
                                                        .addComponent(serviceDateField,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(detailsPanelLayout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(jLabel10)
                                                        .addComponent(providerField,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGroup(detailsPanelLayout.createSequentialGroup()
                                                .addGap(19, 19, 19)
                                                .addGroup(detailsPanelLayout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(approveButton)
                                                        .addComponent(denyButton))))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(detailsPanelLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel11)
                                        .addGroup(detailsPanelLayout
                                                .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(diagnosisField, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(clearButton)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(detailsPanelLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel12)
                                        .addComponent(serviceTypeField, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(detailsPanelLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel13)
                                        .addComponent(claimAmountField, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(139, 139, 139)
                                .addGroup(detailsPanelLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel14)
                                        .addComponent(approvedAmountField, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(26, 26, 26)
                                                .addGroup(layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel2)
                                                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                175, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(17, 17, 17)
                                                .addGroup(layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(queuePanel,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(detailsPanel,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addContainerGap(237, Short.MAX_VALUE)));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(23, 23, 23)
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(queuePanel, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(detailsPanel, javax.swing.GroupLayout.DEFAULT_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(14, 14, 14)));
    }// </editor-fold>//GEN-END:initComponents

    private void patientNameFieldActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_patientNameFieldActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_patientNameFieldActionPerformed

    private void serviceDateFieldActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_serviceDateFieldActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_serviceDateFieldActionPerformed

    private void providerFieldActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_providerFieldActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_providerFieldActionPerformed

    private void diagnosisFieldActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_diagnosisFieldActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_diagnosisFieldActionPerformed

    private void serviceTypeFieldActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_serviceTypeFieldActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_serviceTypeFieldActionPerformed

    private void claimAmountFieldActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_claimAmountFieldActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_claimAmountFieldActionPerformed

    private void approvedAmountFieldActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_approvedAmountFieldActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_approvedAmountFieldActionPerformed

    private void clearButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_clearButtonActionPerformed
        // TODO add your handling code here:
        clearDetails();
    }// GEN-LAST:event_clearButtonActionPerformed

    private void statusFilterComboBoxActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_statusFilterComboBoxActionPerformed
        // TODO add your handling code here:
        loadClaimsTable();
    }// GEN-LAST:event_statusFilterComboBoxActionPerformed

    private void approveButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_approveButtonActionPerformed
        // TODO add your handling code here:
        approveClaim();
    }// GEN-LAST:event_approveButtonActionPerformed

    private void denyButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_denyButtonActionPerformed
        // TODO add your handling code here:
        denyClaim();
    }// GEN-LAST:event_denyButtonActionPerformed

    private void refreshButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_refreshButtonActionPerformed
        // TODO add your handling code here:
        refreshTable();
    }// GEN-LAST:event_refreshButtonActionPerformed

    private void claimNumberFieldActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_claimNumberFieldActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_claimNumberFieldActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton approveButton;
    private javax.swing.JTextField approvedAmountField;
    private javax.swing.JTextField claimAmountField;
    private javax.swing.JTextField claimNumberField;
    private javax.swing.JTable claimsTable;
    private javax.swing.JButton clearButton;
    private javax.swing.JButton denyButton;
    private javax.swing.JPanel detailsPanel;
    private javax.swing.JTextField diagnosisField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField patientNameField;
    private javax.swing.JTextField policyNumberField;
    private javax.swing.JTextField providerField;
    private javax.swing.JPanel queuePanel;
    private javax.swing.JButton refreshButton;
    private javax.swing.JScrollPane reviewNotesScrollPane;
    private javax.swing.JTextArea reviewNotesTextArea;
    private javax.swing.JTextField serviceDateField;
    private javax.swing.JTextField serviceTypeField;
    private javax.swing.JComboBox<String> statusFilterComboBox;
    // End of variables declaration//GEN-END:variables
}
