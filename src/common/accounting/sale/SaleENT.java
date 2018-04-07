package common.accounting.sale;

import java.util.Date;

import org.apache.struts.action.ActionForm;

public class SaleENT  extends ActionForm{
	/**
	 * 
	 */
	static final long serialVersionUID = 42L;
	private long saleId ;
	private String username ;
    private  Date dateTime ;
    private int status ;
    private double amount ;
    private String bankResponse ;
    private String currency ;
    private int paymentMethod ;
    private String creatorUsername ;
	
	public long getSaleId() {
		return saleId;
	}
	public void setSaleId(long saleId) {
		this.saleId = saleId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getBankResponse() {
		return bankResponse;
	}
	public void setBankResponse(String bankResponse) {
		this.bankResponse = bankResponse;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getCreatorUsername() {
		return creatorUsername;
	}

	public void setCreatorUsername(String creatorUsername) {
		this.creatorUsername = creatorUsername;
	}
	public int getPaymentMethod() {
		return paymentMethod;
	}
	public void setPaymentMethod(int paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
	
    

}
