package com.shishir.workmanagerapplication.dashboard.utils

import androidx.recyclerview.widget.DiffUtil
import com.shishir.workmanagerapplication.common.pojo.BaseJobItem
import com.shishir.workmanagerapplication.dashboard.DashBoardConstantUtils
import com.shishir.workmanagerapplication.dashboard.pojo.DashBoardJobItemVO
import com.shishir.workmanagerapplication.dashboard.pojo.DashboardHeaderItemVO

class DashboardItemsDiffUtil(private val mOldList: List<BaseJobItem>, private val mNewList: List<BaseJobItem>) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldList[oldItemPosition].itemType == mNewList[newItemPosition].itemType
    }

    override fun getOldListSize(): Int {
        return mOldList.size
    }

    override fun getNewListSize(): Int {
        return mNewList.size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val itemType = mOldList.get(oldItemPosition).itemType
        if (itemType == DashBoardConstantUtils.DashBoardItemTypes.HEADER_ITEM_TYPE) {
            val oldItem = mOldList[oldItemPosition] as DashboardHeaderItemVO
            val newItem = mNewList[oldItemPosition] as DashboardHeaderItemVO
            if (oldItem.jobName == newItem.jobName && oldItem.jobType == newItem.jobType)
                return true
            return false
        } else {
            val oldItem = mOldList[oldItemPosition] as DashBoardJobItemVO
            val newItem = mNewList[oldItemPosition] as DashBoardJobItemVO
            if (oldItem.id == newItem.id && oldItem.name == newItem.name && oldItem.state == newItem.state && oldItem.keys == newItem.keys)
                return true
            return false
        }
    }

}