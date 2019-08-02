package com.shishir.workmanagerapplication.dashboard

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.WorkInfo
import com.shishir.workmanagerapplication.ConstantUtil
import com.shishir.workmanagerapplication.JobTypesListActivity
import com.shishir.workmanagerapplication.R
import com.shishir.workmanagerapplication.WorkManagerUtil
import com.shishir.workmanagerapplication.common.pojo.BaseJobItem
import com.shishir.workmanagerapplication.common.utils.SharedPrefUtil
import com.shishir.workmanagerapplication.dashboard.pojo.DashBoardJobItemVO
import com.shishir.workmanagerapplication.dashboard.pojo.DashboardHeaderItemVO
import com.shishir.workmanagerapplication.databinding.ActivityDashboardBinding

class JobsDashboardActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityDashboardBinding
    private lateinit var mJobsAdapter: JobsDashboardAdapter
    private var mLastJobs: MutableSet<String> = mutableSetOf()
    private var mJobsMap: MutableLiveData<HashMap<String, MutableList<out BaseJobItem>>> = MutableLiveData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard)
        initRecyclerView()
        observeMap()
        initClick()
    }

    private fun initClick() {
        mBinding.fabAddTask.setOnClickListener(View.OnClickListener {
            opentCreateTaskList()
        })
    }

    override fun onResume() {
        super.onResume()
        checkAddedOrRemovedJobs()
    }

    private fun checkAddedOrRemovedJobs() {
        val jobNames = getListedJobsFromSharedPreference()
        if(jobNames != mLastJobs)
            fetchWorkInfos(jobNames)
    }

    private fun opentCreateTaskList() {
        val intent = Intent(this, JobTypesListActivity::class.java)
        startActivity(intent)
    }

    private fun observeMap() {
        mJobsMap.observe(this, Observer {
            val items: MutableList<BaseJobItem> = mutableListOf()
            it?.let { map ->
                for ((jobName, jobs) in map) {
                    val jobType = getJobType(jobs[0] as DashBoardJobItemVO)
                    addHeaderItem(items, jobName, jobType)
                    for (job in jobs) {
                        items.add(job)
                    }
                }
                mJobsAdapter.setData(items)
            }
        })
        val map = mJobsMap.value
        val items: MutableList<BaseJobItem> = mutableListOf()
        map?.let { it ->
            for ((jobName, jobs) in it) {
                val jobType = getJobType(jobs[0] as DashBoardJobItemVO)
                addHeaderItem(items, jobName, jobType)
                for (job in jobs) {
                    items.add(job as BaseJobItem)
                }
            }
            mJobsAdapter.setData(items)
        }
    }

    private fun getJobType(dashBoardJobItemVO: DashBoardJobItemVO): String {
        val keys = dashBoardJobItemVO.keys
        return when {
            keys.contains(ConstantUtil.JobTypes.JOB_TYPE_CHANINED) -> ConstantUtil.JobTypes.JOB_TYPE_CHANINED
            keys.contains(ConstantUtil.JobTypes.JOB_TYPE_UNIQUE) -> ConstantUtil.JobTypes.JOB_TYPE_UNIQUE
            keys.contains(ConstantUtil.JobTypes.JOB_TYPE_PARALLEL) -> ConstantUtil.JobTypes.JOB_TYPE_PARALLEL
            else -> ConstantUtil.JobTypes.DEFAULT
        }
    }

    private fun addHeaderItem(items: MutableList<BaseJobItem>, jobName: String, jobType: String) {
        val item = DashboardHeaderItemVO(jobName, jobType)
        items.add(item)
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
        for (jobName in JobNamesSet) {
            WorkManagerUtil.getWorkInfosByTag(jobName, this).observe(this, Observer { jobsList ->
                Log.d("check123 " + jobName, "size = " + jobsList.size)
                if (jobsList.isNotEmpty())
                    refreshList(jobName, jobsList)
            })
        }
    }

    private fun showEmptyState(enable: Boolean) {
        if(enable) {
            mBinding.recyclerJobs.visibility = View.GONE
            mBinding.layoutEmptyState.visibility = View.VISIBLE
        } else {
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
        if (jobList.isNotEmpty())
            refreshMapValue(jobName, jobList)
    }

    private fun refreshMapValue(jobName: String, jobList: MutableList<DashBoardJobItemVO>) {
        var map = mJobsMap.value
        if (map == null) {
            map = hashMapOf()
        }
        map.put(jobName, jobList)
        mJobsMap.value = map
    }

    private fun initRecyclerView() {
        mJobsAdapter = JobsDashboardAdapter()
        mBinding.recyclerJobs.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        mBinding.recyclerJobs.adapter = mJobsAdapter
    }
}