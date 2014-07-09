package com.kelltontech.framework.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.util.Log;

/**
 * @author sachin.gupta
 */
public class StorageUtils {
	
	/**
	 * @param pFilePath
	 * @param pFileData
	 * @return
	 */
	public static boolean createFile( String pDirectoryPath, String pFileName, byte[] pFileData ) {
		try {
			File directory = new File(pDirectoryPath);
			if( ! directory.exists() || ! directory.isDirectory() ) {
				directory.mkdirs();
			}
			File file = new File(directory, pFileName);
			OutputStream os= new FileOutputStream(file);
			os.write(pFileData);
			os.close();
			return true;
		} catch (IOException e) {
			Log.e("StorageUtils", "createFile: " + pDirectoryPath + "/" + pFileName, e);
			return false;
		}
	}
}

