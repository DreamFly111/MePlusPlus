package com.meplus.fancy.model.entity

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by dandanba on 3/30/16.
 */
data class Address(
        @JsonProperty("lon") var lon: Float,
        @JsonProperty("level") var level: Int,
        @JsonProperty("address") var address: String,
        @JsonProperty("cityName") var cityName: String,
        @JsonProperty("alevel") var alevel: Int,
        @JsonProperty("lat") var lat: Float
)