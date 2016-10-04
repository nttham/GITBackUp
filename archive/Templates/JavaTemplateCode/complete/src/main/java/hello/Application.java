package hello;

import java.util.Locale;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public SessionLocaleResolver localeResolver() {
           SessionLocaleResolver slr = new SessionLocaleResolver();
           slr.setDefaultLocale(Locale.US);
           return slr;

    }
    /**
    * Method which will be used to load the property file
    * 
     * @return
    */
    @Bean
    public ReloadableResourceBundleMessageSource messageSource() {
    	
           ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
           String[] propertyArray = new String[1];
           propertyArray[0] = "classpath:/properties/deployprocessor";         
           messageSource.setBasenames(propertyArray);
           
           return messageSource;

    }
   
}
