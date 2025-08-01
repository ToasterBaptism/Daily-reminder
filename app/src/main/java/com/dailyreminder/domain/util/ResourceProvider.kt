package com.dailyreminder.domain.util

import android.content.Context
import androidx.annotation.StringRes
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ResourceProvider @Inject constructor(
    private val context: Context
) {
    
    fun getString(@StringRes resId: Int): String = context.getString(resId)
    
    fun getString(@StringRes resId: Int, vararg formatArgs: Any): String = 
        context.getString(resId, *formatArgs)
    
    fun getStringArray(resId: Int): Array<String> = context.resources.getStringArray(resId)
    
    fun getColor(resId: Int): Int = context.getColor(resId)
    
    fun getDimension(resId: Int): Float = context.resources.getDimension(resId)
    
    fun getDimensionPixelSize(resId: Int): Int = context.resources.getDimensionPixelSize(resId)
}