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
import android.widget.TextView;
import com.michaelblouin.todo.TodoGroup;

public class TodoGroupListAdapter<T extends TodoGroup> extends ArrayAdapter<T> {
	final static int resource = R.layout.todo_group_list_item;

	public TodoGroupListAdapter(Context context, List<T> objects) {
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
	    subtext.setText(String.format("%d items, %d checked", group.getItemCount(), group.getCheckedItemCount()));
	    
	    return view;
	}
}
