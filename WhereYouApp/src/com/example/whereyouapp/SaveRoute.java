package com.example.whereyouapp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
		
		FileOutputStream saveFileStream = new FileOutputStream("/tmp/saveFile", false);
		ObjectOutputStream objOut = new ObjectOutputStream(saveFileStream);
		objOut.writeObject(route);
		
		objOut.close();
		saveFileStream.close();
	}
}
