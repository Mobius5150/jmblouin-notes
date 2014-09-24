/*
Copyright 2014 Michael Blouin

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

	http://www.apache.org/licenses/LICENSE-2.0
	
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package com.michaelblouin.notes;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.michaelblouin.todo.TodoItem;

public class TodoItemListAdapter<T extends TodoItem> extends ArrayAdapter<T> {
	final static int resource = R.layout.todo_item_list_item;

	public TodoItemListAdapter(Context context, List<T> objects) {
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
		if (null != checkbox) {
			checkbox.setChecked(item.isChecked());
			checkbox.setTag(item);
		}
		
		view.setTag(String.format("ItemId-%d", item.getId()));
		
		TextView textview = (TextView) view.findViewById(R.id.titletext);
		if (null != textview) {
			textview.setText(item.getText());
		} else if (null != checkbox) {
			checkbox.setText(item.getText());
		}
		
	    return view;
	}
}
