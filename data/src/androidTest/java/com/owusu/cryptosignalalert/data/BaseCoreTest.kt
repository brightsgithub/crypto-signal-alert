package com.owusu.cryptosignalalert.data

import android.content.Context
import android.util.Log
import androidx.annotation.CallSuper
import androidx.test.InstrumentationRegistry
import androidx.test.InstrumentationRegistry.getInstrumentation
import com.owusu.testutils.FakeServer
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.mockwebserver.MockResponse

abstract class BaseCoreTest {


    private lateinit var fakeServer: FakeServer
    protected lateinit var context: Context
    protected val OK = 200

    @CallSuper
    open fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext
        initLoggingInterceptor()
        fakeServer = FakeServer(getInstrumentation().context)
        fakeServer.initServer()
    }

    private fun initLoggingInterceptor() {
        val logging = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { message ->
            Log.v("OKHTTP_TEST_LOGS", message)
        })
        logging.level = HttpLoggingInterceptor.Level.BASIC

        // ICNetwork.init(context as Application, interceptors = listOf(logging))
    }

    @CallSuper
    open fun tearDown() {
        fakeServer.tearDown()
    }

    fun getMockedResponse(statusCode: Int, res: Int): MockResponse {
        val json = fakeServer.getMockedJson(res)
        return fakeServer.getMockedResponse(statusCode, json)
    }

    fun initDispatcher(responseMap: Map<String, MockResponse>) {
        fakeServer.initDispatcher(responseMap)
    }

    fun getString(resId: Int): String {
        return context.getString(resId)
    }
}