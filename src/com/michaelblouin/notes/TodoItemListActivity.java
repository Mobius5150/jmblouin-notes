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

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;

import com.michaelblouin.data.IDataTask;
import com.michaelblouin.data.IDataTaskCompletionHandler;
import com.michaelblouin.data.LoadDataTask;
import com.michaelblouin.todo.TodoGroup;
import com.michaelblouin.todo.TodoItem;
import com.michaelblouin.todo.TodoItemMailer;

/**
 * An activity representing a list of Todo Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link TodoItemDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link TodoGroupListFragment} and the item details
 * (if present) is a {@link TodoItemListFragment}.
 */

public class TodoItemListActivity extends Activity {
	private static final String TodoGroupListFragmentTag = "TodoGroupListFragment";
	private static final String TodoItemListFragmentTag = "TodoItemListFragment";
	private String activeFragmentTag;
	private TodoGroup selectedTodoGroup;
	private NewTodoItemPrompt newItemPrompt;
	private TodoGroupListFragment todoGroupListFragment;
	
	private NotesDataManager dataManager;
	
	/**
	 * Handles the completion of a data save/load task. Triggers updating of the list activity when data is initially loaded.
	 */
	private class DataTaskCompletionHandler implements IDataTaskCompletionHandler {
		@Override
		public void taskCompleted(IDataTask task) {
			if (null == task) {
				return;
			}
			
			if (task instanceof LoadDataTask) {
		        TodoGroupListFragment todoList = (TodoGroupListFragment) getFragmentManager().findFragmentByTag(TodoGroupListFragmentTag);
		        
		        if (null != todoList) {
		        	todoList.notifyDataSetChanged();
		        }
			}
		}
	}
	
	private NewTodoItemPrompt.TodoItemPromptCompletionListener newTodoItemCompletionListener;
	
	/**
	 * Handles the completion of the new todo item view. Creates the new todo item.
	 */
	private class NewTodoItemCompletionListener extends NewTodoItemPrompt.TodoItemPromptCompletionListener {
		@Override
		public void onComplete(Boolean accepted, String value) {
			if (accepted) {
				selectedTodoGroup.addItemToGroup(new TodoItem(value));
				newItemPrompt = null;
				
				TodoItemListFragment todoList = (TodoItemListFragment) getFragmentManager().findFragmentByTag(TodoItemListFragmentTag);
				todoList.notifyDataSetChanged();
			}
		}
	}

	/**
	 * Handles any changes to the back stack and ensures that the layout of the activity matches the correct layout for the active fragment.
	 */
	private class TodoGroupActivityBackStackChangeListener implements FragmentManager.OnBackStackChangedListener {
		@Override
		public void onBackStackChanged() {
			FragmentManager fragmentManager = getFragmentManager();
	    	
	    	if (fragmentManager.getBackStackEntryCount() == 0) {
	    		// If the back stack is empty, we're back at the main view
	    		getActionBar().setTitle(getString(R.string.app_name));
	    		getActionBar().setDisplayHomeAsUpEnabled(false);
	    		getActionBar().setHomeButtonEnabled(false);
	    		activeFragmentTag = TodoGroupListFragmentTag;
	    		selectedTodoGroup = null;
	    		invalidateOptionsMenu();
	    	} else if (null != fragmentManager.findFragmentByTag(TodoItemListFragmentTag)) {
	    		// Change the name of the activity for the current list fragment to the active TodoGroup.
	    		if (selectedTodoGroup != null) {
	    			getActionBar().setTitle(selectedTodoGroup.getGroupName());
	    		}

	    		getActionBar().setHomeButtonEnabled(true);
	        	getActionBar().setDisplayHomeAsUpEnabled(true);
	    	}
	    	
	    	invalidateOptionsMenu();
		}
	}
	
	/**
	 * Handles the selection of a todo group in the todo group fragment. Launches the next fragment for viewing the group.
	 */
	private class TodoGroupListFragmentCallbackHandler implements TodoGroupListFragment.Callback {
		/**
	     * Callback method from {@link TodoGroupListFragment.Callbacks}
	     * indicating that the item with the given ID was selected.
	     */
	    @Override
	    public void onItemSelected(TodoGroup group) {
	    	selectedTodoGroup = group;
	    	
	    	if (null == selectedTodoGroup) {
	    		throw new IllegalStateException("The given TodoGroup was not found");
	    	}
	    	
	    	// Get the selected group, and pass it to the TodoItemListFragment in a bundle
	    	Bundle arguments = new Bundle();
	        arguments.putString(TodoItemListFragment.ARG_ITEM_ID, group.getGroupName());
	        
	        TodoItemListFragment fragment = new TodoItemListFragment(dataManager);
	        fragment.setArguments(arguments);
	        
	        getFragmentManager()
	        	.beginTransaction()
	            .replace(R.id.todoitem_detail_container, fragment, TodoItemListFragmentTag)
	            .addToBackStack(null)
	            .commit();
	        
	    	activeFragmentTag = TodoItemListFragmentTag;
	    }
	}
	
