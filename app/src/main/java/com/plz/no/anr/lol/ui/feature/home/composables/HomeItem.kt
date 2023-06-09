package com.plz.no.anr.lol.ui.feature.home.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.HorizontalRule
import androidx.compose.material.icons.rounded.Square
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.plz.no.anr.lol.R
import com.plz.no.anr.lol.domain.model.Summoner
import com.plz.no.anr.lol.domain.model.getDummySummoner
import com.plz.no.anr.lol.ui.feature.common.IconImage
import com.plz.no.anr.lol.ui.feature.common.summoner.getTierIcon
import com.plz.no.anr.lol.ui.feature.home.HomeContract
import com.plz.no.anr.lol.ui.theme.sky

@Composable
fun HomeItem(
    modifier: Modifier = Modifier,
    summoner: Summoner,
    onIntent: (HomeContract.Intent) -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = sky,
            contentColor = Color.White
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
        ) {

            SummonerView(
                icon = summoner.icon,
                name = summoner.name,
                level = summoner.levelInfo
            )

            Divider(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(2.dp)
                    .padding(vertical = 10.dp),
                color = Color.White
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                TierView(
                    modifier = modifier
                        .weight(1f),
                    tierRank = summoner.tierRank,
                    tierIcon = getTierIcon(summoner.tier)
                )

                Spacer(modifier = Modifier.height(20.dp))

                LeagueInfoView(
                    modifier = modifier
                        .weight(1f),
                    pointWinLose = summoner.lpWinLose,
                    miniSeries = summoner.miniSeries,
                    isPlaying = summoner.isPlaying,
                    onAdd = { onIntent(HomeContract.Intent.Profile.OnAdd(summoner.asProfile())) },
                    onDelete = { onIntent(HomeContract.Intent.Summoner.OnDelete(summoner.name)) },
                    onSpectator = { onIntent(HomeContract.Intent.Spectator.OnWatch(summoner.name)) }
                )
            }

        }
    }

}

@Composable
private fun SummonerView(
    icon: String,
    name: String,
    level: String
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 10.dp, vertical = 10.dp)
    ) {
        IconImage(
            modifier = Modifier
                .size(90.dp)
                .clip(RoundedCornerShape(10)),
            url = icon
        )

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = name,
            modifier = Modifier,
            fontSize = 14.sp
        )

        Text(
            text = level,
            modifier = Modifier,
            fontSize = 13.sp
        )
    }
}

@Composable
private fun TierView(
    modifier: Modifier = Modifier,
    tierRank: String,
    tierIcon: Painter = painterResource(id = R.drawable.emblem_bronze)
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = 8.dp,
                top = 16.dp
            )
    ) {
        Image(
            painter = tierIcon,
            contentDescription = null,
            modifier = Modifier
                .size(70.dp)
        )

        Column {
            Text(
                text = stringResource(id = R.string.solo_rank),
                modifier = Modifier
                    .padding(bottom = 8.dp),
                fontSize = 13.sp
            )

            Text(
                text = tierRank,
                modifier = Modifier,
                fontSize = 13.sp
            )
        }
    }
}

@Composable
private fun LeagueInfoView(
    modifier: Modifier = Modifier,
    pointWinLose: String,
    miniSeries: Summoner.MiniSeries?,
    isPlaying: Boolean,
    onAdd: () -> Unit,
    onDelete: () -> Unit,
    onSpectator: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {

        LeaguePointView(
            pointWinLose = pointWinLose
        )

        miniSeries?.let {
            MiniSeriesView(
                miniSeries = it
            )
        }

        IconView(
            isPlaying = isPlaying,
            onAddClick = onAdd,
            onDeleteClick = onDelete,
            onSpectator = onSpectator
        )

    }
}

@Composable
private fun LeaguePointView(
    pointWinLose: String
) {
    Row {
        Text(
            text = pointWinLose,
            fontSize = 12.sp
        )
    }
}

@Composable
private fun IconView(
    isPlaying: Boolean = false,
    onAddClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {},
    onSpectator: () -> Unit = {}
) {
    Row(
        Modifier.padding(top = 8.dp)
    ) {
        Icon(
            Icons.Default.Add,
            contentDescription = null,
            modifier = Modifier
                .clickable { onAddClick() }
        )
        Spacer(modifier = Modifier.width(4.dp))
        Icon(
            Icons.Default.Delete,
            contentDescription = null,
            modifier = Modifier
                .clickable { onDeleteClick() }
        )

        Spacer(modifier = Modifier.weight(1f))

        SpectatorView(
            modifier = Modifier,
            isPlaying = isPlaying,
            onSpectator = onSpectator
        )

    }

}

@Composable
private fun MiniSeriesView(
    miniSeries: Summoner.MiniSeries,
) {
    Row {
        miniSeries.progress.let {
            if (it != "No") {
                it.forEach { result ->
                    when (result) {
                        'W' -> Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = Color.Green
                        )
                        'L' -> Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null,
                            tint = Color.Red
                        )
                        'N' -> Icon(
                            imageVector = Icons.Default.HorizontalRule,
                            contentDescription = null
                        )
                    }
                }
            }
        }

    }
}

@Composable
private fun SpectatorView(
    modifier: Modifier = Modifier,
    isPlaying: Boolean,
    onSpectator: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .clickable {
                if (isPlaying) onSpectator()
            }
    ) {
        Text(
            text = stringResource(id = R.string.playing),
            fontSize = 12.sp
        )
        Icon(
            imageVector = Icons.Rounded.Square,
            contentDescription = null,
            modifier = Modifier
                .size(20.dp),
            tint = if (isPlaying) Color.Green else Color.Red
        )
    }
}

@Preview
@Composable
private fun HomeItemPreview() {
    HomeItem(
        summoner = getDummySummoner()
    ) {}
}