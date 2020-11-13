package spring.api.rest.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import spring.api.rest.service.UsuarioService;

@Configuration
@EnableWebSecurity
public class WebConfigSecurity extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private UsuarioService implatacao;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//ativa proteção contra usuario que nao estão validados por TOKEN
		http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
		
		//Ativação a restrição por url
		.disable().authorizeRequests().antMatchers("/", "/index").permitAll()
		
		/*URL DE LOGOUT*/
		.anyRequest().authenticated().and().logout().logoutSuccessUrl("/index")
		
		/*URL DE LOGOUT E INVALIDA O USUÁRIO*/
		.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
		
		//filtra a requisições de login para autenticação
		.and().addFilterBefore(new JWTLoginFilter("/login", authenticationManager()), 
				UsernamePasswordAuthenticationFilter.class)
		
		//filtra demais requisições para verificar a presença do token jwt no header http
		.addFilterBefore(new JWTApiAutenticacaoFilter(), UsernamePasswordAuthenticationFilter.class);
	}
	
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(implatacao).passwordEncoder(new BCryptPasswordEncoder());
	}
}