	/**
	 * Called when the options menu is created. This method makes sure that the visible groups match the current view. 
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu_todogroup_options, menu);
	    
	    // Only show the TodoItem Menu Group for the TodoItemListFragment
	    menu.setGroupVisible(
			R.id.todoitem_menu_group, 
			TodoItemListFragmentTag == activeFragmentTag);
	    
	    // Only show the TodoGroup Menu Group for the TodoGroupListFragment
	    menu.setGroupVisible(
			R.id.todogroup_menu_group, 
			TodoGroupListFragmentTag == activeFragmentTag);
	    
	    return true;
	}
	
	/**
	 * Initializes the view and instantiates the default fragment.
	 */
	@SuppressWarnings("serial")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Initialize the data manager with the set of default TodoGroups
        dataManager = new NotesDataManager(getApplication(), new DataTaskCompletionHandler());
        dataManager.beginLoadTodoGroups(new ArrayList<Pair<String, String>>() {{
        	add(new Pair<String, String>("TodoGroup", "1"));
    		add(new Pair<String, String>("TodoGroup", "2"));
        }});
        
        setContentView(R.layout.activity_todoitem_list);
        
        // Setup the default fragment
        activeFragmentTag = TodoGroupListFragmentTag;
        todoGroupListFragment = new TodoGroupListFragment(dataManager);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager
	    	.beginTransaction()
	        .add(R.id.todoitem_detail_container, todoGroupListFragment, TodoGroupListFragmentTag)
	        .commit();
        
        fragmentManager.addOnBackStackChangedListener(new TodoGroupActivityBackStackChangeListener());
        todoGroupListFragment.setItemClickedListener(new TodoGroupListFragmentCallbackHandler());
        
        getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        
        checkViewStateConsistency();
    }
    
	/**
	 * Called when the activity is paused. Triggers the saving of user data.
	 */
    @Override
    protected void onPause() {
    	super.onPause();
    	dataManager.beginSaveTodoGroups();
    }

    /**
     * Ensures that we never have two fragments open at a given time.
     */
    private void checkViewStateConsistency() {
    	FragmentManager fragmentManager = getFragmentManager();
    	
    	Boolean todoGroupFound = null != fragmentManager.findFragmentByTag(TodoGroupListFragmentTag);
    	Boolean todoListFound = null != fragmentManager.findFragmentByTag(TodoItemListFragmentTag);
    	
    	if (todoGroupFound && todoListFound) {
    		getFragmentManager()
		    	.beginTransaction()
		        .remove(fragmentManager.findFragmentByTag(TodoItemListFragmentTag))
		        .commit();
    		
    		activeFragmentTag = TodoGroupListFragmentTag;
    		selectedTodoGroup = null;
    		
    		invalidateOptionsMenu();
    	}
    }

    /**
     * Called when a menu item is pressed. Begins most of the menu actions.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
    		case R.id.new_todoitem:
    			// Create a new item prompt to get the name of the new item from the user.
    			newItemPrompt = new NewTodoItemPrompt(this);
    			
    			if (null == newTodoItemCompletionListener) {
    				newTodoItemCompletionListener = new NewTodoItemCompletionListener();
    			}
    			
    			newItemPrompt.setOnCompletionListener(newTodoItemCompletionListener);
    			
    			newItemPrompt.show();
    			break;
    			
    		case R.id.email_todogroup:
				// Email the selected todogroup using a TodoItemMailer.
				if (null == selectedTodoGroup) {
					throw new IllegalStateException("Cannot email todo items: No todo group selected");
				}
				
				TodoItemMailer mailer = new TodoItemMailer(this);
				mailer.addTodoGroup(selectedTodoGroup);
				mailer.send();
				break;
				
    		case R.id.email_todogroups:
    			// Email the selected todogroups using a TodoItemMailer.
				List<TodoGroup> todoGroups = dataManager.getTodoGroups();
				
				if (null == todoGroups) {
					throw new IllegalStateException("Cannot email todo items: Todo groups not loaded");
				}

				mailer = new TodoItemMailer(this);
				
				for (TodoGroup group: todoGroups) {
					mailer.addTodoGroup(group);
				}
				
				mailer.send();
				break;
				
    		default:
    			// Menu button pressed. Pop back stack.
    			FragmentManager fragmentManager = getFragmentManager();
    	    	
    	    	if (fragmentManager.getBackStackEntryCount() > 0) {
    	    		activeFragmentTag = TodoGroupListFragmentTag;
    	    		fragmentManager.popBackStack();
    	    	}
    	}
    	
    	return true;
    }
    
    /**
     * Called when a checkbox in one of the lists is clicked. Sets the checked status of the checkbox.
     * @param view
     */
    public void onCheckboxClicked(View view) {
		CheckBox checkbox = (CheckBox) view;
		Object checkboxTag = checkbox.getTag();
		
		if (!(checkboxTag instanceof TodoItem)) {
			throw new IllegalStateException("Error: Expected TodoItem in checkbox tag");
		}
		
		TodoItem item = (TodoItem) checkboxTag;
		item.setChecked(checkbox.isChecked());
    }
}
