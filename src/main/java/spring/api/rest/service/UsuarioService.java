package spring.api.rest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import spring.api.rest.entitys.Usuarios;
import spring.api.rest.repositorys.UsuariosRepository;

@Service
public class UsuarioService implements UserDetailsService{
	
	@Autowired
	private UsuariosRepository usuarioRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		/*Consultar no banco por usuário*/
		Usuarios usuario = usuarioRepository.findUserByLogin(username);
		if(usuario == null) {
			throw new UsernameNotFoundException("Usuário não encontrado");
		}else {
			return new User(usuario.getLogin(), usuario.getPassword(), usuario.getAuthorities());
		}
	}

}
