package com.michaelblouin.todo;

import java.util.List;

public interface TodoGroupProvider {
	public List<TodoGroup> getTodoGroups();
	public TodoGroup getTodoGroup(Integer id);
	public TodoGroup getTodoGroupByName(String name);
}
