package com.mironov.coursework.util

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.ok
import com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching

class MockChannels(private val wireMockServer: WireMockServer) {
    companion object {
        private val subscribedUrlPattern = urlPathMatching(".+/subscriptions")
        private val topicUrlPattern = urlPathMatching(".+/topics")

        fun WireMockServer.channels(block: MockChannels.() -> Unit) {
            MockChannels(this).apply(block)
        }
    }

    private val subscribedMatcher = WireMock.get(subscribedUrlPattern)
    private val topicMatcher = WireMock.get(topicUrlPattern)

    fun withChannels() {
        wireMockServer.stubFor(subscribedMatcher.willReturn(ok(fromAssets("streams.json"))))
        wireMockServer.stubFor(topicMatcher.willReturn(ok(fromAssets("topics.json"))))
    }
}