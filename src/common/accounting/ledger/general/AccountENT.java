
package common.accounting.ledger.general;

import java.util.ArrayList;
import java.util.List;

import common.accounting.payment.PaymentENT;

import common.accounting.sale.SaleENT;

public class AccountENT {
	private String name;
	private List<PaymentENT> debitList;
	private double debitAmount;
	private double creditAmount;
	private String accountId;
	private List<SaleENT> creditList;

	public void setDebitList(List<PaymentENT> debitList) {
		this.debitList = debitList;
	}
	
	public void addDebitList(List<PaymentENT> debitList) {
		
		this.debitList.addAll(debitList) ;
	}

	public void addCreditList(List<SaleENT> debitList) {
		this.creditList.addAll(creditList) ;
	}
	public void setDebitAmount(double debitAmount) {
		this.debitAmount = debitAmount;
	}
      
	public void setCreditAmount(double creditAmount) {
		this.creditAmount = creditAmount;
	}

	public void setCreditList(List<SaleENT> creditList) {
		this.creditList = creditList;
	}

	public List<PaymentENT> getDebitList() {
		return debitList;
	}

	public List<SaleENT> getCreditList() {
		return creditList;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getBalance() {
		for (PaymentENT ent : debitList) {
			debitAmount += ent.getAmount();
		}
		for (SaleENT ent : creditList) {
			creditAmount += ent.getAmount();
		}
		switch (accountId.charAt(0)) {
		case 'A':
		case 'E': {
			return debitAmount - creditAmount;
		}
		default: {
			return creditAmount - debitAmount;

		}

		}
	}

}
