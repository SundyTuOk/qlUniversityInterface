package com.sf.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component("springContextUtil")
public class SpringContextUtil implements ApplicationContextAware{
	
	private static ApplicationContext context = null;
	
	private SpringContextUtil(){}

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		context = applicationContext;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getBean(String beanName){
		return (T) context.getBean(beanName);
	}
	
	public static <T> T getBean(Class<T> clazz){
		return (T) context.getBean(clazz);
	}
}
