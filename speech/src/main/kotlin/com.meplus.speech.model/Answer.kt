package com.meplus.speech.model

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by dandanba on 3/30/16.
 */
data class Answer(
        @JsonProperty("type") var type: String,
        @JsonProperty("text") var text: String
)