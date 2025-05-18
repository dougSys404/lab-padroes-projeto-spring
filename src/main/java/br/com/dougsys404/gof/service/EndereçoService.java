package br.com.dougsys404.gof.service;

import br.com.dougsys404.gof.model.Endereco;
import br.com.dougsys404.gof.repository.EnderecoRepository;
import org.springframework.web.client.RestClientException;

public class EndereçoService {

    private final EnderecoRepository repository;
    private final ViaCepService viaCepService;

    public EndereçoService(EnderecoRepository repository, ViaCepService viaCepService){
        this.repository = repository;
        this.viaCepService = viaCepService;
    }

    public Endereco findById(String cep){
        return repository.findById(cep).orElseThrow(() -> new RuntimeException("Endereço não encontrado"));
    }


    public Endereco save(Endereco endereco) {
        return repository.findById(endereco.getCep()).orElseGet(() -> {
            try {
                Endereco enderecoConsultado = viaCepService.consultarCep(endereco.getCep());

                if (enderecoConsultado == null) {
                    throw new RuntimeException("Endereço não encontrado para o CEP: " + endereco.getCep());
                }

                return repository.save(enderecoConsultado);

            } catch (RestClientException e) {
                throw new RuntimeException("Erro ao consultar o CEP: " + endereco.getCep(), e);
            }
        });
    }

    public void update(String cep, Endereco endereco){
        Endereco enderecoToUpdate = this.findById(cep);

        if (enderecoToUpdate.equals(endereco)){
            repository.save(endereco);
        } else {
            throw new RuntimeException("Endereço não encontrado");
        }
    }

    public void delete(String cep){
        repository.deleteById(cep);
    }

}
