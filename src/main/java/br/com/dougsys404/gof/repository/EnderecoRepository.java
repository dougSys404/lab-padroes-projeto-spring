package br.com.dougsys404.gof.repository;

import br.com.dougsys404.gof.model.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnderecoRepository extends JpaRepository<Endereco, String> {
}
