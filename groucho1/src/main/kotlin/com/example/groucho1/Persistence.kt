package com.example.groucho1

import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono


interface Persistence<T> {
    fun add(ent: T): Mono<Void>
    fun rem(ent: T): Mono<Void>
    fun get(ent: T): Mono<out T>
    fun all(): Flux<out T>
}

open class PersistenceController<T>(private val that: Persistence<T>) : Persistence<T> {
    @MessageMapping("add")
    override fun add(ent: T): Mono<Void> = that.add(ent)

    @MessageMapping("rem")
    override fun rem(ent: T): Mono<Void> = that.rem(ent)

    @MessageMapping("get")
    override fun get(ent: T): Mono<out T> = that.get(ent)

    @MessageMapping("all")
    override fun all(): Flux<out T> = that.all()
}
