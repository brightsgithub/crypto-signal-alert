package com.owusu.cryptosignalalert.data.datasource.coingecko.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.owusu.cryptosignalalert.domain.models.CoinDomain
import com.owusu.cryptosignalalert.domain.usecase.GetCoinsListUseCase

class CoinsSource(
    private val recordsPerPage: Int,
    private val currencies: String,
    private val ids: String?,
    private val getCoinsListUseCase: GetCoinsListUseCase
) : PagingSource<Int, CoinDomain>() {

    private var currentPageNum = 1

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CoinDomain> {

        // Key may be null during a refresh, if no explicit key is passed into Pager
        // construction. Use 0 as default, because our API is indexed started at index 0
        val pageNumber = params.key ?: 1

        currentPageNum = pageNumber

        // Make api call
        val params = GetCoinsListUseCase.Params(pageNumber, recordsPerPage, currencies, ids)
        val coinsList = getCoinsListUseCase.invoke(params)

        // Since 0 is the lowest page number, return null to signify no more pages should
        // be loaded before it.
        val prevKey = if (pageNumber > 1) pageNumber - 1 else null

        // This API defines that it's out of data when a page returns empty. When out of
        // data, we return `null` to signify no more pages should be loaded
        val nextKey = if (coinsList.isNotEmpty()) pageNumber + 1 else null

        return LoadResult.Page(
            data = coinsList,
            prevKey = prevKey,
            nextKey = nextKey
        )
    }

    fun getCurrentPageNumber(): Int {
        return currentPageNum
    }

    override fun getRefreshKey(state: PagingState<Int, CoinDomain>): Int? {
        return state.anchorPosition
    }
}