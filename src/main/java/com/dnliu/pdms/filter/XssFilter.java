/**   
 * Copyright © 2019 Liufh. All rights reserved.
 * 
 * @Package: com.ssm.configuration.filter 
 * @author: liufuhua
 * @date: 2019年5月29日 上午10:37:25 
 */
package com.dnliu.pdms.filter;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

 /** 
 * @desc: 防止SQL注入和XSS攻击Filter
 * @version 1.0
 * @company: ***
 * @author: liufuhua
 * @since: 2019年5月29日 上午10:37:25 
 *
 */
 @Component
public class XssFilter implements Filter {
	
	/* (non-Javadoc)
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException,
            ServletException {
    	XssHttpServletRequestWrapper xssRequest = new XssHttpServletRequestWrapper((HttpServletRequest) request);
    	filterChain.doFilter(xssRequest, response);
    }

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#destroy()
	 */
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}

}
