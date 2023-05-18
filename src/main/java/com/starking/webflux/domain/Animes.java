package com.starking.webflux.domain;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@With
@Table("animes")
public class Animes {
	
	@Id
	@NotNull
	private Integer id;
	
	@NotEmpty(message = "The name of this anime cannot be empty")
	@NotNull
	private String name;
}
