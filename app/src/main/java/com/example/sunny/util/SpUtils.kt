package com.example.sunny.util

import android.content.Context
import com.tencent.mmkv.MMKV

enum class SpType {
    City,
}

/// 存储管理
class SpUtils private constructor(){
    /// 生成单例
    companion object {
        private var instance: SpUtils? = null

        fun getInstance(): SpUtils {
            if (instance == null) {
                synchronized(SpUtils::class) {
                    if (instance == null) {
                        instance = SpUtils()
                    }
                }
            }
            return instance!!
        }
    }

    fun initialize(context: Context) {
        MMKV.initialize(context)
    }

    private val mmkv: MMKV by lazy {
        MMKV.defaultMMKV()
    }

    // 存储字符串
    fun putString(key: String, value: String) {
        mmkv.encode(key, value)
    }

    // 获取字符串
    fun getString(key: String, defaultValue: String = ""): String {
        return mmkv.decodeString(key, defaultValue) ?: defaultValue
    }

    // 存储整数
    fun putInt(key: String, value: Int) {
        mmkv.encode(key, value)
    }

    // 获取整数
    fun getInt(key: String, defaultValue: Int = 0): Int {
        return mmkv.decodeInt(key, defaultValue)
    }

    // 存储布尔值
    fun putBoolean(key: String, value: Boolean) {
        mmkv.encode(key, value)
    }

    // 获取布尔值
    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean {
        return mmkv.decodeBool(key, defaultValue)
    }

    // 存储浮点数
    fun putFloat(key: String, value: Float) {
        mmkv.encode(key, value)
    }

    // 获取浮点数
    fun getFloat(key: String, defaultValue: Float = 0f): Float {
        return mmkv.decodeFloat(key, defaultValue)
    }

    // 存储长整型
    fun putLong(key: String, value: Long) {
        mmkv.encode(key, value)
    }

    // 获取长整型
    fun getLong(key: String, defaultValue: Long = 0L): Long {
        return mmkv.decodeLong(key, defaultValue)
    }

    // 删除指定key
    fun remove(key: String) {
        mmkv.removeValueForKey(key)
    }

    // 清空所有数据
    fun clearAll() {
        mmkv.clearAll()
    }

    // 检查是否包含指定key
    fun contains(key: String): Boolean {
        return mmkv.containsKey(key)
    }

}