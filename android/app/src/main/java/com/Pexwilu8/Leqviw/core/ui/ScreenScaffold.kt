package com.Pexwilu8.Leqviw.core.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.Pexwilu8.Leqviw.R
import com.Pexwilu8.Leqviw.core.theme.TextPrimary
import com.Pexwilu8.Leqviw.core.theme.TextSecondary

private val SubScreenOverlay = listOf(
    Color(0xE60B0B0F),
    Color(0xF2141418),
    Color(0xFA0B0B0F)
)

@Composable
fun ScreenScaffold(
    @StringRes titleRes: Int,
    @StringRes subtitleRes: Int,
    onMenu: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Box(modifier = modifier.fillMaxSize()) {
        ScreenBackground(overlay = SubScreenOverlay)
        VelvetBlobs()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(titleRes),
                color = TextPrimary,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(subtitleRes),
                color = TextSecondary,
                style = MaterialTheme.typography.labelSmall,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(14.dp))
            content()
            Spacer(modifier = Modifier.height(12.dp))
            SecondaryButton(
                textRes = R.string.menu,
                onClick = onMenu,
                leadingIcon = R.drawable.ic_home,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(14.dp))
        }
    }
}
