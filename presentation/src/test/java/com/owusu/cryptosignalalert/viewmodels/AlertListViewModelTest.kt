package com.owusu.cryptosignalalert.viewmodels

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.owusu.cryptosignalalert.domain.models.PriceTargetDomain
import com.owusu.cryptosignalalert.domain.usecase.DeletePriceTargetsUseCase
import com.owusu.cryptosignalalert.domain.usecase.GetPriceTargetsUseCase
import com.owusu.cryptosignalalert.mappers.UIMapper
import com.owusu.cryptosignalalert.models.PriceTargetUI
import com.owusu.cryptosignalalert.workmanager.Constants.SYNC_PRICES_WORKER_TAG
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.*
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test

class AlertListViewModelTest {

    //@Test
    fun testLoadAlertList() {

        runTest {
            // Create a mock mapper
            val mockMapper = mockk<UIMapper<PriceTargetDomain, PriceTargetUI>>()
            // Create a mock use case
            val mockGetPriceTargetsUseCase = mockk<GetPriceTargetsUseCase>()
            // Create a mock delete use case
            val mockDeletePriceTargetsUseCase = mockk<DeletePriceTargetsUseCase>()
            // Create a mock CoroutineDispatcher
            val mockDispatcher = mockk<CoroutineDispatcher>()
            // Create a mock Application object
            val mockApp = mockk<Application>()
            val mockWorkManager = mockk<WorkManager>()
            val mockWorkInfo = mockk<WorkInfo>()

            val mockWorkInfoLiveData = mockk<LiveData<List<WorkInfo>>>()

            val mockContext = mockk<Context>()
            every { mockApp.applicationContext } returns mockContext
            every { mockWorkInfoLiveData.value } returns listOf(mockWorkInfo)

            every { mockWorkManager.getWorkInfosForUniqueWorkLiveData(SYNC_PRICES_WORKER_TAG) } returns mockWorkInfoLiveData

            // Create an instance of AlertListViewModel with the mock dependencies
            val viewModel = AlertListViewModel(
                mockMapper,
                mockGetPriceTargetsUseCase,
                mockDeletePriceTargetsUseCase,
                mockDispatcher,
                mockDispatcher,
                "",
                mockApp,
                mockWorkManager
            )

            // Set up the mock use case to return a list of domain objects when invoked
            val domainObjects = listOf(
                PriceTargetDomain(id = "1", currentPrice = 100.0, userPriceTarget = 120.0, localPrimeId = 11),
                PriceTargetDomain(id = "2", currentPrice = 50.0, userPriceTarget = 60.0, localPrimeId = 22)
            )
            coEvery { mockGetPriceTargetsUseCase.invoke() } returns domainObjects

            // Set up the mock mapper to return a list of UI objects when mapDomainListToUIList is called
            val uiObjects = listOf(
                PriceTargetUI(id = "1", currentPrice = 100.0, userPriceTarget = 120.0, localPrimeId = 11),
                PriceTargetUI(id = "2", currentPrice = 50.0, userPriceTarget = 60.0, localPrimeId = 22)
            )
            every { mockMapper.mapDomainListToUIList(domainObjects) } returns uiObjects

            // Call the loadAlertList function on the view model
            viewModel.loadAlertList()

            // Verify that the use case is invoked
            coVerify { mockGetPriceTargetsUseCase.invoke() }
            // Verify that the mapper is called with the correct input
            verify { mockMapper.mapDomainListToUIList(domainObjects) }
            // Verify that the view state is updated with the correct list of UI objects
            //assertEquals(viewModel.viewState.value?.priceTargets, uiObjects)
        }
    }

}