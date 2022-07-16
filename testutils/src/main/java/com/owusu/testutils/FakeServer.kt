package com.owusu.testutils

import android.content.Context
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import java.io.BufferedReader
import java.util.concurrent.TimeUnit

/**
 * Created by Bright Owusu-Amankwaa on 2019-10-02.
 */
class FakeServer(private val context: Context) {

    private lateinit var mockWebServer: MockWebServer
    private val PORT = 8080
    private var delay: Long = 0

    fun initServer() {
        try {
            // Create a MockWebServer. These are lean enough that you can create a new
            // instance for every unit test.
            mockWebServer = MockWebServer()

            // Start the server.
            mockWebServer.start(PORT)
        } catch (e: Throwable) {
            throw RuntimeException(e)
        }
    }

    fun initDispatcher(responseMap: Map<String, MockResponse>) {
        mockWebServer.setDispatcher(FakeServerDispatcher(responseMap))
    }

    private inner class FakeServerDispatcher(private val responseMap: Map<String, MockResponse>) : Dispatcher() {

        override fun dispatch(recordedRequest: RecordedRequest): MockResponse {
            val url = recordedRequest.requestUrl.url().toString()
            return responseMap.getOrElse(url) {
                getMockedResponse(500, "some error")
            }
        }
    }

    fun tearDown() {
        try {
            mockWebServer.shutdown()
        } catch (e: Throwable) {
            e.printStackTrace()
            throw RuntimeException(e)
        }
    }

    fun setBodyDelayInSeconds(delay: Long) {
        this.delay = delay
    }

    // https://stackoverflow.com/questions/41000584/best-way-to-use-bufferedreader-in-kotlin
    fun getMockedJson(res: Int): String {
        val inputStream = context.resources.openRawResource(res)
        val allText = inputStream.bufferedReader().use(BufferedReader::readText)
        return allText
    }

    /**
     * Gets a MockedResponse based on the supplied json string.
     */
    fun getMockedResponse(statusCode: Int, json: String?): MockResponse {
        val response = MockResponse()
        if (json != null) {
            response.setBody(json)
        }
        response.setResponseCode(statusCode)
        response.setBodyDelay(delay, TimeUnit.SECONDS)
        return response
    }
}