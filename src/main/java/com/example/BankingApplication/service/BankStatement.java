// BankStatement.java
package com.example.BankingApplication.service;

import com.example.BankingApplication.entity.Transaction;
import com.example.BankingApplication.repository.TransactionRepository;
import com.example.BankingApplication.repository.UserRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@NoArgsConstructor
@AllArgsConstructor
@Data
@Slf4j
public class BankStatement {

  @Autowired
  private TransactionRepository transactionRepository;

  @Autowired
  private UserRepository userRepository;

  private static final String fileName = "C:\\Users\\HP VICTUS\\Downloads\\testingpdfbanking\\Doc1.pdf";

  public List<Transaction> generateStatement(String accountNumber, String startDate, String endDate) throws FileNotFoundException, DocumentException {
    // Trim and parse start and end dates
    LocalDate start = LocalDate.parse(startDate.trim(), DateTimeFormatter.ISO_DATE);
    LocalDate end = LocalDate.parse(endDate.trim(), DateTimeFormatter.ISO_DATE);
    LocalDateTime startDateTime = start.atStartOfDay();
    LocalDateTime endDateTime = end.plusDays(1).atStartOfDay().minusSeconds(1);

    log.info("Fetching transactions for account: {} between {} and {}", accountNumber, startDate, endDate);

    // Fetch transactions for the account number and date range
    List<Transaction> transactionList = transactionRepository.findByAccountNumberAndCreatedAtBetween(accountNumber, startDateTime, endDateTime);

    log.info("Transactions found: {}", transactionList.size());
    transactionList.forEach(tx -> log.info("Transaction: {}", tx));

    if (transactionList.isEmpty()) {
      log.warn("No transactions found for account {} in the specified date range.", accountNumber);
    }

    // Generate PDF if transactions exist
    if (!transactionList.isEmpty()) {
      log.info("Generating PDF for account: {}", accountNumber);

      // Fetch customer information
      String customerName = userRepository.findByAccountNumber(accountNumber).getFirstName() + " " +
              userRepository.findByAccountNumber(accountNumber).getLastName();
      String customerAddress = userRepository.findByAccountNumber(accountNumber).getAddress();

      // Set document size and create the PDF
      Document document = new Document(PageSize.A4);

      try (OutputStream outputStream = new FileOutputStream(fileName, false)) { // 'false' overwrites the existing file
        PdfWriter.getInstance(document, outputStream);
        document.open();

        // Bank Info Table
        PdfPTable bankInfoTable = new PdfPTable(1); // Single column
        bankInfoTable.setWidthPercentage(100); // Full width of the page

        PdfPCell bankName = new PdfPCell(new Phrase("Banking Application", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE)));
        bankName.setBorder(PdfPCell.NO_BORDER);
        bankName.setBackgroundColor(BaseColor.BLUE);
        bankName.setPadding(10f);
        bankName.setHorizontalAlignment(Element.ALIGN_CENTER);

        PdfPCell bankAddress = new PdfPCell(new Phrase("Address: 123, ABC Street, XYZ City", FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.WHITE)));
        bankAddress.setBorder(PdfPCell.NO_BORDER);
        bankAddress.setBackgroundColor(BaseColor.BLUE);
        bankAddress.setPadding(10f);
        bankAddress.setHorizontalAlignment(Element.ALIGN_CENTER);

        bankInfoTable.addCell(bankName);
        bankInfoTable.addCell(bankAddress);

        // Statement Info Table
        PdfPTable statementInfo = new PdfPTable(2); // Two columns
        statementInfo.setWidthPercentage(100);
        statementInfo.setSpacingBefore(10f);

        PdfPCell customerInfo = new PdfPCell(new Phrase("Start Date: " + startDate));
        customerInfo.setBorder(PdfPCell.NO_BORDER);

        PdfPCell statement = new PdfPCell(new Phrase("Statement Of Account", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));
        statement.setBorder(PdfPCell.NO_BORDER);
        statement.setHorizontalAlignment(Element.ALIGN_CENTER);

        PdfPCell stopDate = new PdfPCell(new Phrase("End Date: " + endDate));
        stopDate.setBorder(PdfPCell.NO_BORDER);

        PdfPCell name = new PdfPCell(new Phrase("Customer Name: " + customerName));
        name.setBorder(PdfPCell.NO_BORDER);

        PdfPCell address = new PdfPCell(new Phrase("Address: " + customerAddress));
        address.setBorder(PdfPCell.NO_BORDER);

        statementInfo.addCell(customerInfo);
        statementInfo.addCell(statement);
        statementInfo.addCell(stopDate);
        statementInfo.addCell(name);
        statementInfo.addCell(new PdfPCell(new Phrase(" "))); // Empty cell for spacing
        statementInfo.addCell(address);

        // Transaction Table
        PdfPTable transactionTable = new PdfPTable(4); // Four columns for transaction details
        transactionTable.setWidthPercentage(100);
        transactionTable.setSpacingBefore(15f);

        // Set column widths
        transactionTable.setWidths(new float[]{2, 4, 3, 2});

        PdfPCell dateHeader = new PdfPCell(new Phrase("Date"));
        PdfPCell transactionTypeHeader = new PdfPCell(new Phrase("Transaction Type"));
        PdfPCell amountHeader = new PdfPCell(new Phrase("Amount"));
        PdfPCell statusHeader = new PdfPCell(new Phrase("Status"));

        dateHeader.setBackgroundColor(BaseColor.LIGHT_GRAY);
        transactionTypeHeader.setBackgroundColor(BaseColor.LIGHT_GRAY);
        amountHeader.setBackgroundColor(BaseColor.LIGHT_GRAY);
        statusHeader.setBackgroundColor(BaseColor.LIGHT_GRAY);

        dateHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
        transactionTypeHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
        amountHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
        statusHeader.setHorizontalAlignment(Element.ALIGN_CENTER);

        transactionTable.addCell(dateHeader);
        transactionTable.addCell(transactionTypeHeader);
        transactionTable.addCell(amountHeader);
        transactionTable.addCell(statusHeader);

        // Add transaction details
        transactionList.forEach(transaction -> {
          PdfPCell dateCell = new PdfPCell(new Phrase(transaction.getCreatedAt().toString()));
          dateCell.setHorizontalAlignment(Element.ALIGN_CENTER);
          dateCell.setBorder(PdfPCell.NO_BORDER); // Remove borders

          PdfPCell transactionTypeCell = new PdfPCell(new Phrase(transaction.getTransactionType()));
          transactionTypeCell.setHorizontalAlignment(Element.ALIGN_CENTER);
          transactionTypeCell.setBorder(PdfPCell.NO_BORDER);

          PdfPCell amountCell = new PdfPCell(new Phrase(transaction.getAmount().toString()));
          amountCell.setHorizontalAlignment(Element.ALIGN_CENTER);
          amountCell.setBorder(PdfPCell.NO_BORDER);

          PdfPCell statusCell = new PdfPCell(new Phrase(transaction.getStatus()));
          statusCell.setHorizontalAlignment(Element.ALIGN_CENTER);
          statusCell.setBorder(PdfPCell.NO_BORDER);

          transactionTable.addCell(dateCell);
          transactionTable.addCell(transactionTypeCell);
          transactionTable.addCell(amountCell);
          transactionTable.addCell(statusCell);
        });

        // Add tables to the document
        document.add(bankInfoTable);
        document.add(statementInfo);
        document.add(transactionTable);
        document.close();
        log.info("PDF generated successfully for account: {}", accountNumber);
      } catch (Exception e) {
        log.error("Error generating PDF", e);
      }
    }

    return transactionList;
  }
}
