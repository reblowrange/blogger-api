package com.blogger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.blogger.config.RSAKeyRecord;

@SpringBootApplication
@EnableConfigurationProperties(RSAKeyRecord.class)
public class BloggerApplication {

	public static void main(String[] args) {
		SpringApplication.run(BloggerApplication.class, args);
	}

}
