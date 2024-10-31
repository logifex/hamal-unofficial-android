package com.shalev.hamal.utils

import android.content.Context
import com.shalev.hamal.R

private const val second = 1000
private const val minute = second * 60
private const val hour = minute * 60
private const val day = hour * 24

fun convertToTimeAgo(date: Long, context: Context): String {
    val now = System.currentTimeMillis()
    val diff = now - date

    return when {
        diff >= day -> if (diff / day > 1) context.getString(
            R.string.days_ago,
            (diff / day).toString()
        )
        else context.getString(R.string.day_ago)

        diff >= hour -> if (diff / hour > 1) context.getString(
            R.string.hours_ago,
            (diff / hour).toString()
        )
        else context.getString(R.string.hour_ago)

        diff >= minute -> if (diff / minute > 1) context.getString(
            R.string.minutes_ago,
            (diff / minute).toString()
        )
        else context.getString(R.string.minute_ago)

        diff >= second -> if (diff / second > 1) context.getString(
            R.string.seconds_ago,
            (diff / second).toString()
        )
        else context.getString(R.string.second_ago)

        else -> context.getString(R.string.just_now)
    }
}