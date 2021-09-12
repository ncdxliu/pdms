package com.dnliu.pdms.interceptor;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.interfaces.Claim;
import com.dnliu.pdms.common.ResponseUtil;
import com.dnliu.pdms.common.utils.JwtUtil;
import com.dnliu.pdms.entity.User;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @desc: 登录拦截器
 * @version 1.0
 * @company: ***
 * @author: liufuhua
 * @since: 2019年3月15日 下午6:04:19 
 *
 */
@Component
public class RequestInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String url = request.getRequestURI();
		if (url.indexOf("/login") >= 0 || url.indexOf("/wxLogin") >= 0 || url.indexOf("image") >= 0 || url.indexOf("js") >= 0
				|| url.indexOf("css") >= 0 || url.indexOf("/register") >= 0 || url.indexOf("/kefuback") >= 0) {
			return true;
		}

		//获取 header里的token
		final String token = request.getHeader("authorization");
		if ("OPTIONS".equals(request.getMethod())) {
			response.setStatus(HttpServletResponse.SC_OK);
		} else {
			if (token == null) {
				response.setHeader("content-type", "text/html;charset=UTF-8");//注意是分号，不能是逗号
				response.getWriter().write(JSON.toJSONString(ResponseUtil.getCommonFailResponse("未登录或会话超时，请重新登录！")));
				return false;
			}

			Map<String, Claim> userData = JwtUtil.verifyToken(token);
			if (userData == null) {
				response.setHeader("content-type", "text/html;charset=UTF-8");//注意是分号，不能是逗号
				response.getWriter().write(JSON.toJSONString(ResponseUtil.getCommonFailResponse("token不合法！")));
				return false;
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
		}

		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView midelAndView) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub

	}
}
