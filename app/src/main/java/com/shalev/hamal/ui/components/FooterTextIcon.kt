package com.shalev.hamal.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
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
    text: String? = null,
    modifier: Modifier = Modifier,
    icon: @Composable () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        text?.let {
            Text(
                text = text,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(modifier = Modifier.width(dimensionResource(R.dimen.padding_minimal)))
        }
        icon()
    }
}

@Preview
@Composable
fun PreviewFooterTextIcon() {
    FooterTextIcon(text = "Text") {
        Icon(painter = painterResource(R.drawable.message), contentDescription = null)
    }
}