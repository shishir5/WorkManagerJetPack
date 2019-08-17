package com.shishir.workmanagerapplication.createJobsTasks

import android.os.AsyncTask
import android.util.Log
import androidx.work.*
import com.shishir.workmanagerapplication.ConstantUtil
import com.shishir.workmanagerapplication.DummyWorker
import com.shishir.workmanagerapplication.common.utils.SharedPrefUtil

class CreateChainedJobsTask(
    private val sharedPrefUtil: SharedPrefUtil,
    private val workManager: WorkManager,
    private val jobName: String,
    private val numberOfJobs: Int,
    private val jobDuration: Int,
    private val mListener: ICreateJobListerner) : AsyncTask<Void?, Void?, Void?>() {

    override fun doInBackground(vararg params: Void?): Void? {
        Log.d("check123", "doIn Background")
        if (numberOfJobs > 0) {
            Log.d("check123 jobNmae", jobName)
            Log.d("check123 numOfJobs", numberOfJobs.toString())
            var i = 1
            var continuation: WorkContinuation? = null

            while (i <= numberOfJobs) {
                i++
                val data = getInputData()
                val constraints = getConstraints()
                val request =
                    OneTimeWorkRequest.Builder(DummyWorker::class.java)
                        .setInputData(data)
                        .setConstraints(constraints)
                        .addTag(ConstantUtil.JobTypes.JOB_TYPE_CHANINED)
                        .addTag(jobName)
                        .build()

                if (continuation == null) {
                    continuation = workManager.beginUniqueWork(jobName, ExistingWorkPolicy.REPLACE, request)
                } else {
                    continuation = continuation.then(request)
                }
            }
            continuation?.enqueue()
        }
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