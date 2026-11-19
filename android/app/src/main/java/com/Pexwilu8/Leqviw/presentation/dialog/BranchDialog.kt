package com.Pexwilu8.Leqviw.presentation.dialog

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.Pexwilu8.Leqviw.R
import com.Pexwilu8.Leqviw.core.theme.AccentAmethyst
import com.Pexwilu8.Leqviw.core.theme.AccentGold
import com.Pexwilu8.Leqviw.core.theme.TextPrimary
import com.Pexwilu8.Leqviw.core.theme.TextSecondary
import com.Pexwilu8.Leqviw.core.ui.SecondaryButton
import com.Pexwilu8.Leqviw.core.ui.VelvetSurface
import com.Pexwilu8.Leqviw.domain.model.BranchDoor

@Composable
fun BranchDialog(
    doors: List<BranchDoor>,
    onChoose: (BranchDoor) -> Unit
) {
    Dialog(
        onDismissRequest = { onChoose(BranchDoor.SERVICE_HALL) },
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
    ) {
        VelvetSurface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
            elevation = 18.dp,
            borderColor = AccentGold.copy(alpha = 0.5f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.branch_title),
                    color = TextPrimary,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(R.string.branch_hint),
                    color = TextSecondary,
                    style = MaterialTheme.typography.labelSmall,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                doors.forEach { door ->
                    DoorOption(door = door, onChoose = onChoose)
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
private fun DoorOption(door: BranchDoor, onChoose: (BranchDoor) -> Unit) {
    val titleRes = when (door) {
        BranchDoor.VELVET_CURTAIN -> R.string.door_curtain
        BranchDoor.SERVICE_HALL -> R.string.door_service
    }
    val noteRes = when (door) {
        BranchDoor.VELVET_CURTAIN -> R.string.door_curtain_note
        BranchDoor.SERVICE_HALL -> R.string.door_service_note
    }
    val spriteRes = when (door) {
        BranchDoor.VELVET_CURTAIN -> R.drawable.sprite_5
        BranchDoor.SERVICE_HALL -> R.drawable.sprite_2
    }
    val accent = when (door) {
        BranchDoor.VELVET_CURTAIN -> AccentAmethyst
        BranchDoor.SERVICE_HALL -> AccentGold
    }
    VelvetSurface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = 8.dp,
        borderColor = accent.copy(alpha = 0.45f)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = painterResource(spriteRes),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(titleRes),
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
            Spacer(modifier = Modifier.width(10.dp))
            SecondaryButton(
                textRes = R.string.choose,
                onClick = { onChoose(door) },
                accent = accent
            )
        }
    }
}
