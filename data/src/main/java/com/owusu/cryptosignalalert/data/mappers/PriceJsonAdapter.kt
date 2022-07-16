package com.owusu.cryptosignalalert.data.mappers

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.owusu.cryptosignalalert.data.models.PriceAPI
import com.owusu.cryptosignalalert.data.models.PriceAPIWrapper
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.lang.reflect.Type

// The Ktor custom serializer has zero examples of how to use it. For now, we go old school.
class PriceJsonAdapter: JsonDeserializer<PriceAPIWrapper> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): PriceAPIWrapper {

        val listOfPrices = arrayListOf<PriceAPI>()
        val jsonObject: JsonObject = json.asJsonObject
        val keySet = jsonObject.keySet()

        for (coinId in keySet) {
            val priceJsonAsString = jsonObject.get(coinId).asJsonObject.toString()
            // Lets lean on the framework to map our object
            val priceApi = Json{ ignoreUnknownKeys = true }.decodeFromString<PriceAPI>(priceJsonAsString)
            listOfPrices.add(priceApi)
        }
        val wrapper = PriceAPIWrapper(listOfPrices)
        return wrapper
    }
}