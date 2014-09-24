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

package com.michaelblouin.notes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.app.Application;
import android.util.Pair;

import com.michaelblouin.data.IDataTask;
import com.michaelblouin.data.IDataTaskCompletionHandler;
import com.michaelblouin.data.ISerializableData;
import com.michaelblouin.data.LoadDataTask;
import com.michaelblouin.data.SaveDataTask;
import com.michaelblouin.todo.TodoGroup;
import com.michaelblouin.todo.TodoGroupProvider;
import com.michaelblouin.todo.TodoItem;

public class NotesDataManager implements TodoGroupProvider {
	private List<TodoGroup> todoGroups;
	private LoadDataTask loadDataTask;
	private SaveDataTask saveDataTask;
	private Application application;
	private IDataTaskCompletionHandler secondaryHandler;
	
	private DataManagerCompletionHandler completionHandler;
	private class DataManagerCompletionHandler implements IDataTaskCompletionHandler {
		@Override
		public void taskCompleted(IDataTask task) {
			if (null == task) {
				return;
			}
			
			if (task instanceof LoadDataTask) {
				System.out.println("Load Task completed");
				// Load Todo Items
		        @SuppressWarnings("unchecked") // This is generated by a cast to a generic type. Java sucks.
				List<ISerializableData> result = (List<ISerializableData>) task.getResult();
		        
		        if (null != result) {
		        	System.out.println("Result wasn't null");
			        for (Serializable serializedGroup: result) {
			        	if (null == serializedGroup || !(serializedGroup instanceof TodoGroup)) {
			        		continue;
			        	}
			        	
			        	todoGroups.add((TodoGroup) serializedGroup);
			        }
		        } else {
		        	System.out.println("Result was null");
		        	todoGroups.add(new TodoGroup(1, "Archive", new ArrayList<TodoItem>()));
		        	todoGroups.add(new TodoGroup(2, "Todo Items", new ArrayList<TodoItem>()));
		        }
				
				loadDataTask = null;
			} else if (task instanceof SaveDataTask) {
				System.out.println("Save data task finished");
				saveDataTask = null;
			}
			
			if (null != secondaryHandler) {
				secondaryHandler.taskCompleted(task);
			}
		}
	}
	
	public NotesDataManager(Application application, IDataTaskCompletionHandler secondaryHandler) {
		this.application = application;
		this.secondaryHandler = secondaryHandler;
		
		// Load Todo Items
        todoGroups = new ArrayList<TodoGroup>();
        
	}

	@Override
	public List<TodoGroup> getTodoGroups() {
		return todoGroups;
	}

	@Override
	public TodoGroup getTodoGroup(Integer id) {
		for (TodoGroup group: todoGroups) {
			if (group.getId() == id) {
				return group;
			}
		}
		
		return null;
	}
	
	@Override
	public TodoGroup getTodoGroupByName(String name) {
		for (TodoGroup group: todoGroups) {
			if (group.getGroupName().equals(name)) {
				return group;
			}
		}
		
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public void beginLoadTodoGroups(List<Pair<String, String>> defaults) {
		loadDataTask = new LoadDataTask(this.application);
        loadDataTask.setDataTaskCompletionHandler(getCompletionManager());
        loadDataTask.execute(defaults);
	}
	
	public void beginSaveTodoGroups() {
		saveDataTask = new SaveDataTask(application);
    	saveDataTask.setDataTaskCompletionHandler(getCompletionManager());
    	saveDataTask.execute(todoGroups.toArray(new TodoGroup[todoGroups.size()]));
	}
	
	private DataManagerCompletionHandler getCompletionManager() {
		if (null == completionHandler) {
			completionHandler = new DataManagerCompletionHandler();
		}
		
		return completionHandler;
	}
}
