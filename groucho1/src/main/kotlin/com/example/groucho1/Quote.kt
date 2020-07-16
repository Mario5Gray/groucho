package com.example.groucho1

import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*


data class Quote(val date: Date, val text: String)


@Component
class QuotePersistence : Persistence<Quote> {
    override fun add(ent: Quote): Mono<Void> {
        TODO("Not yet implemented")
    }

    override fun rem(ent: Quote): Mono<Void> {
        TODO("Not yet implemented")
    }

    override fun get(ent: Quote): Mono<Quote> {
        TODO("Not yet implemented")
    }

    override fun all(): Flux<Quote> {
        TODO("Not yet implemented")
    }
}