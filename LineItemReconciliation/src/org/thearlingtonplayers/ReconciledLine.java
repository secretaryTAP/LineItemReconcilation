package org.thearlingtonplayers;

public class ReconciledLine {

	String NEW_LINE_SEPARATOR = "\n";
	String purchaseId;
	String date;
	String item;
	Float price = new Float("0");
	Float fees = new Float("0");
	Float numOfItems = new Float("0");
	String paymentTerm;
	Float donationAmount = new Float("0");
	Float totalAmount = new Float("0");
	String name = "";
	int lineNumber;

	static Float ZERO = new Float("0.00");

	public ReconciledLine() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPurchaseId() {
		return purchaseId;
	}

	public void setPurchaseId(String purchaseId) {
		this.purchaseId = purchaseId;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public Float getPrice() {
		return price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}

	public Float getFees() {
		return fees;
	}

	public void setFees(Float fees) {
		this.fees = fees;
	}

	public Float getNumOfItems() {
		return numOfItems;
	}

	public void setNumOfItems(Float numOfItems) {
		this.numOfItems = numOfItems;
	}

	public Float getDonationAmount() {
		return donationAmount;
	}

	public void setDonationAmount(Float donationAmount) {
		this.donationAmount = donationAmount;
	}

	public Float getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Float totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getPaymentTerm() {
		return paymentTerm;
	}

	public void setPaymentTerm(String paymentTerm) {

		if (paymentTerm != null) {
			this.paymentTerm = paymentTerm.substring(0, 1).toUpperCase() + paymentTerm.substring(1);
		}
	}

	public int getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}

	public String toString2() {
		StringBuffer sb = new StringBuffer();

		String localDate = date.split(" ")[0];

		// String FILE_HEADER2 = "Sales Receipt No,Customer,Sales Receipt
		// Date,Deposit To,Payment Method,Memo,Line Item Service Date,Line
		// Item,Line Item Amount";

		sb.append(lineNumber + "," + name + "," + localDate + ",200.100 Undeposited Funds," + paymentTerm + ",Order: "
				+ purchaseId + "," + localDate + ",");
		sb.append(item.trim() + ",");
		if (price == null) {
			sb.append("0.00,");
		} else {
			sb.append(price + ",");
		}

		String myTicketingFees = fees.toString();

		if ((!"0.0".equals(myTicketingFees))) {
			sb.append(NEW_LINE_SEPARATOR);

			sb.append(lineNumber + ",,,,,,,Ticketing Fees,");

			if (fees == null) {
				sb.append("0.00,");
			} else {
				sb.append(fees + ",");
			}

		}

		String myDonationAmount = donationAmount.toString();
		if ((!"0.0".equals(myDonationAmount))) {

			sb.append(NEW_LINE_SEPARATOR);

			sb.append(lineNumber + ",,,,,,,");

			if (item.equals("Membership")) {
				sb.append("Contributed Income:Unrestricted Contributions:Member Donation,");
			} else if (item.equals("Subscription")) {
				sb.append("Contributed Income:Unrestricted Contributions:Subscriber Donation,");
			} else if (item.equals("Donation")) {
				sb.append("Contributed Income:Unrestricted Contributions:Other Individual,");
			} else {
				sb.append("Contributed Income:Unrestricted Contributions:Other Individual,");
			}

			if (donationAmount == null) {
				sb.append("0.00");
			} else
				sb.append(donationAmount);
		}

		return sb.toString();
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();

		// String FILE_HEADER="Purchase Id, Date, Name, Item, Number of Items,
		// Item Cost, Fee, Donation, Total, Payment Type";

		sb.append(purchaseId + "," + date + "," + name + "," + item + ",");

		if (numOfItems == null) {
			sb.append("0,");
		} else {
			sb.append(numOfItems + ",");
		}

		if (price == null) {
			sb.append("0.00,");
		} else {
			sb.append(price + ",");
		}

		if (fees == null) {
			sb.append("0.00,");
		} else {
			sb.append(fees + ",");
		}

		if (donationAmount == null) {
			sb.append("0.00,");
		} else
			sb.append(donationAmount + ",");

		if (this.totalAmount == null) {
			sb.append("0.00");
		} else {
			sb.append(this.totalAmount);
		}

		sb.append("," + paymentTerm);

		return sb.toString();
	}

}
