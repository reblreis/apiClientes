package br.com.cotiinformatica.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.cotiinformatica.dtos.ClientePostRequestDto;
import br.com.cotiinformatica.dtos.ClientePutRequestDto;
import br.com.cotiinformatica.entities.Cliente;
import br.com.cotiinformatica.entities.Plano;
import br.com.cotiinformatica.repositories.ClienteRepository;
import br.com.cotiinformatica.repositories.PlanoRepository;

@RestController
@RequestMapping(value = "/api/clientes")
public class ClienteController {

	@PostMapping()
	public String post(@RequestBody ClientePostRequestDto dto) {

		try {

			// preenchendo os dados do cliente
			Cliente cliente = new Cliente();
			cliente.setId(UUID.randomUUID());
			cliente.setNome(dto.getNome());
			cliente.setEmail(dto.getEmail());
			cliente.setTelefone(dto.getTelefone());

			// consultar o plano no banco de dados através do ID
			PlanoRepository planoRepository = new PlanoRepository();
			Plano plano = planoRepository.findById(dto.getPlanoId());

			// verificando se o plano não foi encontrado
			if (plano == null)
				throw new Exception("Plano não encontrado. Verifique o ID informado.");

			// associar o plano ao cliente
			cliente.setPlano(plano);

			// cadastrar o cliente no banco de dados
			ClienteRepository clienteRepository = new ClienteRepository();
			clienteRepository.insert(cliente);

			return "Cliente cadastrado com sucesso.";
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	@PutMapping()
	public String put(@RequestBody ClientePutRequestDto dto) {

		try {

			// consultar o cliente no banco de dados através do ID
			ClienteRepository clienteRepository = new ClienteRepository();
			Cliente cliente = clienteRepository.findById(dto.getId());

			// verificar se o cliente não foi encontrado
			if (cliente == null)
				throw new Exception("Cliente não encontrado. Verifique o ID informado.");

			// consultar o plano no banco de dados através do ID
			PlanoRepository planoRepository = new PlanoRepository();
			Plano plano = planoRepository.findById(dto.getPlanoId());

			// verificar se o plano não foi encontrado
			if (plano == null)
				throw new Exception("Plano não encontrado. Verifique o ID informado.");

			// modificando os dados do cliente
			cliente.setNome(dto.getNome());
			cliente.setEmail(dto.getEmail());
			cliente.setTelefone(dto.getTelefone());
			cliente.setPlano(plano);

			// atualizar o cliente no banco de dados
			clienteRepository.update(cliente);

			return "Cliente atualizado com sucesso.";
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	@DeleteMapping("{id}")
	public String delete(@PathVariable("id") UUID id) {

		try {

			// consultar os dados do cliente através do ID
			ClienteRepository clienteRepository = new ClienteRepository();
			Cliente cliente = clienteRepository.findById(id);

			// verificando se o cliente não foi encontrado
			if (cliente == null)
				throw new Exception("Cliente não encontrado. Verifique o ID informado.");

			// excluindo o cliente
			clienteRepository.delete(cliente);

			return "Cliente excluído com sucesso.";
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	@GetMapping()
	public List<Cliente> getAll() throws Exception {

		ClienteRepository clienteRepository = new ClienteRepository();
		return clienteRepository.findAll();
	}

	@GetMapping("{id}")
	public Cliente getById(@PathVariable("id") UUID id) throws Exception {

		ClienteRepository clienteRepository = new ClienteRepository();
		return clienteRepository.findById(id);
	}
}