package com.lhpc.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.lhpc.model.User;
import com.lhpc.service.IUserService;

/**
 * 拦截器
 * 
 * @author YangGuang
 */
public class LoginInterceptor implements HandlerInterceptor {
	@Autowired
	private HttpSession session;
	@Autowired
	private IUserService userService;

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object object, Exception arg3)
			throws Exception {
		// System.out.println("afterCompletion---");
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object object,
			ModelAndView modelAndView) throws Exception {
		// System.out.println("postHandle---");
	}

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object object) throws Exception {
		String openID = request.getParameter("openID");
		if (openID != null && !openID.equals("") && openID.length() == 28) {
			User user = userService.selectByOpenID(openID);
			if (user == null) {
				response.setContentType("application/json");
				response.sendRedirect("/user/noLogin");
				return false;
			} else {
				if (!openID.equals(user.getOpenId())) {
					response.setContentType("application/json");
					response.sendRedirect("/user/illegal");
					return false;
				}
				session.setAttribute("CURRENT_USER", user);
				return true;
			}
		} else {
			response.setContentType("application/json");
			response.sendRedirect("/openID/error");
			return false;
		}

	}
}
