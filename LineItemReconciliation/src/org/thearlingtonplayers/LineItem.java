package org.thearlingtonplayers;

public class LineItem {
	
	String purchaseId;
	String date;
	String lineItem;
	String customer;
	Float price;
	Float fees;
	String paymentType;
	Float total; 
	
	public LineItem() {}

	public String getPurchaseId() {
		return purchaseId;
	}

	public void setPurchaseId(String purchaseId) {
		this.purchaseId = purchaseId;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date){ 
		this.date = date;  
	}

	public String getLineItem() {
		return lineItem;
	}

	public void setLineItem(String lineItem) {
		this.lineItem = lineItem;
	}

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public Float getPrice() {
		return price;
	}

	public void setPrice(String price) {
		if (!price.isEmpty()) 
		this.price = Float.valueOf(price);
		else this.price=Float.valueOf("0");
	}

	public Float getFees() {
		return fees;
	}

	public void setFees(String fees) {
		if (!fees.isEmpty())
		this.fees = Float.valueOf(fees);
		else this.fees= Float.valueOf("0"); 
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType.toLowerCase();
	}

	public Float getTotal() {
		return total;
	}

	public void setTotal(String total) {
		if (!total.isEmpty())
		this.total = Float.valueOf(total);
		else this.total = Float.valueOf("0");
	} 
	

}
