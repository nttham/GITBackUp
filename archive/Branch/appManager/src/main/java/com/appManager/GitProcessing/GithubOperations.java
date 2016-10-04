package com.appManager.GitProcessing;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.jgit.api.AddCommand;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.CommitCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.errors.AbortedByHookException;
import org.eclipse.jgit.api.errors.ConcurrentRefUpdateException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.NoFilepatternException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.api.errors.NoMessageException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.api.errors.UnmergedPathsException;
import org.eclipse.jgit.api.errors.WrongRepositoryStateException;
import org.eclipse.jgit.lib.ProgressMonitor;
import org.eclipse.jgit.lib.TextProgressMonitor;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.PushResult;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNUpdateClient;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

import com.appManager.exceptions.CustomErrorMessage;
import com.appManager.exceptions.MyException;
import com.appManager.ioprocessing.MultiIOprocessing;
import com.appManager.resources.Messages;
import com.appManager.service.AccessData;
import com.appManager.utils.ExceptionUtils;
@Component
public class GithubOperations {
	MultiIOprocessing miop = new MultiIOprocessing();

	@Autowired
	private ExceptionUtils eUtils;
	public ArrayList<String> cloneGit(File path, AccessData accessdata) throws MyException {
		GithubOperations gito = new GithubOperations();
		MultiIOprocessing mio = new MultiIOprocessing();
		String whichdirectory;
		String whatname;
		File dir; 
		/*File parentpath = new File(parentdirectory);*/
		String giturl = Messages.getString("private_git_url");
		String gitnewurl;
		String username=Messages.getString("private_git_user"); 
		String password=Messages.getString("private_git_pass");
		whichdirectory = path.toString();
		ArrayList<String> arr = new ArrayList<String>();
		
		if (!(accessdata.getBlueprint().getPrimaryservice().getAppurl() == null)) {
			whatname = accessdata.getBlueprint().getServicename();
			dir = mio.createFolder(whichdirectory, whatname);
			
			@SuppressWarnings("unused")
			CredentialsProvider cp = new UsernamePasswordCredentialsProvider(
					username,
					password);
			 gitnewurl = giturl+accessdata.getBlueprint().getPrimaryservice().getAppurl();
			/*gito.cloneIt(giturl+accessdata.getBlueprint().getPrimaryservice().getAppurl(), cp, dir);*/
			String result=gito.downloadGitFolder(gitnewurl, path.getAbsolutePath(),username,password);
			if(result.equalsIgnoreCase("error")){
				arr.add("error");
			}else{
				arr.add(dir.toString());
			}
			/*if (!(accessdata.getBlueprint().getPrimaryservice().getPrehook()==null)) {
				
				download provider
				whatname = "prehook_" + accessdata.getBlueprint().getPrimaryservice().getPrehook().getProvider();
				dir = mio.createFolder(whichdirectory, whatname);
				arr.add(dir.toString());
				gito.cloneIt(accessdata.getBlueprint().getPrimaryservice().getPrehook().getProvider_url(), cp, dir);
				gitnewurl=giturl+accessdata.getBlueprint().getPrimaryservice().getPrehook().getProviderurl();
				gito.downloadGitFolder(gitnewurl, dir.getAbsolutePath(),username,password);
				download channel
				whatname = "prehook_" + accessdata.getBlueprint().getPrimaryservice().getPrehook().getChannel();
				dir = mio.createFolder(whichdirectory, whatname);
				arr.add(dir.toString());
				gito.cloneIt(accessdata.getBlueprint().getPrimaryservice().getPrehook().getHook_channel_url(), cp, dir);
				gitnewurl=giturl+accessdata.getBlueprint().getPrimaryservice().getPrehook().getHookchannelurl();
				gito.downloadGitFolder(gitnewurl, dir.getAbsolutePath(),username,password);

			}
			if (!(accessdata.getBlueprint().getPrimaryservice().getOnhook()==null)) {
				
				download provider
				whatname = "onhook_" + accessdata.getBlueprint().getPrimaryservice().getOnhook().getProvider();
				dir = mio.createFolder(whichdirectory, whatname);
				arr.add(dir.toString());
				gito.cloneIt(accessdata.getBlueprint().getPrimaryservice().getOnhook().getProvider_url(), cp, dir);
				gitnewurl=giturl+accessdata.getBlueprint().getPrimaryservice().getOnhook().getProviderurl();
				gito.downloadGitFolder(gitnewurl, dir.getAbsolutePath(),username,password);
				
				download channel
				whatname = "prehook_" + accessdata.getBlueprint().getPrimaryservice().getOnhook().getChannel();
				dir = mio.createFolder(whichdirectory, whatname);
				arr.add(dir.toString());
				gito.cloneIt(accessdata.getBlueprint().getPrimaryservice().getOnhook().getHook_channel_url(), cp, dir);
				gitnewurl=giturl+accessdata.getBlueprint().getPrimaryservice().getOnhook().getHookchannelurl();
				gito.downloadGitFolder(gitnewurl, dir.getAbsolutePath(),username,password);
			}
			if (!(accessdata.getBlueprint().getPrimaryservice().getPosthook()==null)) {
				download provider
				whatname = "posthook_" + accessdata.getBlueprint().getPrimaryservice().getPosthook().getProvider();
				dir = mio.createFolder(whichdirectory, whatname);
				arr.add(dir.toString());
				gito.cloneIt(accessdata.getBlueprint().getPrimaryservice().getPosthook().getProvider_url(), cp,
						dir);
				gitnewurl=giturl+accessdata.getBlueprint().getPrimaryservice().getPosthook().getProviderurl();
				gito.downloadGitFolder(gitnewurl, dir.getAbsolutePath(),username,password);
				
				
				download channel
				whatname = "posthook_" + accessdata.getBlueprint().getPrimaryservice().getPosthook().getChannel();
				dir = mio.createFolder(whichdirectory, whatname);
				arr.add(dir.toString());
				gito.cloneIt(accessdata.getBlueprint().getPrimaryservice().getPosthook().getHook_channel_url(), cp, dir);
				gitnewurl=giturl+accessdata.getBlueprint().getPrimaryservice().getPosthook().getHookchannelurl();
				gito.downloadGitFolder(gitnewurl, dir.getAbsolutePath(),username,password);
			}*/
		} else {
			System.out.println("nothing is there in primary service");
		}
		return arr;

	}
@SuppressWarnings("deprecation")
private String downloadGitFolder(String svnurl,String destPath,String svnUserName,String svnPassword) throws MyException{
	try{

		SVNRepository repository = null;

		DAVRepositoryFactory.setup();

		//initiate the reporitory from the svnurl

		repository = SVNRepositoryFactory.create(SVNURL.parseURIEncoded(svnurl));

		//create authentication data

		ISVNAuthenticationManager authManager =

		SVNWCUtil.createDefaultAuthenticationManager(svnUserName, svnPassword);

		repository.setAuthenticationManager(authManager);

		//output some data to verify connection

		System.out.println( "Repository Root: " + repository.getRepositoryRoot( true ) );

		System.out.println(  "Repository UUID: " + repository.getRepositoryUUID( true ) );

		//need to identify latest revision

		long latestRevision = repository.getLatestRevision();

		System.out.println(  "Repository Latest Revision: " + latestRevision);

		//create client manager and set authentication

		SVNClientManager ourClientManager = SVNClientManager.newInstance();
				

		ourClientManager.setAuthenticationManager(authManager);

		//use SVNUpdateClient to do the export

		SVNUpdateClient updateClient = ourClientManager.getUpdateClient( );

		updateClient.setIgnoreExternals( false );

		updateClient.doExport( repository.getLocation(), new File(destPath),

		SVNRevision.create(latestRevision), SVNRevision.create(latestRevision),

		null, true, SVNDepth.INFINITY);

		/*System.out.println("Checkout file/folder successfully !");

		System.out.println(  "**************************************!" );*/

		} catch (SVNException e) {
			throw eUtils.myException(CustomErrorMessage.INVALID_GITHUB_URL, null);
		}

	
	return "Folder downloaded";
}
	@SuppressWarnings("unused")
	private String cloneIt(String url, CredentialsProvider cp, File path) {
		if (!path.exists()) {
			try {
				Git.cloneRepository()
						.setProgressMonitor((ProgressMonitor) new TextProgressMonitor(new PrintWriter(System.out)))
						//.setCredentialsProvider(cp)
						.setDirectory(path)
						.setURI(url)
						.call()
						.close();
			} catch (InvalidRemoteException e) {
				e.printStackTrace();
			} catch (TransportException e) {
				e.printStackTrace();
			} catch (GitAPIException e) {
				e.printStackTrace();
			}
		}else{
			System.out.println("Completed");
		}
		
		return "Cloning Completed";
	}
public String push(String appname,String name,String password,String url,File dir,File copydir,String commituser){
     // credentials
     CredentialsProvider cp = new UsernamePasswordCredentialsProvider(name, password);
 	MultiIOprocessing miop = new MultiIOprocessing();

     // clone
     CloneCommand cc = new CloneCommand()
             .setCredentialsProvider(cp)
             .setDirectory(dir)
             .setURI(url);
     Git git = null;
	try {
		git = cc.call();
	} catch (InvalidRemoteException e1) {
		e1.printStackTrace();
	} catch (TransportException e1) {
		e1.printStackTrace();
	} catch (GitAPIException e1) {
		e1.printStackTrace();
	}
 
     //get all names 
	File newdir = miop.createFolder(dir.getAbsolutePath(), appname);
	miop.copyFiles(copydir, newdir);
 	 AddCommand addc = git.add(); 
 	addc.addFilepattern(".");	
	   System.out.println("Files are added and ready for commit");
	    try {
	    	addc.call();
      } catch (NoFilepatternException e) {
          e.printStackTrace();
      } catch (GitAPIException e) {
		e.printStackTrace();
	}

      // commit
      CommitCommand commit = git.commit();
      commit.setCommitter("TMall", commituser)
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
 catch (UnmergedPathsException e) {
		e.printStackTrace();
	} catch (AbortedByHookException e) {
		e.printStackTrace();
	} catch (GitAPIException e) {
		e.printStackTrace();
	}
     
      PushCommand pc = git.push();
      pc.setCredentialsProvider(cp)
              .setForce(true)
              .setPushAll();
      try {
          Iterator<PushResult> it = pc.call().iterator();
          if(it.hasNext()){
              System.out.println("Application is pushed to the provided users repository");
          }
      } catch (GitAPIException e) {
          e.printStackTrace();
      }
     return "success";
 }
}
