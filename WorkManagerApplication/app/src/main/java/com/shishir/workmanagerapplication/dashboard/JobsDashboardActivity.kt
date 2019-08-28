package com.shishir.workmanagerapplication.dashboard

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.WorkInfo
import com.shishir.workmanagerapplication.common.constants.ConstantUtil
import com.shishir.workmanagerapplication.JobTypesListActivity
import com.shishir.workmanagerapplication.R
import com.shishir.workmanagerapplication.common.utils.WorkManagerUtil
import com.shishir.workmanagerapplication.common.utils.SharedPrefUtil
import com.shishir.workmanagerapplication.dashboard.JobsDashboardActivity.Const.SELECT_JOB_TYPE_REQUEST
import com.shishir.workmanagerapplication.dashboard.pojo.DashBoardJobItemVO
import com.shishir.workmanagerapplication.dashboard.pojo.DashboardHeaderItemVO
import com.shishir.workmanagerapplication.dashboard.pojo.DashboardItemVO
import com.shishir.workmanagerapplication.databinding.ActivityDashboardBinding

class JobsDashboardActivity : AppCompatActivity() {

    private object Const {
        val SELECT_JOB_TYPE_REQUEST = 1000
    }

    private lateinit var mBinding: ActivityDashboardBinding
    private lateinit var mJobsAdapter: JobsDashboardAdapter
    private var mLastJobs: MutableSet<String> = mutableSetOf()
    private var mJobsMap: MutableLiveData<HashMap<String, MutableList<DashBoardJobItemVO>>> = MutableLiveData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard)
        initRecyclerView()
        observeMap()
        initClick()
    }

    private fun initClick() {
        mBinding.fabAddTask.setOnClickListener {
            openCreateTaskList()
        }
    }

    override fun onResume() {
        super.onResume()
        checkAddedOrRemovedJobs()
    }

    private fun checkAddedOrRemovedJobs() {
        val jobNames = getListedJobsFromSharedPreference()
        if (jobNames != mLastJobs)
            fetchWorkInfos(jobNames)
        else if (jobNames.isEmpty())
            showEmptyState(true)
    }

    private fun openCreateTaskList() {
        val intent = Intent(this, JobTypesListActivity::class.java)
        startActivityForResult(intent, Const.SELECT_JOB_TYPE_REQUEST)
    }

    private fun observeMap() {
        mJobsMap.observe(this, Observer {
            val items: MutableList<DashboardItemVO> = mutableListOf()
            it?.let { map ->
                for ((jobName, jobs) in map) {
                    if (jobs.isNotEmpty()) {
                        val jobType = getJobType(jobs[0])

                        val header = addHeaderItem(jobName, jobType)

                        val jobsList = mutableListOf<DashBoardJobItemVO>()
                        if (jobs.isNotEmpty()) {
                            jobsList.addAll(jobs)
                            val item = DashboardItemVO(header, jobsList)
                            items.add(item)
                        } else {
                            showEmptyState(true)
                        }
                    }
                }
                if (it.size > 0)
                    mJobsAdapter.setData(items)
                else
                    showEmptyState(true)
            }
        })
    }

    private fun getJobType(dashBoardJobItemVO: DashBoardJobItemVO): String {
        val keys = dashBoardJobItemVO.keys
        return when {
            keys.contains(ConstantUtil.JobTypes.JOB_TYPE_CHANINED) -> ConstantUtil.JobTypes.JOB_TYPE_CHANINED
            keys.contains(ConstantUtil.JobTypes.JOB_TYPE_UNIQUE) -> ConstantUtil.JobTypes.JOB_TYPE_UNIQUE
            keys.contains(ConstantUtil.JobTypes.JOB_TYPE_PARALLEL) -> ConstantUtil.JobTypes.JOB_TYPE_PARALLEL
            keys.contains(ConstantUtil.JobTypes.JOB_TYPE_PERIODIC) -> ConstantUtil.JobTypes.JOB_TYPE_PERIODIC
            else -> ConstantUtil.JobTypes.DEFAULT
        }
    }

    private fun addHeaderItem(jobName: String, jobType: String): DashboardHeaderItemVO {
        return DashboardHeaderItemVO(jobName, jobType)
    }

    private fun fetchWorkInfos(jobNames: MutableSet<String>) {
        mLastJobs = jobNames
        if (jobNames.isNotEmpty()) {
            showEmptyState(false)
            prepareJobList(jobNames)
        } else {
            showEmptyState(true)
        }
    }

    private fun getListedJobsFromSharedPreference(): MutableSet<String> {
        val sharedPref = SharedPrefUtil.getInstance(this)
        return sharedPref?.getJobsList()!!
    }

    private fun prepareJobList(JobNamesSet: Set<String>) {
        showEmptyState(true)
        for (jobName in JobNamesSet) {
            WorkManagerUtil.getWorkInfosByTag(jobName, this).observe(this, Observer { jobsList ->
                if (jobsList.isEmpty())
                    removeJobNameFromStoredList(jobName)
                else
                    showEmptyState(false)
                refreshList(jobName, jobsList)
            })
        }
    }

    private fun removeJobNameFromStoredList(jobName: String) {
        val jobNames = getListedJobsFromSharedPreference()
        if (jobNames.contains(jobName)) {
            jobNames.remove(jobName)
            SharedPrefUtil.getInstance(this)?.setJobsList(jobNames)
        }
    }

    private fun showEmptyState(enable: Boolean) {
        if (enable) {
            mBinding.label.visibility = View.GONE
            mBinding.recyclerJobs.visibility = View.GONE
            mBinding.layoutEmptyState.visibility = View.VISIBLE
        } else {
            mBinding.label.visibility = View.VISIBLE
            mBinding.recyclerJobs.visibility = View.VISIBLE
            mBinding.layoutEmptyState.visibility = View.GONE
        }
    }

    private fun refreshList(jobName: String, jobsList: MutableList<WorkInfo>) {
        val jobList: MutableList<DashBoardJobItemVO> = mutableListOf()
        for (job in jobsList) {
            val jobData = DashBoardJobItemVO(job.id.toString(), jobName, job.state.toString(), job.tags.toString())
            jobList.add(jobData)
        }
        refreshMapValue(jobName, jobList)
    }

    private fun refreshMapValue(jobName: String, jobList: MutableList<DashBoardJobItemVO>) {
        var map = mJobsMap.value
        if (map == null) {
            map = hashMapOf()
        }
        map[jobName] = jobList
        mJobsMap.value = map
    }

    private fun initRecyclerView() {
        mJobsAdapter = JobsDashboardAdapter(object : IJobInteractor {
            override fun stopJob(jobName: String) {
                stopJobsForJobName(jobName)
                checkAddedOrRemovedJobs()
            }
        })
        mBinding.recyclerJobs.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        mBinding.recyclerJobs.adapter = mJobsAdapter
    }

    private fun stopJobsForJobName(jobName: String) {
        WorkManagerUtil.stopJobWithName(this, jobName)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            SELECT_JOB_TYPE_REQUEST ->
                if (resultCode == Activity.RESULT_OK) {
                    val jobNames = getListedJobsFromSharedPreference()
                    fetchWorkInfos(jobNames)
                }
        }
    }


}