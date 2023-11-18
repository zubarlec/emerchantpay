package com.emerchantpay.backend.web.dto;

import java.util.ArrayList;
import java.util.List;

public class ListWrapper<T> {
	private List<T> value = new ArrayList<T>();
	
	public ListWrapper() {
	}
	
	public ListWrapper(List<T> value) {
		this.value = value;
	}

	public List<T> getValue() {
		return value;
	}

	public void setValue(List<T> value) {
		this.value = value;
	}

}
