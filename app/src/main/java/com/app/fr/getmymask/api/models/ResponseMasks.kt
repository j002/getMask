package com.app.fr.getmymask.api.models

import javax.annotation.Generated
import com.squareup.moshi.Json

@Generated("com.robohorse.robopojogenerator")
data class ResponseMasks(

	@field:Json(name="owner")
	val owner: String? = null,

	@field:Json(name="quantity")
	val quantity: Int? = null,

	@field:Json(name="latitude")
	val latitude: Double? = null,

	@field:Json(name="__v")
	val V: Int? = null,

	@field:Json(name="_id")
	val id: String? = null,

	@field:Json(name="position")
	val position: List<Double?>? = null,

	@field:Json(name="updatedDate")
	val updatedDate: String? = null,

	@field:Json(name="longitude")
	val longitude: Double? = null
)