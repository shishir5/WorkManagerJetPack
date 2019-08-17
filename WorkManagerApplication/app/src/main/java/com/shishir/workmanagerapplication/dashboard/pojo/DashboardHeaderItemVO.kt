package com.shishir.workmanagerapplication.dashboard.pojo

import com.shishir.workmanagerapplication.common.pojo.BaseJobItem
import com.shishir.workmanagerapplication.dashboard.DashBoardConstantUtils
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DashboardHeaderItemVO(val jobName: String, val jobType: String): BaseJobItem(DashBoardConstantUtils.DashBoardItemTypes.HEADER_ITEM_TYPE)