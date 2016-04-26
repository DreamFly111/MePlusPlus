package com.meplus.fancy.model.entity

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by dandanba on 3/30/16.
 */
data class Binary(
        @JsonProperty("fsize") var fsize: Long
)