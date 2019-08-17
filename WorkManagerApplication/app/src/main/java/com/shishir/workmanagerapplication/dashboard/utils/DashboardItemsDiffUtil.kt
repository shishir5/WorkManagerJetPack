package com.shishir.workmanagerapplication.dashboard.utils

import androidx.recyclerview.widget.DiffUtil
import com.shishir.workmanagerapplication.dashboard.pojo.DashboardItemVO

class DashboardItemsDiffUtil(private val mOldList: List<DashboardItemVO>, private val mNewList: List<DashboardItemVO>) :
    DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldList[oldItemPosition].header.jobName == mNewList[newItemPosition].header.jobName
    }

    override fun getOldListSize(): Int {
        return mOldList.size
    }

    override fun getNewListSize(): Int {
        return mNewList.size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItemHeader = mOldList[oldItemPosition].header
        val oldItemJobsList = mOldList[oldItemPosition].jobsList

        val newItemHeader = mNewList[newItemPosition].header
        val newItemJobsList = mNewList[newItemPosition].jobsList

        if (oldItemHeader.jobName != newItemHeader.jobName || oldItemHeader.jobType != newItemHeader.jobType)
            return false

        if (oldItemJobsList != newItemJobsList)
            return false
        return true
    }
}