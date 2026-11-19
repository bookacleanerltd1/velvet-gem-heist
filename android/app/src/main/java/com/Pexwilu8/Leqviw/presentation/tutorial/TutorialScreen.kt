package com.Pexwilu8.Leqviw.presentation.tutorial

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.Pexwilu8.Leqviw.core.theme.AccentAmethyst
import com.Pexwilu8.Leqviw.core.theme.AccentGold
import com.Pexwilu8.Leqviw.core.theme.SurfaceRaised
import com.Pexwilu8.Leqviw.core.theme.TextPrimary
import com.Pexwilu8.Leqviw.core.theme.TextSecondary
import com.Pexwilu8.Leqviw.core.ui.PrimaryButton
import com.Pexwilu8.Leqviw.core.ui.ScreenScaffold
import com.Pexwilu8.Leqviw.core.ui.SecondaryButton
import com.Pexwilu8.Leqviw.core.ui.VelvetSurface

@Composable
fun TutorialScreen(
    onStart: () -> Unit,
    onMenu: () -> Unit,
    modifier: Modifier = Modifier
) {
    val tutorialViewModel: TutorialViewModel = viewModel(factory = AppViewModelFactory)
    val state by tutorialViewModel.state.collectAsStateWithLifecycle()

    ScreenScaffold(
        titleRes = R.string.tutorial,
        subtitleRes = R.string.tutorial_subtitle,
        onMenu = {
            tutorialViewModel.markSeen()
            onMenu()
        },
        modifier = modifier
    ) {
        val step = state.step
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (step != null) {
                VelvetSurface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    elevation = 12.dp,
                    borderColor = AccentGold.copy(alpha = 0.4f)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(96.dp)
                                .background(SurfaceRaised, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(step.spriteRes),
                                contentDescription = stringResource(R.string.cd_gem),
                                contentScale = ContentScale.Fit,
                                modifier = Modifier.size(62.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = stringResource(step.titleRes),
                            color = TextPrimary,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = stringResource(step.bodyRes),
                            color = TextSecondary,
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.step_label, state.index + 1, state.steps.size),
                color = TextSecondary,
                style = MaterialTheme.typography.labelSmall
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                state.steps.indices.forEach { index ->
                    Box(
                        modifier = Modifier
                            .size(9.dp)
                            .background(
                                color = if (index == state.index) AccentGold else AccentAmethyst.copy(alpha = 0.4f),
                                shape = CircleShape
                            )
                    )
                }
            }
            Spacer(modifier = Modifier.height(18.dp))
            if (state.isLast) {
                PrimaryButton(
                    textRes = R.string.start,
                    onClick = {
                        tutorialViewModel.markSeen()
                        onStart()
                    },
                    leadingIcon = R.drawable.ic_play,
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                PrimaryButton(
                    textRes = R.string.next,
                    onClick = tutorialViewModel::next,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            SecondaryButton(
                textRes = R.string.back,
                onClick = tutorialViewModel::previous,
                enabled = state.index > 0,
                leadingIcon = R.drawable.ic_back,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
