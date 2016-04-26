package com.meplus.fir

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by dandanba on 3/30/16.
 */
data class FirVersion(
        @JsonProperty("name") var name: String,
        @JsonProperty("version") var version: Int,
        @JsonProperty("changelog") var changelog: String,
        @JsonProperty("updated_at") var updated_at: Long,
        @JsonProperty("versionShort") var versionShort: String,
        @JsonProperty("build") var build: Int,
        @JsonProperty("installUrl") var installUrl: String,
        @JsonProperty("install_url") var install_url: String,
        @JsonProperty("direct_install_url") var direct_install_url: String,
        @JsonProperty("update_url") var update_url: String,
        @JsonProperty("binary") var binary: Binary//
)