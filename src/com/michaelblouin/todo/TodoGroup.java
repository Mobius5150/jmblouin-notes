package com.michaelblouin.todo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TodoGroup implements Serializable {
	/**
	 * Random variable UID for Serializable.
	 */
	private static final long serialVersionUID = 8153760974926491L;
	List<TodoItem> items = new ArrayList<TodoItem>();
	String groupName = null;
	
	public TodoGroup(String groupName, List<TodoItem> items) {
		this.setGroupName(groupName);
		this.setItems(items);
	}
	
	public String getGroupName() {
		return this.groupName;
	}
	
	private void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
	public List<TodoItem> getItems() {
		return items;
	}
	
	private void setItems(List<TodoItem> items) {
		this.items = items;
	}
	
	public Integer getItemCount() {
		return items.size();
	}
	
	public Integer getCheckedItemCount() {
		Integer count = 0;
		
		for (TodoItem item: items) {
			if (item.isChecked()) {
				++count;
			}
		}
		
		return count;
	}
	
	public void addItemToGroup(TodoItem todoItem) {
		if (null == todoItem) {
			throw new IllegalArgumentException("New TodoItem musn't be null");
		}
		
		this.items.add(todoItem);
	}
	
	public Boolean moveItemToGroup(TodoItem item, TodoGroup group) {
		if (!getItems().contains(item) || !getItems().remove(item)) {
			return false;
		}
		
		return group.getItems().add(item);
	}
	
	@Override
	public String toString() {
		return toString("\n");
	}
	
	public String toString(String seperator) {
		String summary = new String();
		
		for (TodoItem item: items) {
			summary += item.toString() + seperator;
		}
		
		return summary;
	}
}
