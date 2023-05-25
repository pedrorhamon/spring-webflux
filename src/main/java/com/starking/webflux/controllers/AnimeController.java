package com.starking.webflux.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.starking.webflux.domain.Animes;
import com.starking.webflux.services.AnimeService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
@RequestMapping("animes")
@Slf4j
public class AnimeController {

	private final AnimeService animeService;
	
	@GetMapping
	public Flux<Animes> listAll() {
		return this.animeService.findAll();
	}
	
	@GetMapping(path = "/{id}")
	public Mono<Animes> findById(@PathVariable Integer id) {
		log.info("Está na lista de Animes");
		return this.animeService.findById(id);
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Mono<Animes> save(@Valid @RequestBody Animes anime) {
		return this.animeService.save(anime);
	}
	
	@PostMapping(path = "batch")
	@ResponseStatus(HttpStatus.CREATED)
	public Flux<Animes> saveBatch(@RequestBody List<Animes> anime) {
		return this.animeService.saveBatch(anime);
	}
	
	@PutMapping(path = "/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public Mono<Void> update(@PathVariable Integer id, @Valid @RequestBody Animes anime) {
		return this.animeService.update(anime);
	}
	
	@DeleteMapping(path = "/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public Mono<Void> delete(@PathVariable Integer id) {
		return this.animeService.delete(id);
	}
}
