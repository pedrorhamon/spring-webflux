package com.starking.webflux.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.starking.webflux.domain.Animes;
import com.starking.webflux.services.AnimeService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
@RestController
@RequestMapping("animes")
@Slf4j
public class AnimeController {

	private final AnimeService AnimeService;
	
	@GetMapping
	public Flux<Animes> listAll() {
		return this.AnimeService.findAll();
	}
}
