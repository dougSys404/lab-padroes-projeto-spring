package br.com.dougsys404.gof.service;

import br.com.dougsys404.gof.model.Cliente;
import br.com.dougsys404.gof.model.Endereco;
import br.com.dougsys404.gof.repository.ClienteRepository;
import br.com.dougsys404.gof.repository.EnderecoRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClienteService {

    private final ClienteRepository repository;
    private final EnderecoRepository enderecoRepository;
    private final ViaCepService viaCepService;

    private ClienteService(ClienteRepository repository, EnderecoRepository enderecoRepository, ViaCepService viaCepService){
        this.repository = repository;
        this.enderecoRepository = enderecoRepository;
        this.viaCepService = viaCepService;
    }

    public Iterable<Cliente> findAll(){
        return repository.findAll();
    }


    public Cliente findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
    }


    public void create(Cliente cliente) { salvarClienteComCep(cliente); }

    private void salvarClienteComCep(Cliente cliente) {
        // FIXME Verificar se o Endereco do Cliente já existe (pelo CEP).
        String cep = cliente.getEndereco().getCep();
        Endereco endereco = enderecoRepository.findById(cep).orElseGet(() -> {
            // FIXME Caso não exista, integrar com o ViaCEP e persistir o retorno.
            Endereco novoEndereco = viaCepService.consultarCep(cep);
            return enderecoRepository.save(novoEndereco);
        });
        // FIXME Inserir Cliente, vinculando o Endereco (novo ou existente).
        cliente.setEndereco(endereco);
        repository.save(cliente);
    }


    public void update(Long id, Cliente cliente) {
        // FIXME Buscar Cliente por ID, caso exista:
        Optional<Cliente> clienteToUpdate = repository.findById(id);

        if (clienteToUpdate.isPresent()) salvarClienteComCep(cliente);
    }


    public void delete(Long id){
            // FIXME Deletar Cliente por ID.
        repository.deleteById(id);
    }
}
