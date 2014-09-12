package com.michaelblouin.todo;

import java.util.List;

import android.app.Activity;
import android.content.Intent;

import com.michaelblouin.notes.R;
import com.michaelblouin.notes.R.string;

public class TodoItemMailer {
	private String subject;
	private String content;
	private Activity sendFromActivity;
	
	public TodoItemMailer(Activity sendFromActivity) {
		this.sendFromActivity = sendFromActivity;
		subject = sendFromActivity.getString(R.string.email_subject_todoitem);
	}
	
	public void addTodoGroup(TodoGroup group) {
		if (null == group) {
			throw new NullPointerException("Group cannot be null for mail intent");
		}

		addTodoItems(group.getGroupName(), group.getItems());
	}

	public void addTodoItems(String sectionHeader, List<? extends TodoItem> items) {
		if (null == items) {
			throw new NullPointerException("Items cannot be null for mail intent");
		}

		if (content == null) {
			content = new String();
		}
		
		content += sectionHeader + ":\n";

		for (Object item: items) {
			content += String.format("	%s\n", item.toString());
		}
		
		content += "\n";
	}
	
	public void send() {
		Intent intent = new Intent(Intent.ACTION_SEND);

		intent.setType("message/rfc822");
		intent.putExtra(Intent.EXTRA_SUBJECT, subject);
		intent.putExtra(Intent.EXTRA_TEXT, content);

		sendFromActivity.startActivity(
			Intent.createChooser(
				intent,
				sendFromActivity.getString(R.string.prompt_email_send_todo_items)));
	}
}
