package com.dnliu.pdms.filter;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.interfaces.Claim;
import com.dnliu.pdms.common.ResponseUtil;
import com.dnliu.pdms.common.utils.AppUtil;
import com.dnliu.pdms.common.utils.JwtUtil;
import com.dnliu.pdms.entity.User;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * JWT过滤器，拦截请求
 * @author dnliu
 * @date 2021-09-11 20:50
 */
@Slf4j
public class JwtFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) req;
        final HttpServletResponse response = (HttpServletResponse) res;

        String url = request.getRequestURI();
        if (url.indexOf("/login") >= 0 || url.indexOf("/wxLogin") >= 0 || url.indexOf("image") >= 0 || url.indexOf("js") >= 0
                || url.indexOf("css") >= 0 || url.indexOf("/register") >= 0 || url.indexOf("/kefuback") >= 0) {
            chain.doFilter(request, response);
            return;
        }

        //获取 header里的token
        final String token = request.getHeader("authorization");
        log.info("method: {}", request.getMethod());
        log.info("token: {}", token);
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

            AppUtil.setUser(user);

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
