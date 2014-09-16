package com.michaelblouin.notes;

import android.R.string;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;

public class NewTodoItemPrompt {
	private Context context;
	private EditText textEditor;
	private Builder alertBuilder;
	private Builder confirmationAlertBuilder;
	private TodoItemPromptCompletionListener completionListener;
	private DialogInterface.OnClickListener clickListener;
	private Boolean allowEmptyValuesWithoutPrompt = false;

	public NewTodoItemPrompt(Context context) {
		this.context = context;
		
		alertBuilder = new Builder(context);
		textEditor = new EditText(context);
		
		clickListener = new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				if (null != completionListener) {
					String value = textEditor.getText().toString();
					Boolean positiveActionTaken = DialogInterface.BUTTON_POSITIVE == whichButton;
					
					if (positiveActionTaken 
							&& !allowEmptyValuesWithoutPrompt 
							&& (null == value || value.isEmpty()) 
							&& null == confirmationAlertBuilder) {
						showEmptyValueConfirmationPrompt();
					} else {
						completionListener.onComplete(
							positiveActionTaken, 
							value);
					}
				}
			}
		};
		
		alertBuilder.setPositiveButton(string.ok, clickListener);
		alertBuilder.setNegativeButton(R.string.cancel_action, clickListener);
		alertBuilder.setTitle(R.string.new_todoitem);
		alertBuilder.setView(textEditor);
	}
	
	public void showEmptyValueConfirmationPrompt() {
		confirmationAlertBuilder = new Builder(context);
		
		confirmationAlertBuilder.setPositiveButton(string.ok, clickListener);
		confirmationAlertBuilder.setNegativeButton(R.string.cancel_action, clickListener);
		confirmationAlertBuilder.setTitle(R.string.new_todoitem);
		confirmationAlertBuilder.setMessage(R.string.confirm_empty_todo_item_prompt);
		confirmationAlertBuilder.show();
	}
	
	public void setAllowEmptyValuesWithoutPrompt(Boolean allow) {
		allowEmptyValuesWithoutPrompt = allow;
	}
	
	public void setOnCompletionListener(TodoItemPromptCompletionListener listener) {
		this.completionListener = listener;  
	}
	
	public void show() {
		alertBuilder.show();
	}

	public static abstract class TodoItemPromptCompletionListener {
		public TodoItemPromptCompletionListener() {};
		public abstract void onComplete(Boolean accepted, String value);
	}
}
