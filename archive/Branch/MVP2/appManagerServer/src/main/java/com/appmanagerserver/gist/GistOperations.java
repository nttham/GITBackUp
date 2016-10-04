package com.appmanagerserver.gist;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.io.FileUtils;
import org.eclipse.egit.github.core.Authorization;
import org.eclipse.egit.github.core.Gist;
import org.eclipse.egit.github.core.GistFile;
import org.eclipse.egit.github.core.service.GistService;
import org.eclipse.egit.github.core.service.OAuthService;
import org.eclipse.jgit.api.AddCommand;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.CommitCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.appmanagerserver.blueprints.AppfactoryResponse;
import com.appmanagerserver.blueprints.Logs;
import com.appmanagerserver.constants.ApplicationConstants;
import com.appmanagerserver.exception.CustomErrorMessage;
import com.appmanagerserver.messages.Messages;
import com.appmanagerserver.utils.ExceptionUtils;
import com.appmanagerserver.utils.SSLUtilities;
import com.appmanagerservice.sendinterface.IGistHandler;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * 
 * @author rudhrapriya
 *@edited 559296
 */

@Component
public class GistOperations implements IGistHandler {
	@Autowired
	private ExceptionUtils eUtils;
	
	private Map<String, String> gistETagMap = new HashMap<String, String>();
	private String authToken=null;
	public GistService getGistInstance() throws IOException {
		OAuthService oauthService = new OAuthService();
		oauthService.getClient().setCredentials(Messages.getString(ApplicationConstants.GIST_USERNAME),
				Messages.getString(ApplicationConstants.GIST_PASSWORD));
		Authorization auth = new Authorization();
		auth.setNote(new Timestamp(new Date().getTime()).toString());
		auth.setScopes(Arrays.asList(ApplicationConstants.GIST_SCOPE));
		auth = oauthService.createAuthorization(auth);
		GistService gistService = new GistService();
		gistService.getClient().setOAuth2Token(auth.getToken());
		return gistService;
	}
	
	public String getGistToken() throws IOException {
		OAuthService oauthService = new OAuthService();
		oauthService.getClient().setCredentials(Messages.getString(ApplicationConstants.GIST_USERNAME),
				Messages.getString(ApplicationConstants.GIST_PASSWORD));
		Authorization auth = new Authorization();
		auth.setNote(new Timestamp(new Date().getTime()).toString());
		auth.setScopes(Arrays.asList(ApplicationConstants.GIST_SCOPE));
		auth = oauthService.createAuthorization(auth);
		return auth.getToken();
	}
/**
 * This method will help basically to read the content from gist.
 */
	@Override
	public AppfactoryResponse readGistContent(String gistFileName) throws IOException {
		
		
		HttpsURLConnection conn = null;
		try {
			if (authToken == null) {
				authToken = getGistToken();
			}
			URL url = new URL(ApplicationConstants.GIST_BASE_APIURL + gistFileName);
			SSLUtilities.disableSSLCertificateChecking();
			conn = (HttpsURLConnection) url.openConnection();
			conn.setRequestMethod(ApplicationConstants.GET_REQUEST);
			String eTag = gistETagMap.get(gistFileName);
			if (eTag != null) {
				conn.setRequestProperty(ApplicationConstants.IF_NONE_MATCH_STR, eTag);
			}
			conn.setRequestProperty(ApplicationConstants.AUTHORIZATION, ApplicationConstants.TOKEN_STRING+" "+authToken);

			if (conn.getResponseCode() == HttpStatus.NOT_MODIFIED.value()) {
				AppfactoryResponse gistResponseVO=new AppfactoryResponse();
				gistResponseVO.setCfAppURL("NOT_MODIFIED");
				return gistResponseVO;
			}
			if (conn.getResponseCode() == HttpStatus.UNAUTHORIZED.value()) {
				authToken = getGistToken();
			} else {
				if (conn.getHeaderField(ApplicationConstants.ETAG_STRING) != null) {
					gistETagMap.put(gistFileName, conn.getHeaderField(ApplicationConstants.ETAG_STRING));
				}
				BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
				StringBuffer buffer = new StringBuffer();
				String line;
				while ((line = br.readLine()) != null) {
					buffer.append(line);
				}
				String responseString = buffer.toString();
				JSONParser parser = new JSONParser();
				JSONObject responseObj = (JSONObject) parser.parse(responseString);
				responseObj = (JSONObject) responseObj.get(ApplicationConstants.FILES_STRING);
				responseObj = (JSONObject) responseObj.get(ApplicationConstants.GIST_STATUS_FILE_NAME);
				String ms_status_json = (String) responseObj.get(ApplicationConstants.CONTENT_STRING);
				Gson gson = new Gson();
				Type collectionType = new TypeToken<AppfactoryResponse>() {
				}.getType();
				AppfactoryResponse gistResponseVO=gson.fromJson(ms_status_json,collectionType);
				return gistResponseVO;
			}
		}
			catch(Exception e){
				eUtils.myException(CustomErrorMessage.GISTEXCEPTION7, ExceptionUtils.getTrackTraceContent(e));
			}
		return null;
	}

