package com.shishir.workmanagerapplication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.work.WorkManager
import com.shishir.workmanagerapplication.common.utils.SharedPrefUtil
import com.shishir.workmanagerapplication.databinding.ActivityJobsTypeBinding

class JobTypesListActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var mBinding: ActivityJobsTypeBinding

    companion object {
        private val CREATE_JOB_REQUEST = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_jobs_type)
        setClickListeners()
    }

    private fun setClickListeners() {
        mBinding.btChained.setOnClickListener(this)
        mBinding.btParallel.setOnClickListener(this)
        mBinding.btUnique.setOnClickListener(this)
        mBinding.btPeriodic.setOnClickListener(this)
        mBinding.btPruneWork.setOnClickListener(this)
        mBinding.btResetAll.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.bt_chained,
            R.id.bt_unique,
            R.id.bt_periodic,
            R.id.bt_parallel -> {
                openCreateJobsScreen(v.id)
            }
            R.id.bt_reset_all -> {
                resetWorkManager()
            }
            R.id.bt_prune_work -> {
                pruneWork()
                finishMe()
            }
        }
    }

    private fun pruneWork() {
        WorkManager.getInstance(this).pruneWork()
    }

    fun openCreateJobsScreen(id: Int) {
        val createJobIntent = Intent(this, CreateJobActivity::class.java)
        when (id) {
            R.id.bt_chained -> {
                createJobIntent.putExtra(IntentUtil.JOB_TYPE, ConstantUtil.JobTypes.JOB_TYPE_CHANINED)
            }
            R.id.bt_unique -> {
                createJobIntent.putExtra(IntentUtil.JOB_TYPE, ConstantUtil.JobTypes.JOB_TYPE_UNIQUE)
            }
            R.id.bt_parallel -> {
                createJobIntent.putExtra(IntentUtil.JOB_TYPE, ConstantUtil.JobTypes.JOB_TYPE_PARALLEL)
            }
            R.id.bt_periodic -> {
                createJobIntent.putExtra(IntentUtil.JOB_TYPE, ConstantUtil.JobTypes.JOB_TYPE_PERIODIC)
            }
        }
        startActivityForResult(createJobIntent, CREATE_JOB_REQUEST)
    }


    private fun resetWorkManager() {
        WorkManager.getInstance(this).cancelAllWork()
//        resetJobsListInSharePreference()
    }

    private fun resetJobsListInSharePreference() {
        SharedPrefUtil.getInstance(this)?.clearAll()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            CREATE_JOB_REQUEST ->
                if (resultCode == Activity.RESULT_OK) {
                    finishMe()
                }
        }

    }

    private fun finishMe() {
        setResult(Activity.RESULT_OK, Intent())
        finish()
    }
}

