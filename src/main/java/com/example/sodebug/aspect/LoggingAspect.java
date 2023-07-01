package com.example.sodebug.aspect;

import com.example.sodebug.aspect.logger.model.LogEntity;
import com.example.sodebug.aspect.logger.model.json.ActionStatusEnum;
import com.example.sodebug.aspect.logger.model.json.Params;
import com.example.sodebug.aspect.logger.model.json.RequestDataChange;
import com.example.sodebug.aspect.logger.model.json.ResponseDataAfterChange;
import com.example.sodebug.aspect.logger.service.LogEntityService;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.apache.commons.lang3.exception.ExceptionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

import static com.example.sodebug.config.ApplicationConfiguration.REGEX_FOR_LOGGER;


/**
 * The LoggingAspect class is an aspect that provides logging functionality for annotated methods.
 * It captures the request and response data, logs the method execution details, and saves the log entity to the database.
 *
 * @author ogbozoyan
 * @date 06.04.2023
 */
@Aspect
@Component
public class LoggingAspect {

    @Autowired
    private HttpServletRequest httpRequest;
    @Autowired
    private HttpServletResponse httpResponse;
    @Autowired
    private LogEntityService logEntityService;
//    @Autowired
//    private UserService userService;
    @Autowired
    private ObjectWriter objectWriter;
    private ContentCachingRequestWrapper requestWrapper;
    private ContentCachingResponseWrapper responseWrapper;
    private LogEntity logEntity;
    private final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    /**
     * Pointcut definition for methods annotated with @ToLogger.
     */
    @Pointcut("@annotation(com.example.sodebug.aspect.ToLogger)")
    public void callLogger() {
    }

