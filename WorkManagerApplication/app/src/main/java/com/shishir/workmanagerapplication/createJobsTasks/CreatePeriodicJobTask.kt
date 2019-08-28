package com.shishir.workmanagerapplication.createJobsTasks

import android.os.AsyncTask
import android.util.Log
import androidx.work.*
import com.shishir.workmanagerapplication.ConstantUtil
import com.shishir.workmanagerapplication.DummyWorker
import com.shishir.workmanagerapplication.common.utils.SharedPrefUtil
import java.util.concurrent.TimeUnit

class CreatePeriodicJobTask(
    private val sharedPrefUtil: SharedPrefUtil,
    private val workManager: WorkManager,
    private val jobName: String,
    private val jobDuration: Int,
    private val mListener: ICreateJobListerner
) : AsyncTask<Void?, Void?, Void?>() {

    override fun doInBackground(vararg params: Void?): Void? {
        val data = getInputData()
        val constraints = getConstraints()
        val request =
            PeriodicWorkRequestBuilder<DummyWorker>(15, TimeUnit.MINUTES)
                .setInputData(data)
                .setConstraints(constraints)
                .addTag(ConstantUtil.JobTypes.JOB_TYPE_PERIODIC)
                .addTag(jobName)
                .build()
        workManager.enqueue(request)
        return null
    }

    override fun onPostExecute(result: Void?) {
        super.onPostExecute(result)
        mListener.onJobAdded()
    }

    private fun getConstraints(): Constraints {
        return Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
    }

    private fun getInputData(): Data {
        return Data.Builder()
            .putInt(ConstantUtil.WorkerKeys.JOB_DURATION, jobDuration)
            .build()
    }

    interface ICreateJobListerner {
        fun onJobAdded()
    }
}