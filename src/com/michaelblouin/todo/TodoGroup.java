/*
Copyright 2014 Michael Blouin

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

	http://www.apache.org/licenses/LICENSE-2.0
	
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package com.michaelblouin.todo;

import java.util.ArrayList;
import java.util.List;

import com.michaelblouin.data.ISerializableData;

public class TodoGroup implements ISerializableData {
	/**
	 * Random variable UID for Serializable.
	 */
	private static final long serialVersionUID = 8153760974926491L;
	private static Integer nextId = 1;
	private List<TodoItem> items = new ArrayList<TodoItem>();
	private String groupName = null;
	private Integer id;
	
	public TodoGroup(Integer id, String groupName, List<TodoItem> items) {
		setId(id);
		setGroupName(groupName);
		setItems(items);
	}
	
	public Integer getId() {
		return id;
	}
	
	private void setId(Integer id) {
		if (0 == id) {
			id = nextId;
		}
		
		this.id = id;
		
		if (nextId <= id) {
			nextId = id + 1;
		}
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
	
	@Override
	public String getIdentifierString() {
		return "TodoGroup" + getId().toString();
	}
}
