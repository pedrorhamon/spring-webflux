package com.starking.webflux.repositories;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.starking.webflux.domain.Animes;

public interface AnimeRepository extends ReactiveCrudRepository<Animes, Integer>{

}
