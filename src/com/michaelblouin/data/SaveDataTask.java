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
