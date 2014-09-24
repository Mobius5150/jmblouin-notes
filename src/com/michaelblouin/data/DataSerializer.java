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

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import android.app.Application;

public class DataSerializer {
	private Application application;
	
	public DataSerializer(Application application) {
		this.application = application;
	}
	
	public Boolean storeDataItem(ISerializableData object) {
		return storeDataItem(object.getIdentifierString(), object);
	}
	
	public Boolean storeDataItem(String type, String id, Serializable object) {
		return storeDataItem(type + id, object);
	}
	
	private Boolean storeDataItem(String filename, Serializable object) {
		try {
			FileOutputStream fileOutputStream = application.openFileOutput(filename, Application.MODE_PRIVATE);
			
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
			objectOutputStream.writeObject(object);
			objectOutputStream.close();
			
			return true;
		} catch (IOException exception) {
			System.out.println(
				String.format("Exception occured saving item %s. Item not saved. Exception %s", 
				filename, 
				exception.getLocalizedMessage()));
		}
		
		return false;
	}
	
	public Serializable getDataItem(String type, String id) {
		String filename = type + id;
		
		try {
			FileInputStream fileInputStream = application.openFileInput(filename);
			
			ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
			Serializable readObject = (Serializable) objectInputStream.readObject();
			objectInputStream.close();
			
			return readObject;
		} catch (Exception exception) {
			System.out.println(
				String.format("Exception occured saving item %s. Item not saved. Exception %s", 
				filename, 
				exception.getLocalizedMessage()));
		}
		
		return null;
	}
}
