package com.michaelblouin.todo;

import java.io.Serializable;

public class TodoItem implements Comparable<TodoItem>, Serializable {
	/**
	 * Random variable UID for Serializable.
	 */
	private static final long serialVersionUID = 1019873874098928L;
	private Integer id = null;
	private String text = null;
	private Boolean checked = null;
	
	public TodoItem(Integer id, String text, Boolean checked) {
		setId(id);
		setText(text);
		setChecked(checked);
	}
	
	public Integer getId() {
		return id;
	}
	
	private void setId(Integer id) {
		this.id = id;
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
		return String.format("[%s] %s", checked ? " " : "X", getText());
	}
}
