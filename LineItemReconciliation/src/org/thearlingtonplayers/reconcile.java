package org.thearlingtonplayers;

import java.io.BufferedReader;
import java.io.FileReader;
//import java.io.FileWriter;
//import java.io.IOException;
import java.util.ArrayList;

import org.thearlingtonplayers.utils.LineReconciliationReport;

public class reconcile {
	static final double ZERO = 0.0;
	int lineNumber = 0;
	String inputFilename;
	String outputFilename;
	String quickbooksFilename;
	ReconciledLine rl = null;
	LineReconciliationReport lrr = null; 
	String NEW_LINE_SEPARATOR = "\n";
	String FILE_HEADER = "Purchase Id, Date, Customer, Item, Number of Items, Item Cost, Fee, Donation, Total, Payment Type";
	String FILE_HEADER2 = "Sales Receipt No,Customer,Sales Receipt Date,Deposit To,Payment Method,Memo,Line Item Service Date,Line Item,Line Item Amount";

	public reconcile(String args[]) {
		System.out.println(args[0]);
		inputFilename = args[0];
		String workbookName = inputFilename.replace(".csv", ".xlsx"); 
		lrr = new LineReconciliationReport(workbookName);
		// outputFilename = args[1];
		// quickbooksFilename = args[2];

	}

	public static void main(String args[]) {

		reconcile rec = new reconcile(args);
		rec.run();

	}

	public void run() {

		BufferedReader br = null;
//		FileWriter fw = null;
//		FileWriter fw2 = null;
		String line = "";
		String cvsSplitBy = ",";

		int count = 0;
		String tempId = "new Setup";

		ArrayList<LineItem> lineItems = new ArrayList<LineItem>();

		try {
			

			br = new BufferedReader(new FileReader(inputFilename));
/*
 			fw = new FileWriter(outputFilename);
			fw2 = new FileWriter(quickbooksFilename);

			fw.append(FILE_HEADER);
			fw.append(NEW_LINE_SEPARATOR);

			fw2.append(FILE_HEADER2);
			fw2.append(NEW_LINE_SEPARATOR);
*/

			
			
			while ((line = br.readLine()) != null) {

				// use comma as separator
				String[] values = line.split(cvsSplitBy);
				// If the first line of the file (header row) skip it.
				System.out.println(values[0]);
				if (count == 0) {
					count++;
					continue;
				}
				
				lrr.addOriginalData(values);

				if (tempId.equals(values[0])) {
					// Continue to build list for processing
					LineItem li = new LineItem();
					li.setPurchaseId(values[0]);
					li.setDate(values[1]);
					li.setLineItem(values[2]);
					li.setCustomer(values[3]);
					li.setPrice(values[4]);
					li.setFees(values[5]);
					li.setPaymentType(values[6]);
					li.setTotal(values[7]);

					lineItems.add(li);
				} else {
					// Process Existing list if applicable
					if (lineItems.size() != 0) {
						lineNumber = lineNumber + 1;
						rl = processLineItems(lineItems);
						rl.setLineNumber(lineNumber);
						lrr.addReconciledLine(rl);
						/*
						fw.append(rl.toString());
						fw.append(NEW_LINE_SEPARATOR);
						*/

						String myTotal = rl.getTotalAmount().toString();
						if (!("0.0".equalsIgnoreCase(myTotal))) {

							lrr.addQuickbooksLine(rl);
							/*
							fw2.append(rl.toString2());
							fw2.append(NEW_LINE_SEPARATOR);
							*/
						}
					}

					// Reset the list to empty
					lineItems.clear();
					LineItem li = new LineItem();
					li.setPurchaseId(values[0]);
					li.setDate(values[1]);
					li.setLineItem(values[2]);
					li.setCustomer(values[3]);
					li.setPrice(values[4]);
					li.setFees(values[5]);
					li.setPaymentType(values[6]);
					li.setTotal(values[7]);

					lineItems.add(li);

					// set the new comparison Id

					tempId = values[0];

				}

			}

			// At the end of the loop - process the remaining items if
			// applicable

			if (lineItems.size() != 0) {
				lineNumber = lineNumber + 1;
				rl = processLineItems(lineItems);
				rl.setLineNumber(lineNumber);
				lrr.addReconciledLine(rl);
				/* fw.append(rl.toString());
				fw.append(NEW_LINE_SEPARATOR);
				*/
				String myTotal = rl.getTotalAmount().toString();
				if (!("0.0".equalsIgnoreCase(myTotal))) {

					/*fw2.append(rl.toString2());
					fw2.append(NEW_LINE_SEPARATOR);
					*/
					
					lrr.addQuickbooksLine(rl);
				}
			}
			
			lrr.writeWorkBook(); 

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
/*			try {

				fw.flush();
				fw.close();
				fw2.flush();
				fw2.close();
			} catch (IOException e) {
				System.out.println("Error while flusing/closing fileWriter !!!!");
				e.printStackTrace();
			}
*/
		}

	}

