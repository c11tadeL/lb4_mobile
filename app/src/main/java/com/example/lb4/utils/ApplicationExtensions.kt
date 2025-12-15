package com.example.lb4.utils

import android.content.Context
import com.example.lb4.ShopApplication


fun Context.getShopApplication(): ShopApplication {
    return applicationContext as ShopApplication
}
