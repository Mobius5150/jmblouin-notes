package com.michaelblouin.notes;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.michaelblouin.todo.TodoGroup;
import com.michaelblouin.todo.TodoGroupProvider;
import com.michaelblouin.todo.TodoItem;
import com.michaelblouin.todo.TodoItemMailer;

/**
 * A fragment representing a single TodoItem detail screen.
 * This fragment is either contained in a {@link TodoItemListActivity}
 * in two-pane mode (on tablets) or a {@link TodoItemDetailActivity}
 * on handsets.
 */
public class TodoItemListFragment extends ListFragment implements MultiChoiceModeListener {
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
     * The current activated item position. Only used on tablets.
     */
    private int mActivatedPosition = ListView.INVALID_POSITION;
	
    /**
     * The dummy content this fragment is presenting.
     */
    private TodoGroup mItem;
    private List<TodoGroup> mItems;
    
    /**
     * The selected items in the list
     */
    List<TodoItem> selectedItems;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TodoItemListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        
        if (arguments.containsKey(ARG_ITEM_ID)) {
        	TodoGroupProvider provider = (TodoGroupProvider) getActivity();
        	
        	mItems = provider.getTodoGroups();
        	mItem = provider.getTodoGroupByName(arguments.getString(ARG_ITEM_ID));
        	
        	if (null == mItem) {
        		throw new IllegalStateException("Error: The given item id was not found in the collection");
        	}
        	
        }
        
        if (null != mItem) {
        	List<TodoItem> todoItems = mItem.getItems();
        	
        	setListAdapter(
        		new TodoItemListAdapter<TodoItem>(
    				getActivity(), 
    				todoItems));
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
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        
        if (!(activity instanceof TodoGroupProvider)) {
        	throw new IllegalStateException("Activity must implement TodoGroupProvider");
        }
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

    private void setActivatedPosition(int position) {
        if (position == ListView.INVALID_POSITION) {
            getListView().setItemChecked(mActivatedPosition, false);
        } else {
            getListView().setItemChecked(position, true);
        }

        mActivatedPosition = position;
    }
    
    /**
     * This method is called when the user clicks on a button in one of the menus.
     * Typically, the user has selected a set of todo items, and wants to apply an action to them. 
     */
	@Override
	public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
		switch (menuItem.getItemId()) {
			case R.id.add_to:
				System.out.println("Add item to clicked");
				break;
				
			case R.id.delete_todoitem:
				System.out.println("Delete todo item clicked");
				List<TodoItem> items = mItem.getItems();

				if (null != selectedItems) {
					for (TodoItem item: selectedItems) {
						System.out.println(String.format("Deleting item %s", item.getText()));
						if (!items.remove(item)) {
							throw new IllegalStateException("Could not find item to delete in the collection");
						}
					}

					selectedItems = null;
				}
				
				refreshListView(getListView(), actionMode);
				break;
				
			case R.id.email_todoitems:
				System.out.println("Email todogroup button pressed");
				
				if (null != selectedItems) {
					TodoItemMailer mailer = new TodoItemMailer(getActivity());
					mailer.addTodoItems(mItem.getGroupName(), selectedItems);
					mailer.send();
					selectedItems = null;
				}

				refreshListView(getListView(), actionMode);
				break;
				
			case Menu.NONE:
				String addToGroupName = menuItem.getTitle().toString();
				System.out.println(String.format("Move to menu item clicked: %s", addToGroupName));
				
				TodoGroup addToGroup = ((TodoGroupProvider) getActivity()).getTodoGroupByName(addToGroupName);
				
				if (null == addToGroup) {
					throw new IllegalStateException("Selected group was not found in the group list");
				}

				ListView view = getListView();

				if (null != selectedItems) {
					for (TodoItem item: selectedItems) {
						System.out.println(String.format("Moving item %s", item.getText()));
						mItem.moveItemToGroup(item, addToGroup);
					}
					
					selectedItems = null;
				}

				refreshListView(view, actionMode);
				break;
				
			default:
				System.out.println("Action item clicked");	
		}
		
		return true;
	}
	
	public void notifyDataSetChanged() {
		ListView listView = getListView();
		
		if (null == listView) {
			throw new IllegalStateException("No list view found in Todo Item List Fragment.");
		}
		
		listView.invalidate();
		ListAdapter adapter = listView.getAdapter();
		
		if (adapter instanceof BaseAdapter) {
			((BaseAdapter)adapter).notifyDataSetChanged();
		}
	}
	
	private void refreshListView(ListView view, ActionMode mode) {
		if (null != view) {
			view.removeAllViewsInLayout();
			view.invalidate();
		}

		if (null != mode) {
			mode.finish();
		}
	}

	@Override
	public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
		MenuInflater inflater = actionMode.getMenuInflater();
		inflater.inflate(R.menu.menu_todoitem_context, menu);
		
		actionMode.setTitle(R.string.action_mode_select_todoitems);
		
		return true;
	}

	@Override
	public void onDestroyActionMode(ActionMode actionMode) {
		System.out.println("Action mode destroyed");
		
		if (selectedItems != null) {
			selectedItems = null;
		}
	}

	@Override
	public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
		// Add the menu items for each todo group, except the current one
		MenuItem addToMenu = menu.findItem(R.id.add_to);

		if (null == addToMenu) {
			return false;
		}
		
		if (!addToMenu.hasSubMenu()) {
			return false;
		}

		Menu moveToMenu = addToMenu.getSubMenu();

		int i = 0;
		for (TodoGroup group: mItems) {
			if (mItem.getGroupName() == group.getGroupName()) {
				continue;
			}
			
			moveToMenu.add(Menu.NONE, Menu.NONE, i, group.getGroupName());
			++i;
		}

		return true;
	}
	
	@Override
	public void onItemCheckedStateChanged(ActionMode actionMode, int position, long id, boolean checked) {
		System.out.println(String.format("Item %d is now %schecked", position, checked ? "": "un"));
		
		TodoItem item = (TodoItem) getListView().getItemAtPosition(position);
		
		if (null == item) {
			return;
		}
		
		if (null == selectedItems) {
			 selectedItems = new ArrayList<TodoItem>();
		}
		
		if (checked) {
			if (!selectedItems.contains(item)) {
				selectedItems.add(item);
			}
		} else {
			selectedItems.remove(item);
		}
	}
}