	/**
	 * This method will help basically to write the content to gist.
	 */
	@Override
	public void writeGistContent(String messageid, AppfactoryResponse appfactoryResponsevo) {
		try {
			GistService gistService = getGistInstance();
			Gist gist = gistService.getGist(messageid);
			GistFile gistFile = gist.getFiles().get(ApplicationConstants.GIST_STATUS_FILE_NAME);
			String existingContent = gistFile.getContent();

			Gson gson = new Gson();
			Type collectionType = new TypeToken<AppfactoryResponse>() {
			}.getType();
			AppfactoryResponse gistResponseVO = gson.fromJson(existingContent, collectionType);

			List<Logs> logList = new ArrayList<Logs>();

			if (gistResponseVO.getLogsList() != null && !gistResponseVO.getLogsList().isEmpty()) {
				logList.addAll(gistResponseVO.getLogsList());
			}
			if (appfactoryResponsevo.getLogsList() != null && !appfactoryResponsevo.getLogsList().isEmpty()) {
				logList.addAll(appfactoryResponsevo.getLogsList());
			}

			appfactoryResponsevo.setLogsList(logList);

			String finalContent = gson.toJson(appfactoryResponsevo);
			gistFile.setContent(finalContent);
			gist.setDescription(ApplicationConstants.GIST_UPDATE_DESCRIPTION);
			gistService.updateGist(gist);
		} catch (IOException e) {
			eUtils.myException(CustomErrorMessage.GISTEXCEPTION7, ExceptionUtils.getTrackTraceContent(e));
		}

	}
	
	/**
	 * This method will help basically to write the content to gist.
	 */
	@Override
	public String defaultGistContent(String messageid, String input) {
		try {
			AppfactoryResponse appfactoryResponsevo = new AppfactoryResponse();
			Logs LogList=new Logs();
			List<Logs> log = new ArrayList<Logs>();
			appfactoryResponsevo.setCfAppURL(ApplicationConstants.LOCAL_DOWNLOAD_MSG);
			LogList.setLoglevel(ApplicationConstants.INFO);
			LogList.setMessage(ApplicationConstants.STAGED_FOR_DEPLOYMENT);
			LogList.setTimeStamp(new Timestamp(new Date().getTime()));
			LogList.setStatusCode(101);
			log.add(LogList);
			appfactoryResponsevo.setLogsList(log);
			
			Gson gson = new Gson();
			String jsonContent = gson.toJson(appfactoryResponsevo);
			
			GistService gistService =  getGistInstance();
			
			Gist gist = new Gist();
			gist.setPublic(true);
			gist.setDescription(ApplicationConstants.GIST_CREATE_DESCRIPTION);
			GistFile file = new GistFile();
			file.setContent(jsonContent);
			file.setFilename(ApplicationConstants.GIST_STATUS_FILE_NAME);
			gist.setFiles(Collections.singletonMap(file.getFilename(), file));
			
			gist = gistService.createGist(gist);
			return gist.getId();
		} catch (IOException e) {
			eUtils.myException(CustomErrorMessage.GISTEXCEPTION7, ExceptionUtils.getTrackTraceContent(e));
			return null;
		}

		
	}
/**
 * This method will take gist id as input param.
 * And will upload the folder to gist to get the gist url
 */
	@Override
	public String uploadTempapp(String gistid,String sourcePath,String destination) {
		try {
			GistService gistService =  getGistInstance();
			Gist gist = gistService.getGist(gistid);
			String pushUrl=gist.getGitPushUrl();
			String userName=Messages.getString(ApplicationConstants.GIST_USERNAME);
			String passWord=Messages.getString(ApplicationConstants.GIST_PASSWORD);

			File sourceDir = new File(sourcePath);

			File destinationDir = new File(destination);

			UsernamePasswordCredentialsProvider usernamePassword = new UsernamePasswordCredentialsProvider(userName,
					passWord);

			CloneCommand cloneCommand = new CloneCommand().setCredentialsProvider(usernamePassword)
					.setDirectory(destinationDir).setURI(pushUrl);
			Git git = null;
			git = cloneCommand.call();

			FileUtils.copyDirectory(sourceDir, destinationDir);

			AddCommand addCommand = git.add();
			addCommand.addFilepattern(".");
			addCommand.call();

			CommitCommand commit = git.commit();
			commit.setCommitter(userName, userName).setMessage(ApplicationConstants.GIST_FILE_UPLOAD_COMMENTS);
			commit.call();

			PushCommand pushCommand = git.push();
			pushCommand.setCredentialsProvider(usernamePassword).setForce(true).setPushAll();
			pushCommand.call();
			git.close();
			destinationDir.delete();
		} catch (IOException | GitAPIException e) {
			eUtils.myException(CustomErrorMessage.GISTEXCEPTION7, ExceptionUtils.getTrackTraceContent(e));
		}
		return null;
	}
	@Override
	public String getRawUrl(String bluePrintID) {
			try {
				GistService gistService = getGistInstance();
				String zipFileName = ApplicationConstants.EMPTY_STRING;
				Map<String, GistFile> filesMap = gistService.getGist(bluePrintID).getFiles();
				for (Map.Entry<String, GistFile> entry : filesMap.entrySet()) {
					GistFile file = entry.getValue();
					if (!file.getFilename().toLowerCase().equalsIgnoreCase(ApplicationConstants.GIST_STATUS_FILE_NAME)) {
						zipFileName = file.getFilename();
					} 
				}
				return gistService.getGist(bluePrintID).getFiles().get(zipFileName).getRawUrl();
			} catch (IOException e) {
				eUtils.myException(CustomErrorMessage.GISTEXCEPTION7, ExceptionUtils.getTrackTraceContent(e));
			}
			return bluePrintID;

		
	}
}
