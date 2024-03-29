package com.plznoanr.lol.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.plznoanr.lol.core.database.model.ProfileEntity
import com.plznoanr.lol.core.database.model.SearchEntity
import com.plznoanr.lol.core.database.model.SummonerEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LolDao {

    @Query("SELECT * FROM Search ORDER BY date DESC LIMIT 20")
    fun getSearchList(): Flow<List<SearchEntity>?>

    @Upsert
    suspend fun upsertSearch(searchEntity: SearchEntity)

    @Query("DELETE FROM Search WHERE name = :sName")
    suspend fun deleteSearch(sName: String)

    @Query("DELETE FROM Search")
    suspend fun deleteSearchAll()

    @Query("SELECT * FROM Profile")
    fun getProfile() : Flow<ProfileEntity?>

    @Upsert
    suspend fun upsertProfile(profileEntity: ProfileEntity)

    @Query("DELETE FROM Profile")
    suspend fun deleteProfile()

    @Query("SELECT * FROM Summoner")
    fun getSummonerList() : Flow<List<SummonerEntity>?>

    @Query("SELECT * FROM Summoner LIMIT :size OFFSET (:page - 1) * :size")
    fun getSummonerList(page: Int, size: Int) : Flow<List<SummonerEntity>?>

    @Query("SELECT * FROM Summoner WHERE id = :summonerId")
    fun getSummoner(summonerId: String) : Flow<SummonerEntity?>

    @Upsert
    fun upsertSummoner(summonerEntity: SummonerEntity)

    @Query("DELETE FROM Summoner WHERE name = :summonerName")
    suspend fun deleteSummoner(summonerName: String)

    @Query("DELETE FROM Summoner")
    suspend fun deleteSummonerAll()

}