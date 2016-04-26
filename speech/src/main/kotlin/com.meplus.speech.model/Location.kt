package com.meplus.speech.model

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by dandanba on 3/30/16.
 */
data class Location(
        @JsonProperty("type") var type: String ,
        @JsonProperty("city") var city: String ,
        @JsonProperty("poi") var poi: String
)