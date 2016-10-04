package hello;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class AuthController extends WebMvcConfigurerAdapter {
	
	@Autowired
	private HttpUtil httpUtil;
	
	@Autowired
	private PropertyFileReader propertyFileReader;
	
	@Autowired
	private VcapReader vcapReader;
	

	
	@Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.favorPathExtension(false);
        configurer.defaultContentType(MediaType.APPLICATION_JSON);
    }
	
   // This method is used to display log in page
	
    @RequestMapping("/")
    public String greeting(@RequestParam(value="name", required=false, defaultValue="User") String name, Model model) {
        model.addAttribute("name", name);
        return "login";
    }
    
    // This method is used for credentials authentication
    
    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public String authenticateUser(@RequestParam String username,@RequestParam String password,Model model ) {
    	
    	model.addAttribute("username", username);
    	model.addAttribute("password", password);
    	
    	
        return "authenticate";
    }
    // This method is used to display Signin  in page
    @RequestMapping(value = "/signinfacebook", method = RequestMethod.GET)
    public String loginFaceBook(Model model ) {
    	
    	
        return "genarateotp";
    }
    
    // This method is used to generate the one time password and send to specified email address.
    
    @RequestMapping(value = "/requestotp", method = RequestMethod.POST)
    public String authValidate(Model model ) {
    	
    
    	String facebookurl=vcapReader.getServiceUrl()+propertyFileReader.getMessage("facebook.auth.url");
    	System.out.println("facebookurl   -------->"+facebookurl);
    	
    	Map<String, String> connectProprties= new HashMap<String, String>();
    	connectProprties.put("apiKey",vcapReader.getApiKey());
    	
    	String responseOTPgenaration =null;    	
    	Map<Integer, String> authRespMap = null;    	
    	
    	String token = null;
    	 try{
    		 authRespMap= httpUtil.executePostCall(facebookurl, "", connectProprties);    		 
    		 if(authRespMap!=null && authRespMap.size()>0  ){    			 
    			 for (Integer responseCode : authRespMap.keySet()) {    				 
    				 if(responseCode==ApplicationConstants.ResponseCode_Pre_Post_Hook){    					 
    					 String authresp = authRespMap.get(responseCode);
    					 JSONObject jsonObj = new JSONObject(authresp);
    					 token =(String)jsonObj.get("token");
    					 
    					 // otp generation call
    					 responseOTPgenaration=genarateOTP(token);
    					 model.addAttribute("token", responseOTPgenaration);
    					 
    					 return "validate"; 
    				 }
    					
    			}
    		 }
    
    	 }
    	catch(Exception e){
    		e.printStackTrace();
    	}
    
        return "validate";
    }
    
    // This method is used to generate the one time password and send to specified email address.
    
    @RequestMapping(value = "/generateotp", method = RequestMethod.POST)    
	public String genarateotp(@RequestParam String token,Model model)
			throws JsonProcessingException, IOException, JsonParseException, JsonMappingException {   	
    	
		 	return genarateOTP(token);
	
	}
 
    private String genarateOTP(String token){
    	
    	  ObjectMapper mapper = new ObjectMapper();
    	  
    	  String toEmail= propertyFileReader.getMessage("facebook.toEmail");
    	  String fromEmail=propertyFileReader.getMessage("facebook.fromEmail");
    	  
    	  String  genarateOtpUrl= vcapReader.getServiceUrl()+propertyFileReader.getMessage("facebook.genarateOtpUrl");
    	  
    	  Map<String, String> requestToheaders= new HashMap<String, String>();
    	  requestToheaders.put("token", token);
    	 
    	  RequestOtpVO requestOtpVO = new RequestOtpVO();    	  
    	  requestOtpVO.setToRecipient(toEmail);
    	  requestOtpVO.setFromMail(fromEmail);
    	  
    	  String otpToken=null;
    	  try{    		  
    	  String jsonStr = mapper.writeValueAsString(requestOtpVO);
    	  String respgenarateOTP= httpUtil.executeGenarateOTPCall(genarateOtpUrl, jsonStr, requestToheaders);    	  
    	  JSONObject jsonObj = new JSONObject(respgenarateOTP);
    	  otpToken =(String)jsonObj.get("token");
		
    	  }
    	  catch(Exception e){
      		e.printStackTrace();
      	}
    	return  otpToken; 
    }
    
    // This method is used to validate  the one time password, and call the callbackurl.
    
    @RequestMapping(value = "/validateotp", method = RequestMethod.POST)
    public String validateOtp(@RequestParam String otpcode,@RequestParam String token,Model model,HttpServletResponse response ) {
    
    	ObjectMapper mapper = new ObjectMapper();
    	Map<String, String> connectProprties= new HashMap<String, String>();
    	connectProprties.put("token", token); 
    	
    	ValidateOTPVO validateOTPVO = new ValidateOTPVO();
    	validateOTPVO.setOtpCode(otpcode);
    	String nextCall=null;
    	String unused= null;
    	
    	String validateUrl=vcapReader.getServiceUrl()+propertyFileReader.getMessage("facebook.validateUrl");
    	
    	Map<Integer, String> authRespMap = null;
    	try{
    	String jsonotp=mapper.writeValueAsString(validateOTPVO);   	
    	
    	 authRespMap= httpUtil.executePostCall(validateUrl, jsonotp, connectProprties);
    	
    	 if(authRespMap!=null && authRespMap.size()>0  ){
			 for (Integer responseCode : authRespMap.keySet()) {
				 if(responseCode==ApplicationConstants.ResponseCode_NextCall){
					 String authresp = authRespMap.get(responseCode);
					 JSONObject jsonObj = new JSONObject(authresp);
					 nextCall =(String)jsonObj.get("nextCall");					
					//call back url					
					 String  callbackRegex   = propertyFileReader.getMessage("facebook.callbackRegex");
					 String  callbackURL     = propertyFileReader.getMessage("facebook.callbackUrl");
					 String facebookurl = vcapReader.getServiceUrl()+nextCall+callbackRegex+callbackURL;
					 
					 System.out.println("facebookurl     " +facebookurl);				    	
				     response.sendRedirect(facebookurl);				     
				 }
				 if(responseCode==ApplicationConstants.ResponseCode_Success){
					 String authresp = authRespMap.get(responseCode);
					 JSONObject jsonObj = new JSONObject(authresp);
					 model.addAttribute("accessToken", (String)jsonObj.get("accessToken")); 
					 model.addAttribute("id", (String)jsonObj.get("id")); 
					 model.addAttribute("displayName", (String)jsonObj.get("displayName")); 
					 model.addAttribute("provider", (String)jsonObj.get("provider"));
					 return "sitedetails";
					
				 }
					
			}
		 }
    
    	}
    	catch(Exception e){
    		e.printStackTrace();
    	}
    	return unused;
    }

    // This method is called as redirect url and get the token as path parameter, the token will be used for auth complete service call.
    
    @RequestMapping(value = "/callback/{token:.+}", method = RequestMethod.GET)
    public String callBackUser(@PathVariable String token,Model model,HttpServletRequest request) {
    	
    	System.out.println("In Callback method the url is " +token);    
    	String authCompleteURL =vcapReader.getServiceUrl()+ propertyFileReader.getMessage("facebook.authCompleteUrl");
    	
    	Map<String, String> connectProprties= new HashMap<String, String>();
    	connectProprties.put("apiKey",vcapReader.getApiKey());
    	connectProprties.put("token", token);
    	
    	Map<Integer, String> authRespMap = null;
    	String nextCall = null;
    	String authComplteToken= null;
    	String responseOTPgenaration= null;
    	String unused =null;
    	 try{
    	 authRespMap= httpUtil.executePostCall(authCompleteURL, "", connectProprties);
    	
    	 if(authRespMap!=null && authRespMap.size()>0  ){
			 for (Integer responseCode : authRespMap.keySet()) {
				 if(responseCode==303){
					 String authresp = authRespMap.get(responseCode);
					 JSONObject jsonObj = new JSONObject(authresp);
					 nextCall =(String)jsonObj.get("nextCall");
					 authComplteToken=(String)jsonObj.get("token");
					//call back url
					 // otp genaration call
					 responseOTPgenaration=genarateOTP(authComplteToken);
					 model.addAttribute("token", responseOTPgenaration);
					 return "validate";
				 }
				
				 if(responseCode==200){
					 String authresp = authRespMap.get(responseCode);
					 JSONObject jsonObj = new JSONObject(authresp);
					 model.addAttribute("accessToken", (String)jsonObj.get("accessToken")); 
					 model.addAttribute("id", (String)jsonObj.get("id")); 
					 model.addAttribute("displayName", (String)jsonObj.get("displayName")); 
					 model.addAttribute("provider", (String)jsonObj.get("provider"));
					 return "sitedetails";
				 }
				 	
			}
		 }
   
    	 }
    	 catch(Exception e){
    		 e.printStackTrace();
    	 }
    
       return unused;
    }
}
