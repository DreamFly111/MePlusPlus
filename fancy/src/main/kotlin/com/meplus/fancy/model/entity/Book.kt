package com.meplus.fancy.model.entity

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

/**
 * Created by dandanba on 3/30/16.
 */
data class Book(
        @JsonProperty("SysNo") var SysNo: Int,
        @JsonProperty("Status") var Status: Int,
        @JsonProperty("Author") var Author: String,
        @JsonProperty("BookName") var BookName: String,
        @JsonProperty("BookISBN") var BookISBN: String,
        @JsonProperty("BookCoverId") var BookCoverId: String,
        @JsonProperty("ActionDate") var ActionDate: Date,
        @JsonProperty("PredictPay") var PredictPay: Int,
        @JsonProperty("ExpiredDay") var ExpiredDay: Int,
        @JsonProperty("ResultNo") var ResultNo: Int
)