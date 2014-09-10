package com.michaelblouin.notes;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.michaelblouin.todo.TodoGroup;
import com.michaelblouin.todo.TodoItem;

/**
 * A fragment representing a single TodoItem detail screen.
 * This fragment is either contained in a {@link TodoItemListActivity}
 * in two-pane mode (on tablets) or a {@link TodoItemDetailActivity}
 * on handsets.
 */
public class TodoItemDetailFragment extends ListFragment implements MultiChoiceModeListener {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
	public static final String ARG_ITEM_ID = "com.michaelblouin.notes.item_id";

	/**
     * The serialization (saved instance state) Bundle key representing the
     * activated item position. Only used on tablets.
     */
    private static final String STATE_ACTIVATED_POSITION = "activated_position";

    /**
     * The fragment's current callback object, which is notified of list item
     * clicks.
     */
    private Callbacks mCallbacks = sDummyCallbacks;

    /**
     * The current activated item position. Only used on tablets.
     */
    private int mActivatedPosition = ListView.INVALID_POSITION;

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callbacks {
        /**
         * Callback for when an item has been selected.
         */
        public void onItemSelected(String id);
    }

    /**
     * A dummy implementation of the {@link Callbacks} interface that does
     * nothing. Used only when this fragment is not attached to an activity.
     */
    private static Callbacks sDummyCallbacks = new Callbacks() {
        @Override
        public void onItemSelected(String id) {
        }
    };
	
    /**
     * The dummy content this fragment is presenting.
     */
    private TodoGroup mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TodoItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        
        if (arguments.containsKey(ARG_ITEM_ID)) {
        	Map<String, TodoGroup> todoGroups = ((TodoGroupProvider) getActivity()).getTodoGroups();
        	
        	if (!todoGroups.containsKey(arguments.getString(ARG_ITEM_ID))) {
        		throw new IllegalStateException("Error: The given item id was not found in the collection");
        	}
        	
        	mItem = todoGroups.get(arguments.getString(ARG_ITEM_ID));
        }
        
        if (null != mItem) {
        	List<TodoItem> todoItems = mItem.getItems();
        	
        	setListAdapter(
        		new TodoItemListAdapter<TodoItem>(
    				getActivity(), 
    				todoItems.toArray(new TodoItem[todoItems.size()])));
        }
    }
    
    public TodoGroup getTodoGroup() {
    	return mItem;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Restore the previously serialized activated item position.
        if (savedInstanceState != null
                && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
        }
        
        ListView listView = getListView();
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(this);
        setActivateOnItemClick(true);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        
        if (!(activity instanceof TodoGroupProvider)) {
        	throw new IllegalStateException("Activity must implement TodoGroupProvider");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        // Reset the active callbacks interface to the dummy implementation.
        mCallbacks = sDummyCallbacks;
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);
        
        listView.setItemChecked(position, !listView.isItemChecked(position));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != ListView.INVALID_POSITION) {
            // Serialize and persist the activated item position.
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
    }

    /**
     * Turns on activate-on-click mode. When this mode is on, list items will be
     * given the 'activated' state when touched.
     */
    public void setActivateOnItemClick(boolean activateOnItemClick) {
        // When setting CHOICE_MODE_SINGLE, ListView will automatically
        // give items the 'activated' state when touched.
//        getListView().setChoiceMode(activateOnItemClick
//            ? ListView.CHOICE_MODE_SINGLE
//            : ListView.CHOICE_MODE_NONE);
    }

    private void setActivatedPosition(int position) {
        if (position == ListView.INVALID_POSITION) {
            getListView().setItemChecked(mActivatedPosition, false);
        } else {
            getListView().setItemChecked(position, true);
        }

        mActivatedPosition = position;
    }
    
	@Override
	public boolean onActionItemClicked(ActionMode arg0, MenuItem arg1) {
		// TODO Auto-generated method stub
		System.out.println("Action item clicked");
		return false;
	}

	@Override
	public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
		MenuInflater inflater = actionMode.getMenuInflater();
		inflater.inflate(R.menu.menu_todoitem_context, menu);
		return true;
	}

	@Override
	public void onDestroyActionMode(ActionMode actionMode) {
		System.out.println("Action mode destroyed");
	}

	@Override
	public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onItemCheckedStateChanged(ActionMode actionMode, int position, long id, boolean checked) {
		// TODO Auto-generated method stub
		System.out.println(String.format("Item %d is now %schecked", position, checked ? "": "un"));
	}
}
