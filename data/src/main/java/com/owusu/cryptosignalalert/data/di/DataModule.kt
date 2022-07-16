package com.owusu.cryptosignalalert.data.di

import android.util.Log
import com.google.gson.GsonBuilder
import com.owusu.cryptosignalalert.data.datasource.CoinsListDataSource
import com.owusu.cryptosignalalert.data.datasource.PricesDataSource
import com.owusu.cryptosignalalert.data.datasource.coingecko.CoinGeckoCoinsListDataSourceImpl
import com.owusu.cryptosignalalert.data.datasource.coingecko.CoinGeckoPricesDataSourceImpl
import com.owusu.cryptosignalalert.data.endpoints.EndPoints
import com.owusu.cryptosignalalert.data.mappers.*
import com.owusu.cryptosignalalert.data.models.CoinAPI
import com.owusu.cryptosignalalert.data.models.PriceAPIWrapper
import com.owusu.cryptosignalalert.data.repository.AlertListRepositoryImpl
import com.owusu.cryptosignalalert.data.repository.CoinsRepositoryImpl
import com.owusu.cryptosignalalert.domain.models.Coin
import com.owusu.cryptosignalalert.domain.models.PriceWrapper
import com.owusu.cryptosignalalert.domain.repository.AlertListRepository
import com.owusu.cryptosignalalert.domain.repository.CoinsRepository
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.observer.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.koin.core.qualifier.named
import org.koin.dsl.module

private const val TIME_OUT = 60_000
private const val PRICE_GSON_ADAPTOR = "PRICE_GSON_ADAPTOR"

val dataModule = module(override = true) {

    single<AlertListRepository> {
        AlertListRepositoryImpl()
    }

    single<CoinsRepository> {
        CoinsRepositoryImpl(get(), get(), get(), get())
    }

    factory<DataAPIListMapper<CoinAPI, Coin>>{ CoinsAPIMapper() }

    factory<DataAPIMapper<PriceAPIWrapper, PriceWrapper>>{ PriceAPIMapper() }

    factory(named(PRICE_GSON_ADAPTOR)) {
        GsonBuilder().registerTypeAdapter(PriceAPIWrapper::class.java, PriceJsonAdapter())
            .setPrettyPrinting()
            .create()
    }

    single<CoinsListDataSource> {
        CoinGeckoCoinsListDataSourceImpl(get(), get())
    }

    single<PricesDataSource> {
        CoinGeckoPricesDataSourceImpl(get(), get(), get(named(PRICE_GSON_ADAPTOR)))
    }



    single {
        EndPoints()
    }

    single {
        HttpClient(Android) {

            // https://ktor.io/docs/default-request.html
            defaultRequest {
                url(get<EndPoints>().getHostName())
            }

            engine {
                connectTimeout = TIME_OUT
                socketTimeout = TIME_OUT
                //proxy = Proxy(Proxy.Type.HTTP, InetSocketAddress("localhost", 8080))
            }

            install(ContentNegotiation) {
                // register(ContentType.Application.Json, SimpleCoinConverter())
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }

            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        Log.v("Logger Ktor =>", message)
                    }

                }
                level = LogLevel.ALL
            }

            install(ResponseObserver) {
                onResponse { response ->
                    Log.d("HTTP status:", "${response.status.value}")
                }
            }
        }
    }
}