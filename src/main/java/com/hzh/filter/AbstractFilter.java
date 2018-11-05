package com.hzh.filter;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;

public abstract class AbstractFilter implements Filter{

	@Override
	public void destroy() {}

	@Override
	public void init(FilterConfig config) throws ServletException {}

}
