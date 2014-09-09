package com.michaelblouin.notes;

import java.util.Map;
import com.michaelblouin.todo.TodoGroup;

public interface TodoGroupProvider {
	public Map<String, TodoGroup> getTodoGroups();
}
