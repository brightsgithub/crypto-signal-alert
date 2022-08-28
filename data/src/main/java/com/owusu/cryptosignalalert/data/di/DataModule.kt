package com.owusu.cryptosignalalert.data.di

import android.content.Context
import android.util.Log
import com.google.gson.GsonBuilder
import com.owusu.cryptosignalalert.data.datasource.CoinsListDataSource
import com.owusu.cryptosignalalert.data.datasource.PriceTargetsDataSource
import com.owusu.cryptosignalalert.data.datasource.PricesDataSource
import com.owusu.cryptosignalalert.data.datasource.coingecko.CoinGeckoCoinsListDataSourceImpl
import com.owusu.cryptosignalalert.data.datasource.coingecko.CoinGeckoPricesDataSourceImpl
import com.owusu.cryptosignalalert.data.datasource.db.CryptoSignalAlertDB
import com.owusu.cryptosignalalert.data.datasource.db.PriceTargetDao
import com.owusu.cryptosignalalert.data.datasource.db.PriceTargetsDataSourceImpl
import com.owusu.cryptosignalalert.data.endpoints.EndPoints
import com.owusu.cryptosignalalert.data.mappers.*
import com.owusu.cryptosignalalert.data.models.api.CoinAPI
import com.owusu.cryptosignalalert.data.models.api.PriceAPIWrapper
import com.owusu.cryptosignalalert.data.models.entity.PriceTargetEntity
import com.owusu.cryptosignalalert.data.repository.PriceTargetsRepositoryImpl
import com.owusu.cryptosignalalert.data.repository.CoinsRepositoryImpl
import com.owusu.cryptosignalalert.data.repository.PriceInfoRepositoryImpl
import com.owusu.cryptosignalalert.domain.models.CoinDomain
import com.owusu.cryptosignalalert.domain.models.PriceTargetDomain
import com.owusu.cryptosignalalert.domain.models.PriceWrapperDomain
import com.owusu.cryptosignalalert.domain.repository.PriceTargetsRepository
import com.owusu.cryptosignalalert.domain.repository.CoinsRepository
import com.owusu.cryptosignalalert.domain.repository.PriceInfoRepository
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.observer.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.lang.Exception

private const val TIME_OUT = 60_000
private const val PRICE_GSON_ADAPTOR = "PRICE_GSON_ADAPTOR"
open class DataModuleWrapper(private val context: Context) {

    val dataModule = module(override = true) {

        single<PriceTargetsRepository> {
            PriceTargetsRepositoryImpl(get())
        }

        single<CoinsRepository> {
            CoinsRepositoryImpl(get(), get())
        }

        single<PriceInfoRepository> {
            PriceInfoRepositoryImpl(get(), get())
        }

        factory<DataAPIListMapper<CoinAPI, CoinDomain>>{ CoinsAPIMapper() }

        factory<DataMapper<PriceAPIWrapper, PriceWrapperDomain>>{ PriceAPIMapper() }

        factory<DataAPIListMapper<PriceTargetEntity, PriceTargetDomain>>{ PriceTargetEntityToPriceTargetDomainMapper() }

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

        single<PriceTargetDao> {
            get<CryptoSignalAlertDB>().priceTargetDao()
        }

        single<CryptoSignalAlertDB> {
            CryptoSignalAlertDB.invoke(context)
        }

        single<PriceTargetsDataSource> {
            PriceTargetsDataSourceImpl(get(), get())
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

}
