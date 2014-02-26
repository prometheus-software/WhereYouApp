package com.example.whereyouapp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SaveRoute {
	public static void saveRoute(Route route) throws IOException
	{
		File saveFile = new File("/tmp/saveFile");
		
		//Check if it exists, if not, create new file
		if(!saveFile.exists())
		{
			saveFile.createNewFile();
		}
		
		FileOutputStream saveFileStream = new FileOutputStream(saveFile, false);
		ObjectOutputStream objOut = new ObjectOutputStream(saveFileStream);
		objOut.writeObject(route);
		
		objOut.close();
		saveFileStream.close();
	}
	
	public void restoreRoutes() throws IOException
	{
		FileInputStream restoreRoutes = new FileInputStream("/tmp/saveFile");
		ObjectInputStream objIn = new ObjectInputStream(restoreRoutes);
		
		try
		{
			Route route = (Route) objIn.readObject();
		}
		catch(Exception e)
		{
			//Means we are done reading objects, redirect to a new screen
			
		}
		
		restoreRoutes.close();
		objIn.close();
	}
}
