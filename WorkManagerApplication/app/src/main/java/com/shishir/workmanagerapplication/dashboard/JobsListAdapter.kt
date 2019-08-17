package com.shishir.workmanagerapplication.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.work.WorkInfo
import com.shishir.workmanagerapplication.R
import com.shishir.workmanagerapplication.common.pojo.BaseJobItem
import com.shishir.workmanagerapplication.dashboard.pojo.DashBoardJobItemVO
import com.shishir.workmanagerapplication.dashboard.utils.JobsItemListDiffUtil
import com.shishir.workmanagerapplication.databinding.LayoutDashboardJobItemBinding

class JobsListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mItemsList: MutableList<BaseJobItem> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LayoutDashboardJobItemBinding.inflate(inflater, parent, false)
        return JobItem(binding)
    }

    override fun getItemCount(): Int {
        return mItemsList.size
    }

    fun setData(newList: List<BaseJobItem>) {
        val diffResult = DiffUtil.calculateDiff(JobsItemListDiffUtil(mItemsList, newList))
        mItemsList.clear()
        this.mItemsList.addAll(newList)
        diffResult.dispatchUpdatesTo(this)

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = mItemsList.get(position)
        val jobItem = holder as JobItem
        val jobData = item as DashBoardJobItemVO
        jobItem.setData(jobData)
    }
}

class JobItem(private val mBinding: LayoutDashboardJobItemBinding) : RecyclerView.ViewHolder(mBinding.root) {
    fun setData(data: DashBoardJobItemVO) {
        mBinding.textJobid.text = data.id
        mBinding.textJobname.text = data.name
        mBinding.textJobKeys.text = data.keys
        mBinding.textJobstate.text = data.state
        when {
            data.state == WorkInfo.State.RUNNING.toString() -> mBinding.container.setBackgroundColor(
                ContextCompat.getColor(
                    mBinding.root.context,
                    R.color.jobEnqueued
                )
            )
            data.state == WorkInfo.State.ENQUEUED.toString() -> mBinding.container.setBackgroundColor(
                ContextCompat.getColor(
                    mBinding.root.context,
                    R.color.jobEnqueued
                )
            )
            data.state == WorkInfo.State.FAILED.toString() -> mBinding.container.setBackgroundColor(
                ContextCompat.getColor(
                    mBinding.root.context,
                    R.color.jobFailed
                )
            )
            data.state == WorkInfo.State.BLOCKED.toString() -> mBinding.container.setBackgroundColor(
                ContextCompat.getColor(
                    mBinding.root.context,
                    R.color.jobBlocked
                )
            )
            data.state == WorkInfo.State.CANCELLED.toString() -> mBinding.container.setBackgroundColor(
                ContextCompat.getColor(
                    mBinding.root.context,
                    R.color.jobCancelled
                )
            )
            data.state == WorkInfo.State.SUCCEEDED.toString() -> mBinding.container.setBackgroundColor(
                ContextCompat.getColor(
                    mBinding.root.context,
                    R.color.jobSucceeded
                )
            )
        }

    }
}