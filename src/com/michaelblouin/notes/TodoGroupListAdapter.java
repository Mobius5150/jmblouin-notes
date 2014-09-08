package com.michaelblouin.notes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.michaelblouin.todo.TodoGroup;

public class TodoGroupListAdapter<T extends TodoGroup> extends ArrayAdapter<T> {
	final static int resource = R.layout.todo_group_list_item;

	public TodoGroupListAdapter(Context context, T[] objects) {
		super(context, resource, objects);
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		TodoGroup group = this.getItem(position);
		View view = convertView;
		
		if (null == view) {
			LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(resource, parent, false);
		}
	    
	    TextView titletext = (TextView) view.findViewById(R.id.titletext);
	    TextView subtext = (TextView) view.findViewById(R.id.subtext);
	    
	    titletext.setText(group.getGroupName());
	    subtext.setText(String.format("%d items, %d checked", group.getItems().size(), 0));
	    
	    return view;
	}
}
