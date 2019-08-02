package com.shishir.workmanagerapplication.dashboard.pojo

import com.shishir.workmanagerapplication.common.pojo.BaseJobItem
import com.shishir.workmanagerapplication.dashboard.DashBoardConstantUtils
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DashBoardJobItemVO(val id:String, val name: String, val state: String, val keys: String): BaseJobItem(DashBoardConstantUtils.DashBoardItemTypes.JOB_ITEM_TYPE)