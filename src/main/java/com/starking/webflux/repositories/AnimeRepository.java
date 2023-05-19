package com.starking.webflux.repositories;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.starking.webflux.domain.Animes;

import reactor.core.publisher.Mono;

public interface AnimeRepository extends ReactiveCrudRepository<Animes, Integer>{
	
	Mono<Animes> findById(Integer id);
}
