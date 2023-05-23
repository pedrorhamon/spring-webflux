package com.starking.webflux.service;

import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.starking.webflux.domain.Animes;
import com.starking.webflux.repositories.AnimeRepository;
import com.starking.webflux.services.AnimeService;
import com.starking.webflux.util.AnimeCreator;

import lombok.extern.slf4j.Slf4j;
import reactor.blockhound.BlockHound;
import reactor.blockhound.BlockingOperationError;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

/**
 * @author pedroRhamon
 */
@Slf4j
@ExtendWith(SpringExtension.class)
public class AnimeServiceTest {

	@InjectMocks
	private AnimeService animeService;

	@Mock
	private AnimeRepository animeRepository;

	private final Animes anime = AnimeCreator.createValidAnime();

	@BeforeAll
	public static void blockHoundSetup() {
		BlockHound.install();
	}

	@Test
	public void blockHoundWorks() {
		try {
			FutureTask<?> task = new FutureTask<>(() -> {
				Thread.sleep(0);
				return "";
			});
			Schedulers.parallel().schedule(task);

			task.get(10, TimeUnit.SECONDS);
			Assertions.fail("should fail");
		} catch (Exception e) {
			Assertions.assertTrue(e.getCause() instanceof BlockingOperationError);
		}
	}
	
	@Test
    public void monoSubscriber() {
        String name = "Pedro Rhamon";
        Mono<String> mono = Mono.just(name)
            .log();

        mono.subscribe();
        log.info("--------------------------");
        StepVerifier.create(mono)
            .expectNext(name)
            .verifyComplete();
    }

    @Test
    public void monoSubscriberConsumer() {
        String name = "Pedro Rhamon";
        Mono<String> mono = Mono.just(name)
            .log();

        mono.subscribe(s -> log.info("Value {}", s));
        log.info("--------------------------");

        StepVerifier.create(mono)
            .expectNext(name)
            .verifyComplete();
    }

    @Test
    public void monoSubscriberConsumerError() {
        String name = "Pedro Rhamon";
        Mono<String> mono = Mono.just(name)
            .map(s -> {
                throw new RuntimeException("Testing mono with error");
            });

        mono.subscribe(s -> log.info("Name {}", s), s -> log.error("Something bad happened"));
        mono.subscribe(s -> log.info("Name {}", s), Throwable::printStackTrace);

        log.info("--------------------------");

        StepVerifier.create(mono)
            .expectError(RuntimeException.class)
            .verify();
    }

    @Test
    public void monoSubscriberConsumerComplete() {
        String name = "Pedro Rhamon";
        Mono<String> mono = Mono.just(name)
            .log()
            .map(String::toUpperCase);

        mono.subscribe(s -> log.info("Value {}", s),
            Throwable::printStackTrace,
            () -> log.info("FINISHED!"));

        log.info("--------------------------");

        StepVerifier.create(mono)
            .expectNext(name.toUpperCase())
            .verifyComplete();
    }

    @Test
    public void monoSubscriberConsumerSubscription() {
        String name = "Pedro Rhamon";
        Mono<String> mono = Mono.just(name)
            .log()
            .map(String::toUpperCase);

        mono.subscribe(s -> log.info("Value {}", s),
            Throwable::printStackTrace,
            () -> log.info("FINISHED!")
            , subscription -> subscription.request(5));

        log.info("--------------------------");

        StepVerifier.create(mono)
            .expectNext(name.toUpperCase())
            .verifyComplete();
    }

    @Test
    public void monoDoOnMethods() {
        String name = "Pedro Rhamon";
        Mono<Object> mono = Mono.just(name)
            .log()
            .map(String::toUpperCase)
            .doOnSubscribe(subscription -> log.info("Subscribed"))
            .doOnRequest(longNumber -> log.info("Request Received, starting doing something..."))
            .doOnNext(s -> log.info("Value is here. Executing doOnNext {}", s))
            .flatMap(s -> Mono.empty())
            .doOnNext(s -> log.info("Value is here. Executing doOnNext {}", s)) //will not be executed
            .doOnSuccess(s -> log.info("doOnSuccess executed {}", s));

        mono.subscribe(s -> log.info("Value {}", s),
            Throwable::printStackTrace,
            () -> log.info("FINISHED!"));

    }

    @Test
    public void monoDoOnError() {
        Mono<Object> error = Mono.error(new IllegalArgumentException("Illegal argument exception"))
            .doOnError(e -> AnimeServiceTest.log.error("Error message: {}", e.getMessage()))
            .doOnNext(s -> log.info("Executing this doOnNext"))
            .log();

        StepVerifier.create(error)
            .expectError(IllegalArgumentException.class)
            .verify();
    }

    @Test
    public void monoOnErrorResume() {
        String name = "Pedro Rhamon";
        Mono<Object> error = Mono.error(new IllegalArgumentException("Illegal argument exception"))
            .onErrorResume(s -> {
                log.info("Inside On Error Resume");
                return Mono.just(name);
            })
            .doOnError(e -> AnimeServiceTest.log.error("Error message: {}", e.getMessage()))
            .log();

        StepVerifier.create(error)
            .expectNext(name)
            .verifyComplete();
    }

    @Test
    public void monoOnErrorReturn() {
        String name = "Pedro Rhamon";
        Mono<Object> error = Mono.error(new IllegalArgumentException("Illegal argument exception"))
            .onErrorReturn("EMPTY")
            .onErrorResume(s -> {
                log.info("Inside On Error Resume");
                return Mono.just(name);
            })
            .doOnError(e -> AnimeServiceTest.log.error("Error message: {}", e.getMessage()))
            .log();

        StepVerifier.create(error)
            .expectNext("EMPTY")
            .verifyComplete();
    }
}

