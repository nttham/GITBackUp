package com.appManager.controller;

import javax.servlet.jsp.jstl.sql.Result;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@CrossOrigin
@RestController
public class AppController {
	 @RequestMapping(value = "/custom", method = RequestMethod.POST)
	    public String custom() {
	        return "custom";
	    }
	 @RequestMapping(value = "/operate/add/{left}/{right}", method = RequestMethod.GET, produces = "application/json")
	 @ApiOperation(value = "addNumbers", nickname = "addNumbers")
	 @ApiResponses(value = { @ApiResponse(code = 200, message = "Success", response = Result.class),
	         @ApiResponse(code = 401, message = "Unauthorized"), 
	         @ApiResponse(code = 500, message = "Failure") })
	 public String add(@PathVariable("left") int left, @PathVariable("right") int right) {
		return "hi";
		 
	 }
}