    /**
     * Advice executed before the annotated method execution.
     */
/*
 In the case of a REST client making a request, the "scheme" field of the HttpServletRequest object should not be null. The "scheme" represents the protocol scheme of the request URL, such as "http" or "https". If the "scheme" is null in such cases, it could indicate a problem or misconfiguration in the server or the way the request is being handled.
Here are a few possible reasons why the "scheme" field could be null in the HttpServletRequest object:
Proxy or Load Balancer: If your application is behind a proxy or load balancer, it may affect how the "scheme" field is set in the HttpServletRequest object. In such cases, you may need to ensure that the proxy or load balancer is properly configured to forward the scheme information.
Custom Servlet Container or Filter: If you are using a custom servlet container or a filter in your application, it might modify or override the scheme value. Make sure your custom components handle the scheme correctly or inspect the configuration to ensure it's not causing the issue.
RequestWrapper or HttpServletRequest Decorator: If you are using a custom RequestWrapper or HttpServletRequest decorator, it's possible that the implementation is not correctly propagating or setting the scheme value. Check your custom code to ensure it sets the scheme appropriately.
It's recommended to investigate your application's server configuration, network setup, and any custom components involved in handling the request to determine the specific cause of the "scheme" being null in the HttpServletRequest object.
 */
    @Before("@annotation(com.example.sodebug.aspect.ToLogger)")
    public void before(JoinPoint joinPoint) {
        try {
            logEntity = new LogEntity();
            initRequest();

            ToLogger toLogger = getLogger(joinPoint);
//            if (userService != null) {
//                logEntity.setUserId(Optional.ofNullable(userService.getCurrentUserId()).orElse(""));
//                if (logEntity.getUserId().equals("anonymous"))
//                    logEntity.setUserLogin(Optional.of("anonymous").orElse(""));
//                else
//                    logEntity.setUserLogin(Optional.ofNullable(userService.getFio()).orElse(""));
//            }
            logEntity.setHttpMethodEnum(toLogger.httpMethod());
            logEntity.setUrl(requestWrapper.getRequestURL().toString()); //here's the cause line
            logEntity.setActionDomain(toLogger.actionDomain());
            logEntity.setAction(toLogger.action());

            RequestDataChange requestDataChange = new RequestDataChange();
            List<Params> paramsList = new ArrayList<>();

            Enumeration<String> paramNames = httpRequest.getParameterNames();
            while (paramNames.hasMoreElements()) {
                String name = paramNames.nextElement();
                String[] values = httpRequest.getParameterValues(name);
                paramsList.add(new Params(name, values));
            }
            requestDataChange.setParams(paramsList);
            requestDataChange.setBody(getRequestBody(requestWrapper));

            logEntity.setRequestDataChange(objectWriter.writeValueAsString(requestDataChange).replaceAll(REGEX_FOR_LOGGER, ""));

        } catch (Exception e) {
            logger.debug("Error in LoggingAspect.before: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    /**
     * Advice executed after the annotated method execution.
     */
    @AfterReturning(value = "@annotation(com.example.sodebug.aspect.ToLogger)", returning = "returnValue")
    public void afterReturning(JoinPoint joinPoint, Object returnValue) {
        try {
            ToLogger toLogger = getLogger(joinPoint);
            initResponse();
            ResponseDataAfterChange responseDataAfterChange = new ResponseDataAfterChange();

            if (toLogger.returnResponse()) {
                responseDataAfterChange.setBody(getResponseBody(returnValue));
            }

            logEntity.setResponseDataAfterChange(objectWriter.writeValueAsString(responseDataAfterChange).replaceAll(REGEX_FOR_LOGGER, ""));

            logEntity.setResponseStatus(String.valueOf(httpResponse != null ? httpResponse.getStatus() : 0));
            logEntity.setActionStatus(String.valueOf(ActionStatusEnum.SUCCESSFULLY));
            logEntity.setDtCreate(Timestamp.valueOf(LocalDateTime.now()));

            if (toLogger.isSaveInDataBase()) {
                logEntityService.save(logEntity);
            }
            logger.info(logEntity.toString());
        } catch (Exception e) {
            logger.debug("Error in LoggingAspect.afterReturning: " + e.getMessage());
            e.printStackTrace();
        }

    }

    /**
     * Advice executed after the annotated method throws an exception.
     */
    @AfterThrowing(value = "@annotation(com.example.sodebug.aspect.ToLogger)", throwing = "exception")
    public void afterError(JoinPoint joinPoint, Throwable exception) {
        try {
            ToLogger toLogger = getLogger(joinPoint);

            logEntity.setResponseDataAfterChange(null);
            logEntity.setResponseStatus(String.valueOf(500));
            logEntity.setActionStatus(String.valueOf(ActionStatusEnum.ERROR));
            logEntity.setBaseException(exception.getClass().getSimpleName());
            logEntity.setStackTraceOnError(ExceptionUtils.getMessage(exception));
            logEntity.setDtCreate(Timestamp.valueOf(LocalDateTime.now()));


            logEntityService.save(logEntity);
            logger.info(logEntity.toString());

        } catch (Exception e) {
            logger.debug("Error in LoggingAspect.afterError: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Retrieves the request body from the given request wrapper.
     */
    private String getRequestBody(ContentCachingRequestWrapper request) {
        try {
            String bodyRequest = new String(request.getContentAsByteArray(), StandardCharsets.UTF_8);
            bodyRequest = bodyRequest.replaceAll(REGEX_FOR_LOGGER, "");
            return bodyRequest;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Retrieves the response body from the given response object.
     */
    private String getResponseBody(Object response) {
        try {
            if (response == null)
                return null;
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Initializes the current request.
     */
    private void initRequest() {
        this.httpRequest = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        this.requestWrapper = (ContentCachingRequestWrapper) httpRequest;
        logger.debug("Init Request httpRequest: " + httpRequest + " " + "requestWrapper: " + requestWrapper);
    }

    /**
     * Initializes the current request.
     */
    private void initResponse() {
        this.httpResponse = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getResponse();
        this.responseWrapper = (ContentCachingResponseWrapper) httpResponse;
        logger.debug("Init Response httpResponse: " + httpResponse + " " + "responseWrapper: " + responseWrapper);
    }

    /**
     * Retrieves the @ToLogger annotation from the given join point.
     */
    private ToLogger getLogger(JoinPoint joinPoint) {
        /*================Extract information from the annotation===============*/
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        return signature.getMethod().getAnnotation(ToLogger.class);
        /*=====================================================================*/
    }
}

