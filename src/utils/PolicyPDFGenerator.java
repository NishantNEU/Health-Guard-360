package utils;

import model.Policy;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utility class for generating PDF documents for insurance policies
 */
public class PolicyPDFGenerator {

    // Page dimensions and margins
    private static final float MARGIN = 50;
    private static final float PAGE_WIDTH = PDRectangle.LETTER.getWidth();
    private static final float PAGE_HEIGHT = PDRectangle.LETTER.getHeight();

    // Font sizes
    private static final int TITLE_FONT_SIZE = 20;
    private static final int SECTION_FONT_SIZE = 14;
    private static final int LABEL_FONT_SIZE = 11;
    private static final int VALUE_FONT_SIZE = 11;
    private static final int FOOTER_FONT_SIZE = 8;

    // Colors (RGB values 0-1)
    private static final float[] HEADER_COLOR = { 0.1f, 0.3f, 0.6f }; // Blue
    private static final float[] SECTION_COLOR = { 0.2f, 0.4f, 0.7f }; // Lighter blue
    private static final float[] ACTIVE_COLOR = { 0.0f, 0.6f, 0.0f }; // Green
    private static final float[] EXPIRED_COLOR = { 0.8f, 0.0f, 0.0f }; // Red
    private static final float[] TEXT_COLOR = { 0.0f, 0.0f, 0.0f }; // Black

    /**
     * Generate a PDF document for the given policy
     * 
     * @param policy The policy to generate the document for
     * @return The file path of the generated PDF
     * @throws IOException If there's an error creating the PDF
     */
    public static String generatePolicyDocument(Policy policy) throws IOException {
        // Create the output directory if it doesn't exist
        String userHome = System.getProperty("user.home");
        String outputDir = userHome + File.separator + "Documents" + File.separator +
                "HealthGuard360" + File.separator + "PolicyDocuments";
        File dir = new File(outputDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // Generate filename
        String filename = "Policy_" + policy.getPolicyNumber().replace("/", "-") + ".pdf";
        String outputPath = outputDir + File.separator + filename;

        // Create PDF document
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.LETTER);
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                float yPosition = PAGE_HEIGHT - MARGIN;

                // Draw header
                yPosition = drawHeader(contentStream, yPosition);
                yPosition -= 30;

                // Draw policy details
                yPosition = drawPolicyDetails(contentStream, policy, yPosition);
                yPosition -= 20;

                // Draw coverage information
                yPosition = drawCoverageInfo(contentStream, policy, yPosition);
                yPosition -= 20;

                // Draw dates and status
                yPosition = drawDatesAndStatus(contentStream, policy, yPosition);
                yPosition -= 20;

                // Draw beneficiaries
                yPosition = drawBeneficiaries(contentStream, policy, yPosition);
                yPosition -= 30;

                // Draw footer
                drawFooter(contentStream);
            }

