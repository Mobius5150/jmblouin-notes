package com.michaelblouin.todo;

import java.util.Map;

public interface TodoGroupProvider {
	public Map<String, TodoGroup> getTodoGroups();
}
