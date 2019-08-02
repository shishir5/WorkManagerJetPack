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
    private val jobDuration: Int
) : AsyncTask<Void?, Void?, Void?>() {

    override fun doInBackground(vararg params: Void?): Void? {
        Log.d("check123", "doIn Background")
        if (numberOfJobs > 0) {
            Log.d("check123 jobNmae", jobName)
            Log.d("check123 numOfJobs", numberOfJobs.toString())
            var i = 0
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
            Log.d("check123 job", "ENQUEUED")
            saveStartedJob(jobName)
        }
        return null
    }

    private fun saveStartedJob(jobName: String) {
        val jobs = sharedPrefUtil.getJobsList()!!
        if (!jobs.contains(jobName)) {
            jobs.add(jobName)
        }
        Log.d("check123 saved", "Job Names List =  " + jobs)
        sharedPrefUtil.setJobsList(jobs)
    }

    private fun getConstraints(): Constraints {
        return Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
    }

    private fun getInputData(): Data {
        return Data.Builder()
            .putInt(ConstantUtil.WorkerKeys.JOB_DURATION, jobDuration)
            .build()
    }

}