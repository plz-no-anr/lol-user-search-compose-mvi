package com.plznoanr.lol.core.network.model

@kotlinx.serialization.Serializable
data class SpectatorResponse(
    val gameId: Long,
    val gameType: String,
    val gameStartTime: Long,
    val mapId: Long, // 맵 정보
    val gameLength: Long,
    val platformId: String, // 플랫폼 정보 ex) KR
    val gameMode: String, // 게임 모드
    val bannedChampions: List<BannedChampion>, // 밴한 챔프 리스트
    val gameQueueConfigId: Long,
    val observers: Observer,
    val participants: List<CurrentGameParticipant> // 팀원 정보
){

    @kotlinx.serialization.Serializable
    data class BannedChampion(
        val pickTurn: Int,
        val championId: Long,
        val teamId: Long
    )

    @kotlinx.serialization.Serializable
    data class Observer(
        val encryptionKey: String
    )

    @kotlinx.serialization.Serializable
    data class CurrentGameParticipant(
        val championId:	Long, // 챔프 정보
        val perks: Perks, // 룬
        val profileIconId: Long,
        val bot: Boolean, // 봇인지
        val teamId:	Long, // 팀정보
        val summonerName: String,
        val summonerId:	String,
        val spell1Id: Long, // 스펠
        val spell2Id: Long, // 스펠2
        val gameCustomizationObjects: List<GameCustomizationObject>
    ) {

        @kotlinx.serialization.Serializable
        data class Perks(
            val perkIds: List<Long>, // 사용룬
            val perkStyle: Long, // 룬 정보
            val perkSubStyle: Long // 보조룬 정도
        )

        @kotlinx.serialization.Serializable
        data class GameCustomizationObject(
            val category: String,
            val content: String
        )
    }

}

