package com.rr.task.model


import com.google.gson.annotations.SerializedName

data class VideoListData(
    @SerializedName("categories")
    val categories: List<Category>
) {
    data class Category(
        @SerializedName("name")
        val name: String,
        @SerializedName("videos")
        val videos: List<Video>
    ) {
        data class Video(
            @SerializedName("description")
            val description: String,
            @SerializedName("sources")
            val sources: List<String>,
            @SerializedName("subtitle")
            val subtitle: String,
            @SerializedName("thumb")
            val thumb: String,
            @SerializedName("title")
            val title: String
        )
    }
}