package com.example.androidtic_tac_toe.ui.screens.game.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.androidtic_tac_toe.R

data class MenuOption(
    val id: Int,
    val name: String,
    val image: Painter?,
)

/**
 * Displays the dialog with game options.
 * It allows changing the difficulty level and starting a new game.
 */
@Composable
fun OptionsMenu(
    expanded: Boolean,
    onDismiss: () -> Unit,
    onClickItem: (MenuOption) -> Unit,
    modifier: Modifier = Modifier
) {
    val options = listOf(
        MenuOption(
            id = 0,
            name = stringResource(R.string.text_option_start_game),
            image = painterResource(id = R.drawable.startgame)
        ),
        MenuOption(
            id = 1,
            name = stringResource(R.string.text_option_change_difficulty),
            image = painterResource(id = R.drawable.change_difficulty)
        ),
        MenuOption(
            id = 2,
            name = stringResource(R.string.text_option_reset_scores),
            image = painterResource(id = R.drawable.reset_scores)
        ),
    )

    DropdownMenu (
        expanded = expanded,
        onDismissRequest = onDismiss,
        modifier = modifier
    ) {
        options.forEach { option ->
            DropdownMenuItem(
                onClick = {
                    onClickItem(option)
                    onDismiss()
                },
                text = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        option.image?.let {
                            Image(
                                painter = it,
                                contentDescription = null,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(Modifier.width(26.dp))
                        Text(text = option.name)
                    }
                }
            )
        }
    }

}
