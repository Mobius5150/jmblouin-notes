package com.michaelblouin.todo;

import java.io.Serializable;

public class TodoItem implements Comparable<TodoItem>, Serializable {
	public static final int NEXT_ID = 0;
	
	/**
	 * Random variable UID for Serializable.
	 */
	private static final long serialVersionUID = 1019873874098928L;
	private static final Boolean itemsCheckedByDefault = false;
	private static Integer nextId = 1;
	private Integer id = null;
	private String text = null;
	private Boolean checked = null;
	
	public TodoItem(String text) {
		setId(NEXT_ID);
		setText(text);
		setChecked(itemsCheckedByDefault);
	}
	
	public TodoItem(Integer id, String text, Boolean checked) {
		setId(id);
		setText(text);
		setChecked(checked);
	}
	
	public Integer getId() {
		return id;
	}
	
	private void setId(Integer id) {
		if (NEXT_ID == id) {
			id = nextId;
		}
		
		this.id = id;
		
		if (nextId <= id) {
			nextId = id + 1;
		}
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public Boolean isChecked() {
		return checked;
	}
	
	public void setChecked(Boolean checked) {
		this.checked = checked;
	}

	@Override
	public int compareTo(TodoItem arg0) {
		return getText().compareTo(arg0.getText());
	}
	
	@Override
	public String toString() {
		return String.format("[%s] %s", checked ? "X" : "_", getText());
	}
}
