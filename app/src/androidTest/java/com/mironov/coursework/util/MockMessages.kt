package com.mironov.coursework.util

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock

class MockMessages(private val wireMockServer: WireMockServer) {

    companion object {
        val messagesUrlPattern = WireMock.urlPathMatching("/messages?.+")

        fun WireMockServer.messages(block: MockMessages.() -> Unit) {
            MockMessages(this).apply(block)
        }
    }

    private val getMessagesMatcher = WireMock.get(messagesUrlPattern)
    private val postMessagesMatcher = WireMock.post(messagesUrlPattern)

    fun withGetMessages() {
        wireMockServer.stubFor(getMessagesMatcher.willReturn(WireMock.ok(fromAssets("messages.json"))))
    }

    fun withPostMessages() {
        wireMockServer.stubFor(postMessagesMatcher.willReturn(WireMock.ok()))
    }
}