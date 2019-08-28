package com.shishir.workmanagerapplication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.work.WorkManager
import com.shishir.workmanagerapplication.common.utils.SharedPrefUtil
import com.shishir.workmanagerapplication.createJobsTasks.CreateChainedJobsTask
import com.shishir.workmanagerapplication.createJobsTasks.CreateParallelJobsTask
import com.shishir.workmanagerapplication.createJobsTasks.CreatePeriodicJobTask
import com.shishir.workmanagerapplication.createJobsTasks.CreateUniqueJobTask
import com.shishir.workmanagerapplication.databinding.ActivityCreateJobBinding

class CreateJobActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityCreateJobBinding
    private lateinit var jobType: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_create_job)
        setJobType()
        initClickListener()
        initView()
    }

    private fun initView() {
        if(jobType == ConstantUtil.JobTypes.JOB_TYPE_UNIQUE || jobType == ConstantUtil.JobTypes.JOB_TYPE_PERIODIC) {
            mBinding.textNumOfJobs.visibility = View.GONE
            mBinding.etNumOfJobs.visibility = View.GONE
        } else {
            mBinding.textNumOfJobs.visibility = View.VISIBLE
            mBinding.etNumOfJobs.visibility = View.VISIBLE
        }
    }

    private fun initClickListener() {
        mBinding.buttonSubmit.setOnClickListener { view ->
            mBinding.loaderView.visibility = View.VISIBLE
            val jobName = mBinding.etJobName.text.toString()
            val jobDuration = mBinding.etJobDuration.text.toString()
            val numOfJobs = mBinding.etNumOfJobs.text.toString()

            when (jobType) {
                ConstantUtil.JobTypes.JOB_TYPE_CHANINED -> {
                    val task = CreateChainedJobsTask(
                        SharedPrefUtil.getInstance(this)!!,
                        WorkManager.getInstance(this),
                        jobName,
                        numOfJobs.toInt(),
                        jobDuration.toInt(),
                        object :
                            CreateChainedJobsTask.ICreateJobListerner {
                            override fun onJobAdded() {
                                mBinding.loaderView.visibility = View.GONE
                                finishMe()
                            }
                        })
                    task.execute()
                    saveStartedJob(jobName)
                }
                ConstantUtil.JobTypes.JOB_TYPE_PARALLEL -> {
                    val task = CreateParallelJobsTask(
                        SharedPrefUtil.getInstance(this)!!,
                        WorkManager.getInstance(this),
                        jobName,
                        numOfJobs.toInt(),
                        jobDuration.toInt(),
                        object :
                            CreateParallelJobsTask.ICreateJobListerner {
                            override fun onJobAdded() {
                                mBinding.loaderView.visibility = View.GONE
                                finishMe()
                            }
                        })
                    task.execute()
                    saveStartedJob(jobName)
                }

                ConstantUtil.JobTypes.JOB_TYPE_UNIQUE -> {
                    val task = CreateUniqueJobTask(
                        SharedPrefUtil.getInstance(this)!!,
                        WorkManager.getInstance(this),
                        jobName,
                        jobDuration.toInt(),
                        object :
                            CreateUniqueJobTask.ICreateJobListerner {
                            override fun onJobAdded() {
                                mBinding.loaderView.visibility = View.GONE
                                finishMe()
                            }
                        })
                    task.execute()
                    saveStartedJob(jobName)
                }

                ConstantUtil.JobTypes.JOB_TYPE_PERIODIC -> {
                    val task = CreatePeriodicJobTask(
                        SharedPrefUtil.getInstance(this)!!,
                        WorkManager.getInstance(this),
                        jobName,
                        jobDuration.toInt(),
                        object :
                            CreatePeriodicJobTask.ICreateJobListerner {
                            override fun onJobAdded() {
                                mBinding.loaderView.visibility = View.GONE
                                finishMe()
                            }
                        })
                    task.execute()
                    saveStartedJob(jobName)
                }
            }
        }
    }

    private fun saveStartedJob(jobName: String) {
        val jobs = SharedPrefUtil.getInstance(this)?.getJobsList()!!
        if (!jobs.contains(jobName)) {
            jobs.add(jobName)
        }
        SharedPrefUtil.getInstance(this)?.setJobsList(jobs)
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