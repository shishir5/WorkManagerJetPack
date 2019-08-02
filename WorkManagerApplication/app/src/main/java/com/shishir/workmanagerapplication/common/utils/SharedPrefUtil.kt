package com.shishir.workmanagerapplication.common.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

class SharedPrefUtil(val context: Context) {
    companion object {
        val SHARED_PREF_NAME = "SHARED_PREF_DEFAULT"
        var mInstance: SharedPrefUtil? = null
        private lateinit var mSharedPref: SharedPreferences

        fun getInstance(context: Context): SharedPrefUtil? {
            if (mInstance == null) {
                synchronized(this) {
                    if (mInstance == null) {
                        mInstance = SharedPrefUtil(context)
                    }
                }
            }
            return mInstance
        }
    }

    init {
        mSharedPref = context.getSharedPreferences(SHARED_PREF_NAME, 0)
    }

    fun setJobsList(jobsList: MutableSet<String>) {
        Log.d("check123 saved", "Job Names List =  " + jobsList.toString())
        mSharedPref.edit()?.putStringSet(SharedPrefConstants.JOBS_ENQUEUED, jobsList.toSet())?.apply()
    }

    fun getJobsList(): MutableSet<String> {
        val abc = mSharedPref.getStringSet(SharedPrefConstants.JOBS_ENQUEUED, mutableSetOf())
        return abc!!
    }

    fun clearAll() {
        mSharedPref.edit().clear().apply()
    }
}