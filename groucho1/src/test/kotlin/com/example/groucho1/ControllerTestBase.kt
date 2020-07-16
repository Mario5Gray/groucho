package com.example.groucho1

import io.rsocket.RSocket
import io.rsocket.core.RSocketServer
import io.rsocket.frame.decoder.PayloadDecoder
import io.rsocket.transport.netty.server.CloseableChannel
import io.rsocket.transport.netty.server.TcpServerTransport
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration
import org.springframework.boot.autoconfigure.rsocket.RSocketStrategiesAutoConfiguration
import org.springframework.context.annotation.Import
import org.springframework.context.support.GenericApplicationContext
import org.springframework.messaging.rsocket.RSocketRequester
import org.springframework.messaging.rsocket.RSocketStrategies
import org.springframework.messaging.rsocket.annotation.support.RSocketMessageHandler
import org.springframework.test.context.junit.jupiter.SpringExtension
import reactor.core.publisher.Hooks


fun <T> anyObject(): T {
    Mockito.anyObject<T>()
    return uninitialized()
}

fun <T> uninitialized(): T = null as T


@ExtendWith(SpringExtension::class)
@Import(JacksonAutoConfiguration::class,
        RSocketStrategiesAutoConfiguration::class)
open class ControllerTestBase {
    private lateinit var socket: RSocket

    lateinit var requestor: RSocketRequester

    private lateinit var server: CloseableChannel

    @BeforeEach
    fun setUp(context: GenericApplicationContext) {
        val strategies = context.getBean(RSocketStrategies::class.java)

        val messageHandler = RSocketMessageHandler().apply {
            this.rSocketStrategies = strategies
            this.afterPropertiesSet()
        }

        context.registerBean(RSocketMessageHandler::class.java, messageHandler)

        server = RSocketServer.create()
                .payloadDecoder(PayloadDecoder.ZERO_COPY)
                .acceptor(messageHandler.responder())
                .bind(TcpServerTransport.create("localhost", 0))
                .block()!!


        requestor = RSocketRequester
                .builder()
                .rsocketStrategies(strategies)
                .connectTcp("localhost", server.address().port)
                .block()!!

        socket = requestor.rsocket()


        Hooks.onOperatorDebug()
    }

    @AfterEach
    fun tearDown() {
        requestor.rsocket().dispose()
        server.dispose()
    }
}