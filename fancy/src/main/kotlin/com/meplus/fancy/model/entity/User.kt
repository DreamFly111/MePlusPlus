package com.meplus.fancy.model.entity

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by dandanba on 3/30/16.
 */
data class User(
        @JsonProperty("IconUrl") var IconUrl: String,
        @JsonProperty("BorrowBookList") var BorrowBookList: List<Book>,
        @JsonProperty("NickName") var NickName: String,
        @JsonProperty("UserId") var UserId: String
)
