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
