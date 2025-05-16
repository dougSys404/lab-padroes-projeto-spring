package br.com.dougsys404.gof.repository;

import br.com.dougsys404.gof.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}
