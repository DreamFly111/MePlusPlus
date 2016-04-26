package com.meplus.speech.model

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by dandanba on 3/30/16.
 */
data class Datetime(
        @JsonProperty("date") var date: String,
        @JsonProperty("type") var type: String
)