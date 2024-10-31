package com.shalev.hamal.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.tooling.preview.Preview

private val regex = Regex("<a\\s+[^>]*href=\"([^\"]+)\"[^>]*>(.*?)</a>")

@Composable
fun LinkText(text: String, modifier: Modifier = Modifier) {
    val linkColor = MaterialTheme.colorScheme.secondary

    val annotatedStringBuilder = remember(text) {
        buildAnnotatedString {
            var lastEndIndex = 0

            regex.findAll(text).forEach { matchResult ->
                if (lastEndIndex < matchResult.range.first) {
                    append(text.substring(lastEndIndex, matchResult.range.first))
                }

                val url = matchResult.groups[1]?.value
                val linkText = matchResult.groups[2]?.value

                if (url != null && linkText != null) {
                    withLink(
                        LinkAnnotation.Url(
                            url, TextLinkStyles(
                                style = SpanStyle(
                                    textDecoration = TextDecoration.Underline,
                                    color = linkColor
                                )
                            )
                        )
                    ) {
                        append(linkText)
                    }
                }

                lastEndIndex = matchResult.range.last + 1
            }

            if (lastEndIndex < text.length) {
                append(text.substring(lastEndIndex))
            }
        }
    }

    Text(
        text = annotatedStringBuilder,
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewLinkText() {
    LinkText("Click this link <a href=\"https://www.example.com\">Example Link</a> to see it")
}