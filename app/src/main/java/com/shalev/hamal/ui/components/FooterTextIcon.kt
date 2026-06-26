package com.shalev.hamal.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.shalev.hamal.R

@Composable
fun FooterTextIcon(
    modifier: Modifier = Modifier,
    text: String? = null,
    icon: @Composable (modifier: Modifier) -> Unit,
) {
    if (text.isNullOrEmpty()) {
        icon(modifier)
    } else {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_minimal)),
            modifier = modifier
        ) {
            Text(
                text = text,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            icon(Modifier)
        }
    }
}

@Preview
@Composable
fun PreviewFooterTextIcon() {
    FooterTextIcon(text = "Text") {
        Icon(painter = painterResource(R.drawable.message), contentDescription = null)
    }
}