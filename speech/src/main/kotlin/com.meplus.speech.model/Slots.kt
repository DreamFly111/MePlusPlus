package com.meplus.speech.model

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by dandanba on 3/30/16.
 */
data class Slots(
        @JsonProperty("name") var name: String ? = null,
        @JsonProperty("location") var location: Location ? = null,
        @JsonProperty("datetime") var datetime: Datetime ? = null,
        @JsonProperty("category") var category: String ? = null
)