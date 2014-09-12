package com.michaelblouin.notes;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.michaelblouin.todo.TodoGroup;
import com.michaelblouin.todo.TodoItem;

public class TodoItemMailer {
	public static void sendTodoItems(Activity sendFromActivity, TodoGroup group) {
		if (null == group) {
			throw new NullPointerException("Group cannot be null for mail intent");
		}

		sendTodoItems(sendFromActivity, group.getItems());
	}

	public static void sendTodoItems(Activity sendFromActivity, List<TodoItem> items) {
		if (null == items) {
			throw new NullPointerException("Items cannot be null for mail intent");
		}

		String itemString = new String();

		for (TodoItem item: items) {
			itemString += item.toString() + "\n";
		}

		Intent intent = new Intent(Intent.ACTION_SEND, Uri.fromParts("mailto","mike@michaelblouin.ca", null));
		
		intent.setType("message/rfc822");
		intent.putExtra(Intent.EXTRA_SUBJECT, sendFromActivity.getString(R.string.email_subject_todoitem));
		intent.putExtra(Intent.EXTRA_TEXT, itemString);

		sendFromActivity.startActivity(
			Intent.createChooser(
				intent, 
				sendFromActivity.getString(R.string.prompt_email_send_todo_items)));
	}
}
