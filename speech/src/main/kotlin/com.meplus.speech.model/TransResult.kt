package com.meplus.speech.model

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by dandanba on 3/30/16.
 */
data class TransResult(
        @JsonProperty("from") var from: String,
        @JsonProperty("to") var to: String,
        @JsonProperty("trans_result") var trans_result: List<Trans> ? = null

)