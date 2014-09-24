package com.michaelblouin.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Pair;

public class LoadDataTask extends AsyncTask<List<Pair<String, String>>, Void, Void> implements IDataTask {
	private Application application;
	private List<Serializable> items;
	private IDataTaskCompletionHandler completionHandler = null;
	
	public LoadDataTask(Application application) {
		this.application = application;
	}
	
	public void setDataTaskCompletionHandler(IDataTaskCompletionHandler handler) {
		completionHandler = handler;
	}
	
	@Override
	public Object getResult() {
		return items;
	}
	
	@Override
	protected Void doInBackground(List<Pair<String, String>> ... getItems) {
		DataSerializer dataSerializer = new DataSerializer(application);
		items = new ArrayList<Serializable>();
		
		int argCount = getItems.length;
		for (int i = 0; i < argCount; ++i) {
			for (Pair<String, String> pair: getItems[i]) {
				Serializable item = dataSerializer.getDataItem(pair.first, pair.second);
				
				if (null != item) {
					items.add(item);
				}
			}
		}
		
		if (0 == items.size()) {
			items = null;
		}
		
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		if (null != completionHandler) {
			completionHandler.taskCompleted(this);
		}
	}

}
