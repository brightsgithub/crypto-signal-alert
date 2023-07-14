package com.owusu.cryptosignalalert.resource

import android.content.res.Resources

/**
 * It is not recommended to access string resources from your ViewModel. This is because the
 * ViewModel should be independent of the UI. It should not know anything about the specific views
 * that will be using it. This makes the ViewModel more reusable and easier to test.
 * If you need to access string resources from the ViewModel, you should do so through a dependency
 * injection mechanism. This means that you should create a class that provides access to the
 * string resources, and then inject that class into the ViewModel. This way, the ViewModel does not
 * have to know about the specific string resources, and it can be easily tested without a UI.
 */
class AppStringProvider(private val resources: Resources) {

    fun getString(resourceId: Int): String {
        return resources.getString(resourceId)
    }
}