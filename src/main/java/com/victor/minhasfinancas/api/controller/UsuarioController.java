package com.victor.minhasfinancas.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.victor.minhasfinancas.api.dto.UsuarioDTO;
import com.victor.minhasfinancas.exception.ErroAutenticacao;
import com.victor.minhasfinancas.exception.RegraNegocioException;
import com.victor.minhasfinancas.model.entity.Usuario;
import com.victor.minhasfinancas.service.UsuarioService;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

	@Autowired
	private UsuarioService service;
	
	@PostMapping("/autenticar")
	public ResponseEntity autenticar(@RequestBody UsuarioDTO dto) {
		try {
		Usuario usuarioAutenticado = service.autenticar(dto.getEmail(),dto.getSenha());
		return ResponseEntity.ok(usuarioAutenticado);
		}catch (ErroAutenticacao e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@PostMapping
	public ResponseEntity salvar(@RequestBody UsuarioDTO dto) {
		
		Usuario usuario =Usuario.builder()
				.nome(dto.getNome())
				.email(dto.getEmail())
				.senha(dto.getSenha()).build();
		
		try {
		Usuario usuarioSalvo = service.salvarUsuario(usuario);
		return new ResponseEntity(usuarioSalvo,HttpStatus.CREATED); 
		}catch (RegraNegocioException e){
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}
