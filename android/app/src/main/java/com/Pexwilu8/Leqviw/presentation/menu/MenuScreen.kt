package com.Pexwilu8.Leqviw.presentation.menu

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Pexwilu8.Leqviw.R
import com.Pexwilu8.Leqviw.core.di.AppViewModelFactory
import com.Pexwilu8.Leqviw.core.navigation.Screen
import com.Pexwilu8.Leqviw.core.theme.AccentAmethyst
import com.Pexwilu8.Leqviw.core.theme.AccentGold
import com.Pexwilu8.Leqviw.core.theme.GemEmerald
import com.Pexwilu8.Leqviw.core.theme.GemSapphire
import com.Pexwilu8.Leqviw.core.theme.GoldHighlight
import com.Pexwilu8.Leqviw.core.theme.TextMuted
import com.Pexwilu8.Leqviw.core.theme.TextPrimary
import com.Pexwilu8.Leqviw.core.theme.TextSecondary
import com.Pexwilu8.Leqviw.core.ui.GemVisuals
import com.Pexwilu8.Leqviw.core.ui.PrimaryButton
import com.Pexwilu8.Leqviw.core.ui.ScreenBackground
import com.Pexwilu8.Leqviw.core.ui.StatCardData
import com.Pexwilu8.Leqviw.core.ui.StatRow
import com.Pexwilu8.Leqviw.core.ui.TileButton
import com.Pexwilu8.Leqviw.core.ui.VelvetBlobs
import com.Pexwilu8.Leqviw.core.ui.VelvetSurface

private val menuOverlay = listOf(
    Color(0x33000000),
    Color(0x660B0B0F),
    Color(0xF20B0B0F)
)

@Composable
fun MenuScreen(
    onPlay: (Int) -> Unit,
    onOpen: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    val menuViewModel: MenuViewModel = viewModel(factory = AppViewModelFactory)
    val state by menuViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        menuViewModel.refresh()
    }

    MenuContent(
        state = state,
        onPlay = { onPlay(state.currentStage) },
        onOpen = onOpen,
        modifier = modifier
    )
}

@Composable
private fun MenuContent(
    state: MenuUiState,
    onPlay: () -> Unit,
    onOpen: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        ScreenBackground(overlay = menuOverlay)
        VelvetBlobs(amethystAlpha = 0.14f, brassAlpha = 0.1f)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            VaultAlcove(modifier = Modifier.weight(1f))
            VelvetSurface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
                elevation = 20.dp,
                borderColor = AccentGold.copy(alpha = 0.35f)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.app_title),
                        color = TextPrimary,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = stringResource(R.string.menu_tagline),
                        color = TextSecondary,
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    if (state.hasProgress) {
                        StatRow(
                            cards = listOf(
                                StatCardData(
                                    labelRes = R.string.stat_best,
                                    value = GemVisuals.groupedNumber(state.bestScore),
                                    accent = AccentGold
                                ),
                                StatCardData(
                                    labelRes = R.string.stat_gems,
                                    value = GemVisuals.groupedNumber(state.gems),
                                    accent = AccentAmethyst
                                )
                            )
                        )
                    } else {
                        Text(
                            text = stringResource(R.string.no_cases_yet),
                            color = TextMuted,
                            style = MaterialTheme.typography.labelSmall,
                            textAlign = TextAlign.Center
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    PrimaryButton(
                        textRes = R.string.play,
                        onClick = onPlay,
                        leadingIcon = R.drawable.ic_play,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    HubTiles(onOpen = onOpen)
                }
            }
        }
    }
}

@Composable
private fun VaultAlcove(modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition(label = "menuArc")
    val float by transition.animateFloat(
        initialValue = -5f,
        targetValue = 5f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 6000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "arcFloat"
    )
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Image(
            painter = painterResource(R.drawable.logo),
            contentDescription = stringResource(R.string.cd_logo),
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(96.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .background(Color(0x33D4AF37), RoundedCornerShape(6.dp))
                .padding(horizontal = 12.dp, vertical = 4.dp)
        ) {
            Text(
                text = stringResource(R.string.brand_plate),
                color = GoldHighlight,
                style = MaterialTheme.typography.labelSmall
            )
        }
        Spacer(modifier = Modifier.height(18.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(18.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ArcGem(spriteRes = R.drawable.sprite_1, offset = float, size = 44)
            ArcGem(spriteRes = R.drawable.sprite_3, offset = -float, size = 52)
            ArcGem(spriteRes = R.drawable.sprite_5, offset = float, size = 44)
        }
    }
}

@Composable
private fun ArcGem(spriteRes: Int, offset: Float, size: Int) {
    Image(
        painter = painterResource(spriteRes),
        contentDescription = null,
        contentScale = ContentScale.Fit,
        modifier = Modifier
            .size(size.dp)
            .graphicsLayer {
                translationY = offset * density
                alpha = 0.78f
            }
    )
}

@Composable
private fun HubTiles(onOpen: (Screen) -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            TileButton(
                textRes = R.string.level_map,
                icon = R.drawable.ic_map,
                onClick = { onOpen(Screen.LevelMap) },
                accent = AccentGold,
                modifier = Modifier.weight(1f)
            )
            TileButton(
                textRes = R.string.missions,
                icon = R.drawable.ic_mission,
                onClick = { onOpen(Screen.Missions) },
                accent = GemSapphire,
                modifier = Modifier.weight(1f)
            )
            TileButton(
                textRes = R.string.rewards,
                icon = R.drawable.ic_reward,
                onClick = { onOpen(Screen.Rewards) },
                accent = GemEmerald,
                modifier = Modifier.weight(1f)
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            TileButton(
                textRes = R.string.collection,
                icon = R.drawable.ic_collection,
                onClick = { onOpen(Screen.Collection) },
                accent = AccentAmethyst,
                modifier = Modifier.weight(1f)
            )
            TileButton(
                textRes = R.string.tutorial,
                icon = R.drawable.ic_tutorial,
                onClick = { onOpen(Screen.Tutorial) },
                accent = GoldHighlight,
                modifier = Modifier.weight(1f)
            )
            TileButton(
                textRes = R.string.settings,
                icon = R.drawable.ic_settings,
                onClick = { onOpen(Screen.Settings) },
                accent = TextSecondary,
                modifier = Modifier.weight(1f)
            )
        }
    }
}
