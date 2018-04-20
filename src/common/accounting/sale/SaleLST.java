package common.accounting.sale;

import java.util.ArrayList;

import org.apache.struts.action.ActionForm;

public class SaleLST extends ActionForm {
	static final long serialVersionUID = 42L;
	ArrayList<SaleENT> saleENTs = new ArrayList<SaleENT>();
	SaleENT saleENT;
	String username;
	private int currentPage = 0;
	private int totalPages;
	private int pageSize = 10;
	private int totalItems;
	private int first;
	private boolean ascending = true;
	private String sortedByField = "username";

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public boolean isAscending() {
		return ascending;
	}

	public void setAscending(boolean ascending) {
		this.ascending = ascending;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
		calcPagingParameters();
	}

	public int getTotalItems() {
		return totalItems;
	}

	public void setTotalItems(int totalItems) {
		this.totalItems = totalItems;
		calcPagingParameters();
	}

	public void setProperties(int totalItems, int currentPage, int pageSize) {
		this.totalItems = totalItems;
		this.currentPage = currentPage;
		this.pageSize = pageSize;
		calcPagingParameters();
	}

	private void calcPagingParameters() {
		try {
			int totalPage = getTotalItems() / getPageSize();
			if (getTotalItems() % getPageSize() != 0)
				totalPage++;
			setTotalPages(totalPage);
			// check request page is exist
			if (getCurrentPage() <= 0 || getCurrentPage() > totalPage) {
				setCurrentPage(1);
			}
			setFirst((getCurrentPage() - 1) * getPageSize());
			// first = ((getCurrentPage()-1)*getPageSize());
		} catch (Exception ex) {

		}
	}

	public String getSortedByField() {
		return sortedByField;
	}

	public void setSortedByField(String sortedByField) {
		this.sortedByField = sortedByField;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public int getFirst() {
		return first;
	}

	public void setFirst(int first) {
		this.first = first;
	}

	/**
	 * @return the saleENTENTs
	 */
	public ArrayList<SaleENT> getSaleENTs() {
		if (saleENTs == null)
			saleENTs = new ArrayList<SaleENT>();
		return saleENTs;
	}

	/**
	 * @param saleENTENTs
	 *            the saleENTENTs to set
	 */
	public void setSaleENTs(ArrayList<SaleENT> saleENTENTs) {
		this.saleENTs = saleENTENTs;
	}

	/**
	 * @return the saleENT
	 */
	public SaleENT getSaleENT() {
		if (saleENT == null)
			saleENT = new SaleENT();
		return saleENT;
	}

	/**
	 * @param saleENT
	 *            the saleENT to set
	 */
	public void setSaleENT(SaleENT saleENT) {
		this.saleENT = saleENT;
	}

}
