package org.thearlingtonplayers.utils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.thearlingtonplayers.ReconciledLine;
import org.thearlingtonplayers.ShowPropertyValues;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class LineReconciliationReport {

	private XSSFWorkbook workbook;
	private XSSFSheet originalSheet;
	private int osRow = 0;
	private XSSFSheet reconciledSheet;
	private int rsRow = 0;
	private XSSFSheet quickbooksSheet;
	private int qbRow = 0;
	private String workbookName;
	private ShowPropertyValues spv = ShowPropertyValues.getInstance();
	private static String FALLSHOW = "fallshow";
	private static String WINTERSHOW = "wintershow";
	private static String SPRINGSHOW = "springshow";

	public LineReconciliationReport(String workbookName) {

		this.workbookName = workbookName;
		workbook = new XSSFWorkbook();
		originalSheet = workbook.createSheet("Original Data");
		reconciledSheet = workbook.createSheet("Reconciled");
		quickbooksSheet = workbook.createSheet("Quickbooks");

		// Create the headers
		addOriginalHeader();
		addReconciledHeader();
		addQuickbooksHeader();

	}

	public void addReconciledHeader() {
		String[] headerRow = { "Purchase Id", "Date", "Customer", "Item", "Number of Items", "Item Cost", "Fee",
				"Donation", "Total", "Payment Type" };
		Cell cell = null;
		int colNum = 0;
		Row row = reconciledSheet.createRow(rsRow++);
		for (String field : headerRow) {
			cell = row.createCell(colNum++);
			cell.setCellValue(field);
		}

	}

	public void addReconciledLine(ReconciledLine rl) {
		int colNum = 0;
		double dValue = 0; 
		Row row = reconciledSheet.createRow(rsRow++);
		Cell cell = row.createCell(colNum++);
		cell.setCellValue(rl.getPurchaseId());

		cell = row.createCell(colNum++);
		cell.setCellValue(rl.getDate());

		cell = row.createCell(colNum++);
		cell.setCellValue(rl.getName());

		cell = row.createCell(colNum++);
		cell.setCellValue(rl.getItem());

		cell = row.createCell(colNum++);
		if (rl.getNumOfItems() == null) {
			cell.setCellValue("0");
		} else
			cell.setCellValue(rl.getNumOfItems().toString());

		cell = row.createCell(colNum++);
		if (rl.getPrice() == null) {
			dValue = new Double("0.00").doubleValue(); 
		} else
			dValue = rl.getPrice().doubleValue();
		cell.setCellValue(dValue);

		cell = row.createCell(colNum++);

		if (rl.getFees() == null) {
			dValue = new Double("0.00").doubleValue();
		} else
			dValue = rl.getFees().doubleValue();
		cell.setCellValue(dValue);

		cell = row.createCell(colNum++);
		if (rl.getDonationAmount() == null) {
			dValue = new Double("0.00").doubleValue();
		} else
			dValue = rl.getDonationAmount().doubleValue();
		cell.setCellValue(dValue);

		cell = row.createCell(colNum++);
		if (rl.getTotalAmount() == null) {
			dValue = new Double("0.00").doubleValue();
		} else
			dValue = rl.getTotalAmount().doubleValue();
		cell.setCellValue(dValue);

		cell = row.createCell(colNum++);
		cell.setCellValue(rl.getPaymentTerm());

	}

	public void addQuickbooksHeader() {
		String[] headerRow = { "Sales Receipt No", "Customer", "Sales Receipt Date", "Deposit To", "Payment Method",
				"Memo", "Line Item Service Date", "Line Item", "Line Item Amount" };
		Cell cell = null;
		int colNum = 0;
		Row row = quickbooksSheet.createRow(qbRow++);
		for (String field : headerRow) {
			cell = row.createCell(colNum++);
			cell.setCellValue(field);
		}
	}

	public void addQuickbooksLine(ReconciledLine rl) {
		int colNum = 0;
		double dValue = 0; 
		Cell cell = null;
		Row row = quickbooksSheet.createRow(qbRow++);

		String localDate = rl.getDate().split(" ")[0];

		// String FILE_HEADER2 = "Sales Receipt No,Customer,Sales Receipt
		// Date,Deposit To,Payment Method,Memo,Line Item Service Date,Line
		// Item,Line Item Amount";

		cell = row.createCell(colNum++);
		cell.setCellValue(rl.getLineNumber());

		cell = row.createCell(colNum++);
		cell.setCellValue(rl.getName().replace("/", ", "));

		cell = row.createCell(colNum++);
		cell.setCellValue(localDate);

		cell = row.createCell(colNum++);
		cell.setCellValue("200.100 Undeposited Funds");

		cell = row.createCell(colNum++);
		cell.setCellValue(rl.getPaymentTerm());

		cell = row.createCell(colNum++);
		cell.setCellValue("Order: " + rl.getPurchaseId());

		cell = row.createCell(colNum++);
		cell.setCellValue(localDate);

		cell = row.createCell(colNum++);
		String myItem = rl.getItem().trim();
		if (myItem.equalsIgnoreCase(spv.lookupProperty(FALLSHOW))) {
			myItem = "Program Income:BO Income:Fall BO";
		} else if (myItem.equalsIgnoreCase(spv.lookupProperty(WINTERSHOW))) {
			myItem = "Program Income:BO Income:Winter BO";
		} else if (myItem.equalsIgnoreCase(spv.lookupProperty(SPRINGSHOW))) {
			myItem = "Program Income:BO Income:Spring BO";
		}
		cell.setCellValue(myItem);

		cell = row.createCell(colNum++);

		if (rl.getPrice() == null) {
			dValue = 0;
		} else {
			dValue = rl.getPrice().doubleValue();
			
		}

		cell.setCellValue(dValue); 

		String myTicketingFees = rl.getFees().toString();

		if ((!"0.0".equals(myTicketingFees))) {
			row = quickbooksSheet.createRow(qbRow++);
			colNum = 0;
			cell = row.createCell(colNum);
			cell.setCellValue(rl.getLineNumber());
			colNum = 7;
			cell = row.createCell(colNum++);
			cell.setCellValue("Ticketing Fees");

			cell = row.createCell(colNum++);
			if (rl.getFees() == null) {
				dValue = 0; 
			} else {
				dValue = rl.getFees().doubleValue(); 
			}
			cell.setCellValue(dValue);

		}

		String myDonationAmount = rl.getDonationAmount().toString();
		if ((!"0.0".equals(myDonationAmount))) {

			row = quickbooksSheet.createRow(qbRow++);
			colNum = 0;
			cell = row.createCell(colNum);
			cell.setCellValue(rl.getLineNumber());
			colNum = 7;
			cell = row.createCell(colNum++);

			String item = rl.getItem();
			if (item.equals("Membership")) {
				cell.setCellValue("Contributed Income:Unrestricted Contributions:Member Donation");
			} else if (item.equals("Subscription")) {
				cell.setCellValue("Contributed Income:Unrestricted Contributions:Subscriber Donation");
			} else if (item.equals("Donation")) {
				cell.setCellValue("Contributed Income:Unrestricted Contributions:Other Individual,");
			} else {
				cell.setCellValue("Contributed Income:Unrestricted Contributions:Other Individual");
			}

			cell = row.createCell(colNum++);
			if (rl.getDonationAmount() == null) {
				dValue = 0; 
			} else
				dValue = rl.getDonationAmount().doubleValue();
			cell.setCellValue(dValue); 
		}
	}

	public void addOriginalHeader() {
		String[] headerRow = { "Purchase Id", "Date", "Line Item", "Customer", "Price", "Fees", "Paid By",
				"Purchase Total", "Gift Cert Code", "GL Acct", "Admin User" };
		Cell cell = null;
		int colNum = 0;
		Row row = originalSheet.createRow(osRow++);
		for (String field : headerRow) {
			cell = row.createCell(colNum++);
			cell.setCellValue(field);
		}

	}

	public void addOriginalData(String[] values) {
		int colNum = 0;
		Cell cell = null;
		Row row = originalSheet.createRow(osRow++);
		for (String value : values) {
			cell = row.createCell(colNum++);
			if (!value.isEmpty() )  {
				if (colNum == 5 || colNum == 6 || colNum == 8) {

					double dvalue = new Double(value).doubleValue();
					cell.setCellValue(dvalue);
				} else
					cell.setCellValue(value);
			}

		}
	}

	public void writeWorkBook() {
		try {
			FileOutputStream outputStream = new FileOutputStream(workbookName);
			workbook.write(outputStream);
			workbook.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
