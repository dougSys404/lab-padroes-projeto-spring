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


    public void create(Cliente cliente) {
        // Verifica se já existe um cliente com esse nome (ignorando maiúsculas/minúsculas)
        Optional<Cliente> clienteExistente = repository.findByNomeIgnoreCase(cliente.getNome());
        if (clienteExistente.isPresent()) {
            throw new RuntimeException("Cliente com o nome '" + cliente.getNome() + "' já está cadastrado.");
        }

        String cep = cliente.getEndereco().getCep();

        // Verifica se o endereço já existe no banco, senão busca via API
        Endereco endereco = enderecoRepository.findById(cep).orElseGet(() -> {
            Endereco enderecoConsultado = viaCepService.consultarCep(cep);
            if (enderecoConsultado == null) {
                throw new RuntimeException("Endereço não encontrado para o CEP: " + cep);
            }
            return enderecoRepository.save(enderecoConsultado);
        });

        cliente.setEndereco(endereco);
        repository.save(cliente);
    }



    public void update(Long id, Cliente cliente) {
        Cliente clienteExistente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente com ID " + id + " não encontrado."));

        String cep = cliente.getEndereco().getCep();

        Endereco endereco = enderecoRepository.findById(cep).orElseGet(() -> {
            Endereco enderecoConsultado = viaCepService.consultarCep(cep);
            if (enderecoConsultado == null) {
                throw new RuntimeException("Endereço não encontrado para o CEP: " + cep);
            }
            return enderecoRepository.save(enderecoConsultado);
        });

        // Atualiza os dados do cliente existente
        clienteExistente.setNome(cliente.getNome());
        clienteExistente.setEndereco(endereco);

        repository.save(clienteExistente);
    }


    public void delete(Long id){
        repository.deleteById(id);
    }
}
