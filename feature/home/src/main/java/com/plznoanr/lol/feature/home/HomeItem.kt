package com.plznoanr.lol.feature.home

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
import androidx.compose.material.icons.rounded.Square
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.plznoanr.lol.core.designsystem.component.IconImage
import com.plznoanr.lol.core.designsystem.component.summoner.TierIcon
import com.plznoanr.lol.core.designsystem.icon.AppIcons
import com.plznoanr.lol.core.designsystem.theme.LolUserSearchComposeTheme
import com.plznoanr.lol.core.model.Summoner
import com.plznoanr.lol.core.model.getDummySummoner

@Composable
fun HomeItem(
    modifier: Modifier = Modifier,
    summoner: Summoner,
    onBookmarked: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.secondary
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
                    tierIcon = TierIcon(
                        summoner.tier
                    ),
                    isBookmark = summoner.isBookMarked,
                    onBookmarked = onBookmarked
                )

                Spacer(modifier = Modifier.height(20.dp))

                LeagueInfoView(
                    modifier = modifier
                        .weight(1f),
                    pointWinLose = summoner.lpWinLose,
                    miniSeries = summoner.miniSeries,
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
    tierIcon: Painter,
    isBookmark: Boolean,
    onBookmarked: () -> Unit
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

        Spacer(modifier = Modifier.weight(1f))

        IconButton(onClick = { onBookmarked() }) {
            Icon(
                imageVector = if (isBookmark) AppIcons.BookMark else AppIcons.BookMarkBorder,
                contentDescription = null
            )
        }

    }
}

@Composable
private fun LeagueInfoView(
    modifier: Modifier = Modifier,
    pointWinLose: String,
    miniSeries: Summoner.MiniSeries?,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
    ) {

        LeaguePointView(
            pointWinLose = pointWinLose
        )

        miniSeries?.let {
            MiniSeriesView(
                miniSeries = it
            )
        }

    }
}

@Composable
private fun LeaguePointView(
    pointWinLose: String
) {
    Row {
        Text(
            text = pointWinLose,
            fontSize = 15.sp
        )
    }
}

//@Composable
//private fun IconView(
//    isPlaying: Boolean = false,
//    onAddClick: () -> Unit = {},
//    onDeleteClick: () -> Unit = {},
//    onSpectator: () -> Unit = {}
//) {
//    Row(
//        Modifier.padding(top = 8.dp)
//    ) {
//        Icon(
//            AppIcons.Add,
//            contentDescription = null,
//            modifier = Modifier
//                .clickable { onAddClick() }
//        )
//        Spacer(modifier = Modifier.width(4.dp))
//        Icon(
//            AppIcons.Delete,
//            contentDescription = null,
//            modifier = Modifier
//                .clickable { onDeleteClick() }
//        )
//
//        Spacer(modifier = Modifier.weight(1f))
//
//        SpectatorView(
//            modifier = Modifier,
//            isPlaying = isPlaying,
//            onSpectator = onSpectator
//        )
//
//    }
//
//}

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
                            imageVector = AppIcons.Check,
                            contentDescription = null,
                            tint = Color.Green
                        )
                        'L' -> Icon(
                            imageVector = AppIcons.Close,
                            contentDescription = null,
                            tint = Color.Red
                        )
                        'N' -> Icon(
                            imageVector = AppIcons.HorizontalRule,
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
    LolUserSearchComposeTheme(darkTheme = false) {
        HomeItem(
            summoner = getDummySummoner()
        ) {}
    }

}