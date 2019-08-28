package com.shishir.workmanagerapplication.common.utils

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.work.WorkInfo
import androidx.work.WorkManager

class WorkManagerUtil  {
    companion object {
        fun getWorkInfosByTag(jobName: String, context: Context): LiveData<MutableList<WorkInfo>> {
            val list = WorkManager.getInstance(context).getWorkInfosByTagLiveData(jobName)
            return list
        }
        fun getWorkInfodForName(jobName: String, context: Context): LiveData<MutableList<WorkInfo>> {
            return WorkManager.getInstance(context).getWorkInfosForUniqueWorkLiveData(jobName)
        }

        fun stopJobWithName(context: Context, jobName: String) {
            WorkManager.getInstance(context).cancelAllWorkByTag(jobName)
        }
    }

}