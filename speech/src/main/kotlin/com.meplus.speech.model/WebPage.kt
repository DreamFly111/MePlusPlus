package com.meplus.speech.model

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by dandanba on 3/30/16.
 */
data class WebPage(
        @JsonProperty("header") var header: String,
        @JsonProperty("url") var url: String
)