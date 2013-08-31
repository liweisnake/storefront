package com.hp.sdf.ngp.android.ui.common;

import java.util.ArrayList;
import java.util.Collection;

import com.hp.sdf.ngp.android.model.Page;

public class PagableList<T> extends ArrayList<T> {

	private static final long serialVersionUID = -2349680432590118346L;

	protected String name;

	protected Page page;

	public PagableList() {
		super();
		page = new Page();
	}

	public PagableList(Collection<? extends T> collection) {
		super(collection);
		page = new Page();
	}

	public PagableList(int capacity) {
		super(capacity);
		page = new Page();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void previous() {
		page.previous();
	}

	public void next() {
		page.next();
	}

	public Page getPage() {
		return page;
	}

	public int getPageStart() {
		return page.getStartRow();
	}

	public int getItemsPerPage() {
		return page.getItemsPerPage();
	}

	protected void flush() {

	}

	@SuppressWarnings("unchecked")
	public boolean contains(Object object) {
		for (Object o : this) {
			PagableList list = (PagableList) o;
			if (list.getName().equalsIgnoreCase(object.toString()))
				return true;
		}
		return false;
	}

}
