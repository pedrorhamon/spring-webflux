package com.starking.webflux.util;

import com.starking.webflux.domain.Animes;

/**
 * @author pedroRhamon
 */
public class AnimeCreator {
	
	public static Animes createAnimeTobeSaved() {
		return Animes.builder()
				.name("Demon Slayer")
				.build();
	}
	public static Animes createValidAnime() {
		return Animes.builder()
				.id(1)
				.name("Demon Slayer")
				.build();
	}
	public static Animes createValidUpdateAnime() {
		return Animes.builder()
				.id(1)
				.name("Bleach")
				.build();
	}

}
