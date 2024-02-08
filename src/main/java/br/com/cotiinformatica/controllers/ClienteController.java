package br.com.cotiinformatica.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
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
import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/api/clientes")
public class ClienteController {

	@PostMapping()
	public ResponseEntity<String> post(@RequestBody @Valid ClientePostRequestDto dto) {

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
				// HTTP 400 - BAD REQUEST
				return ResponseEntity.status(400).body("Plano não encontrado. Verifique o ID informado.");

			// associar o plano ao cliente
			cliente.setPlano(plano);

			// cadastrar o cliente no banco de dados
			ClienteRepository clienteRepository = new ClienteRepository();
			clienteRepository.insert(cliente);

			// HTTP 201 - CREATED
			return ResponseEntity.status(201).body("Cliente cadastrado com sucesso.");
		} catch (Exception e) {

			// HTTP 500 - INTERNAL SERVER ERROR
			return ResponseEntity.status(500).body(e.getMessage());
		}
	}

	@PutMapping()
	public ResponseEntity<String> put(@RequestBody @Valid ClientePutRequestDto dto) {

		try {

			// consultar o cliente no banco de dados através do ID
			ClienteRepository clienteRepository = new ClienteRepository();
			Cliente cliente = clienteRepository.findById(dto.getId());

			// verificar se o cliente não foi encontrado
			if (cliente == null)
				return ResponseEntity.status(400) // HTTP 400 - BAD REQUEST
						.body("Cliente não encontrado. Verifique o ID informado.");

			// consultar o plano no banco de dados através do ID
			PlanoRepository planoRepository = new PlanoRepository();
			Plano plano = planoRepository.findById(dto.getPlanoId());

			// verificar se o plano não foi encontrado
			if (plano == null)
				return ResponseEntity.status(400) // HTTP 400 - BAD REQUEST
						.body("Plano não encontrado. Verifique o ID informado.");

			// modificando os dados do cliente
			cliente.setNome(dto.getNome());
			cliente.setEmail(dto.getEmail());
			cliente.setTelefone(dto.getTelefone());
			cliente.setPlano(plano);

			// atualizar o cliente no banco de dados
			clienteRepository.update(cliente);

			// HTTP 200 - OK
			return ResponseEntity.status(200).body("Cliente atualizado com sucesso.");
		} catch (Exception e) {

			// HTTP 500 - INTERNAL SERVER ERROR
			return ResponseEntity.status(500).body(e.getMessage());
		}
	}

	@DeleteMapping("{id}")
	public ResponseEntity<String> delete(@PathVariable("id") UUID id) {

		try {

			// consultar os dados do cliente através do ID
			ClienteRepository clienteRepository = new ClienteRepository();
			Cliente cliente = clienteRepository.findById(id);

			// verificando se o cliente não foi encontrado
			if (cliente == null)
				// HTTP 400 - BAD REQUEST
				return ResponseEntity.status(400).body("Cliente não encontrado. Verifique o ID informado.");

			// excluindo o cliente
			clienteRepository.delete(cliente);

			// HTTP 200 - OK
			return ResponseEntity.status(200).body("Cliente excluído com sucesso.");
		} catch (Exception e) {
			// HTTP 500 - INTERNAL SERVER ERROR
			return ResponseEntity.status(500).body(e.getMessage());
		}
	}

	@GetMapping()
	public ResponseEntity<List<Cliente>> getAll() throws Exception {

		try {

			ClienteRepository clienteRepository = new ClienteRepository();
			List<Cliente> clientes = clienteRepository.findAll();

			if (clientes.size() == 0) // se a lista está vazia
				// HTTP 204 - NO CONTENT
				return ResponseEntity.status(204).body(null);

			// HTTP 200 - OK
			return ResponseEntity.status(200).body(clientes);
		} catch (Exception e) {
			// HTTP 500 - INTERNAL SERVER ERROR
			return ResponseEntity.status(500).body(null);
		}
	}

	@GetMapping("{id}")
	public ResponseEntity<Cliente> getById(@PathVariable("id") UUID id) throws Exception {

		try {

			ClienteRepository clienteRepository = new ClienteRepository();
			Cliente cliente = clienteRepository.findById(id);

			if (cliente == null)
				// HTTP 204 - NO CONTENT
				return ResponseEntity.status(204).body(null);

			// HTTP 200 - OK
			return ResponseEntity.status(200).body(cliente);
		} catch (Exception e) {
			// HTTP 500 - INTERNAL SERVER ERROR
			return ResponseEntity.status(500).body(null);
		}
	}

}