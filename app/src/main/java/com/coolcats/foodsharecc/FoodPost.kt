package com.coolcats.foodsharecc

data class FoodPost(val userId: String, val postId: String, val imageUrl: String, val caption: String){
    constructor(): this("", "", "", "")
}