package com.owusu.cryptosignalalert.data.models

import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.serialization.*
import io.ktor.util.reflect.*
import io.ktor.utils.io.*
import io.ktor.utils.io.charsets.*
import io.ktor.utils.io.jvm.javaio.*

class SimpleCoinConverter : ContentConverter {
    override suspend fun deserialize(
        charset: Charset,
        typeInfo: TypeInfo,
        content: ByteReadChannel
    ): List<PriceAPI>? {

        val channel = content
        val reader = content.toInputStream().reader(charset)
        val type = typeInfo

//        val xmlmapper = XmlMapper()
//        val objectoreturn = xmlmapper.readValue(reader, type.javaObjectType)

        return arrayListOf<PriceAPI>()
    }

    override suspend fun serialize(
        contentType: ContentType,
        charset: Charset,
        typeInfo: TypeInfo,
        value: Any
    ): OutgoingContent? {
        return null
    }
}