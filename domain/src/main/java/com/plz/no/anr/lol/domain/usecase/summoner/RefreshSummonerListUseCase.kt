package com.plz.no.anr.lol.domain.usecase.summoner

import com.plz.no.anr.lol.domain.model.Summoner
import com.plz.no.anr.lol.domain.repository.SummonerRepository
import com.plz.no.anr.lol.domain.usecase.base.BaseUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

class RefreshSummonerListUseCase(
    coroutineDispatcher: CoroutineDispatcher,
    private val appRepository: SummonerRepository
): BaseUseCase<Unit, List<Summoner>>(coroutineDispatcher) {

    override fun execute(parameter: Unit): Flow<Result<List<Summoner>>> {
        return appRepository.refreshSummonerList()
    }

}