package com.michaelblouin.notes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
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
 * {@link TodoItemListFragment} and the item details
 * (if present) is a {@link TodoItemDetailFragment}.
 * <p>
 * This activity also implements the required
 * {@link TodoItemListFragment.Callbacks} interface
 * to listen for item selections.
 */
public class TodoItemListActivity extends Activity implements TodoItemListFragment.Callbacks, TodoGroupProvider {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todoitem_list);
        
        getFragmentManager()
	    	.beginTransaction()
	        .add(R.id.todoitem_detail_container, new TodoItemListFragment(), TodoGroupListFragmentTag)
	        .commit();
        
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
    	}
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	FragmentManager fragmentManager = getFragmentManager();
    	
    	if (fragmentManager.getBackStackEntryCount() > 0) {
    		getActionBar().setDisplayHomeAsUpEnabled(false);
    		getActionBar().setHomeButtonEnabled(false);
    		fragmentManager.popBackStack();
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
     * Callback method from {@link TodoItemListFragment.Callbacks}
     * indicating that the item with the given ID was selected.
     */
    @Override
    public void onItemSelected(String id) {
    	if (!todoGroups.containsKey(id)) {
    		throw new IllegalStateException("The given TodoGroup was not found");
    	}
    	
    	Bundle arguments = new Bundle();
        arguments.putString(TodoItemDetailFragment.ARG_ITEM_ID, id);
        
        TodoItemDetailFragment fragment = new TodoItemDetailFragment();
        fragment.setArguments(arguments);
        
        getFragmentManager()
        	.beginTransaction()
            .replace(R.id.todoitem_detail_container, fragment, TodoItemListFragmentTag)
            .addToBackStack(null)
            .commit();
        
        getActionBar().setHomeButtonEnabled(true);
    	getActionBar().setDisplayHomeAsUpEnabled(true);
    }

	@Override
	public Map<String, TodoGroup> getTodoGroups() {
		return todoGroups;
	}
}
