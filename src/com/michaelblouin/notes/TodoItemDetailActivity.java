package com.michaelblouin.notes;

import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

import com.michaelblouin.todo.TodoGroup;


/**
 * An activity representing a single TodoItem detail screen. This
 * activity is only used on handset devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link TodoItemListActivity}.
 * <p>
 * This activity is mostly just a 'shell' activity containing nothing
 * more than a {@link TodoItemDetailFragment}.
 */
public class TodoItemDetailActivity extends Activity implements TodoItemDetailFragment.Callbacks {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todoitem_detail);

        // Show the Up button in the action bar.
        getActionBar().setDisplayHomeAsUpEnabled(true);
        
        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
        	Intent intent = getIntent();
        	
        	TodoGroup detailGroup = (TodoGroup) intent.getSerializableExtra(TodoItemDetailFragment.ARG_ITEM_GROUP);
        	
        	if (null == detailGroup) {
        		throw new IllegalStateException("No group given to detail view");
        	}
        	
        	setTitle(detailGroup.getGroupName());
        	
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putSerializable(
        		TodoItemDetailFragment.ARG_ITEM_GROUP,
                detailGroup);
            
            TodoItemDetailFragment fragment = new TodoItemDetailFragment();
            fragment.setArguments(arguments);
            fragment.onAttach(this);
            
            getFragmentManager()
            	.beginTransaction()
                .add(R.id.todoitem_detail_container, fragment)
                .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            NavUtils.navigateUpTo(this, new Intent(this, TodoItemListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    /**
     * Callback method from {@link TodoItemListFragment.Callbacks}
     * indicating that the item with the given ID was selected.
     */
    @Override
    public void onItemSelected(String id) {
//    	Map<String, TodoGroup> todoGroups =
//			((TodoItemListFragment) getFragmentManager()
//				.findFragmentById(R.id.todoitem_list))
//            		.getTodoGroups();
//    	
//    	if (!todoGroups.containsKey(id)) {
//    		throw new IllegalStateException("The given TodoGroup was not found");
//    	}
//    	
//    	TodoGroup selectedGroup = todoGroups.get(id);
//    	
//        if (mTwoPane) {
//            // In two-pane mode, show the detail view in this activity by
//            // adding or replacing the detail fragment using a
//            // fragment transaction.
//            Bundle arguments = new Bundle();
//            arguments.putSerializable(TodoItemDetailFragment.ARG_ITEM_GROUP, selectedGroup);
//            
//            TodoItemDetailFragment fragment = new TodoItemDetailFragment();
//            fragment.setArguments(arguments);
//            getFragmentManager()
//            	.beginTransaction()
//                .replace(R.id.todoitem_detail_container, fragment)
//                .commit();
//        } else {
//            // In single-pane mode, simply start the detail activity
//            // for the selected item ID.
//            Intent detailIntent = new Intent(this, TodoItemDetailActivity.class);
//            detailIntent.putExtra(TodoItemDetailFragment.ARG_ITEM_GROUP, selectedGroup);
//            startActivity(detailIntent);
//        }
    }
}
