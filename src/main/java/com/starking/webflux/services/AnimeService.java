package com.starking.webflux.services;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.starking.webflux.domain.Animes;
import com.starking.webflux.repositories.AnimeRepository;

import io.netty.util.internal.StringUtil;
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

	public Mono<Animes> save(Animes anime) {
		return this.animeRepository.save(anime);
	}
	
	public Mono<Void> update(Animes anime) {
		return this.animeRepository.findById(anime.getId())
				.flatMap(animeRepository::save)
				.then();
	}

	public Mono<Void> delete(Integer id) {
		return findById(id)
				.flatMap(animeRepository::delete);
	}

	@Transactional
	public Flux<Animes> saveBatch(List<Animes> animes) {
		return this.animeRepository.saveAll(animes)
				.doOnNext(this::thrownResponseStatusExceptionWhenEmptyName);
	}
	
	private void thrownResponseStatusExceptionWhenEmptyName(Animes anime) {
		if(StringUtil.isNullOrEmpty(anime.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid name");
		}
	}
}
