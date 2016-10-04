package com.appManager.ioprocessing;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;



public class MultiZipProcessing {
	  List<String> fileList;
	    private static  String outputzip;
	    private static  String sourcefile;
	    public MultiZipProcessing(){
			fileList = new ArrayList<String>();
		    }
	public String zipAction(String source,String destination,String appname,String whatAction){
		switch (whatAction) {
		case "zipit":
			zipAll(source,destination);
			break;
		case "unzipit":
			try {
				unZipAll(source,destination);
			} catch (IOException e) {
				e.printStackTrace(); 
			}
			break;	
		default:
			return "failed";
		}
		return "success";
	}
	
	 private void zipAll( String source,String dest )
	    {
	    	MultiZipProcessing testZip = new MultiZipProcessing();
	    	outputzip = dest;
	    	sourcefile =source;
	    	testZip.generateFileList(new File(sourcefile));
	    	testZip.zipIt(outputzip);
	    }
	    
	    /**
	     * Zip it
	     * @param zipFile output ZIP file location
	     */
	    private void zipIt(String zipFile){

	     byte[] buffer = new byte[1024];
	    	
	     try{
	    		
	    	FileOutputStream fos = new FileOutputStream(zipFile);
	    	ZipOutputStream zos = new ZipOutputStream(fos);
	    		
	    	/*System.out.println("Output to Zip : " + zipFile);*/
	    		
	    	for(String file : this.fileList){
	    			
	    		/*System.out.println("File Added : " + file);*/
	    		ZipEntry ze= new ZipEntry(file);
	        	zos.putNextEntry(ze);
	               
	        	FileInputStream in = 
	                       new FileInputStream(sourcefile + File.separator + file);
	       	   
	        	int len;
	        	while ((len = in.read(buffer)) > 0) {
	        		zos.write(buffer, 0, len);
	        	}
	               
	        	in.close();
	    	}
	    		
	    	zos.closeEntry();
	    	//remember close it
	    	zos.close();
	          
	    }catch(IOException ex){
	       ex.printStackTrace();   
	    }
	   }
	    
	    /**
	     * Traverse a directory and get all files,
	     * and add the file into fileList  
	     * @param node file or directory
	     */
	    public void generateFileList(File node){

	    	//add file only
		if(node.isFile()){
			fileList.add(generateZipEntry(node.getAbsoluteFile().toString()));
		}
			
		if(node.isDirectory()){
			String[] subNote = node.list();
			for(String filename : subNote){
				generateFileList(new File(node, filename));
			}
		}
	 
	    }

	    /**
	     * Format the file path for zip
	     * @param file file path
	     * @return Formatted file path
	     */
	    private String generateZipEntry(String file){
	    	return file.substring(sourcefile.length()+1, file.length());
	    }
@SuppressWarnings("rawtypes")
	private static void unZipAll(String sources, String destinations) throws IOException 
   {
       int buffer = 2048;
       File source = new File(sources);
       File destination = new File(destinations);
       ZipFile zip = new ZipFile(source);
       try{
           destination.getParentFile().mkdirs();
           Enumeration zipFileEntries = zip.entries();

           while (zipFileEntries.hasMoreElements())
           {
               ZipEntry entry = (ZipEntry) zipFileEntries.nextElement();
               String currentEntry = entry.getName();
               File destFile = new File(destination, currentEntry);
               File destinationParent = destFile.getParentFile();

               destinationParent.mkdirs();

               if (!entry.isDirectory())
               {
                   BufferedInputStream is = null;
                   FileOutputStream fos = null;
                   BufferedOutputStream dest = null;
                   try{
                       is = new BufferedInputStream(zip.getInputStream(entry));
                       int currentByte;
                       byte data[] = new byte[buffer];
                       fos = new FileOutputStream(destFile);
                       dest = new BufferedOutputStream(fos, buffer);
                       while ((currentByte = is.read(data, 0, buffer)) != -1) {
                           dest.write(data, 0, currentByte);
                       }
                   } catch (Exception e){
                       System.out.println("unable to extract entry:" + entry.getName());
                       throw e;
                   } finally{
                       if (dest != null){
                           dest.close();
                       }
                       if (fos != null){
                           fos.close();
                       }
                       if (is != null){
                           is.close();
                       }
                   }
               }else{
                   //Create directory
                   destFile.mkdirs();
               }

               if (currentEntry.endsWith(".zip"))
               {
                  /* unZipAll(destFile, destinationParent);*/
                   if(!destFile.delete()){
                       System.out.println("Could not delete zip");
                   }
               }
           }
       } catch(Exception e){
           e.printStackTrace();
       } finally {
           zip.close();
       }
   }
}
