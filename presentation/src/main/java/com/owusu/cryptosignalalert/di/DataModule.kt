package com.owusu.cryptosignalalert.di

import android.util.Log
import com.owusu.cryptosignalalert.data.datasource.CoinsDataSource
import com.owusu.cryptosignalalert.data.datasource.coingecko.CoinGeckoDataSourceImpl
import com.owusu.cryptosignalalert.data.endpoints.EndPointProd
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
        CoinGeckoDataSourceImpl(get())
    }

    single<EndPoints> {
        EndPointProd()
    }

    single {
        HttpClient(Android) {

            // https://ktor.io/docs/default-request.html
            defaultRequest {
                url("https://api.coingecko.com/api/v3/")
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