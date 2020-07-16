package com.example.groucho1

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.core.ParameterizedTypeReference
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.rsocket.RSocketRequester
import org.springframework.stereotype.Component
import org.springframework.stereotype.Controller
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*

@SpringBootApplication
class Groucho1Application

fun main(args: Array<String>) {
    runApplication<Groucho1Application>(*args)
}

@Profile("service")
@Configuration
class PersistenceServiceConfiguration {
	@Controller
	@MessageMapping("quotes")
	class QuotePersistenceRSocket(t: QuotePersistence) : PersistenceController<Quote>(t)

}

@Profile("client")
@Configuration
class PersistenceClientConfiguration {
	class QuotePersistenceClient<T: Any>(requester: RSocketRequester) :
			PersistenceClient<T>("quotes.", requester, ParameterizedTypeReference.forType(Quote::class.java))

	@Bean
	fun quoteClient(requester: RSocketRequester.Builder): Persistence<Quote> = QuotePersistenceClient(
			requester
					.connectTcp("localhost", 6501)
					.block()!!)
}