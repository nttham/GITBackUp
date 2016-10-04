package com.micro.main;

import static java.lang.System.currentTimeMillis;
import static java.net.URLConnection.guessContentTypeFromName;
import static java.util.logging.Logger.getLogger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;


public class MultipartUtility {
	@SuppressWarnings("unused")
	private static final Logger log = getLogger(MultipartUtility.class.getName());

	private static final String CRLF = "\r\n";
	private static final String CHARSET = "UTF-8";

	private static final int CONNECT_TIMEOUT = 15000;
	private static final int READ_TIMEOUT = 10000;

	private final HttpURLConnection connection;
	private final OutputStream outputStream;
	private final PrintWriter writer;
	private final String boundary;

	public MultipartUtility(String requestURL, String token) throws IOException {
		URL url = new URL(requestURL);

		boundary = "---------------------------" + currentTimeMillis();

		connection = (HttpURLConnection) url.openConnection();
		connection.setConnectTimeout(CONNECT_TIMEOUT);
		connection.setReadTimeout(READ_TIMEOUT);
		connection.setRequestMethod("PUT");
		connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
		connection.setRequestProperty("Authorization", "bearer " + token);
		connection.setUseCaches(false);
		connection.setDoInput(true);
		connection.setDoOutput(true);

		outputStream = connection.getOutputStream();
		writer = new PrintWriter(new OutputStreamWriter(outputStream, CHARSET), true);
	}

	public void addFormField(final String name, final String value) {
		writer.append("--").append(boundary).append(CRLF).append("Content-Disposition: form-data; name=\"").append(name)
				.append("\"").append(CRLF).append("Content-Type: text/plain; charset=").append(CHARSET).append(CRLF)
				.append(CRLF).append(value).append(CRLF);
	}

	public void addFilePart(final String fieldName, final File uploadFile) throws IOException {
		final String fileName = uploadFile.getName();
		writer.append("--").append(boundary).append(CRLF).append("Content-Disposition: form-data; name=\"")
				.append(fieldName).append("\"; filename=\"").append(fileName).append("\"").append(CRLF)
				.append("Content-Type: ").append(guessContentTypeFromName(fileName)).append(CRLF)
				.append("Content-Transfer-Encoding: binary").append(CRLF).append(CRLF);

		writer.flush();
		outputStream.flush();
		try (final FileInputStream inputStream = new FileInputStream(uploadFile);) {
			final byte[] buffer = new byte[4096];
			int bytesRead;
			while ((bytesRead = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, bytesRead);
			}
			outputStream.flush();
		}

		writer.append(CRLF);
	}

	public void addHeaderField(String name, String value) {
		writer.append(name).append(": ").append(value).append(CRLF);
	}

	public String finish() throws IOException {
		writer.append(CRLF).append("--").append(boundary).append("--").append(CRLF);
		writer.close();
		BufferedReader br = new BufferedReader(new InputStreamReader((connection.getInputStream())));
		String output = "";
		String line;
		while ((line = br.readLine()) != null) {
			output = output + line;
		}
		return output;
	}
}
