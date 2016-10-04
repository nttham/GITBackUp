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
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.ProgressMonitor;
import org.eclipse.jgit.lib.TextProgressMonitor;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.PushResult;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.appfactory.constants.ApplicationConstants;
import com.appfactory.exceptions.MyException;
import com.appfactory.ioprocessing.MultiIOprocessing;
import com.appfactory.service.AccessData;
import com.appfactory.utils.ExceptionUtils;
import com.appfactory.utils.StatusUtils;

@Component
public class GithubOperations {

	@Autowired
	private ExceptionUtils eUtils;
	@Autowired
	private MultiIOprocessing mio;
	@Autowired
	private StatusUtils su;
	@Autowired
	private AccessData accessdata;
	@Autowired
	private MultiIOprocessing miop;
	private Git git;

	public ArrayList<String> cloneGit(final File path, AccessData accessdata) throws MyException {
		String whichdirectory;
		String whatname;
		File dir;
		final String giturl = ApplicationConstants.GIT_URL;
		final String username = ApplicationConstants.GIT_USERNAME;
		final String password = ApplicationConstants.GIT_PASSWORD;
		whichdirectory = path.toString();
		ArrayList<String> arr = new ArrayList<String>();

		if (!(accessdata.getuIModelJson().getAppurl() == null)) {
			whatname = accessdata.getuIModelJson().getServicename();
			dir = mio.createFolder(whichdirectory, whatname);
			CredentialsProvider cp = new UsernamePasswordCredentialsProvider(username, password);
			File GitClone = new File(accessdata.getGithub_clonepath() + ApplicationConstants.GITHUB_FOLDER);
			if (!GitClone.exists()) {
				GitClone.mkdirs();
				cloneIt(giturl, cp, GitClone);
			} else {
				gitPull(GitClone, username, password);
			}
			String source_str = GitClone + ApplicationConstants.TEMPLATE + accessdata.getuIModelJson().getAppurl();
			File f_source = new File(source_str);
			mio.copyFiles(f_source, path);
			arr.add(dir.toString());
		}
		return arr;

	}

	private void cloneIt(String url, CredentialsProvider cp, File path) throws MyException {
		try {
			Git.cloneRepository()
					.setProgressMonitor((ProgressMonitor) new TextProgressMonitor(new PrintWriter(System.out)))
					.setCredentialsProvider(cp).setDirectory(path).setURI(url).call().close();
		} catch (GitAPIException e) {
			su.updatestatus(accessdata.getMessageid(), accessdata.getuIModelJson().getGistid(),
					ApplicationConstants.GITHUB_FAILED, ApplicationConstants.CFURL_FAILED, 102,
					ApplicationConstants.LOG_LEVEL_ERROR);
			throw eUtils.myException(com.appfactory.exceptions.CustomErrorMessage.GIT_API_EXCEPTION_CLONE,
					e.getMessage());
		}
	}

	public void push( File dir, File copydir) throws MyException {
		// credentialssy
		try {
			CredentialsProvider cp = new UsernamePasswordCredentialsProvider(accessdata.getuIModelJson().getDevlopergitusername(), accessdata.getuIModelJson().getDevlopergitpassword());

			// clone
			CloneCommand cc = new CloneCommand().setCredentialsProvider(cp).setDirectory(dir).setURI(accessdata.getuIModelJson().getDevlopergiturl());
			Git git = null;
			git = cc.call();

			// get all names
			File newdir = miop.createFolder(dir.getAbsolutePath(), accessdata.getuIModelJson().getServicename());
			miop.copyFiles(copydir, newdir);
			AddCommand addc = git.add();
			addc.addFilepattern(".");

			addc.call();

			// commit
			CommitCommand commit = git.commit();
			commit.setCommitter("TMall", accessdata.getuIModelJson().getDevlopergitusername()).setMessage("We have uploaded your required codebase");

			commit.call();

			PushCommand pc = git.push();
			pc.setCredentialsProvider(cp).setForce(true).setPushAll();

			Iterator<PushResult> it = pc.call().iterator();
			if (it.hasNext()) {

			}
		} catch (GitAPIException e) {
			
			su.updatestatus(accessdata.getMessageid(), accessdata.getuIModelJson().getGistid(),
					ApplicationConstants.GITHUB_FAILED, ApplicationConstants.CFURL_FAILED, 102,
					ApplicationConstants.LOG_LEVEL_ERROR);
			throw eUtils.myException(com.appfactory.exceptions.CustomErrorMessage.GIT_API_EXCEPTION_CLONE_PUSH,
					e.getMessage());
		}
	}

