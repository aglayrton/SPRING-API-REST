package spring.api.rest.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import spring.api.rest.entitys.Usuarios;
import spring.api.rest.repositorys.UsuariosRepository;

//somente esse domínio  tem acesso a API, para todos é * @CrossOrigin(origins = "*") 
@RestController /*Arquitetura REST*/
@RequestMapping(value = "/usuario")
public class IndexController {
	
	
	@Autowired
	private UsuariosRepository repository;
	
	/*Serviço RESTful*/
	@GetMapping("/")
	public ResponseEntity<?> listar(){
		List<Usuarios> usuarios = repository.findAll();
		return ResponseEntity.ok(usuarios);
	}
	
	@CrossOrigin(origins = "java.com.br, cliente1.com.br, localhost:8080") //permissão de acesso a api nas endpoint(método)
	@GetMapping("/{id}")
	public ResponseEntity<?> listarPorId(@PathVariable("id") Long id){
		Usuarios usuarios = repository.findById(id).get();
		return ResponseEntity.ok(usuarios);
	}
	
	//A anotação pega o json e converte no objeto
	@PostMapping("/")
	public ResponseEntity<?> salvar(@RequestBody Usuarios usuario){
		for(int key = 0; key < usuario.getTelefones().size(); key++){
			usuario.getTelefones().get(key).setUsuario(usuario);
		}
		Usuarios usuarioSalvo = repository.save(usuario);
		return ResponseEntity.ok().body(usuarioSalvo);
	}
	
	@PutMapping("/")
	public ResponseEntity<?> atualizar(@RequestBody Usuarios usuario){
		for(int key = 0; key < usuario.getTelefones().size(); key++){
			usuario.getTelefones().get(key).setUsuario(usuario);
		}
		Usuarios usuarioSalvo = repository.save(usuario);
		return ResponseEntity.ok().body(usuarioSalvo);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") Long id){
		repository.deleteById(id);
		return ResponseEntity.ok("Deletado com sucesso");
	}
	
}
