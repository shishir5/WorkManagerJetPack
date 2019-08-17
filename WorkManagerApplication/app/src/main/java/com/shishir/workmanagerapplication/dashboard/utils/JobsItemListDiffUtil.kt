package com.shishir.workmanagerapplication.dashboard.utils

import androidx.recyclerview.widget.DiffUtil
import com.shishir.workmanagerapplication.common.pojo.BaseJobItem
import com.shishir.workmanagerapplication.dashboard.pojo.DashBoardJobItemVO

class JobsItemListDiffUtil(private val mOldList: List<BaseJobItem>, private val mNewList: List<BaseJobItem>) :
    DiffUtil.Callback() {

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
        val oldItem = mOldList[oldItemPosition] as DashBoardJobItemVO
        val newItem = mNewList[newItemPosition] as DashBoardJobItemVO
        if (oldItem.id == newItem.id && oldItem.name == newItem.name && oldItem.state == newItem.state && oldItem.keys == newItem.keys)
            return true
        return false
    }

}