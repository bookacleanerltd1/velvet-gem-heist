package com.Pexwilu8.Leqviw.presentation.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Pexwilu8.Leqviw.R
import com.Pexwilu8.Leqviw.core.di.AppViewModelFactory
import com.Pexwilu8.Leqviw.core.theme.AccentAmethyst
import com.Pexwilu8.Leqviw.core.theme.AccentGold
import com.Pexwilu8.Leqviw.core.theme.GemRuby
import com.Pexwilu8.Leqviw.core.theme.Hairline
import com.Pexwilu8.Leqviw.core.theme.SurfaceRaised
import com.Pexwilu8.Leqviw.core.theme.TextMuted
import com.Pexwilu8.Leqviw.core.theme.TextOnGold
import com.Pexwilu8.Leqviw.core.theme.TextPrimary
import com.Pexwilu8.Leqviw.core.theme.TextSecondary
import com.Pexwilu8.Leqviw.core.ui.GemVisuals
import com.Pexwilu8.Leqviw.core.ui.ScreenScaffold
import com.Pexwilu8.Leqviw.core.ui.SecondaryButton
import com.Pexwilu8.Leqviw.core.ui.VelvetSurface
import com.Pexwilu8.Leqviw.domain.model.RevealSpeed
import com.Pexwilu8.Leqviw.presentation.dialog.ConfirmDialog

@Composable
fun SettingsScreen(
    onMenu: () -> Unit,
    modifier: Modifier = Modifier
) {
    val settingsViewModel: SettingsViewModel = viewModel(factory = AppViewModelFactory)
    val state by settingsViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        settingsViewModel.refresh()
    }

    ScreenScaffold(
        titleRes = R.string.settings,
        subtitleRes = R.string.settings_subtitle,
        onMenu = onMenu,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            VelvetSurface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                elevation = 10.dp,
                borderColor = AccentGold.copy(alpha = 0.3f)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = stringResource(R.string.setting_reveal),
                        color = TextPrimary,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = stringResource(R.string.setting_reveal_note),
                        color = TextSecondary,
                        style = MaterialTheme.typography.labelSmall
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        RevealSpeed.entries.forEach { speed ->
                            SecondaryButton(
                                textRes = GemVisuals.speedRes(speed),
                                onClick = { settingsViewModel.setSpeed(speed) },
                                accent = if (state.settings.revealSpeed == speed) AccentGold else Hairline,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
            ToggleRow(
                labelRes = R.string.setting_chalk,
                noteRes = R.string.setting_chalk_note,
                checked = state.settings.chalkMarks,
                onToggle = settingsViewModel::toggleChalk
            )
            ToggleRow(
                labelRes = R.string.setting_hint,
                noteRes = R.string.setting_hint_note,
                checked = state.settings.hintPlate,
                onToggle = settingsViewModel::toggleHint
            )
            ToggleRow(
                labelRes = R.string.setting_motion,
                noteRes = R.string.setting_motion_note,
                checked = state.settings.reducedMotion,
                onToggle = settingsViewModel::toggleMotion
            )
            SecondaryButton(
                textRes = R.string.reset_progress,
                onClick = settingsViewModel::askReset,
                accent = GemRuby,
                modifier = Modifier.fillMaxWidth()
            )
        }
        Text(
            text = stringResource(R.string.settings_footer),
            color = TextMuted,
            style = MaterialTheme.typography.labelSmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = stringResource(R.string.version_line),
            color = TextMuted,
            style = MaterialTheme.typography.labelSmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }

    if (state.showReset) {
        ConfirmDialog(
            titleRes = R.string.reset_title,
            bodyRes = R.string.reset_body,
            confirmRes = R.string.reset,
            onConfirm = settingsViewModel::confirmReset,
            onDismiss = settingsViewModel::dismissReset
        )
    }
}

@Composable
private fun ToggleRow(
    labelRes: Int,
    noteRes: Int,
    checked: Boolean,
    onToggle: () -> Unit
) {
    val stateLabel = stringResource(if (checked) R.string.on else R.string.off)
    VelvetSurface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        elevation = 8.dp,
        borderColor = if (checked) AccentAmethyst.copy(alpha = 0.5f) else Hairline
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .semantics { stateDescription = stateLabel },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(labelRes),
                    color = TextPrimary,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = stringResource(noteRes),
                    color = TextSecondary,
                    style = MaterialTheme.typography.labelSmall
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Box(contentAlignment = Alignment.Center) {
                Switch(
                    checked = checked,
                    onCheckedChange = { onToggle() },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = TextOnGold,
                        checkedTrackColor = AccentGold,
                        uncheckedThumbColor = TextSecondary,
                        uncheckedTrackColor = SurfaceRaised
                    )
                )
            }
        }
    }
}
