package com.appManager.core.Test;

import static org.junit.Assert.*;

import org.json.JSONObject;
import org.json.JSONString;
import org.junit.Test;

import com.appManager.core.PlatformAuthenticate;



public class PlatformAuthenticateTest {

	@Test
	public void loginTest() {
		PlatformAuthenticate platformAuthenticate = new PlatformAuthenticate();
		JSONObject json =new JSONObject();
		json.put("org_guid",  "0f87ea0f-b95f-475d-b361-96dfd212add6");
		json.put("space_guid", "b169a527-a10a-4a84-a45a-2909fee6b1d9");
		json.put("token", "eyJhbGciOiJSUzI1NiIsImtpZCI6ImxlZ2FjeS10b2tlbi1rZXkiLCJ0eXAiOiJKV1QifQ.eyJqdGkiOiJkNGEzYmM0Yjg1YWI0NGQyYjczOWJmM2I5OTg3ZjgwZSIsInN1YiI6ImM3NDQ4ZGZmLWRmZjktNGJmZC05MjYxLTdiMmI2YjE2OGVlMyIsInNjb3BlIjpbIm9wZW5pZCIsInNjaW0ucmVhZCIsImNsb3VkX2NvbnRyb2xsZXIuYWRtaW4iLCJ1YWEudXNlciIsInJvdXRpbmcucm91dGVyX2dyb3Vwcy5yZWFkIiwiY2xvdWRfY29udHJvbGxlci5yZWFkIiwicGFzc3dvcmQud3JpdGUiLCJjbG91ZF9jb250cm9sbGVyLndyaXRlIiwiZG9wcGxlci5maXJlaG9zZSIsInNjaW0ud3JpdGUiXSwiY2xpZW50X2lkIjoiY2YiLCJjaWQiOiJjZiIsImF6cCI6ImNmIiwiZ3JhbnRfdHlwZSI6InBhc3N3b3JkIiwidXNlcl9pZCI6ImM3NDQ4ZGZmLWRmZjktNGJmZC05MjYxLTdiMmI2YjE2OGVlMyIsIm9yaWdpbiI6InVhYSIsInVzZXJfbmFtZSI6ImFkbWluIiwiZW1haWwiOiJhZG1pbiIsImF1dGhfdGltZSI6MTQ2NzExODEyOCwicmV2X3NpZyI6ImVlZjAzNDhjIiwiaWF0IjoxNDY3MTE4MTI4LCJleHAiOjE0NjcxMTg3MjgsImlzcyI6Imh0dHBzOi8vdWFhLjU0LjIwOC4xOTQuMTg5LnhpcC5pby9vYXV0aC90b2tlbiIsInppZCI6InVhYSIsImF1ZCI6WyJjZiIsIm9wZW5pZCIsInNjaW0iLCJjbG91ZF9jb250cm9sbGVyIiwidWFhIiwicm91dGluZy5yb3V0ZXJfZ3JvdXBzIiwicGFzc3dvcmQiLCJkb3BwbGVyIl19.qYr8L2C0nepu2pxFBRRqBn7VvEo4wcJ8bkAomXylnoAnY1368C3xS0HTN3OlTIglWm6CBXbhL5O4-0FNSX2-G_o-vY8BOiIDxqxeKT1SyGuLTvMq08CxElHtC_j_1CVXkDuI6BwdR7sAxPHRMQn6uLW1J-ugn-5T_hWZBs6DFt8");
		assertEquals(json.toString(), platformAuthenticate.login("admin", "admin", "0"));
		
	}

}
