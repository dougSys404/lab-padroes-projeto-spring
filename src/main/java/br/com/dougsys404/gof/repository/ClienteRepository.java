package br.com.dougsys404.gof.repository;

import br.com.dougsys404.gof.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByNomeIgnoreCase(String nome);
}
