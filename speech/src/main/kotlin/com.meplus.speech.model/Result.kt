package com.meplus.speech.model

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by dandanba on 3/30/16.
 */
data class Result(
        @JsonProperty("rc") var rc: Int,
        @JsonProperty("text") var text: String,
        @JsonProperty("operation") var operation: String ? = null,
        @JsonProperty("answer") var answer: Answer ? = null,
        @JsonProperty("service") var service: String? = null, //openQA, music
        @JsonProperty("moreResults") var moreResults: List<Result>  ? = null,
        @JsonProperty("webPage") var webPage: WebPage  ? = null,
        @JsonProperty("semantic") var semantic: Semantic  ? = null,
        @JsonProperty("data") var data: Data  ? = null

)