package com.owusu.cryptosignalalert.data

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner

/**
 * Test Runner created so it references our TestApplication which in turns sets up our test dagger dependencies.
 */
class MyTestRunner : AndroidJUnitRunner() {


    @Throws(InstantiationException::class, IllegalAccessException::class, ClassNotFoundException::class)
    override fun newApplication(cl: ClassLoader, className: String, context: Context): Application {
        return super.newApplication(cl, TestApplication::class.java.name, context)
    }

}