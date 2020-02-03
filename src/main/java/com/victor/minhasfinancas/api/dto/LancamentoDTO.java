package com.victor.minhasfinancas.api.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LancamentoDTO {

	private Long id;
	String descricao;
	Integer mes;
	Integer ano;
	BigDecimal valor;
	private Long usuario;
	private String tipo;
	private String status;
}