	public ReconciledLine processLineItems(ArrayList<LineItem> lineItems) {

		ReconciledLine rl = new ReconciledLine();
		boolean itemDetermined = false;
		float value = 0;
		float count = 0;

		ArrayList<String> types = new ArrayList<String>();

		for (LineItem li : lineItems) {
			if (!itemDetermined) {
				if (li.getLineItem().equalsIgnoreCase("Retail: Pass")) {
					types.add("Membership");
					itemDetermined = true;
				} else if (li.getLineItem().equalsIgnoreCase("Payment")) {

				} else if (li.getLineItem().equalsIgnoreCase("Exchange")
						|| li.getLineItem().equalsIgnoreCase("Order Fees")) {
					// do nothing continue
				} else if (li.getLineItem().contains("Ticket")) {
					// Possible Ticket Type

					// Split the line
					String[] values = li.getLineItem().split(":");

					if (!types.contains(values[1])) {
						types.add(values[1]);
					}
				} else if (li.getLineItem().contains("Donation") && types.size() == 0) {
					types.add(li.getLineItem());
				}
			}

			if (li.getLineItem().equalsIgnoreCase("Payment")) {
				rl.setTotalAmount(li.getTotal());
				rl.setPaymentTerm(li.getPaymentType());

			} else if (li.getLineItem().equalsIgnoreCase("Order Fees")) {
				rl.setFees(li.getFees());
			} else if (li.getLineItem().contains("Donation")) {
				rl.setDonationAmount(li.getPrice());
			} else if (li.getLineItem().equalsIgnoreCase("Exchange")) {
				// Do nothing
			} else if (li.getLineItem().equalsIgnoreCase("Purchase Comped")) {
				// Do nothing
				value = 0;
				rl.setTotalAmount(Float.valueOf("0"));
				rl.setPaymentTerm("Comped");
			} else if (li.getLineItem().contains("Refund")) {
				value = value + li.getPrice();
				count++;
				types.add("Refund");
			}

			else {
				value = value + li.getPrice();
				count++;
			}
		}

		if (types.size() == 0 && !"Comped".equals(rl.getPaymentTerm())) {
			rl.setItem("Unknown");
		} else if (types.size() == 1) {
			rl.setItem(types.get(0));
		} else if (types.size() == 2) {
			rl.setItem("Upgrade");
		} else {
			rl.setItem("Subscription");
		}

		rl.setDate(lineItems.get(0).getDate());
		rl.setPurchaseId(lineItems.get(0).getPurchaseId());
		rl.setNumOfItems(count);
		rl.setPrice(value);
		rl.setName(lineItems.get(0).getCustomer());

		if ("Refund".equals(rl.getItem())) {
			rl.setTotalAmount(rl.getPrice() + rl.getFees() + rl.getDonationAmount());
		}

		return rl;
	}

}
