package com.example.groucho1

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.stereotype.Controller
import org.springframework.test.context.junit.jupiter.SpringExtension
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.time.Instant
import java.util.*


@ExtendWith(SpringExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Import(PersistencControllerTests.TestConfiguration::class)
class PersistencControllerTests : ControllerTestBase() {
    @MockBean
    private lateinit var quotePersistence: QuotePersistence

    @Test
    fun `should add Quote`() {
        BDDMockito
                .given(quotePersistence.add(anyObject()))
                .willReturn(Mono.empty())

        StepVerifier
                .create(
                        requestor
                                .route("quotes.add")
                                .data(Mono.just(Quote(Date.from(Instant.now()), "Pigeons in time")))
                                .retrieveMono(Void::class.java)
                )
                .verifyComplete()
    }

    @org.springframework.boot.test.context.TestConfiguration
    class TestConfiguration {

        @Controller
        @MessageMapping("quotes")
        class QuotePersistenceController(t: QuotePersistence) : PersistenceController<Quote>(t)
    }
}