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
	    		if (selectedTodoGroup != null) {
	    			getActionBar().setTitle(selectedTodoGroup.getGroupName());
	    		}

	    		getActionBar().setHomeButtonEnabled(true);
	        	getActionBar().setDisplayHomeAsUpEnabled(true);
	    	}
	    	
	    	invalidateOptionsMenu();
		}
	}
	
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
	    	
	    	Bundle arguments = new Bundle();
	        arguments.putString(TodoItemListFragment.ARG_ITEM_ID, group.getGroupName());
	        
	        TodoItemListFragment fragment = new TodoItemListFragment();
	        fragment.setArguments(arguments);
	        
	        getFragmentManager()
	        	.beginTransaction()
	            .replace(R.id.todoitem_detail_container, fragment, TodoItemListFragmentTag)
	            .addToBackStack(null)
	            .commit();
	        
	    	activeFragmentTag = TodoItemListFragmentTag;
	    }
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu_todogroup_options, menu);
	    
	    menu.setGroupVisible(
			R.id.todoitem_menu_group, 
			TodoItemListFragmentTag == activeFragmentTag);
	    
	    menu.setGroupVisible(
			R.id.todogroup_menu_group, 
			TodoGroupListFragmentTag == activeFragmentTag);
	    
	    return true;
	}
	
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
        activeFragmentTag = TodoGroupListFragmentTag;
        
        todoGroupListFragment = new TodoGroupListFragment();
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
    
    @Override
    protected void onPause() {
    	super.onPause();
    	dataManager.beginSaveTodoGroups();
    }

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
    		case R.id.new_todoitem:
    			System.out.println("New todoitem pressed");
    			newItemPrompt = new NewTodoItemPrompt(this);
    			
    			if (null == newTodoItemCompletionListener) {
    				newTodoItemCompletionListener = new NewTodoItemCompletionListener();
    			}
    			
    			newItemPrompt.setOnCompletionListener(newTodoItemCompletionListener);
    			
    			newItemPrompt.show();
    			break;
    			
    		case R.id.email_todogroup:
				System.out.println("Email todogroup button pressed");
				
				if (null == selectedTodoGroup) {
					throw new IllegalStateException("Cannot email todo items: No todo group selected");
				}
				
				TodoItemMailer mailer = new TodoItemMailer(this);
				mailer.addTodoGroup(selectedTodoGroup);
				mailer.send();
				break;
				
    		case R.id.email_todogroups:
				System.out.println("Email todogroups button pressed");

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
    			FragmentManager fragmentManager = getFragmentManager();
    	    	
    	    	if (fragmentManager.getBackStackEntryCount() > 0) {
    	    		activeFragmentTag = TodoGroupListFragmentTag;
    	    		fragmentManager.popBackStack();
    	    	}
    	}
    	
    	return true;
    }
    
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
