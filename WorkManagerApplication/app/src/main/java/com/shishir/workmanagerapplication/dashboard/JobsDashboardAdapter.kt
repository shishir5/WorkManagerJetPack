package com.shishir.workmanagerapplication.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shishir.workmanagerapplication.common.pojo.BaseJobItem
import com.shishir.workmanagerapplication.dashboard.pojo.DashboardItemVO
import com.shishir.workmanagerapplication.dashboard.utils.DashboardItemsDiffUtil
import com.shishir.workmanagerapplication.databinding.DashboardItemViewBinding

class JobsDashboardAdapter(val mListener: IJobInteractor) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mItemsList: MutableList<DashboardItemVO> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DashboardItemViewBinding.inflate(inflater, parent, false)
        return DashBoardJobsItem(binding)
    }


    override fun getItemCount(): Int {
        return mItemsList.size
    }

    fun setData(newList: List<DashboardItemVO>) {
        val diffResult = DiffUtil.calculateDiff(DashboardItemsDiffUtil(mItemsList, newList))
        mItemsList.clear()
        this.mItemsList.addAll(newList)
        diffResult.dispatchUpdatesTo(this)

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = mItemsList[position]
        (holder as DashBoardJobsItem).setData(item, mListener)
    }
}


class DashBoardJobsItem(private val mBinding: DashboardItemViewBinding) : RecyclerView.ViewHolder(mBinding.root) {
    private lateinit var mJobsAdapter: JobsListAdapter

    init {
        initRecyclerView()
    }

    fun setData(taskItem: DashboardItemVO, listener: IJobInteractor) {
        mBinding.textJobName.text = "JOB NAME: " + taskItem.header.jobName + "  ( " + taskItem.header.jobType + " )"
        var itemsList = mutableListOf<BaseJobItem>()
        itemsList.addAll(taskItem.jobsList)
        refreshAdapter(itemsList)
        mBinding.imageDeleteTask.setOnClickListener { v ->
            listener.removeJob(taskItem.header.jobName)
        }
    }

    private fun initRecyclerView() {
        mJobsAdapter = JobsListAdapter()
        mBinding.recyclerJobs.layoutManager = LinearLayoutManager(mBinding.root.context, RecyclerView.VERTICAL, false)
        mBinding.recyclerJobs.adapter = mJobsAdapter
    }

    private fun refreshAdapter(itemsList: MutableList<BaseJobItem>) {
        mJobsAdapter.setData(itemsList)
    }
}

public interface IJobInteractor {
    fun removeJob(jobName: String)
}
