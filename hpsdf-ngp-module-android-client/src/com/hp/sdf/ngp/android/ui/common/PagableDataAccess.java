package com.hp.sdf.ngp.android.ui.common;

import java.util.ArrayList;

public interface PagableDataAccess<T> {

	public ArrayList<T> getDatas();

	public Long getDataCount();

	public int getItemsPerPage();

}
