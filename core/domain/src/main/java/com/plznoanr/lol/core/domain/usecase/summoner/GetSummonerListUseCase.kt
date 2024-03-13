package com.plznoanr.lol.core.domain.usecase.summoner

import com.plznoanr.lol.core.common.model.PagingResult
import com.plznoanr.lol.core.data.repository.SummonerRepository
import com.plznoanr.lol.core.domain.usecase.paging.PagingUseCase
import com.plznoanr.lol.core.model.Summoner
import com.plznoanr.lol.core.model.getDummyName
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.random.Random

class GetSummonerListUseCase @Inject constructor(
    private val summonerRepository: SummonerRepository,
): PagingUseCase<Summoner>() {

    operator fun invoke(
        pageSize: Int = 20,
        sortedBookmark: Boolean = false
    ): Flow<List<Summoner>> = executeFlow(pageSize) {
        summonerRepository.getSummonerList(it)
    }.combine(
        summonerRepository.getBookMarkedSummonerIds()
    ) { pagingList, ids ->
        pagingList.map {
            it.copy(
                isBookMarked = ids.contains(it.id)
            )
        }
    }.map {
        if (sortedBookmark) {
            it.sortedByDescending { summoner -> summoner.isBookMarked }
        } else {
            it
        }
    }

    private fun getDummy() = flow {
        val random = Random(10000)
        emit(
            PagingResult(
                data = getDummySummonerList(random.nextInt()),
                page = 1,
                hasNext = true
            )
        )
    }

    private fun getDummySummonerList(id: Int) = (1..20).map {
        val random = it * id
        Summoner(
            id = "id $random",
            nickname = getDummyName(),
            icon = "http://ddragon.leagueoflegends.com/cdn/13.6.1/img/profileicon/6.png",
            level = "100$random",
            leaguePoints = 99,
            wins = 100,
            losses = 100,
            rank = "sd $it",
            tier = "sdf $it",
            isBookMarked = false,
        )
    }

}
