package common.game.poker.holdem;

import java.util.ArrayList;

import common.user.UserENT;

public class GameLST {
	ArrayList<GameENT> userENTs = new ArrayList<GameENT>();
	GameENT searchPayment = new GameENT() ;
	private int currentPage = 0;
	private int totalPages;
	private int pageSize = 10;
	private int totalItems;
	private int first;
	private boolean ascending = true;
	private String sortedByField = "gameId";
   
	public GameLST() {
		
	}
	public GameLST(ArrayList<GameENT> userENTs, GameENT searchPayment,
			int currentPage, int totalPages, int pageSize, int totalItems,
			int first, boolean ascending, String sortedByField) {
		super();
		this.userENTs = userENTs;
		this.searchPayment = searchPayment;
		this.currentPage = currentPage;
		this.totalPages = totalPages;
		this.pageSize = pageSize;
		this.totalItems = totalItems;
		this.first = first;
		this.ascending = ascending;
		this.sortedByField = sortedByField;
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
	public ArrayList<GameENT> getUserENTs() {
		return userENTs;
	}
	public void setUserENTs(ArrayList<GameENT> userENTs) {
		this.userENTs = userENTs;
	}
	public GameENT getSearchPayment() {
		return searchPayment;
	}
	public void setSearchPayment(GameENT searchPayment) {
		this.searchPayment = searchPayment;
	}
	public int getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	public int getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
		calcPagingParameters();
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getTotalItems() {
		return totalItems;
	}
	public void setTotalItems(int totalItems) {
		this.totalItems = totalItems;
	}
	public int getFirst() {
		return first;
	}
	public void setFirst(int first) {
		this.first = first;
	}
	public boolean isAscending() {
		return ascending;
	}
	public void setAscending(boolean ascending) {
		this.ascending = ascending;
	}
	public String getSortedByField() {
		return sortedByField;
	}
	public void setSortedByField(String sortedByField) {
		this.sortedByField = sortedByField;
	}

}
