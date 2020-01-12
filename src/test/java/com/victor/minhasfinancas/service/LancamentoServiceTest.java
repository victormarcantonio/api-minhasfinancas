package com.victor.minhasfinancas.service;








import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Example;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.victor.minhasfinancas.exception.RegraNegocioException;
import com.victor.minhasfinancas.model.entity.Lancamento;
import com.victor.minhasfinancas.model.entity.Usuario;
import com.victor.minhasfinancas.model.enums.StatusLancamento;
import com.victor.minhasfinancas.model.repository.LancamentoRepository;
import com.victor.minhasfinancas.model.repository.LancamentoRepositoryTest;
import com.victor.minhasfinancas.service.impl.LancamentoServiceImpl;



@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class LancamentoServiceTest {

	@SpyBean
	LancamentoServiceImpl service;
	@MockBean
	LancamentoRepository repository;
	
	@Test
	public void deveSalvarUmLancamento() {
		//cenario
		Lancamento lancamentoSalvar = LancamentoRepositoryTest.criarLancamento(); 
		Mockito.doNothing().when(service).validar(lancamentoSalvar);
		
		 Lancamento lancamentoSalvo = LancamentoRepositoryTest.criarLancamento();
		 lancamentoSalvo.setId(1l);
		 lancamentoSalvo.setStatus(StatusLancamento.PENDENTE);
		Mockito.when(repository.save(lancamentoSalvar)).thenReturn(lancamentoSalvo);
		
		//execucao
		Lancamento lancamento = service.salvar(lancamentoSalvar);
		
		//verificacao
		Assertions.assertThat(lancamento.getId()).isEqualTo(lancamentoSalvo.getId());
		Assertions.assertThat(lancamento.getStatus()).isEqualTo(StatusLancamento.PENDENTE);
	}
	
	@Test
	public void naoDeveSalvarUmLancamentoQuandoHouverErroDeValidacao(){
		//cenario
		Lancamento lancamentoSalvar = LancamentoRepositoryTest.criarLancamento(); 
		Mockito.doThrow(RegraNegocioException.class).when(service).validar(lancamentoSalvar);
		
		//execucao e verificacao
		Assertions.catchThrowableOfType(() -> service.salvar(lancamentoSalvar), RegraNegocioException.class);
		Mockito.verify(repository, Mockito.never()).save(lancamentoSalvar);
		
	}
	
	@Test
	public void deveAtualizarUmLancamento() {
		//cenario
		 Lancamento lancamentoSalvo = LancamentoRepositoryTest.criarLancamento();
		 lancamentoSalvo.setId(1l);
		 lancamentoSalvo.setStatus(StatusLancamento.PENDENTE);
		 
		Mockito.doNothing().when(service).validar(lancamentoSalvo);
		
		
		Mockito.when(repository.save(lancamentoSalvo)).thenReturn(lancamentoSalvo);
		
		//execucao
		 service.atualizar(lancamentoSalvo);
		
		//verificacao
	
		Mockito.verify(repository, Mockito.times(1)).save(lancamentoSalvo);
	}
	
	@Test
	public void DeveLancarUmErroAoTentarSalvarUmLancamentoQueNaoFoiSalvo(){
		//cenario
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento(); 
		
		
		//execucao e verificacao
		Assertions.catchThrowableOfType(() -> service.atualizar(lancamento), NullPointerException.class);
		Mockito.verify(repository, Mockito.never()).save(lancamento);
		
	}
	
	@Test
	public void deveDeletarUmLancamento() {
		//cenario
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento(); 
		lancamento.setId(1l);
		
		//execucao
		Assertions.catchThrowableOfType(() -> service.deletar(lancamento), NullPointerException.class);
		
		//verificacao
		Mockito.verify(repository).delete(lancamento);
	}
	
	@Test
	public void deveFiltrarUmLancamento() {
		//cenario
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento(); 
		lancamento.setId(1l);
	
		List<Lancamento> lista = java.util.Arrays.asList(lancamento);
		Mockito.when(repository.findAll(Mockito.any(Example.class))) .thenReturn(lista);
		
		//execucao
		List<Lancamento> resultado = service.buscar(lancamento);
		
		//verificacao
		Assertions.assertThat(resultado).isNotEmpty().hasSize(1).contains(lancamento);
		
		
	}
	
	@Test
	public void deveAtualizaroStatusDeUmLancamento() {
		
		//cenario
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento(); 
		lancamento.setId(1l);
		lancamento.setStatus(StatusLancamento.PENDENTE);
		
		StatusLancamento novoStatus = StatusLancamento.EFETIVADO;
		
		Mockito.doReturn(lancamento).when(service).atualizar(lancamento);
		
		//execucao
		service.atualizarStatus(lancamento, novoStatus);
		
		//verificacoes
		Assertions.assertThat(lancamento.getStatus()).isEqualTo(novoStatus);
		Mockito.verify(service).atualizar(lancamento);
		
		
	}
	
	@Test
	public void deveObterUmLancamentoPorId() {
		//cenario
		Long id =1l;
		
		Lancamento lancamento  = LancamentoRepositoryTest.criarLancamento();
		lancamento.setId(id);
		
		Mockito.when(repository.findById(id)).thenReturn(Optional.of(lancamento));
		
		//execucao
		Optional<Lancamento> resultado = service.obterPorId(id);
		
		//verificacao
		Assertions.assertThat(resultado.isPresent()).isTrue();
			
		
	}
	
	@Test
	public void deveRetornarVazioQuandoLancamentoNaoExiste() {
		//cenario
		Long id =1l;
		
		Lancamento lancamento  = LancamentoRepositoryTest.criarLancamento();
		lancamento.setId(id);
		
		Mockito.when(repository.findById(id)).thenReturn(Optional.empty());
		
		//execucao
		Optional<Lancamento> resultado = service.obterPorId(id);
		
		//verificacao
		Assertions.assertThat(resultado.isPresent()).isFalse();
			
		
	}
	
	@Test
	public void deveValidarUmLancamento() {
		Lancamento lancamento = new Lancamento();
		
		Throwable erro = Assertions.catchThrowable(()-> service.validar(lancamento));
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe uma descrição válida");
		
		lancamento.setDescricao("");
		
		erro = Assertions.catchThrowable(()-> service.validar(lancamento));
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe uma descrição válida");
		
		lancamento.setDescricao("Salário");
		
		erro = Assertions.catchThrowable(()-> service.validar(lancamento));
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um mês válido");
		
		lancamento.setMes(13);
		
		erro = Assertions.catchThrowable(()-> service.validar(lancamento));
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um mês válido");
		
		lancamento.setMes(1);
		
		erro = Assertions.catchThrowable(()-> service.validar(lancamento));
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um ano válido");
		
		lancamento.setAno(202);
		
		erro = Assertions.catchThrowable(()-> service.validar(lancamento));
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um ano válido");
		
		
		lancamento.setAno(2019);
		
		erro = Assertions.catchThrowable(()-> service.validar(lancamento));
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um usuário");
		
		lancamento.setUsuario(new Usuario());
		
		erro = Assertions.catchThrowable(()-> service.validar(lancamento));
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um usuário");
		
		lancamento.getUsuario().setId(1l);
		
		erro = Assertions.catchThrowable(()-> service.validar(lancamento));
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Digite um valor válido");
		
		lancamento.setValor(BigDecimal.ZERO);
		
		erro = Assertions.catchThrowable(()-> service.validar(lancamento));
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Digite um valor válido");
		
		lancamento.setValor(BigDecimal.valueOf(500));
		
		erro = Assertions.catchThrowable(()-> service.validar(lancamento));
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um tipo de lançamento");
		
		
		
		
		
	}
	
	
}
