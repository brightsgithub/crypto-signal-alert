package com.owusu.cryptosignalalert.data.di

import android.util.Log
import com.owusu.cryptosignalalert.data.datasource.CoinsDataSource
import com.owusu.cryptosignalalert.data.datasource.coingecko.CoinGeckoDataSourceImpl
import com.owusu.cryptosignalalert.data.endpoints.EndPoints
import com.owusu.cryptosignalalert.data.mappers.CoinsAPIMapper
import com.owusu.cryptosignalalert.data.mappers.DataAPIListMapper
import com.owusu.cryptosignalalert.data.models.CoinAPI
import com.owusu.cryptosignalalert.data.repository.AlertListRepositoryImpl
import com.owusu.cryptosignalalert.data.repository.CoinsRepositoryImpl
import com.owusu.cryptosignalalert.domain.models.Coin
import com.owusu.cryptosignalalert.domain.repository.AlertListRepository
import com.owusu.cryptosignalalert.domain.repository.CoinsRepository
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.observer.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.koin.dsl.module

private const val TIME_OUT = 60_000

val dataModule = module(override = true) {

    single<AlertListRepository> {
        AlertListRepositoryImpl()
    }

    single<CoinsRepository> {
        CoinsRepositoryImpl(get(), get())
    }

    factory<DataAPIListMapper<CoinAPI, Coin>>{ CoinsAPIMapper() }

    single<CoinsDataSource> {
        CoinGeckoDataSourceImpl(get(), get())
    }

    single {
        EndPoints()
    }

    single {
        HttpClient(Android) {

            // https://ktor.io/docs/default-request.html

            // http://localhost:8080/api/v3/v3/coins/markets?vs_currency=usd&order=market_cap_desc&per_page=100&page=1&sparkline=false


            defaultRequest {
                url(get<EndPoints>().getHostName())
            }

            engine {
                connectTimeout = TIME_OUT
                socketTimeout = TIME_OUT
                //proxy = Proxy(Proxy.Type.HTTP, InetSocketAddress("localhost", 8080))
            }

            install(ContentNegotiation) {
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