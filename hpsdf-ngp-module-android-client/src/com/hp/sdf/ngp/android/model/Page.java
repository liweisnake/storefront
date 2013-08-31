package com.hp.sdf.ngp.android.model;

import android.util.Log;

public class Page {

	public static final String FIRST_PAGE = "FIRST_PAGE";

	public static final String LAST_PAGE = "LAST_PAGE";

	private int count = -1;// total count

	private int itemsPerPage = 10;// count per page

	private int currentPage = 1;// current page NO.

	private int pageCount = -1;// page count

	private int startRow = 0;// start count

	public Page() {
	}

	public void clear() {
		count = -1;// total count

		currentPage = 1;// current page NO.

		pageCount = -1;// page count

		startRow = 0;// start count
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
		setInitPageCount();
	}

	public int getItemsPerPage() {
		return itemsPerPage;
	}

	public void setItemsPerPage(int itemsPerPage) {
		this.itemsPerPage = itemsPerPage;
		setInitPageCount();
	}

	private void setInitPageCount() {

		if (count > 0) {
			Log.d("setInitPageCount", "original pageCount :" + pageCount);

			pageCount = count / itemsPerPage
					+ (count % itemsPerPage > 0 ? 1 : 0);
			Log.d("After setting pageCount :", "pageCount :" + pageCount);
		}
	}

	public void previous() {
		if (currentPage == 1) {
			return;
		}
		currentPage--;
		Log.d("currentPage", "currentPage :" + currentPage);

		startRow = (currentPage - 1) * itemsPerPage;
		Log.d("startRow", "startRow :" + startRow);
	}

	public void next() {
		Log.d("pageCount", pageCount + "");

		if (currentPage < pageCount) {
			Log.d("currentPage", "currentPage will add 1.");
			currentPage++;
		}
		Log.d("currentPage", "currentPage :" + currentPage);

		startRow = (currentPage - 1) * itemsPerPage;
		Log.d("startRow", "startRow :" + startRow);
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public int getStartRow() {
		return startRow;
	}

	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}
}
