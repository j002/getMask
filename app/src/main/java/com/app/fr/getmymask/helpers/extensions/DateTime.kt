package com.app.fr.getmymask.helpers.extensions

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

fun DateTime.convertDate(): String {
    val fmt = DateTimeFormat.forPattern("dd / MM / yyyy à HH'h'mm")
    val str = this.toString(fmt)
    return str
}
