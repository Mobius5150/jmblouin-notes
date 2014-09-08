package com.michaelblouin.notes;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.michaelblouin.todo.TodoGroup;

/**
 * A fragment representing a single TodoItem detail screen.
 * This fragment is either contained in a {@link TodoItemListActivity}
 * in two-pane mode (on tablets) or a {@link TodoItemDetailActivity}
 * on handsets.
 */
public class TodoItemDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
	public static final String ARG_ITEM_GROUP = "com.michaelblouin.notes.item_group";
	public static final String ARG_ITEM_ID = "com.michaelblouin.notes.item_id";

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
        
        if (arguments.containsKey(ARG_ITEM_GROUP)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = (TodoGroup) arguments.getSerializable(ARG_ITEM_GROUP);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_todoitem_detail, container, false);
        
        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.todoitem_detail)).setText("");
        }

        return rootView;
    }
}
