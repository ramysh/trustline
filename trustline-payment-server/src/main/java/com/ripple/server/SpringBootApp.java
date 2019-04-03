/* 
 * Author ::. Sivateja Kandula | www.java4s.com 
 *
 */

package com.ripple.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.ripple")
public class SpringBootApp {
	public static void main(String[] args) {

		 SpringApplication.run(SpringBootApp.class, args);

	}
}