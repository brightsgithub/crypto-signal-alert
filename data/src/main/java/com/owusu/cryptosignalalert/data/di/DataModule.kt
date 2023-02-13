package com.owusu.cryptosignalalert.data.di

import android.app.Application
import android.content.Context
import android.util.Log
import com.google.gson.GsonBuilder
import com.owusu.cryptosignalalert.data.datasource.*
import com.owusu.cryptosignalalert.data.datasource.coingecko.CoinGeckoCoinsListDataSourceImpl
import com.owusu.cryptosignalalert.data.datasource.coingecko.CoinGeckoPricesDataSourceImpl
import com.owusu.cryptosignalalert.data.datasource.db.*
import com.owusu.cryptosignalalert.data.endpoints.EndPoints
import com.owusu.cryptosignalalert.data.mappers.*
import com.owusu.cryptosignalalert.data.models.api.CoinAPI
import com.owusu.cryptosignalalert.data.models.api.CoinIdAPI
import com.owusu.cryptosignalalert.data.models.api.PriceAPIWrapper
import com.owusu.cryptosignalalert.data.models.entity.PriceTargetEntity
import com.owusu.cryptosignalalert.data.models.skus.Skus.INAPP_SKUS
import com.owusu.cryptosignalalert.data.repository.PriceTargetsRepositoryImpl
import com.owusu.cryptosignalalert.data.repository.CoinsRepositoryImpl
import com.owusu.cryptosignalalert.data.repository.PriceInfoRepositoryImpl
import com.owusu.cryptosignalalert.data.repository.billing.BillingDataSource
import com.owusu.cryptosignalalert.data.repository.billing.GoogleBillingRepository
import com.owusu.cryptosignalalert.domain.models.CoinDomain
import com.owusu.cryptosignalalert.domain.models.CoinIdDomain
import com.owusu.cryptosignalalert.domain.models.PriceTargetDomain
import com.owusu.cryptosignalalert.domain.models.PriceWrapperDomain
import com.owusu.cryptosignalalert.domain.repository.BillingRepository
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
import kotlinx.coroutines.GlobalScope
import kotlinx.serialization.json.Json
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.lang.Exception

private const val TIME_OUT = 60_000
private const val PRICE_GSON_ADAPTOR = "PRICE_GSON_ADAPTOR"
const val NAMED_CoinsAPIMapper = "CoinsAPIMapper"
const val NAMED_PriceTargetEntityToPriceTargetDomainMapper = "PriceTargetEntityToPriceTargetDomainMapper"
const val NAMED_PriceAPIMapper = "PriceAPIMapper"

// so we can pass in Context!
open class DataModuleWrapper(private val context: Context) {

    val dataModule = module() {

        single<PriceTargetsRepository> {
            PriceTargetsRepositoryImpl(get())
        }

        single<CoinsRepository> {
            CoinsRepositoryImpl(
                get(),
                get(named(NAMED_CoinsAPIMapper)),
                coinIdAPIMapper = get(),
                coinIdsLocalDataSource = get(),
                appPreferences = get(),
                coinDetailAPIToDomainMapper = get()
            )
        }

        single<PriceInfoRepository> {
            PriceInfoRepositoryImpl(get(), get(named(NAMED_PriceAPIMapper)))
        }

        // mappers. Using names since Im using an inetrface, Koin wont know which one to use and will
        // en up overriding i.e. using the latest declared DataMapper<>. so use named
        factory<DataAPIListMapper<CoinAPI, CoinDomain>>(named(NAMED_CoinsAPIMapper)){ CoinsAPIMapper() }
        factory<DataAPIListMapper<PriceTargetEntity, PriceTargetDomain>>(named(NAMED_PriceTargetEntityToPriceTargetDomainMapper)) { PriceTargetEntityToPriceTargetDomainMapper() }
        factory { CoinIdAPIToDomainIdMapper() }
        factory { CoinDetailAPIToDomainMapper() }
        factory<DataMapper<PriceAPIWrapper, PriceWrapperDomain>>(named(NAMED_PriceAPIMapper)){ PriceAPIMapper() }

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

        single<CoinIdsLocalDataSource> {
            CoinIdsLocalDataSourceImpl(coinIdsDao = get())
        }

        single<CoinIdsDao> {
            get<CryptoSignalAlertDB>().coinIdsDao()
        }

        single<CryptoSignalAlertDB> {
            CryptoSignalAlertDB.invoke(context)
        }

        single<PriceTargetsDataSource> {
            PriceTargetsDataSourceImpl(get(), get(named(NAMED_PriceTargetEntityToPriceTargetDomainMapper)))
        }

        single { AppPreferences(context) }

        single<BillingRepository> {
            GoogleBillingRepository(
                billingDataSource = get(),
                defaultScope = GlobalScope,
                skuMapper = get()
            )
        }

        single {
            BillingDataSource.getInstance(
                application = context as Application,
                defaultScope = GlobalScope,
                knownInappSKUs = INAPP_SKUS,
                knownSubscriptionSKUs = null,
                autoConsumeSKUs = null
            )
        }

        factory {
            SkuMapper()
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
