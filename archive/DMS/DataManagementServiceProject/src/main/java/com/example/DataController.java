package com.example;



import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Controller
public class DataController extends WebMvcConfigurerAdapter {
	
	@Autowired
	private JsonReader jsonReader;
	
	@Autowired
	private HttpUtil httpUtil;
	
	@Autowired
	private CommandUtilityVO commandUtilityVO;
	
	@Autowired
	private PropertyFileReader propertyFileReader;
	
	
	@Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.favorPathExtension(false);
        configurer.defaultContentType(MediaType.APPLICATION_JSON);
    }

    @RequestMapping("/")
    public String greeting( Model model) {
        model.addAttribute("dataVO", new DataVO());
        return "dashboard";
    }
    
    
    @RequestMapping(value="/deploy", method = RequestMethod.POST)
   	public String getData(@RequestBody String  input)  {
    	 System.out.println(" Inside Get Data method");
    	String response=null;
    	boolean resopnseflag = false;
    	try{
    	
    		
        System.out.println("input ----------->"+input);
       
        
    	JSONParser jsonParser =new JSONParser();
    	
		JSONObject object = (JSONObject) jsonParser.parse(input.trim());
		
		List<StreamDataVO> streamDataVOList=jsonReader.iterateJSON(object);	
		
		response= commandUtilityVO.genarateData(streamDataVOList);		
		
		String createStreamURL=propertyFileReader.getMessage("datamanagement.createstreamurl");
		
		System.out.println("respone command------------>"+response);		
		
		 resopnseflag=httpUtil.executePostCallCreatStream(createStreamURL, response);
		 
		
    	}
    	catch(Exception e){
    		e.printStackTrace();
    	}
   	 return "message";
    }
   
}

