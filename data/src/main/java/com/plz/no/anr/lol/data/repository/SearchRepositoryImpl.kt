package com.plz.no.anr.lol.data.repository

import com.plz.no.anr.lol.data.repository.local.search.SearchLocalDataSource
import com.plz.no.anr.lol.data.utils.toEntity
import com.plz.no.anr.lol.data.utils.toSearchList
import com.plz.no.anr.lol.domain.model.Search
import com.plz.no.anr.lol.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

internal class SearchRepositoryImpl(
    private val localDataSource: SearchLocalDataSource,
) : SearchRepository {

    override fun getSearchList(): Flow<Result<List<Search>>> = flow {
        emit(Result.success(localDataSource.getSearch().toSearchList()))
    }

    override fun insertSearch(search: Search): Flow<Result<Unit>> = flow {
        localDataSource.insertSearch(search.toEntity()).run {
            emit(Result.success(Unit))
        }
    }

    override fun deleteSearch(sName: String): Flow<Result<Unit>> = flow {
        localDataSource.deleteSearch(sName).run {
            emit(Result.success(Unit))
        }
    }

    override fun deleteSearchAll(): Flow<Result<Unit>> = flow {
        localDataSource.deleteSearchAll().run {
            emit(Result.success(Unit))
        }
    }

}