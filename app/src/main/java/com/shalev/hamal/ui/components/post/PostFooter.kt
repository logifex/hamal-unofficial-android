package com.shalev.hamal.ui.components.post

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import com.shalev.hamal.R
import com.shalev.hamal.ui.components.FooterTextIcon

@Composable
fun PostFooter(
    likesCount: Int,
    commentsCount: Int,
    shareUrl: String,
    onCommentsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Box(modifier = Modifier.weight(1f)) {
            Icon(
                painter = painterResource(R.drawable.share),
                contentDescription = stringResource(R.string.share),
                tint = Color.Unspecified,
                modifier = Modifier.clickable { onShareClick(shareUrl, context) }
            )
        }
        FooterTextIcon(
            text = if (likesCount > 0) likesCount.toString() else null,
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                painter = painterResource(R.drawable.heart),
                contentDescription = stringResource(R.string.likes),
                tint = Color.Unspecified
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        CommentsButton(commentsCount = commentsCount, onClick = onCommentsClick)
    }
}

fun onShareClick(shareUrl: String, context: Context) {
    val sendIntent = Intent(Intent.ACTION_SEND).apply {
        putExtra(Intent.EXTRA_TEXT, shareUrl)
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, null)
    ContextCompat.startActivity(
        context,
        shareIntent,
        null
    )
}

@Preview
@Composable
fun PreviewPostFooter() {
    PostFooter(5, 2, "", {})
}