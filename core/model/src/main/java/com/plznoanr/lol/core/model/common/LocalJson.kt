package com.plznoanr.lol.core.model.common

import com.plznoanr.lol.core.model.common.json.ChampionJson
import com.plznoanr.lol.core.model.common.json.MapJson
import com.plznoanr.lol.core.model.common.json.RuneJson
import com.plznoanr.lol.core.model.common.json.SummonerJson

data class LocalJson(
    val champ: ChampionJson,
    val map: MapJson,
    val rune: List<RuneJson>,
    val summoner: SummonerJson
)