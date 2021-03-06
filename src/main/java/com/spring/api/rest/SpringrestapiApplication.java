package com.spring.api.rest;



import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@SpringBootApplication
@EntityScan(basePackages = {"spring.api.rest.entitys"})
@ComponentScan(basePackages = {"spring.*"})
@EnableJpaRepositories(basePackages = {"spring.api.rest.repositorys"})
@EnableTransactionManagement
@EnableWebMvc
@RestController
@EnableAutoConfiguration
public class SpringrestapiApplication implements WebMvcConfigurer{

	public static void main(String[] args) {
		SpringApplication.run(SpringrestapiApplication.class, args);
		System.out.println(new BCryptPasswordEncoder().encode("123"));
	}
	
	/*reflete em todo sistema*/
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**"); //libera acesso a todos endpoints
		
		registry.addMapping("/usuario/**")
		.allowedMethods("*")///Libera o mapeamento e as ações
		.allowedOrigins("*"); //libera as requições somente para estes*/
	}

}
