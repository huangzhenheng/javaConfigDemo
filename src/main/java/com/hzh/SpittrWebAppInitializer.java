package com.hzh;

import javax.servlet.Filter;

import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
/**
 * 在Servlet 3.0环境中， 容器会在类路径中查找实
 * 现javax.servlet.ServletContainerInitializer接口的类， 如果能发现的话， 就会用它来配置Servlet容器。
 * Spring提供了这个接口的实现， 名为SpringServletContainerInitializer， 这个类反过来又会查找实
 * 现WebApplicationInitializer的类并将配置的任务交给它们来完成。 Spring 3.2引入了一个便利
 * 的WebApplicationInitializer基础实现， 也就是AbstractAnnotationConfigDispatcherServletInitializer。 因为我们
 * 的Spittr-WebAppInitializer扩展了AbstractAnnotationConfig DispatcherServlet-Initializer（同时也就实现
 * 了WebApplicationInitializer） ， 因此当部署到Servlet 3.0容器中的时候， 容器会自动发现它， 并用它来配置Servlet上下文。
 * @author Administrator
 *
 */
public class SpittrWebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer{
	//那唯一问题在于它只能部署到支持Servlet 3.0的服务器中才能正常 工作， 如Tomcat 7或更高版本。否则只能使用web.xml了。
	
	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class<?>[]{RootConfig.class};
	}

	/**
	 * 要求DispatcherServlet加载应用上下文时， 使用定义在WebConfig配置类（使用Java配置） 中的bean
	 */
	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class<?>[]{WebConfig.class};
	}

	/**
	 * 它会将一个或多个路径映射到DispatcherServlet上
	 */
	@Override
	protected String[] getServletMappings() {
		return new String[]{"/"};
	}
	
	//配置字符过滤器
   @Override  
   protected Filter[] getServletFilters() {  
       CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();  
       characterEncodingFilter.setEncoding("UTF-8");  
       characterEncodingFilter.setForceEncoding(true);  
       return new Filter[] {characterEncodingFilter};  
   }   

}
