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
