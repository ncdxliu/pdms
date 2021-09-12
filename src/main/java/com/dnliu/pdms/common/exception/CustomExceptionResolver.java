package com.dnliu.pdms.common.exception;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @desc: 全局异常处理器
 * @version 1.0
 * @company: ***
 * @author: liufuhua
 * @since: 2019年3月25日 下午2:27:59 
 *
 */
@Component
public class CustomExceptionResolver implements HandlerExceptionResolver {
	protected final Logger logger = LoggerFactory.getLogger(CustomExceptionResolver.class);

	@SuppressWarnings({ "rawtypes", "unchecked", "static-access" })
	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		// handler就是处理器适配器要执行的Handler对象(只有method)
		// 解析异常类型
		CustomException customException = null;
		StackTraceElement stackTraceElement = ex.getStackTrace()[1];
		logger.info("异常文件名:{}|异常方法名:{}|异常行号: {}|异常原因:{}", stackTraceElement.getFileName(), stackTraceElement.getMethodName(), stackTraceElement.getLineNumber(), ex.getLocalizedMessage());
		if (ex instanceof CustomException) {
			customException = (CustomException) ex;
		} else {
			customException = new CustomException("未知错误");
		}
		// 错误信息
		String message = customException.getMessage();
		ModelAndView modelAndView = new ModelAndView();
		// 将错误信息传到页面
		/*
		 * modelAndView.addObject("message", message); //指向错误页面
		 * modelAndView.setViewName("error/error");
		 */

		logger.error("异常文件名: {}|异常方法名:{}|异常行号: {}|异常原因: {}, e: ", stackTraceElement.getFileName(), stackTraceElement.getMethodName(), stackTraceElement.getLineNumber(), ex.getLocalizedMessage(), ex);
		
		Map rspMap = new HashMap<>();
    	rspMap.put("rspCode", "R9999");
    	String rspMsg = ex.getLocalizedMessage();
    	
    	rspMap.put("rspMsg", rspMsg);
    	
    	JSONObject result = new JSONObject();
    	
    	response.setStatus(HttpStatus.OK.value()); //设置状态码    
        response.setContentType(MediaType.APPLICATION_JSON_VALUE); //设置ContentType  
        response.setCharacterEncoding("UTF-8"); //避免乱码  
        response.setHeader("Cache-Control", "no-cache, must-revalidate"); 
        try {
			response.getWriter().write(result.toJSONString(rspMap));
		} catch (IOException e) {
			logger.error("回写响应报文失败, e: ", e);
		}

		logger.info("错误信息: {}", message);
		
		return modelAndView;
	}

}
