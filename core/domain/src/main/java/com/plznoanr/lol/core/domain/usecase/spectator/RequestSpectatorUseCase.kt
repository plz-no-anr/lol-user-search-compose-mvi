package com.plznoanr.lol.core.domain.usecase.spectator

import com.plznoanr.lol.core.common.model.Result
import com.plznoanr.lol.core.data.repository.SummonerRepository
import com.plznoanr.lol.core.model.Spectator
import javax.inject.Inject

class RequestSpectatorUseCase @Inject constructor(
    private val summonerRepository: SummonerRepository,
) {

    suspend operator fun invoke(summonerId: String): Result<Spectator> {
        return summonerRepository.requestSpectator(summonerId)
    }

}