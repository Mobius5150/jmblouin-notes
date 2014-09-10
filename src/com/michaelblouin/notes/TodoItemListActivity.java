package com.michaelblouin.notes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;

import com.michaelblouin.todo.TodoGroup;
import com.michaelblouin.todo.TodoItem;

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
 * <p>
 * This activity also implements the required
 * {@link TodoGroupListFragment.Callbacks} interface
 * to listen for item selections.
 */

// TODO: Remove the Supress Warnings
@SuppressWarnings("serial")
public class TodoItemListActivity extends Activity implements TodoGroupListFragment.Callbacks, TodoGroupProvider, FragmentManager.OnBackStackChangedListener {
	private final static Map<String, TodoGroup> todoGroups;
	static
    {
		todoGroups = new HashMap<String, TodoGroup>();
		todoGroups.put("Todo Items", new TodoGroup("Todo Items", new ArrayList<TodoItem>()));
		todoGroups.put("Archive", new TodoGroup("Archive", new ArrayList<TodoItem>()));
		
		todoGroups.get("Todo Items").getItems().addAll(new ArrayList<TodoItem>() {{
			add(new TodoItem(1, "Hello World", false));
			add(new TodoItem(2, "Get milk", false));
			add(new TodoItem(3, "Get eggs", true));
		}});
		
		todoGroups.get("Archive").getItems().addAll(new ArrayList<TodoItem>() {{
			add(new TodoItem(4, "Hi World", false));
		}});
    }
	
	private static final String TodoGroupListFragmentTag = "TodoGroupListFragment";
	private static final String TodoItemListFragmentTag = "TodoItemListFragment";
	private String activeFragmentTag;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu_todogroup_options, menu);
	    menu.setGroupVisible(
				R.id.todoitem_menu_group, 
				TodoItemListFragmentTag == activeFragmentTag);
	    
	    return true;
	}
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todoitem_list);
        activeFragmentTag = TodoGroupListFragmentTag;
        
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager
	    	.beginTransaction()
	        .add(R.id.todoitem_detail_container, new TodoGroupListFragment(), TodoGroupListFragmentTag)
	        .commit();
        
        fragmentManager.addOnBackStackChangedListener(this);
        
        getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        
        checkViewStateConsistency();
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
    		
    		invalidateOptionsMenu();
    	}
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
    		case R.id.new_todoitem:
    			System.out.println("New todoitem pressed");
    			break;
    			
    		case R.id.add_to:
    			System.out.println("Add to pressed");
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

    /**
     * Callback method from {@link TodoGroupListFragment.Callbacks}
     * indicating that the item with the given ID was selected.
     */
    @Override
    public void onItemSelected(String id) {
    	if (!todoGroups.containsKey(id)) {
    		throw new IllegalStateException("The given TodoGroup was not found");
    	}
    	
    	Bundle arguments = new Bundle();
        arguments.putString(TodoItemListFragment.ARG_ITEM_ID, id);
        
        TodoItemListFragment fragment = new TodoItemListFragment();
        fragment.setArguments(arguments);
        
        getFragmentManager()
        	.beginTransaction()
            .replace(R.id.todoitem_detail_container, fragment, TodoItemListFragmentTag)
            .addToBackStack(null)
            .commit();
        
    	activeFragmentTag = TodoItemListFragmentTag;
    }

	@Override
	public Map<String, TodoGroup> getTodoGroups() {
		return todoGroups;
	}

	@Override
	public void onBackStackChanged() {
		FragmentManager fragmentManager = getFragmentManager();
    	
    	if (fragmentManager.getBackStackEntryCount() == 0) {
    		// If the back stack is empty, we're back at the main view
    		getActionBar().setDisplayHomeAsUpEnabled(false);
    		getActionBar().setHomeButtonEnabled(false);
    		activeFragmentTag = TodoGroupListFragmentTag;
    		invalidateOptionsMenu();
    	} else if (null != fragmentManager.findFragmentByTag(TodoItemListFragmentTag)) {
    		getActionBar().setHomeButtonEnabled(true);
        	getActionBar().setDisplayHomeAsUpEnabled(true);
    	}
    	
    	invalidateOptionsMenu();
	}
}
