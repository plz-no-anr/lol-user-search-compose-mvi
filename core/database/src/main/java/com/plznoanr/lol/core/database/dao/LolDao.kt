package com.plznoanr.lol.core.database.dao

import androidx.room.*
import com.plznoanr.lol.core.database.model.ProfileEntity
import com.plznoanr.lol.core.database.model.SearchEntity
import com.plznoanr.lol.core.database.model.SummonerEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LolDao {

    @Query("SELECT * FROM Search LIMIT 20 OFFSET 0")
    fun getSearchList(): Flow<List<SearchEntity>?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSearch(searchEntity: SearchEntity)

    @Query("DELETE FROM Search WHERE name = :sName")
    suspend fun deleteSearch(sName: String)

    @Update
    suspend fun updateSearch(searchEntity: SearchEntity)

    @Query("DELETE FROM Search")
    suspend fun deleteSearchAll()

    @Query("SELECT * FROM Profile")
    fun getProfile() : Flow<ProfileEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profileEntity: ProfileEntity)

    @Update
    suspend fun updateProfile(profileEntity: ProfileEntity)

    @Query("DELETE FROM Profile")
    suspend fun deleteProfile()

    @Query("SELECT * FROM Summoner")
    fun getSummonerList() : Flow<List<SummonerEntity>?>

    @Query("SELECT * FROM Summoner WHERE name = :summonerName")
    fun getSummoner(summonerName: String) : Flow<SummonerEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSummoner(summonerEntity: SummonerEntity)

    @Update
    suspend fun updateSummoner(summonerEntity: SummonerEntity)

    @Query("DELETE FROM Summoner WHERE name = :summonerName")
    suspend fun deleteSummoner(summonerName: String)

    @Query("DELETE FROM Summoner")
    suspend fun deleteSummonerAll()

}