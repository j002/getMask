package com.app.fr.getmymask.api.helpers

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

class DateTimeAdapter : JsonAdapter<DateTime>() {
    //todo do something
    override fun fromJson(reader: JsonReader?): DateTime? {
        val df = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")
        return df.parseDateTime(reader!!.nextString())
    }

    override fun toJson(writer: JsonWriter?, value: DateTime?) {
        writer?.value(value.toString())
    }

}