            // Save document
            document.save(outputPath);
        }

        return outputPath;
    }

    /**
     * Draw the document header
     */
    private static float drawHeader(PDPageContentStream contentStream, float yPosition) throws IOException {
        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), TITLE_FONT_SIZE);
        contentStream.setNonStrokingColor(HEADER_COLOR[0], HEADER_COLOR[1], HEADER_COLOR[2]);

        String title = "HEALTHGUARD360";
        float titleWidth = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD).getStringWidth(title) / 1000
                * TITLE_FONT_SIZE;
        float titleX = (PAGE_WIDTH - titleWidth) / 2;

        contentStream.beginText();
        contentStream.newLineAtOffset(titleX, yPosition);
        contentStream.showText(title);
        contentStream.endText();

        yPosition -= 15;

        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
        String subtitle = "Insurance Policy Document";
        float subtitleWidth = new PDType1Font(Standard14Fonts.FontName.HELVETICA).getStringWidth(subtitle) / 1000 * 12;
        float subtitleX = (PAGE_WIDTH - subtitleWidth) / 2;

        contentStream.beginText();
        contentStream.newLineAtOffset(subtitleX, yPosition);
        contentStream.showText(subtitle);
        contentStream.endText();

        yPosition -= 10;

        // Draw line under header
        contentStream.setStrokingColor(HEADER_COLOR[0], HEADER_COLOR[1], HEADER_COLOR[2]);
        contentStream.setLineWidth(2);
        contentStream.moveTo(MARGIN, yPosition);
        contentStream.lineTo(PAGE_WIDTH - MARGIN, yPosition);
        contentStream.stroke();

        return yPosition;
    }

    /**
     * Draw policy details section
     */
    private static float drawPolicyDetails(PDPageContentStream contentStream, Policy policy, float yPosition)
            throws IOException {
        yPosition = drawSectionHeader(contentStream, "Policy Information", yPosition);
        yPosition -= 15;

        contentStream.setNonStrokingColor(TEXT_COLOR[0], TEXT_COLOR[1], TEXT_COLOR[2]);

        yPosition = drawLabelValuePair(contentStream, "Policy Number:", policy.getPolicyNumber(), yPosition);
        yPosition = drawLabelValuePair(contentStream, "Policy Type:", policy.getPolicyType().getDisplayName(),
                yPosition);
        yPosition = drawLabelValuePair(contentStream, "Policyholder ID:", policy.getPatientId(), yPosition);

        return yPosition;
    }

    /**
     * Draw coverage information section
     */
    private static float drawCoverageInfo(PDPageContentStream contentStream, Policy policy, float yPosition)
            throws IOException {
        yPosition = drawSectionHeader(contentStream, "Coverage Details", yPosition);
        yPosition -= 15;

        contentStream.setNonStrokingColor(TEXT_COLOR[0], TEXT_COLOR[1], TEXT_COLOR[2]);

        yPosition = drawLabelValuePair(contentStream, "Coverage Amount:", policy.getFormattedCoverageAmount(),
                yPosition);
        yPosition = drawLabelValuePair(contentStream, "Deductible:", policy.getFormattedDeductible(), yPosition);
        yPosition = drawLabelValuePair(contentStream, "Co-payment:", policy.getFormattedCopayment(), yPosition);
        yPosition = drawLabelValuePair(contentStream, "Monthly Premium:", policy.getFormattedMonthlyPremium(),
                yPosition);
        yPosition = drawLabelValuePair(contentStream, "Annual Premium:", policy.getFormattedAnnualPremium(), yPosition);

        return yPosition;
    }

    /**
     * Draw dates and status section
     */
    private static float drawDatesAndStatus(PDPageContentStream contentStream, Policy policy, float yPosition)
            throws IOException {
        yPosition = drawSectionHeader(contentStream, "Policy Dates & Status", yPosition);
        yPosition -= 15;

        contentStream.setNonStrokingColor(TEXT_COLOR[0], TEXT_COLOR[1], TEXT_COLOR[2]);

        yPosition = drawLabelValuePair(contentStream, "Start Date:", policy.getFormattedStartDate(), yPosition);
        yPosition = drawLabelValuePair(contentStream, "Expiry Date:", policy.getFormattedExpiryDate(), yPosition);

        // Draw status with color coding
        String statusLabel = "Status:";
        String statusValue = policy.getPolicyStatus().getDisplayName();

        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), LABEL_FONT_SIZE);
        contentStream.beginText();
        contentStream.newLineAtOffset(MARGIN, yPosition);
        contentStream.showText(statusLabel);
        contentStream.endText();

        // Color code the status
        if (policy.isCurrentlyActive()) {
            contentStream.setNonStrokingColor(ACTIVE_COLOR[0], ACTIVE_COLOR[1], ACTIVE_COLOR[2]);
        } else if (policy.isExpired()) {
            contentStream.setNonStrokingColor(EXPIRED_COLOR[0], EXPIRED_COLOR[1], EXPIRED_COLOR[2]);
        }

        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), VALUE_FONT_SIZE);
        float labelWidth = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD).getStringWidth(statusLabel) / 1000
                * LABEL_FONT_SIZE;
        contentStream.beginText();
        contentStream.newLineAtOffset(MARGIN + labelWidth + 10, yPosition);
        contentStream.showText(statusValue);
        contentStream.endText();

        yPosition -= 15;

        return yPosition;
    }

    /**
     * Draw beneficiaries section
     */
    private static float drawBeneficiaries(PDPageContentStream contentStream, Policy policy, float yPosition)
            throws IOException {
        yPosition = drawSectionHeader(contentStream, "Beneficiaries", yPosition);
        yPosition -= 15;

        contentStream.setNonStrokingColor(TEXT_COLOR[0], TEXT_COLOR[1], TEXT_COLOR[2]);

        String beneficiaries = policy.getBeneficiariesString();
        yPosition = drawLabelValuePair(contentStream, "Listed Beneficiaries:", beneficiaries, yPosition);

        return yPosition;
    }

    /**
     * Draw a section header
     */
    private static float drawSectionHeader(PDPageContentStream contentStream, String header, float yPosition)
            throws IOException {
        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), SECTION_FONT_SIZE);
        contentStream.setNonStrokingColor(SECTION_COLOR[0], SECTION_COLOR[1], SECTION_COLOR[2]);

        contentStream.beginText();
        contentStream.newLineAtOffset(MARGIN, yPosition);
        contentStream.showText(header);
        contentStream.endText();

        yPosition -= 5;

        // Draw underline
        contentStream.setStrokingColor(SECTION_COLOR[0], SECTION_COLOR[1], SECTION_COLOR[2]);
        contentStream.setLineWidth(1);
        contentStream.moveTo(MARGIN, yPosition);
        contentStream.lineTo(PAGE_WIDTH - MARGIN, yPosition);
        contentStream.stroke();

        return yPosition;
    }

    /**
     * Draw a label-value pair
     */
    private static float drawLabelValuePair(PDPageContentStream contentStream, String label, String value,
            float yPosition) throws IOException {
        contentStream.setNonStrokingColor(TEXT_COLOR[0], TEXT_COLOR[1], TEXT_COLOR[2]);

        // Draw label
        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), LABEL_FONT_SIZE);
        contentStream.beginText();
        contentStream.newLineAtOffset(MARGIN, yPosition);
        contentStream.showText(label);
        contentStream.endText();

        // Draw value
        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), VALUE_FONT_SIZE);
        float labelWidth = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD).getStringWidth(label) / 1000
                * LABEL_FONT_SIZE;
        contentStream.beginText();
        contentStream.newLineAtOffset(MARGIN + labelWidth + 10, yPosition);
        contentStream.showText(value);
        contentStream.endText();

        return yPosition - 15;
    }

    /**
     * Draw the document footer
     */
    private static void drawFooter(PDPageContentStream contentStream) throws IOException {
        float yPosition = MARGIN - 20;

        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), FOOTER_FONT_SIZE);
        contentStream.setNonStrokingColor(0.5f, 0.5f, 0.5f);

        // Generate timestamp
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm a"));
        String footerText = "Generated on " + timestamp + " by HealthGuard360 System";

        float textWidth = new PDType1Font(Standard14Fonts.FontName.HELVETICA).getStringWidth(footerText) / 1000
                * FOOTER_FONT_SIZE;
        float textX = (PAGE_WIDTH - textWidth) / 2;

        contentStream.beginText();
        contentStream.newLineAtOffset(textX, yPosition);
        contentStream.showText(footerText);
        contentStream.endText();

        yPosition -= 10;

        String disclaimer = "This is an official policy document. Please retain for your records.";
        textWidth = new PDType1Font(Standard14Fonts.FontName.HELVETICA).getStringWidth(disclaimer) / 1000
                * FOOTER_FONT_SIZE;
        textX = (PAGE_WIDTH - textWidth) / 2;

        contentStream.beginText();
        contentStream.newLineAtOffset(textX, yPosition);
        contentStream.showText(disclaimer);
        contentStream.endText();
    }
}
