package com.appfactory;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.cloud.Cloud;
import org.springframework.cloud.CloudFactory;
import org.springframework.context.annotation.Bean;

import com.appfactory.constants.ApplicationConstants;
import com.appfactory.rabbitmq.DeployListener;


/**
 * File : Application.java 
 * Description : Class which start the Spring
 * boot process for microservice application 
 * Revision History : 
 * Version 	Date  	Author Reason 
 * 0.1 	27-May-2016 559296 Initial version
 */
@SpringBootApplication
public class Application extends SpringBootServletInitializer{
	/**
	 * Fields required for rabitMQHost
	 * 
	 * @param args
	 */
	@Value("${spring.rabbitmq.host}")
	private String rabbitMQHost;

	@Value("${spring.rabbitmq.port}")
	private String rabbitMQPort;

	@Value("${spring.rabbitmq.username}")
	private String rabbitMQUserName;

	@Value("${spring.rabbitmq.password}")
	private String rabbitMQPassword;

	@Value("${spring.rabbitmq.queuename}")
	private String queueName;
	
	@Value("${spring.rabbitmq.response.queuename}")
	private String responsequeueName;

	@Value("${spring.rabbitmq.exchange}")
	private String exchange;
	
	@Value("${rabbitmq.service.name}")
	private String serviceName;
	
	@Value("${war.deployment.environment}")
	private String deployLocation;
	
	@Value("${rabbitmq.concurrent.consumers}")
	private String concurrentConsumers;

	/**
	 * methods required to start the application
	 */
	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }
    public static void main(String[] args) {
    	  SpringApplication.run(Application.class, args);
    }
   /* @Bean
	public LocaleResolver localeResolver() {
		SessionLocaleResolver slr = new SessionLocaleResolver();
		slr.setDefaultLocale(Locale.US);
		return slr;
	}*/
	/**
	 * ConnectionFactory class to make rabbitMQ connection
	 * @return
	 */
    @Bean
	public ConnectionFactory connectionFactory() {
		String appdeployLocation = System.getenv(ApplicationConstants.DEPLOY_LOCATION);
		if (appdeployLocation==null){
			appdeployLocation=deployLocation;
		}
		ConnectionFactory connectionFactory = null;
		 if(ApplicationConstants.ENVIRONMENT.equalsIgnoreCase(appdeployLocation)){
			 CloudFactory  cloudFactory = new CloudFactory(); 
			 Cloud cloud =  cloudFactory.getCloud(); 
			 connectionFactory = cloud.getServiceConnector(System.getenv(ApplicationConstants.RABBIT_MQ_SERVICE), ConnectionFactory.class,  null); 
		 }else{
			 CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(rabbitMQHost);
				cachingConnectionFactory.setPort(Integer.valueOf(rabbitMQPort));
				cachingConnectionFactory.setUsername(rabbitMQUserName);
				cachingConnectionFactory.setPassword(rabbitMQPassword);
				connectionFactory = cachingConnectionFactory;
		 }
		return connectionFactory;
	}


	/**
	 * RabbitTemplate Instance will be created to send message
	 * 
	 * @return
	 */
	@Bean
	public RabbitTemplate rabbitTemplate() {
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
		rabbitTemplate.setMessageConverter(jsonMessageConverter());
		return rabbitTemplate;
	}

	/**
	 * MessageConverter instance to convert the Java Object in Json Object
	 * @return
	 */
	@Bean
	public MessageConverter jsonMessageConverter() {
		JsonMessageConverter jsonMessageConverter = new JsonMessageConverter();
		return jsonMessageConverter;
	}

	/**
	 * to create a queue instance based on the Queue name fetched from property files
	 * @return
	 */
	@Bean
	Queue queue() {
		return new Queue(queueName, true);
	}

	/**
	 * to create a Direct Exchange instance which will send the message based on the router key
	 * @return
	 */
	@Bean
	DirectExchange exchange() {
		return new DirectExchange(exchange);
	}

	/**
	 * will create a binding instance for queue and exchange based on the router key
	 * @param queue
	 * @param exchange
	 * @return
	 */
	@Bean
	Binding binding(Queue queue, DirectExchange exchange) {
		return BindingBuilder.bind(queue).to(exchange).with(queueName);
	}
	/**
	 * It helps to increase the concurrent consumers
	 * @return
	 */
	@Bean
    public SimpleMessageListenerContainer messageListenerContainer() {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory());
        container.addQueueNames(queueName);
        container.setMessageListener(DeployProcessorListener());
        container.setConcurrentConsumers(Integer.valueOf(concurrentConsumers));
        return container;
    }

	
	@Bean 
	public DeployListener deployListener(){
		return new DeployListener();
	}
	
	@Bean
    public MessageListener DeployProcessorListener() {
        return new MessageListener() {
            public void onMessage(Message message) {
            	deployListener().onMessage(message);
            }
        };
    }
}