	/**
	 * This method will take gist id as input param. And will upload the folder
	 * to gist to get the gist url
	 */
	public GistService getGistInstance() throws IOException {
		OAuthService oauthService = new OAuthService();
		oauthService.getClient().setCredentials(ApplicationConstants.GIST_USERNAME, ApplicationConstants.GIST_PASSWORD);
		Authorization auth = new Authorization();
		auth.setNote(new Timestamp(new Date().getTime()).toString());
		auth.setScopes(Arrays.asList(ApplicationConstants.GIST_SCOPE));
		auth = oauthService.createAuthorization(auth);
		GistService gistService = new GistService();
		gistService.getClient().setOAuth2Token(auth.getToken());
		return gistService;
	}

	/**
	 * This method is for uploading the zip file to gist
	 * 
	 * @param sourceDir
	 * @param destinationDir
	 * @throws MyException
	 */
	public void uploadTempapp(File sourceDir, File destinationDir) throws MyException {
		try {
			MultiIOprocessing miop = new MultiIOprocessing();
			GistService gistService = getGistInstance();
			Gist gist = gistService.getGist(accessdata.getuIModelJson().getGistid());
			String pushUrl = gist.getGitPushUrl();
			String userName = ApplicationConstants.GIST_USERNAME;
			String passWord = ApplicationConstants.GIST_PASSWORD;

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
			commit.setCommitter(userName, userName).setMessage(ApplicationConstants.GIT_MESSAGE);
			commit.call();

			PushCommand pushCommand = git.push();
			pushCommand.setCredentialsProvider(usernamePassword).setForce(true).setPushAll();
			pushCommand.call();
			git.close();
			destinationDir.delete();
		} catch (IOException | GitAPIException e) {
			su.updatestatus(accessdata.getMessageid(), accessdata.getuIModelJson().getGistid(),
					ApplicationConstants.GITHUB_FAILED, ApplicationConstants.CFURL_FAILED, 102,
					ApplicationConstants.LOG_LEVEL_ERROR);
			throw eUtils.myException(com.appfactory.exceptions.CustomErrorMessage.GIT_API_EXCEPTION_CLONE_PUSH,
					e.getMessage());
		}
	}

	/**
	 * This method is used to pull the latest changes from server
	 * 
	 * @param pulledRepository
	 * @param userName
	 * @param password
	 * @throws MyException
	 */
	public void gitPull(final File pulledRepository, final String userName, final String password) throws MyException {
		String localPath;

		FileRepository localRepo = null;

		localPath = pulledRepository.getAbsolutePath();

		try {
			localRepo = new FileRepository(localPath + "/.git");
		} catch (IOException e) {
			/*
			 * "Error performing Git pull operation. Caught under superclass exception"
			 * ;
			 */
		}

		git = new Git(localRepo);

		final PullCommand pullCmd = git.pull();
		try {
			pullCmd.setProgressMonitor((ProgressMonitor) new TextProgressMonitor(new PrintWriter(System.out)))
			.setCredentialsProvider(new UsernamePasswordCredentialsProvider(userName, password));
			pullCmd.call();

		}

		catch (Exception e) {
			System.out.println("its failing here");
			miop.removeDirectory(pulledRepository);
			su.updatestatus(accessdata.getMessageid(), accessdata.getuIModelJson().getGistid(),
					ApplicationConstants.GITHUB_FAILED, ApplicationConstants.CFURL_FAILED, 102,
					ApplicationConstants.LOG_LEVEL_ERROR);
			throw eUtils.myException(com.appfactory.exceptions.CustomErrorMessage.GIT_API_EXCEPTION_CLONE,
					e.getMessage());
		} finally {
			git.close();
		}

	}
}
