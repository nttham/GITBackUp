package com.micro.common;

import java.io.File;

public class DeleteDirectory {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String dirPath = "D:/Project Setup/Micro/repo/.git";
		File dir = new File(dirPath);
		DeleteDirectory.removeDirectory(dir);
		//will delete a  emptey directory
		try {
		    boolean deleted = dir.delete();
		    if (deleted) {
		        System.out.println(Colors.getAnsiGreen()+"Directory removed."+Colors.getAnsiReset());
		    } else {
		        System.out.println(Colors.getAnsiGreen()+"Directory could not be removed"+Colors.getAnsiReset());
		    }
		} catch (SecurityException ex) {
			 System.out.println(Colors.getAnsiRed()+"Delete is denied."+Colors.getAnsiReset());
		}
	}
	//will delete a non emptey directory
	public static void removeDirectory(File dir) {
	    if (dir.isDirectory()) {
	        File[] files = dir.listFiles();
	        if (files != null && files.length > 0) {
	            for (File aFile : files) {
	                removeDirectory(aFile);
	            }
	        }
	        dir.delete();
	    } else {
	        dir.delete();
	    }
	}
	//will delete a non emptey directory but only subfolders
	public static void cleanDirectory(File dir) {
	    if (dir.isDirectory()) {
	        File[] files = dir.listFiles();
	        if (files != null && files.length > 0) {
	            for (File aFile : files) {
	                removeDirectory(aFile);
	            }
	        }
	    }
	}
}
