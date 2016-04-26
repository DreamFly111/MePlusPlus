package com.meplus.fancy.model.entity

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by dandanba on 3/30/16.
 */
data class Baby(
        @JsonProperty("babyId") var babyId: String,
        @JsonProperty("check") var check: String,
        @JsonProperty("parentsUserId") var parentsUserId: String,
        @JsonProperty("time") var time: String
)