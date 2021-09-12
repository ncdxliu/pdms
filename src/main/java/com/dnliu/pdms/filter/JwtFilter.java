package com.dnliu.pdms.filter;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.interfaces.Claim;
import com.dnliu.pdms.common.ResponseUtil;
import com.dnliu.pdms.common.utils.JwtUtil;
import com.dnliu.pdms.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * JWT过滤器，拦截请求
 * @author dnliu
 * @date 2021-09-11 20:50
 */
@WebFilter(filterName = "JwtFilter", urlPatterns = "/*")
public class JwtFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) req;
        final HttpServletResponse response = (HttpServletResponse) res;

        response.setCharacterEncoding("UTF-8");

        if (request.getRequestURI().indexOf("loginApi/login") >= 0) {
            chain.doFilter(request, response);
            return;
        }

        //获取 header里的token
        final String token = request.getHeader("authorization");
        logger.info("method: {}", request.getMethod());
        logger.info("token: {}", token);
        if ("OPTIONS".equals(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            chain.doFilter(request, response);
        } else {
            if (token == null) {
                response.getWriter().write(JSON.toJSONString(ResponseUtil.getCommonFailResponse("没有token！")));
                return;
            }

            Map<String, Claim> userData = JwtUtil.verifyToken(token);
            if (userData == null) {
                response.getWriter().write(JSON.toJSONString(ResponseUtil.getCommonFailResponse("token不合法！")));
                return;
            }

            Long id = userData.get("id").asLong();
            String name = userData.get("name").asString();
            String userName = userData.get("userName").asString();
            User user = new User();
            user.setId(id);
            user.setUserName(userName);

            //拦截器 拿到用户信息，放到request中
//            request.setAttribute("id", id);
//            request.setAttribute("name", name);
//            request.setAttribute("userName", userName);

            chain.doFilter(req, res);
        }
    }

    @Override
    public void destroy() {
    }
}
