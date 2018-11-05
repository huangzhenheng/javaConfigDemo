package com.hzh.filter;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ValidateFilter extends AbstractFilter {

	String staticPath = null;
	List<String> excludePathList = new ArrayList<>();

	@Override
	public void init(FilterConfig config) throws ServletException {
		staticPath = config.getInitParameter("staticPath");

		String excludePath = config.getInitParameter("excludePath");
		String[] paths = excludePath.split(",");
		excludePathList = Arrays.asList(paths);
	}


	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain filterChain)
			throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;

		// 获取用户请求路径
		String url = request.getRequestURI();

		// 静态资源直接通过
		if (url.startsWith(staticPath)) {
			filterChain.doFilter(request, response);

		} else {
			// // 跟登录有关的资源直接通过
			if (excludePathList.contains(url)) {

				filterChain.doFilter(request, response);
			} else {
				filterChain.doFilter(request, response);
//				HttpSession session = request.getSession();
//				if (session.getAttribute("user") == null) {
//					// 说明当前客户端未登录，需要重定向到登录页面
//					response.sendRedirect("/?code=1002");
//				} else {
//					// 已登录用户直接通过
//					filterChain.doFilter(request, response);
//				}
			}
		}
	}
}
