package com.starking.webflux.services;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.starking.webflux.domain.Animes;
import com.starking.webflux.repositories.AnimeRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AnimeService {

	private final AnimeRepository animeRepository;

	public Flux<Animes> findAll() {
		return this.animeRepository.findAll();
	}
	
	public Mono<Animes> findById(Integer id) {
		return this.animeRepository.findById(id)
				.switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Anime not found")));
	}
}
