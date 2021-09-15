/**   
 * Copyright © 2019 Liufh. All rights reserved.
 * 
 * @Package: com.ssm.configuration.filter 
 * @author: liufuhua
 * @date: 2019年5月29日 下午9:12:26 
 */
package com.dnliu.pdms.filter;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/** 
* @desc: CORS过滤器
* @version 1.0
* @company: ***
* @author: liufuhua
* @since: 2019年5月29日 下午9:12:26 
*
*/
@Component
public class CorsFilter implements Filter {
 
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
 
    }
 
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin")); //http://lfhtimes.cn
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        /* Accept, Origin, XRequestedWith, Content-Type, LastModified,  */
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with,Authorization, content-type");
        response.setHeader("Access-Control-Allow-Credentials", "true");

        filterChain.doFilter(servletRequest, servletResponse);
    }
 
    @Override
    public void destroy() {
 
    }
}
