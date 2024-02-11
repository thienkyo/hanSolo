package com.hanSolo.kinhNguyen;

import com.hanSolo.kinhNguyen.filter.JwtFilter;
import com.hanSolo.kinhNguyen.filter.jwtFilterMgnt;
import com.hanSolo.kinhNguyen.filter.jwtFilterOpticShopMgnt;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAutoConfiguration
@SpringBootApplication
@EnableCaching
@EnableScheduling
public class KinhNguyenApplication {

	@Bean
	public FilterRegistrationBean jwtFilter() {
		final FilterRegistrationBean registrationBean = new FilterRegistrationBean();
		registrationBean.setFilter(new JwtFilter());
		registrationBean.addUrlPatterns("/authenticated/*");

		return registrationBean;
	}

	@Bean
	public FilterRegistrationBean jwtFilterMgnt() {
		final FilterRegistrationBean registrationBean = new FilterRegistrationBean();
		registrationBean.setFilter(new jwtFilterMgnt());
		registrationBean.addUrlPatterns("/mgnt/*");

		return registrationBean;
	}

	@Bean
	public FilterRegistrationBean jwtFilterOpticShopMgnt() {
		final FilterRegistrationBean registrationBean = new FilterRegistrationBean();
		registrationBean.setFilter(new jwtFilterOpticShopMgnt());
		registrationBean.addUrlPatterns("/Hmgnt/*");

		return registrationBean;
	}

	public static void main(String[] args) {
		SpringApplication.run(KinhNguyenApplication.class, args);
	}

}
