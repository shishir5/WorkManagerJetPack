package com.shishir.workmanagerapplication

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.util.*

class DummyWorker(val context: Context, val params: WorkerParameters): Worker(context, params) {

    override fun doWork(): Result {
        val jobDuration = inputData.getInt(ConstantUtil.WorkerKeys.JOB_DURATION, 0)
        val currrentTimeInMillis = Calendar.getInstance().timeInMillis
        var time = currrentTimeInMillis
        while((time - currrentTimeInMillis) < (jobDuration * 1000)) {
            for (i in 1..10000) {

            }
            time = Calendar.getInstance().timeInMillis
        }
        return Result.success()
    }

}