package com.appfactory.GitProcessing;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;

import org.eclipse.egit.github.core.Authorization;
import org.eclipse.egit.github.core.Gist;
import org.eclipse.egit.github.core.service.GistService;
import org.eclipse.egit.github.core.service.OAuthService;
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

import com.appfactory.exceptions.MyException;
import com.appfactory.ioprocessing.MultiIOprocessing;
import com.appfactory.resources.Messages;
import com.appfactory.service.AccessData;
import com.appfactory.utils.ExceptionUtils;

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
		File clonedir;
		String giturl = Messages.getString("private_git_url");
		String username = Messages.getString("private_git_user");
		String password = Messages.getString("private_git_pass");
		whichdirectory = path.toString();
		ArrayList<String> arr = new ArrayList<String>();

		if (!(accessdata.getuIModelJson().getAppurl() == null)) {
			whatname = accessdata.getuIModelJson().getServicename();
			dir = mio.createFolder(whichdirectory, whatname);
			clonedir = mio.createFolder(whichdirectory, "GitHubClone");

			CredentialsProvider cp = new UsernamePasswordCredentialsProvider(username, password);
			String result = gito.cloneIt(giturl, cp, clonedir,
					accessdata.getuIModelJson().getAppurl());
			String source_str = clonedir + "/Templates/"+accessdata.getuIModelJson().getAppurl();
			File f_source = new File(source_str);
			mio.copyFiles(f_source, path);

			if (result.equalsIgnoreCase("error")) {
				arr.add("error");
			} else {
				arr.add(dir.toString());

			}
			mio.removeDirectory(clonedir);
		} 
		return arr;

	}	
	private String cloneIt(String url, CredentialsProvider cp, File path, final String starterapp) {
		if (!path.exists()) {
			try {
				Git.cloneRepository()
						.setProgressMonitor((ProgressMonitor) new TextProgressMonitor(new PrintWriter(System.out)))
						.setCredentialsProvider(cp).setDirectory(path).setURI(url).call().close();
			} catch (InvalidRemoteException e) {
				e.printStackTrace();
			} catch (TransportException e) {
				e.printStackTrace();
			} catch (GitAPIException e) {
				e.printStackTrace();
			}
		} 

		return "Cloning Completed";
	}

	public String push(String appname, String name, String password, String url, File dir, File copydir,
			String commituser) {
		// credentials
		CredentialsProvider cp = new UsernamePasswordCredentialsProvider(name, password);
		MultiIOprocessing miop = new MultiIOprocessing();

		// clone
		CloneCommand cc = new CloneCommand().setCredentialsProvider(cp).setDirectory(dir).setURI(url);
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

		// get all names
		File newdir = miop.createFolder(dir.getAbsolutePath(), appname);
		miop.copyFiles(copydir, newdir);
		AddCommand addc = git.add();
		addc.addFilepattern(".");
		try {
			addc.call();
		} catch (NoFilepatternException e) {
			e.printStackTrace();
		} catch (GitAPIException e) {
			e.printStackTrace();
		}

		// commit
		CommitCommand commit = git.commit();
		commit.setCommitter("TMall", commituser).setMessage("We have uploaded your required codebase");
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
		pc.setCredentialsProvider(cp).setForce(true).setPushAll();
		try {
			Iterator<PushResult> it = pc.call().iterator();
			if (it.hasNext()) {
				
			}
		} catch (GitAPIException e) {
			e.printStackTrace();
		}
		return "success";
	}
	
	/**
	 * This method will take gist id as input param.
	 * And will upload the folder to gist to get the gist url
	 */
	public GistService getGistInstance() throws IOException {
		OAuthService oauthService = new OAuthService();
		oauthService.getClient().setCredentials(Messages.getString("DeploymentStsus.gist.user"),
				Messages.getString("DeploymentStsus.gist.pwd"));
		Authorization auth = new Authorization();
		auth.setNote(new Timestamp(new Date().getTime()).toString());
		auth.setScopes(Arrays.asList("gist"));
		auth = oauthService.createAuthorization(auth);
		GistService gistService = new GistService();
		gistService.getClient().setOAuth2Token(auth.getToken());
		return gistService;
	}
		public void uploadTempapp(String gistid,File sourceDir,File destinationDir) {
			try {
				MultiIOprocessing miop = new MultiIOprocessing();
				GistService gistService =  getGistInstance();
				Gist gist = gistService.getGist(gistid);
				String pushUrl=gist.getGitPushUrl();
				String userName=Messages.getString("DeploymentStsus.gist.user");
				String passWord=Messages.getString("DeploymentStsus.gist.pwd");


				UsernamePasswordCredentialsProvider usernamePassword = new UsernamePasswordCredentialsProvider(userName,
						passWord);

				CloneCommand cloneCommand = new CloneCommand().setCredentialsProvider(usernamePassword)
						.setDirectory(destinationDir).setURI(pushUrl);
				Git git = null;
				git = cloneCommand.call();
				miop.copyFiles(sourceDir, destinationDir);

				AddCommand addCommand = git.add();
				addCommand.addFilepattern(".");
				addCommand.call();

				CommitCommand commit = git.commit();
				commit.setCommitter(userName, userName).setMessage("We have uploaded your required codebase");
				commit.call();

				PushCommand pushCommand = git.push();
				pushCommand.setCredentialsProvider(usernamePassword).setForce(true).setPushAll();
				pushCommand.call();
				git.close();
				destinationDir.delete();
			} catch (IOException | GitAPIException e) {
				e.printStackTrace();
			}
		}
}
