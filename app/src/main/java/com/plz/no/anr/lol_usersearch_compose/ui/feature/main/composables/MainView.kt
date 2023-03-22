package com.plz.no.anr.lol_usersearch_compose.ui.feature.main.composables

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.plz.no.anr.lol_usersearch_compose.ui.feature.common.IconImage
import com.plz.no.anr.lol_usersearch_compose.ui.feature.main.MainContract
import com.plz.no.anr.lol_usersearch_compose.ui.theme.sky
import com.plznoanr.domain.model.Profile
import com.plznoanr.domain.model.Summoner
import com.plznoanr.lol_usersearch_compose.R

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainView(
    modifier: Modifier = Modifier,
    summonerList: List<Summoner>,
    pullRefreshState: PullRefreshState,
    onEvent: (MainContract.Event) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        Text(
            text = stringResource(id = R.string.delete_all),
            modifier = Modifier
                .align(Alignment.End)
                .padding(top = 16.dp, end = 16.dp)
                .clickable { onEvent(MainContract.Event.Summoner.OnDeleteAll) },
        )
        Box(
            modifier = Modifier
                .pullRefresh(state = pullRefreshState)
        ) {
            LazyColumn {
                items(summonerList) {
                    MainItem(summoner = it) { event ->
                        onEvent(event)
                    }
                }
            }
        }

    }
}

@Composable
fun Drawers(
    data: Profile,
    apiKey: String?,
    onAddKey: (String) -> Unit,
    onDeleteKey: () -> Unit
) {

    IconImage(
        modifier = Modifier
            .padding(top = 30.dp, start = 16.dp, bottom = 16.dp)
            .size(size = 100.dp)
            .clip(RoundedCornerShape(10)),
        url = data.icon,
        scale = ContentScale.FillWidth,
    )

    Text(
        text = data.name,
        color = Color.White,
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .padding(start = 16.dp, bottom = 16.dp)
    )

    Text(
        text = data.getLevels(),
        color = Color.White,
        fontSize = 18.sp,
        modifier = Modifier
            .padding(start = 16.dp, bottom = 16.dp)
    )

    Divider(
        color = Color.White,
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, bottom = 12.dp)
    )

    KeyView(
        apiKey = apiKey,
        onAddKey = onAddKey,
        onDeleteKey = onDeleteKey
    )

}


@Composable
fun KeyView(
    apiKey: String?,
    onAddKey: (String) -> Unit,
    onDeleteKey: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(
                start = 16.dp,
            )
    ) {
        Text(
            text = stringResource(id = R.string.api_key),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
        )

        Spacer(modifier = Modifier.height(16.dp))

        apiKey?.let {
            Text(
                text = it,
                color = Color.White,
                modifier = Modifier
                    .border(
                        width = 2.dp,
                        color = Color.White,
                    )
                    .padding(16.dp),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(onClick = onDeleteKey) {
                Text(
                    text = stringResource(id = R.string.delete),
                    color = sky
                )
            }
        } ?: run {
            KeyAddView { onAddKey(it) }
        }

    }
}


@Composable
fun KeyAddView(
    onAddKey: (String) -> Unit
) {
    var textState by remember {
        mutableStateOf("")
    }
    Row(
        modifier = Modifier
            .padding(end = 16.dp)
    ) {
        OutlinedTextField(
            value = textState,
            onValueChange = { textState = it },
            label = { Text(
                text = stringResource(id = R.string.add_key),
                color = Color.White
            ) },
            modifier = Modifier
                .weight(1f),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.White,
                textColor = Color.White,
                focusedLabelColor = Color.White,
                unfocusedLabelColor = Color.White,
                cursorColor = Color.White,
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.width(4.dp))

        IconButton(
            onClick = { onAddKey(textState) },
            Modifier
                .padding(top = 8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                tint = Color.White
            )
        }
    }
}