package com.generation.farmacia.model;





import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;




@Entity
@Table(name = "tb_categorias")
public class Categoria {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "O Atributo Descrição é obrigatório")
	@Size(min = 3, max = 100, message = "O Atributo Descrição deve conter no mínimo 3 e no máximo 100 caracteres")
	private String nome;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTipo() {
		return nome;
	}

	public void setTipo(String nome) {
		this.nome = nome;
	}
	
	
	
	
	}
