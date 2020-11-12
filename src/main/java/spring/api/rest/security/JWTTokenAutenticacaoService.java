package spring.api.rest.security;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.spring.api.rest.ApplicationContextLoad;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import spring.api.rest.entitys.Usuarios;
import spring.api.rest.repositorys.UsuariosRepository;

@Service
public class JWTTokenAutenticacaoService {
	
	@Autowired
	private UsuariosRepository usuarioRepository;
	
	//tempo de validação token por 2 dias
	private static final long EXPIRATION_TIME = 172800000;
	
	//Uma senha unica para compor a autenticacao
	private static final String SECRET = "SenhaSecreta";
	
	/*Prefixo padrao de token*/
	private static final String TOKEN_PREFIX = "Bearer";
	
	/**/
	private static final String HEADER_STRING = "Authorization";
	
	
	//==================================MÉTODOS================================
	//Gerando token de autenticado e adicionando ao cabeçalho e resposta Http
	public void addAuthentication(HttpServletResponse response, String username) throws Exception{
		
		//montagem do token
		String JWT = Jwts.builder() //chama o gerador de token
				.setSubject(username) //adiciona o usuario
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) //tempo de expiração
				.signWith(SignatureAlgorithm.HS512, SECRET).compact();//compactação e agolritmo de geração de senha
		
		//Junta o token com o prefixo
		String token = TOKEN_PREFIX +" "+JWT; //o resultado tipo isso Bearer 3287284327918219www909w	
		
		//cabeçalho de resposta
		response.addHeader(HEADER_STRING, token);//Authorization: Bearer 3287284327918219www909w é o token de resposta no vcabeçalho
		
		/*escreve token como resposta no corpo http*/
		response.getWriter().write("{\"Authorization\": \""+token+"\"}");
	}
	
	/*Retorna o usuário validado com token ou caso não seja válido retorna null*/
	public Authentication getAuthentication(HttpServletRequest request) {
		
		/*pega o otken enviado no cabeçalho http*/
		String token = request.getHeader(HEADER_STRING);
		
		if(token != null) {
			
			//Faz a validação do token do usuario na requisicao
			String user = Jwts.parser().setSigningKey(SECRET) //senha secreta pra descompactar o token
					.parseClaimsJws(token.replace(TOKEN_PREFIX, "")) //tirar o prefixo do token e fica so a numeração
					.getBody().getSubject(); //Retorna o usuario
			if(user != null) {
				//Usuarios usuarios = usuarioRepository.findUserByLogin(user);
				//carrega todos os controlles, repositorios e tudo do contexto
				Usuarios usuario = ApplicationContextLoad.getApplicationContext()
						.getBean(UsuariosRepository.class).findUserByLogin(user);
				
				/*retornar o usuario logado*/
				if(usuario != null) {
					//classe para trablhar com token, para retornar um usuario para validação de token
					return new UsernamePasswordAuthenticationToken(
							usuario.getLogin(), 
							usuario.getSenha(), 
							usuario.getAuthorities());		
				}
			}
			
		}
			return null; //não autorizado

	}
}
