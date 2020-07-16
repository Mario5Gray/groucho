package com.example.groucho1

import org.springframework.core.ParameterizedTypeReference
import org.springframework.messaging.rsocket.RSocketRequester
import org.springframework.messaging.rsocket.retrieveMono
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

open class PersistenceClient<T : Any>(private val prefix: String,
                                         private val requester: RSocketRequester,
                                         private val ref: ParameterizedTypeReference<T>) : Persistence<T> {

    override fun add(ent: T): Mono<Void> = requester
            .route("${prefix}add")
            .data(ent)
            .retrieveMono()

    override fun rem(ent: T): Mono<Void> = requester
            .route("${prefix}rem")
            .data(ent)
            .retrieveMono()

    override fun get(ent: T): Mono<T> = requester
            .route("${prefix}get")
            .data(ent)
            .retrieveMono(ref)

    override fun all(): Flux<T> = requester
            .route("${prefix}all")
            .retrieveFlux(ref)
}