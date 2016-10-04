/**
 * 
 */
package com.appfactory.aop;

import java.util.Arrays;

import javax.annotation.PostConstruct;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.appfactory.constants.ApplicationConstants;
import com.appfactory.logger.LoggerFactory;
import com.appfactory.resources.Messages;
import com.appfactory.route.ILogger;
import com.appfactory.utils.ExceptionUtils;

/**
 * @author 559296
 *
 */
@Aspect
@Component
public class AppFactoryLoggerAOP {

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
		@Pointcut("execution(* com.appfactory.core.*.*(..))|| "
				+ "execution(* com.appfactory.GitProcessing.*.*(..)) ||"
				+ "execution(* com.appfactory.platformpush.*.*(..)) || execution(* com.appfactory.utils.*.*(..))")
		
		public void appFactoryMethods() {

		}

		/**
		 * around event for all mentioned package methods to implement the logging code 
		 * @param joinPoint
		 * @return
		 * @throws Throwable
		 */
		@Around("appFactoryMethods()")
		public Object appManagerAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
			try {
				logger.info( ApplicationConstants.ENTER_METHOD+ joinPoint.getSignature().toShortString() + " with Arguments "
						+ Arrays.toString(joinPoint.getArgs()));
				Object result = joinPoint.proceed();
				logger.info(ApplicationConstants.EXIT_METHOD + joinPoint.getSignature().toShortString());
				if (Messages.getString(ApplicationConstants.CONFIG_LOG_LEVEL)
						.equalsIgnoreCase(ApplicationConstants.DEBUG)) {
					logger.debug(
							"Exiting  method " + joinPoint.getSignature().toShortString() + " with results: " + result);
				}
				return result;
			} catch (Exception e) {
				/*
				logger.error("******  * * *    * * *       **     * * *   ");
				logger.error("***     *    *   *    *    *    *   *    *  ");
				logger.error("***     * *      * *       *    *   *  *    ");
				logger.error("******  *    *   *    *      **     *    *   ");
				*/
				logger.error("****************************************************");

				logger.error(ApplicationConstants.EXIT_METHOD + joinPoint.getSignature().toShortString() + " with Error: "
						+ ExceptionUtils.getTrackTraceContent(e));
				throw e;
			}
			
			
		}
}

