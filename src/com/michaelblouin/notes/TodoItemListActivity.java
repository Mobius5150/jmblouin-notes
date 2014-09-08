package com.michaelblouin.notes;

import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.michaelblouin.todo.TodoGroup;

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
public class TodoItemListActivity extends Activity implements TodoItemListFragment.Callbacks {
    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todoitem_list);

        if (findViewById(R.id.todoitem_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-large and
            // res/values-sw600dp). If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;

            // In two-pane mode, list items should be given the
            // 'activated' state when touched.
            ((TodoItemListFragment) getFragmentManager()
            	.findFragmentById(R.id.todoitem_list))
                	.setActivateOnItemClick(true);
        }
    }

    /**
     * Callback method from {@link TodoItemListFragment.Callbacks}
     * indicating that the item with the given ID was selected.
     */
    @Override
    public void onItemSelected(String id) {
    	Map<String, TodoGroup> todoGroups =
			((TodoItemListFragment) getFragmentManager()
				.findFragmentById(R.id.todoitem_list))
            		.getTodoGroups();
    	
    	if (!todoGroups.containsKey(id)) {
    		throw new IllegalStateException("The given TodoGroup was not found");
    	}
    	
    	TodoGroup selectedGroup = todoGroups.get(id);
    	
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putSerializable(TodoItemDetailFragment.ARG_ITEM_GROUP, selectedGroup);
            
            TodoItemDetailFragment fragment = new TodoItemDetailFragment();
            fragment.setArguments(arguments);
            getFragmentManager()
            	.beginTransaction()
                .replace(R.id.todoitem_detail_container, fragment)
                .commit();
        } else {
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            Intent detailIntent = new Intent(this, TodoItemDetailActivity.class);
            detailIntent.putExtra(TodoItemDetailFragment.ARG_ITEM_GROUP, selectedGroup);
            startActivity(detailIntent);
        }
    }
}
