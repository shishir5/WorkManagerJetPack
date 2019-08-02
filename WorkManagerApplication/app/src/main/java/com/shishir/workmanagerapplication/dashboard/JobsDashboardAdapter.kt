package com.shishir.workmanagerapplication.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.work.WorkInfo
import com.shishir.workmanagerapplication.ConstantUtil
import com.shishir.workmanagerapplication.R
import com.shishir.workmanagerapplication.common.pojo.BaseJobItem
import com.shishir.workmanagerapplication.dashboard.pojo.DashBoardJobItemVO
import com.shishir.workmanagerapplication.dashboard.pojo.DashboardHeaderItemVO
import com.shishir.workmanagerapplication.dashboard.utils.DashboardItemsDiffUtil
import com.shishir.workmanagerapplication.databinding.LayoutDashboardHeaderItemBinding
import com.shishir.workmanagerapplication.databinding.LayoutDashboardJobItemBinding

class JobsDashboardAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mItemsList: MutableList<BaseJobItem> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        if (viewType == DashBoardConstantUtils.DashBoardItemTypes.HEADER_ITEM_TYPE) {
            val binding = LayoutDashboardHeaderItemBinding.inflate(inflater, parent, false)
            return HeaderItem(binding)
        } else {
            val binding = LayoutDashboardJobItemBinding.inflate(inflater, parent, false)
            return JobItem(binding)
        }
    }

    override fun getItemCount(): Int {
        return mItemsList.size
    }

    fun setData(newList: List<BaseJobItem>) {
        val diffResult = DiffUtil.calculateDiff(DashboardItemsDiffUtil(mItemsList, newList))
        diffResult.dispatchUpdatesTo(this)
        mItemsList.clear()
        this.mItemsList.addAll(newList)
    }

    override fun getItemViewType(position: Int): Int {
        if(mItemsList[position].itemType == DashBoardConstantUtils.DashBoardItemTypes.HEADER_ITEM_TYPE)
            return DashBoardConstantUtils.DashBoardItemTypes.HEADER_ITEM_TYPE
        else
        return DashBoardConstantUtils.DashBoardItemTypes.JOB_ITEM_TYPE
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = mItemsList.get(position)
        if(item.itemType == DashBoardConstantUtils.DashBoardItemTypes.HEADER_ITEM_TYPE) {
            val headerItem = holder as HeaderItem
            val headerData = item as DashboardHeaderItemVO
            headerItem.setData(headerData)
        } else {
            val jobItem = holder as JobItem
            val jobData = item as DashBoardJobItemVO
            jobItem.setData(jobData)
        }
    }
}

class HeaderItem(private val mBinding: LayoutDashboardHeaderItemBinding) : RecyclerView.ViewHolder(mBinding.root) {
    fun setData(item: DashboardHeaderItemVO) {
        mBinding.textJobName.text = "JOB NAME: " + item.jobName + "  ( " + item.jobType + " )"
    }
}

class JobItem(private val mBinding: LayoutDashboardJobItemBinding) : RecyclerView.ViewHolder(mBinding.root) {
    fun setData(data: DashBoardJobItemVO) {
        mBinding.textJobid.text = data.id
        mBinding.textJobname.text = data.name
        mBinding.textJobKeys.text = data.keys
        mBinding.textJobstate.text = data.state
        when {
            data.state == WorkInfo.State.RUNNING.toString() -> mBinding.container.setBackgroundColor(ContextCompat.getColor(mBinding.root.context, R.color.jobEnqueued))
            data.state == WorkInfo.State.ENQUEUED.toString() -> mBinding.container.setBackgroundColor(ContextCompat.getColor(mBinding.root.context, R.color.jobEnqueued))
            data.state == WorkInfo.State.FAILED.toString() -> mBinding.container.setBackgroundColor(ContextCompat.getColor(mBinding.root.context, R.color.jobFailed))
            data.state == WorkInfo.State.BLOCKED.toString() -> mBinding.container.setBackgroundColor(ContextCompat.getColor(mBinding.root.context, R.color.jobBlocked))
            data.state == WorkInfo.State.CANCELLED.toString() -> mBinding.container.setBackgroundColor(ContextCompat.getColor(mBinding.root.context, R.color.jobCancelled))
            data.state == WorkInfo.State.SUCCEEDED.toString() -> mBinding.container.setBackgroundColor(ContextCompat.getColor(mBinding.root.context, R.color.jobSucceeded))
        }

    }
}
