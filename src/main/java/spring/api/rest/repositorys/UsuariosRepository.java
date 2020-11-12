package spring.api.rest.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import spring.api.rest.entitys.Usuarios;

@Repository
public interface UsuariosRepository extends JpaRepository<Usuarios, Long> {
	
	@Query("select u from Usuarios u where u.login =: login")
	Usuarios findUserByLogin(String login);
}
