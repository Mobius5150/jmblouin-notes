package com.michaelblouin.data;

import android.app.Application;
import android.os.AsyncTask;

public class SaveDataTask extends AsyncTask<ISerializableData, Void, Void> implements IDataTask {
	private Application application;
	private Boolean result = true;
	private IDataTaskCompletionHandler completionHandler = null;
	
	public SaveDataTask(Application application) {
		this.application = application;
	}
	
	public void setDataTaskCompletionHandler(IDataTaskCompletionHandler handler) {
		completionHandler = handler;
	}
	
	@Override
	public Object getResult() {
		return result;
	}
	
	@Override
	protected Void doInBackground(ISerializableData... saveItems) {
		DataSerializer dataSerializer = new DataSerializer(application);
		
		for (int i = 0; i < saveItems.length; ++i) {
			result &= dataSerializer.storeDataItem(saveItems[i]);
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
