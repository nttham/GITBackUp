package com.micro.main;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import org.eclipse.jgit.api.AddCommand;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.CommitCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.errors.ConcurrentRefUpdateException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.NoFilepatternException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.api.errors.NoMessageException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.api.errors.WrongRepositoryStateException;
import org.eclipse.jgit.errors.UnmergedPathException;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.PushResult;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import com.micro.common.UnZip;


public class GitPush {
	@SuppressWarnings("static-access")
	public static void push(String name,String password,String url,String Appname,String commit_user ) throws InvalidRemoteException, TransportException, GitAPIException, UnmergedPathException{
        File dir = new File(System.getProperty("user.dir")+"/StarterCode");

		
        // credentials
        CredentialsProvider cp = new UsernamePasswordCredentialsProvider(name, password);
        // clone
        CloneCommand cc = new CloneCommand()
                .setCredentialsProvider(cp)
                .setDirectory(dir)
                .setURI(url);
        Git git = cc.call();
        //UnZip
        UnZip unZip = new UnZip();
    	File source = new File(System.getProperty("user.dir")+"/"+Appname+".zip");
    	try {
			unZip.unZipAll(source, dir);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	//delete .git folder
    	
        //get all names 
    	 AddCommand ac = git.add(); 
 	    //ac.addFilepattern("D:/Project Setup/TestRepo/Learn/app");	
 	    ac.addFilepattern(".");	
 	   System.out.println("Files are added and ready for commit");
 	    try {
            ac.call();
         } catch (NoFilepatternException e) {
             e.printStackTrace();
         }

         // commit
         CommitCommand commit = git.commit();
         commit.setCommitter("TMall", commit_user)
                 .setMessage("We have uploaded your required codebase");
         try {
             commit.call();
         } catch (NoHeadException e) {
             e.printStackTrace();
         } catch (NoMessageException e) {
             e.printStackTrace();
         } catch (ConcurrentRefUpdateException e) {
             e.printStackTrace();
         } catch (WrongRepositoryStateException e) {
             e.printStackTrace();
         }
         // push
        
         PushCommand pc = git.push();
         pc.setCredentialsProvider(cp)
                 .setForce(true)
                 .setPushAll();
         try {
             Iterator<PushResult> it = pc.call().iterator();
             if(it.hasNext()){
                 System.out.println("Application is pushed to the provided users repository");
             }
         } catch (InvalidRemoteException e) {
             e.printStackTrace();
         }
        dir.deleteOnExit();
    }
}
