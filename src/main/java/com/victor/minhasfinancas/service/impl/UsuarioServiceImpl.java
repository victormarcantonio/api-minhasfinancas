package com.victor.minhasfinancas.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.victor.minhasfinancas.exception.ErroAutenticacao;
import com.victor.minhasfinancas.exception.RegraNegocioException;
import com.victor.minhasfinancas.model.entity.Usuario;
import com.victor.minhasfinancas.model.repository.UsuarioRepository;
import com.victor.minhasfinancas.service.UsuarioService;

@Service
public class UsuarioServiceImpl implements UsuarioService {

	@Autowired
	private UsuarioRepository repository;
	

	

	public UsuarioServiceImpl(UsuarioRepository repository) {
		super();
		this.repository = repository;
	}

	@Override
	public Usuario autenticar(String email, String senha) {
		Optional<Usuario> usuario =repository.findByEmail(email);
		
		if(!usuario.isPresent()) {
			throw new ErroAutenticacao("Usuário não encontrado para o e-mail informado");
		}
		if(!usuario.get().getSenha().contentEquals(senha)) {
			throw new ErroAutenticacao("Senha inválida");
		}
		return usuario.get();
		
	}

	@Override
	@Transactional
	public Usuario salvarUsuario(Usuario usuario) {
		validarEmail(usuario.getEmail());
		return repository.save(usuario);
		
	}

	@Override
	public void validarEmail(String email) {
		
		boolean existe = repository.existsByEmail(email);
		if(existe) {
			throw new RegraNegocioException("Já existe um usuário cadastrado com esse e-mail");
		}
	}

	
}
