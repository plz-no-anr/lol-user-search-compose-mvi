package com.plznoanr.lol.core.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.plznoanr.lol.core.common.model.AppError
import com.plznoanr.lol.core.common.model.Paging
import com.plznoanr.lol.core.data.utils.QueueType
import com.plznoanr.lol.core.data.utils.asEntity
import com.plznoanr.lol.core.data.utils.asSummonerList
import com.plznoanr.lol.core.data.utils.catchResultError
import com.plznoanr.lol.core.data.utils.toChampImage
import com.plznoanr.lol.core.data.utils.toIcon
import com.plznoanr.lol.core.data.utils.toSpellImage
import com.plznoanr.lol.core.database.data.app.AppLocalDataSource
import com.plznoanr.lol.core.database.data.summoner.SummonerLocalDataSource
import com.plznoanr.lol.core.database.model.asDomain
import com.plznoanr.lol.core.datastore.PreferenceDataSource
import com.plznoanr.lol.core.model.Spectator
import com.plznoanr.lol.core.model.Summoner
import com.plznoanr.lol.core.model.Team
import com.plznoanr.lol.core.network.NetworkDataSource
import com.plznoanr.lol.core.network.model.SpectatorResponse
import com.plznoanr.lol.core.network.model.asDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SummonerRepositoryImpl @Inject constructor(
    private val appLocalDataSource: AppLocalDataSource,
    private val summonerLocalDataSource: SummonerLocalDataSource,
    private val networkDataSource: NetworkDataSource,
    private val preferenceDataSource: PreferenceDataSource,
) : SummonerRepository {

    private suspend fun authTokenHeader() = HashMap<String, String>().apply {
        val key = requireNotNull(preferenceDataSource.apiKeyFlow.first()) {
            throw Exception(AppError.Forbidden.parse())
        }
        put("X-Riot-Token", key)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun requestSummoner(name: String): Flow<Result<Summoner>> = flow {
        val summoner = networkDataSource.requestSummoner(
            authTokenHeader(),
            name
        ).getOrThrow()

        val league = networkDataSource.requestLeague(
            authTokenHeader(),
            summoner.id
        ).getOrThrow()

        if (league.isNotEmpty()) {
            league.find { it.queueType == QueueType.SOLO_RANK }?.let {
                emit(
                    Result.success(
                        Summoner(
                            id = summoner.id,
                            name = summoner.name,
                            level = summoner.summonerLevel.toString(),
                            icon = summoner.profileIconId.toIcon(),
                            tier = it.tier,
                            leaguePoints = it.leaguePoints,
                            rank = it.rank,
                            wins = it.wins,
                            losses = it.losses,
                            miniSeries = it.miniSeries?.asDomain(),
                        )
                    )
                )
            } ?: emit(Result.failure(AppError.NoMatchHistory.exception())) // 솔로 랭크만 지원
        } else {
            emit(Result.failure(AppError.NoMatchHistory.exception()))
        }

    }.catchResultError()

    override fun getSummoner(name: String): Flow<Result<Summoner>> =
        summonerLocalDataSource.getSummoner(name)
            .map { entity ->
                entity?.let {
                    Result.success(it.asDomain())
                } ?: Result.failure(AppError.Default.exception())
            }

    override fun getSummonerList(): Flow<Result<List<Summoner>>> =
        summonerLocalDataSource.getSummonerList().map { entity ->
            entity?.let {
                Result.success(it.asSummonerList())
            } ?: Result.failure(AppError.Default.exception())
        }

    override fun getSummonerList(paging: Paging): Flow<Result<List<Summoner>>> =
        summonerLocalDataSource.getSummonerList(
            page = paging.page,
            size = paging.size
        ).map { entity ->
            entity?.let {
                Result.success(it.asSummonerList())
            } ?: Result.failure(AppError.Default.exception())
        }

    override fun requestSpectator(summonerId: String): Flow<Result<Spectator>> = flow {
        val result = networkDataSource.requestSpectator(
            authTokenHeader(),
            summonerId
        )
        val spectator = result.getOrNull()?.let { response ->
            Spectator(
                map = response.mapId.toMap(),
                banChamp = response.bannedChampions.toBanChamp(),
                redTeam = response.participants.filter { it.teamId.toTeam() == Team.RED }
                    .asDomain(),
                blueTeam = response.participants.filter { it.teamId.toTeam() == Team.BLUE }
                    .asDomain()
            )
        }
        emit(
            spectator?.let {
                Result.success(it)
            } ?: Result.failure(Exception(AppError.NotPlaying.parse()))
        )
    }.catchResultError()

    override fun insertSummoner(summoner: Summoner): Flow<Result<Unit>> = flow {
        summonerLocalDataSource.insertSummoner(summoner.asEntity()).run {
            emit(Result.success(Unit))
        }
    }

    override fun updateSummoner(summoner: Summoner): Flow<Result<Unit>> = flow {
        summonerLocalDataSource.updateSummoner(summoner.asEntity()).run {
            emit(Result.success(Unit))
        }
    }

    override fun deleteSummoner(name: String): Flow<Result<Unit>> = flow {
        summonerLocalDataSource.deleteSummoner(name).run {
            emit(Result.success(Unit))
        }
    }

    override fun deleteSummonerAll(): Flow<Result<Unit>> = flow {
        summonerLocalDataSource.deleteSummonerAll().run {
            emit(Result.success(Unit))
        }
    }

    private suspend fun List<SpectatorResponse.BannedChampion>.toBanChamp() = map {
        Spectator.BanChamp(
            team = it.teamId.toTeam(),
            champName = it.championId.toChampInfo().first
        )
    }

    private suspend fun List<SpectatorResponse.CurrentGameParticipant>.asDomain() = map {
        val champInfo = it.championId.toChampInfo()
        Spectator.SpectatorInfo(
            name = it.summonerName,
            champName = champInfo.first,
            champImg = champInfo.second,
            team = it.teamId.toTeam(),
            spell1 = it.spell1Id.toSpellImage(),
            spell2 = it.spell2Id.toSpellImage(),
            runeStyle = it.perks.perkStyle.toRuneStyle(),
            subStyle = it.perks.perkSubStyle.toRuneStyle(),
            mainRune = getMainRune(it.perks.perkStyle, it.perks.perkIds[0]),
            rune = getRunes(it.perks.perkStyle, it.perks.perkSubStyle, it.perks.perkIds),
        )
    }

    private fun Long.toTeam() = if (this.toString() == "100") Team.BLUE else Team.RED
    private suspend fun Long.toMap(): String =
        appLocalDataSource.getMaps()
            .map { maps ->
                maps.find { it.mapId == this.toString() }?.mapName
                    ?: throw Exception("Map Not Found")
            }.first()


    private suspend fun Long.toChampInfo(): Pair<String, String> =
        appLocalDataSource.getChamps()
            .map { champs ->
                if (this@toChampInfo == (-1).toLong()) return@map "NoBan" to "NoBan"
                champs.find { it.key == this.toString() }?.let {
                    it.name to it.image.full.toChampImage()
                } ?: ("Not Found" to "Not Found")
            }.first()

    private suspend fun Long.toRuneStyle(): Spectator.SpectatorInfo.Rune =
        appLocalDataSource.getRunes()
            .map { runes ->
                runes.find { it.id == this@toRuneStyle }?.let {
                    Spectator.SpectatorInfo.Rune(it.name, it.icon)
                } ?: throw Exception("Rune Not Found")
            }.first()

    private suspend fun Long.toSpellImage(): String =
        appLocalDataSource.getSpells()
            .map { spells ->
                spells.find { it.key == this.toString() }?.image?.full?.toSpellImage()
                    ?: throw Exception("Spell Not Found")
            }.first()

    private suspend fun getMainRune(perkStyle: Long, perks: Long): String =
        appLocalDataSource.getRunes()
            .map { runeEntities ->
                runeEntities.find { it.id == perkStyle }?.let { runeEntity ->
                    runeEntity.slots.map { slot ->
                        slot.runes
                    }.flatten()
                        .find { it.id == perks }?.icon ?: throw Exception("Main Rune Not Found")
                } ?: throw Exception("Main Rune Not Found")
            }.first()


    private suspend fun getRunes(
        perkStyle: Long,
        subStyle: Long,
        perks: List<Long>
    ): List<Spectator.SpectatorInfo.Rune> =
        appLocalDataSource.getRunes()
            .map { runeEntities ->
                val runeNames = mutableListOf<Spectator.SpectatorInfo.Rune>()
                runeEntities.find { it.id == perkStyle }?.let { runeEntity ->
                    runeEntity.slots.map { it.runes }
                        .flatten()
                        .forEachIndexed { index, rune ->
                            perks.find { it == rune.id }.let {
                                runeNames.add(
                                    index,
                                    Spectator.SpectatorInfo.Rune(rune.name, rune.icon)
                                )
                            }
                            if (index > 3) {
                                return@forEachIndexed
                            }
                        }
                } ?: runeEntities.find { it.id == subStyle }?.let { runeEntity ->
                    runeEntity.slots.map { it.runes }
                        .flatten()
                        .forEachIndexed { index, rune ->
                            if (index in 4..5) {
                                perks.find { it == rune.id }.let {
                                    runeNames.add(
                                        index,
                                        Spectator.SpectatorInfo.Rune(rune.name, rune.icon)
                                    )
                                }
                            }
                        }
                }
                runeNames
            }.first()

}