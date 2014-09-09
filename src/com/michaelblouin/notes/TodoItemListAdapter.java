package com.michaelblouin.notes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import com.michaelblouin.todo.TodoItem;

public class TodoItemListAdapter<T extends TodoItem> extends ArrayAdapter<T> {
	final static int resource = R.layout.todo_item_list_item;

	public TodoItemListAdapter(Context context, T[] objects) {
		super(context, resource, objects);
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		TodoItem item = this.getItem(position);
		View view = convertView;
		
		if (null == view) {
			LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(resource, parent, false);
		}
	    
		CheckBox checkbox = (CheckBox) view.findViewById(R.id.todoCheckBox);
		checkbox.setText(item.getText());
		checkbox.setChecked(item.isChecked());
		checkbox.setId(item.getId());
		checkbox.setTag(item);
		
	    return view;
	}
}
