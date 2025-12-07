/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package ui;

/**
 *
 * @author Sandeep Patil
 */
public class BuyPolicyPanel extends javax.swing.JPanel {

    /**
     * Creates new form BuyPolicyPanel
     */
    public BuyPolicyPanel() {
        initComponents();

        // Set size for beneficiary field
        jTextField1.setPreferredSize(new java.awt.Dimension(300, 22));

        // Load insurance providers
        loadInsuranceProviders();

        // Set default values
        setDefaultValues();

        // Add action listeners
        addActionListeners();
    }

    /**
     * Load insurance providers from system
     */
    private void loadInsuranceProviders() {
        business.SystemData systemData = business.SystemData.getInstance();
        java.util.List<model.Enterprise> insuranceProviders = new java.util.ArrayList<>();

        // Get all enterprises
        for (model.Enterprise enterprise : systemData.getEnterpriseDirectory().getAllEnterprises()) {
            if (enterprise.getEnterpriseType() == model.Enterprise.EnterpriseType.INSURANCE_PROVIDER) {
                insuranceProviders.add(enterprise);
            }
        }

        // Populate combo box
        insuranceProviderComboBox.removeAllItems();
        for (model.Enterprise provider : insuranceProviders) {
            insuranceProviderComboBox.addItem(provider.getEnterpriseName());
        }

        if (insuranceProviderComboBox.getItemCount() == 0) {
            insuranceProviderComboBox.addItem("No providers available");
        }
    }

    /**
     * Set default values
     */
    private void setDefaultValues() {
        // Set today's date
        java.time.LocalDate today = java.time.LocalDate.now();
        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("MM/dd/yyyy");
        startDateField.setText(today.format(formatter));
        startDateField.setEditable(false);

        // Calculate and set expiry date (default 1 year)
        updateExpiryDate();
        expiryDateLabel.setEditable(false);

        // Set default values
        policyTypeComboBox.setSelectedIndex(0); // Individual HMO
        policyDurationComboBox.setSelectedIndex(0); // 1 Year
        coverageAmountField.setText("100000");
        deductibleField.setText("1000");
        copaymentField.setText("30");

        // Set premium fields as non-editable
        jTextField7.setEditable(false); // Monthly premium
        jTextField8.setEditable(false); // Annual premium

        // Set beneficiary field as placeholder
        jTextField1.setText("e.g., John Doe, Jane Smith, Bob Johnson");
    }

    /**
     * Add action listeners
     */
    private void addActionListeners() {
        // Calculate Premium button
        calculatePremiumButton.addActionListener(evt -> calculatePremiumButtonActionPerformed(evt));

        // Purchase Policy button
        purchasePolicyButton.addActionListener(evt -> purchasePolicyButtonActionPerformed(evt));

        // Cancel button
        cancelButton.addActionListener(evt -> cancelButtonActionPerformed(evt));

        // NOTE: addBeneficiaryButton listener is already added by NetBeans form
        // designer
        // Don't add it again or it will trigger twice!

        // Duration change listener
        policyDurationComboBox.addActionListener(evt -> updateExpiryDate());
    }

    /**
     * Update expiry date based on duration
     */
    private void updateExpiryDate() {
        try {
            String startDateStr = startDateField.getText();
            if (startDateStr == null || startDateStr.trim().isEmpty()) {
                return;
            }

            java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("MM/dd/yyyy");
            java.time.LocalDate startDate = java.time.LocalDate.parse(startDateStr, formatter);

            // Parse duration
            String durationStr = (String) policyDurationComboBox.getSelectedItem();
            int years = Integer.parseInt(durationStr.split(" ")[0]);

            // Calculate expiry
            java.time.LocalDate expiryDate = startDate.plusYears(years);
            expiryDateLabel.setText(expiryDate.format(formatter));

        } catch (Exception ex) {
            // Ignore parsing errors during initialization
        }
    }

    /**
     * Calculate premium based on policy details
     */
    private void calculatePremiumButtonActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            // Get values
            double coverageAmount = Double.parseDouble(coverageAmountField.getText());
            double deductible = Double.parseDouble(deductibleField.getText());
            double copayment = Double.parseDouble(copaymentField.getText());
            String policyType = (String) policyTypeComboBox.getSelectedItem();

            // Validate
            if (coverageAmount <= 0 || coverageAmount > 10000000) {
                showError("Coverage amount must be between $1 and $10,000,000");
                return;
            }
            if (deductible < 0 || deductible >= coverageAmount) {
                showError("Deductible must be non-negative and less than coverage amount");
                return;
            }
            if (copayment < 0 || copayment >= 500) {
                showError("Copayment must be between $0 and $500");
                return;
            }

