package com.plznoanr.lol.core.ui.error

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.plznoanr.lol.core.common.model.Result
import com.plznoanr.lol.core.common.model.Result.ForbiddenError.description
import com.plznoanr.lol.core.designsystem.R

@Composable
fun ErrorScreen(
    modifier: Modifier = Modifier,
    error: Result.Error = Result.DefaultError,
    onRetry: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(80.dp))

        Icon(
            imageVector = Icons.Default.Error,
            contentDescription = null,
            modifier = Modifier
                .size(100.dp)
                .align(Alignment.CenterHorizontally),
            tint = Color.Red
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = error.message,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = error.description(),
            fontSize = 18.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(40.dp))

        RetryComponent(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .clickable { onRetry() }
        )

    }

}

@Composable
fun RetryComponent(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
        .size(120.dp),
        contentAlignment = Alignment.Center
    ) {
        Column {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .align(Alignment.CenterHorizontally),
            )
            Text(
                text = stringResource(id = R.string.retry),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Preview
@Composable
private fun ErrorScreenPreview() {
    ErrorScreen()
}