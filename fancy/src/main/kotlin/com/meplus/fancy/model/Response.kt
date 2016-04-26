package com.meplus.fancy.model

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by dandanba on 4/11/16.
 */
data class Response<T>(
        @JsonProperty("Code")
        var Code: String ? = null,
        @JsonProperty("Message")
        var Message: String ? = null,
        @JsonProperty("Result")
        var Result: T ? = null
)