            // Calculate base premium
            double basePremium = 0;
            switch (policyType) {
                case "Individual HMO":
                    basePremium = (coverageAmount * 0.03) / 12;
                    break;
                case "Individual PPO":
                    basePremium = (coverageAmount * 0.045) / 12;
                    break;
                case "Family HMO":
                    basePremium = (coverageAmount * 0.05) / 12;
                    break;
                case "Family PPO":
                    basePremium = (coverageAmount * 0.07) / 12;
                    break;
                case "Group":
                    basePremium = (coverageAmount * 0.04) / 12;
                    break;
                case "Medicare":
                    basePremium = (coverageAmount * 0.02) / 12;
                    break;
                case "Medicaid":
                    basePremium = (coverageAmount * 0.015) / 12;
                    break;
            }

            // Apply adjustments
            double monthlyPremium = basePremium - (deductible * 0.001) - (copayment * 0.5);

            // Ensure minimum premium
            if (monthlyPremium < 50) {
                monthlyPremium = 50;
            }

            double annualPremium = monthlyPremium * 12;

            // Display
            jTextField7.setText(String.format("$%.2f", monthlyPremium));
            jTextField8.setText(String.format("$%.2f", annualPremium));

            javax.swing.JOptionPane.showMessageDialog(this,
                    "Premium calculated successfully!\n\n" + "Monthly Premium: $"
                            + String.format("%.2f", monthlyPremium) + "\n" + "Annual Premium: $"
                            + String.format("%.2f", annualPremium),
                    "Premium Calculation", javax.swing.JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException ex) {
            showError("Please enter valid numeric values for all fields");
        }
    }

    /**
     * Add beneficiary field dynamically
     */

    /**
     * Purchase policy
     */
    private void purchasePolicyButtonActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            // Validate all fields
            if (!validateFields()) {
                return;
            }

            // Get current patient
            business.SystemData systemData = business.SystemData.getInstance();
            model.User currentUser = systemData.getCurrentUser();
            if (currentUser == null) {
                showError("No user logged in");
                return;
            }

            String patientId = currentUser.getPerson().getPersonId();

            // Get values
            String policyTypeStr = (String) policyTypeComboBox.getSelectedItem();
            model.Policy.PolicyType policyType = getPolicyTypeEnum(policyTypeStr);
            double coverageAmount = Double.parseDouble(coverageAmountField.getText());
            double deductible = Double.parseDouble(deductibleField.getText());
            double copayment = Double.parseDouble(copaymentField.getText());
            String durationStr = (String) policyDurationComboBox.getSelectedItem();
            int years = Integer.parseInt(durationStr.split(" ")[0]);
            String insuranceProvider = (String) insuranceProviderComboBox.getSelectedItem();

            // Parse dates
            java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("MM/dd/yyyy");
            java.time.LocalDate startDate = java.time.LocalDate.parse(startDateField.getText(), formatter);

            // Get monthly premium
            String premiumStr = jTextField7.getText().replace("$", "").trim();
            double monthlyPremium = Double.parseDouble(premiumStr);

            // Create new policy
            model.Policy newPolicy = new model.Policy(patientId, policyType, coverageAmount, deductible, copayment,
                    insuranceProvider, startDate, years);
            newPolicy.setMonthlyPremium(monthlyPremium);
            newPolicy.setPolicyStatus(model.Policy.PolicyStatus.PENDING);

            // Add beneficiaries from comma-separated field
            String beneficiariesText = jTextField1.getText().trim();
            // Clear placeholder text if present
            if (beneficiariesText.startsWith("e.g.,")) {
                beneficiariesText = "";
            }

            if (!beneficiariesText.isEmpty()) {
                // Split by comma and add each beneficiary
                String[] beneficiaryNames = beneficiariesText.split(",");
                for (String name : beneficiaryNames) {
                    String trimmedName = name.trim();
                    if (!trimmedName.isEmpty()) {
                        newPolicy.addBeneficiary(trimmedName);
                    }
                }
            }

            // Add to policy directory
            systemData.getPolicyDirectory().addPolicy(newPolicy);

            // Show success
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Policy purchased successfully!\n\n" + "Policy Number: " + newPolicy.getPolicyNumber() + "\n"
                            + "Policy Type: " + newPolicy.getPolicyType().getDisplayName() + "\n" + "Coverage: "
                            + newPolicy.getFormattedCoverageAmount() + "\n" + "Status: PENDING (awaiting approval)\n\n"
                            + "You will be notified once your policy is approved.",
                    "Policy Purchase Successful", javax.swing.JOptionPane.INFORMATION_MESSAGE);

            // Navigate back (you'll need to implement navigation)
            // For now, just clear the form
            resetForm();

        } catch (Exception ex) {
            showError("Error purchasing policy: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * Cancel button
     */
    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {
        int confirm = javax.swing.JOptionPane.showConfirmDialog(this,
                "Are you sure you want to cancel?\nAll entered data will be lost.", "Confirm Cancel",
                javax.swing.JOptionPane.YES_NO_OPTION);

        if (confirm == javax.swing.JOptionPane.YES_OPTION) {
            resetForm();
        }
    }

    /**
     * Validate all input fields
     */
    private boolean validateFields() {
        // Check coverage amount
        try {
            double coverage = Double.parseDouble(coverageAmountField.getText());
            if (coverage <= 0 || coverage > 10000000) {
                showError("Coverage amount must be between $1 and $10,000,000");
                return false;
            }
        } catch (NumberFormatException ex) {
            showError("Please enter a valid coverage amount");
            return false;
        }

        // Check deductible
        try {
            double deductible = Double.parseDouble(deductibleField.getText());
            double coverage = Double.parseDouble(coverageAmountField.getText());
            if (deductible < 0 || deductible >= coverage) {
                showError("Deductible must be non-negative and less than coverage amount");
                return false;
            }
        } catch (NumberFormatException ex) {
            showError("Please enter a valid deductible amount");
            return false;
        }

        // Check copayment
        try {
            double copayment = Double.parseDouble(copaymentField.getText());
            if (copayment < 0 || copayment >= 500) {
                showError("Copayment must be between $0 and $500");
                return false;
            }
        } catch (NumberFormatException ex) {
            showError("Please enter a valid copayment amount");
            return false;
        }

        // Check insurance provider
        if (insuranceProviderComboBox.getSelectedItem() == null
                || insuranceProviderComboBox.getSelectedItem().toString().equals("No providers available")) {
            showError("Please select an insurance provider");
            return false;
        }

        // Check at least one beneficiary
        String beneficiariesText = jTextField1.getText().trim();
        // Clear placeholder text if present
        if (beneficiariesText.isEmpty() || beneficiariesText.startsWith("e.g.,")) {
            showError("Please add at least one beneficiary\n(Enter names separated by commas)");
            return false;
        }

        // Check premium calculated
        if (jTextField7.getText().trim().isEmpty() || jTextField7.getText().equals("Estimated Monthly Premium:")) {
            showError("Please calculate premium first");
            return false;
        }

        return true;
    }

    /**
     * Convert policy type string to enum
     */
    private model.Policy.PolicyType getPolicyTypeEnum(String typeStr) {
        switch (typeStr) {
            case "Individual HMO":
                return model.Policy.PolicyType.INDIVIDUAL_HMO;
            case "Individual PPO":
                return model.Policy.PolicyType.INDIVIDUAL_PPO;
            case "Family HMO":
                return model.Policy.PolicyType.FAMILY_HMO;
            case "Family PPO":
                return model.Policy.PolicyType.FAMILY_PPO;
            case "Group":
                return model.Policy.PolicyType.GROUP;
            case "Medicare":
                return model.Policy.PolicyType.MEDICARE;
            case "Medicaid":
                return model.Policy.PolicyType.MEDICAID;
            default:
                return model.Policy.PolicyType.INDIVIDUAL_HMO;
        }
    }

    /**
     * Reset form to defaults
     */
    private void resetForm() {
        coverageAmountField.setText("100000");
        deductibleField.setText("1000");
        copaymentField.setText("30");
        policyTypeComboBox.setSelectedIndex(0);
        policyDurationComboBox.setSelectedIndex(0);
        jTextField7.setText("");
        jTextField8.setText("");

        // Clear beneficiary field
        jTextField1.setText("e.g., John Doe, Jane Smith, Bob Johnson");

        updateExpiryDate();
    }

    /**
     * Show error message
     */
    private void showError(String message) {
        javax.swing.JOptionPane.showMessageDialog(this, message, "Validation Error",
                javax.swing.JOptionPane.ERROR_MESSAGE);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        policyTypeComboBox = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        coverageAmountField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        deductibleField = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        copaymentField = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        policyDurationComboBox = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        insuranceProviderComboBox = new javax.swing.JComboBox<>();
        beneficiariesPanel = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        numBeneficiariesSpinner = new javax.swing.JSpinner();
        jLabel19 = new javax.swing.JLabel();
        startDateField = new javax.swing.JTextField();
        jTextField7 = new javax.swing.JTextField();
        jTextField8 = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        expiryDateLabel = new javax.swing.JTextField();
        purchasePolicyButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        monthlyPremiumLabel = new javax.swing.JLabel();
        annualPremiumLabel = new javax.swing.JLabel();
        calculatePremiumButton = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setText("Purchase New Insurance Policy");

        jLabel2.setText("Policy Type:");

        policyTypeComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Individual HMO",
                "Individual PPO", "Family HMO", "Family PPO", "Group", "Medicare", "Medicaid" }));

        jLabel3.setText("Coverage Amount:");

        coverageAmountField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                coverageAmountFieldActionPerformed(evt);
            }
        });

        jLabel4.setText("Deductible ($)");

        deductibleField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deductibleFieldActionPerformed(evt);
            }
        });

        jLabel5.setText("Copayment: ");

        jLabel6.setText("Policy Duration:");

        policyDurationComboBox.setModel(
                new javax.swing.DefaultComboBoxModel<>(new String[] { "1 Year", "2 Years", "3 Years", "5 Years" }));

        jLabel7.setText("Insurance Provider");

        insuranceProviderComboBox.setModel(
                new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel18.setText("Number of Beneficiaries");

        numBeneficiariesSpinner.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jLabel19.setText("Beneficiary :");

        startDateField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startDateFieldActionPerformed(evt);
            }
        });

        jLabel14.setText("Policy Expiry Date");

        expiryDateLabel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                expiryDateLabelActionPerformed(evt);
            }
        });

        purchasePolicyButton.setText("Purchase Policy");

        cancelButton.setText("Cancel");

        monthlyPremiumLabel.setText("Estimated Monthly Premium:");

        annualPremiumLabel.setText("Estimated Annual Premium:");

        calculatePremiumButton.setText("Calculate Premium");

        jLabel13.setText("Policy Start Date");

        javax.swing.GroupLayout beneficiariesPanelLayout = new javax.swing.GroupLayout(beneficiariesPanel);
        beneficiariesPanel.setLayout(beneficiariesPanelLayout);
        beneficiariesPanelLayout.setHorizontalGroup(
                beneficiariesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(beneficiariesPanelLayout.createSequentialGroup()
                                .addGroup(beneficiariesPanelLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(beneficiariesPanelLayout.createSequentialGroup()
                                                .addGroup(beneficiariesPanelLayout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(calculatePremiumButton)
                                                        .addGroup(beneficiariesPanelLayout
                                                                .createParallelGroup(
                                                                        javax.swing.GroupLayout.Alignment.TRAILING,
                                                                        false)
                                                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING,
                                                                        beneficiariesPanelLayout.createSequentialGroup()
                                                                                .addComponent(annualPremiumLabel)
                                                                                .addPreferredGap(
                                                                                        javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                                .addComponent(jTextField8))
                                                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING,
                                                                        beneficiariesPanelLayout.createSequentialGroup()
                                                                                .addComponent(monthlyPremiumLabel)
                                                                                .addPreferredGap(
                                                                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                .addComponent(jTextField7,
                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                        118,
                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                        .addGroup(beneficiariesPanelLayout.createSequentialGroup()
                                                                .addComponent(purchasePolicyButton)
                                                                .addGap(71, 71, 71)
                                                                .addComponent(cancelButton)))
                                                .addGap(73, 73, 73)
                                                .addGroup(beneficiariesPanelLayout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addComponent(jLabel13)
                                                        .addComponent(jLabel14))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, beneficiariesPanelLayout
                                                .createSequentialGroup()
                                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 300,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(77, 77, 77)))
                                .addGroup(beneficiariesPanelLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(startDateField, javax.swing.GroupLayout.PREFERRED_SIZE, 133,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(expiryDateLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 133,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(160, Short.MAX_VALUE))
                        .addGroup(
                                beneficiariesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(beneficiariesPanelLayout.createSequentialGroup()
                                                .addContainerGap()
                                                .addGroup(beneficiariesPanelLayout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel19)
                                                        .addGroup(beneficiariesPanelLayout.createSequentialGroup()
                                                                .addComponent(jLabel18)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(numBeneficiariesSpinner,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                .addContainerGap(533, Short.MAX_VALUE))));
        beneficiariesPanelLayout.setVerticalGroup(
                beneficiariesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(beneficiariesPanelLayout.createSequentialGroup()
                                .addGap(42, 42, 42)
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(58, 58, 58)
                                .addGroup(beneficiariesPanelLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(monthlyPremiumLabel)
                                        .addComponent(jLabel13)
                                        .addComponent(startDateField, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(beneficiariesPanelLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(annualPremiumLabel)
                                        .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel14)
                                        .addComponent(expiryDateLabel, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addComponent(calculatePremiumButton)
                                .addGap(28, 28, 28)
                                .addGroup(beneficiariesPanelLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(purchasePolicyButton)
                                        .addComponent(cancelButton))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(
                                beneficiariesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(beneficiariesPanelLayout.createSequentialGroup()
                                                .addContainerGap()
                                                .addGroup(beneficiariesPanelLayout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(jLabel18)
                                                        .addComponent(numBeneficiariesSpinner,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(18, 18, 18)
                                                .addComponent(jLabel19)
                                                .addContainerGap(62, Short.MAX_VALUE))));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(37, 37, 37)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addComponent(jLabel5)
                                                        .addGroup(layout
                                                                .createParallelGroup(
                                                                        javax.swing.GroupLayout.Alignment.LEADING)
                                                                .addComponent(jLabel3)
                                                                .addComponent(jLabel6,
                                                                        javax.swing.GroupLayout.Alignment.TRAILING)))
                                                .addGap(18, 18, 18)
                                                .addGroup(layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING,
                                                                false)
                                                        .addComponent(coverageAmountField)
                                                        .addComponent(policyDurationComboBox, 0, 200, Short.MAX_VALUE)
                                                        .addComponent(copaymentField,
                                                                javax.swing.GroupLayout.Alignment.TRAILING))
                                                .addGap(64, 64, 64)
                                                .addGroup(layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel4)
                                                        .addComponent(jLabel7))
                                                .addGap(18, 18, 18)
                                                .addGroup(layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addComponent(insuranceProviderComboBox,
                                                                javax.swing.GroupLayout.Alignment.LEADING, 0,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGap(0, 3, Short.MAX_VALUE)
                                                                .addComponent(deductibleField,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 179,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                .addGap(251, 251, 251))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(jLabel2)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(policyTypeComboBox,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 183,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addComponent(jLabel1)
                                                        .addComponent(beneficiariesPanel,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(0, 0, Short.MAX_VALUE)))));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(26, 26, 26)
                                .addComponent(jLabel1)
                                .addGap(39, 39, 39)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel2)
                                        .addComponent(policyTypeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(28, 28, 28)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel3)
                                        .addComponent(coverageAmountField, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel4)
                                        .addComponent(deductibleField, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel5)
                                        .addComponent(copaymentField, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel7)
                                        .addComponent(insuranceProviderComboBox, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel6)
                                        .addComponent(policyDurationComboBox, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addComponent(beneficiariesPanel, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(100, Short.MAX_VALUE)));
    }// </editor-fold>//GEN-END:initComponents

    private void coverageAmountFieldActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_coverageAmountFieldActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_coverageAmountFieldActionPerformed

    private void deductibleFieldActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_deductibleFieldActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_deductibleFieldActionPerformed

    private void startDateFieldActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_startDateFieldActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_startDateFieldActionPerformed

    private void expiryDateLabelActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_expiryDateLabelActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_expiryDateLabelActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel annualPremiumLabel;
    private javax.swing.JPanel beneficiariesPanel;
    private javax.swing.JButton calculatePremiumButton;
    private javax.swing.JButton cancelButton;
    private javax.swing.JTextField copaymentField;
    private javax.swing.JTextField coverageAmountField;
    private javax.swing.JTextField deductibleField;
    private javax.swing.JTextField expiryDateLabel;
    private javax.swing.JComboBox<String> insuranceProviderComboBox;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JLabel monthlyPremiumLabel;
    private javax.swing.JSpinner numBeneficiariesSpinner;
    private javax.swing.JComboBox<String> policyDurationComboBox;
    private javax.swing.JComboBox<String> policyTypeComboBox;
    private javax.swing.JButton purchasePolicyButton;
    private javax.swing.JTextField startDateField;
    // End of variables declaration//GEN-END:variables
}
