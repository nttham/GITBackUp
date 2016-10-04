
package com.appmanagerserver.aop;

import java.util.Arrays;

import javax.annotation.PostConstruct;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.appmanagerserver.constants.ApplicationConstants;
import com.appmanagerserver.logger.LoggerFactory;
import com.appmanagerserver.messages.Messages;
import com.appmanagerserver.utils.ExceptionUtils;
import com.appmanagerservice.sendinterface.ILogger;

/**
 * @author 559296
 *This class is desgined to log all
 *the methods
 */
@Aspect
@Component
public class AppManagerLoggerAOP {
	private static ILogger logger;

	@Autowired
	private LoggerFactory loggerFactory;

	@PostConstruct
	public void loadLogger() {
		logger = loggerFactory.getLoggerInstance();
	}
	
	/**
	 * PointCut to mention the packages where the loggers will be handled
	 */
	@Pointcut("execution(* com.appmanagerserver.gist.*.*(..)) ||"
			+ "execution(* com.appmanagerserver.producer.*.*(..)) || execution(* com.appmanagerserver.services.*.*(..)) || "
			+ "execution(* com.appmanagerserver.status.*.*(..)) || execution(* com.appmanagerserver.utils.*.*(..))")
	
	public void appManagerMethods() {

	}

	/**
	 * around event for all mentioned package methods to implement the logging code 
	 * @param joinPoint
	 * @return
	 * @throws Throwable
	 */
	@Around("appManagerMethods()")
	public Object appManagerAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
		try {
			logger.info("Entering method " + joinPoint.getSignature().toShortString() + " with Arguments "
					+ Arrays.toString(joinPoint.getArgs()));
			Object result = joinPoint.proceed();
			logger.info("Exiting  method " + joinPoint.getSignature().toShortString());
			if (Messages.getString(ApplicationConstants.CONFIG_LOG_LEVEL)
					.equalsIgnoreCase(ApplicationConstants.DEBUG)) {
				logger.debug(
						"Exiting  method " + joinPoint.getSignature().toShortString() + " with results: " + result);
			}
			return result;
		} catch (Exception e) {
			logger.error("****************************************************");
			logger.error("Exiting  method " + joinPoint.getSignature().toShortString() + " with Error: "
					+ ExceptionUtils.getTrackTraceContent(e));
			throw e;
		}
		
		
	}
}
