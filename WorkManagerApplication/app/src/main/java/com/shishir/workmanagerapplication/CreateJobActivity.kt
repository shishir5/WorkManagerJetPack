package com.shishir.workmanagerapplication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.work.WorkManager
import com.shishir.workmanagerapplication.common.utils.SharedPrefUtil
import com.shishir.workmanagerapplication.createJobsTasks.CreateChainedJobsTask
import com.shishir.workmanagerapplication.databinding.ActivityCreateJobBinding

class CreateJobActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityCreateJobBinding
    private lateinit var jobType: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_create_job)
        setJobType()
        initClickListener()
    }

    private fun initClickListener() {
        mBinding.buttonSubmit.setOnClickListener { view ->
            if(jobType == ConstantUtil.JobTypes.JOB_TYPE_CHANINED) {
                val jobName = mBinding.etJobName.text.toString()
                val jobDuration = mBinding.etJobDuration.text.toString()
                val numOfJobs = mBinding.etNumOfJobs.text.toString()
                val task =  CreateChainedJobsTask(SharedPrefUtil.getInstance(this)!! , WorkManager.getInstance(this), jobName, numOfJobs.toInt() , jobDuration.toInt())
                task.execute()
                finishMe()
            }
        }
    }

    private fun finishMe() {
        setResult(Activity.RESULT_OK, Intent())
        finish()
    }

    private fun showInvalidJobName() {
        showToastMessage("Invalid Job Name")
    }

    private fun showToastMessage(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    private fun validateJobName(jobName: String): Boolean {
        return jobName.isNotEmpty()
    }

    private fun setJobType() {
        jobType = intent.getStringExtra(IntentUtil.JOB_TYPE)
        mBinding.textLabelJobType.setText("Job Type: " + jobType)
    }
}