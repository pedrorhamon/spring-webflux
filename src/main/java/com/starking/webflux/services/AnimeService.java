package com.starking.webflux.services;

import org.springframework.stereotype.Service;

import com.starking.webflux.domain.Animes;
import com.starking.webflux.repositories.AnimeRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class AnimeService {

	private final AnimeRepository animeRepository;

	public Flux<Animes> findAll() {
		return this.animeRepository.findAll();
	}
}