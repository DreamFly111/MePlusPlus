package com.meplus.speech.model

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by dandanba on 3/30/16.
 */
data class Common(
        // music
        @JsonProperty("singer") var singer: String ? = null,
        @JsonProperty("sourceName") var sourceName: String  ? = null,
        @JsonProperty("name") var name: String  ? = null,
        @JsonProperty("downloadUrl") var downloadUrl: String  ? = null,
        // weather
        @JsonProperty("airQuality") var airQuality: String  ? = null,
        // @JsonProperty("sourceName") var sourceName: String  ? = null,
        @JsonProperty("date") var date: String  ? = null,
        @JsonProperty("lastUpdateTime") var lastUpdateTime: String  ? = null,
        @JsonProperty("dateLong") var dateLong: String  ? = null,
        @JsonProperty("city") var city: String  ? = null,
        @JsonProperty("wind") var wind: String  ? = null,
        @JsonProperty("windLevel") var windLevel: Int  ? = null,
        @JsonProperty("weather") var weather: String  ? = null,
        @JsonProperty("tempRange") var tempRange: String  ? = null,
        @JsonProperty("province") var province: String  ? = null,
        @JsonProperty("humidity") var humidity: String  ? = null
)