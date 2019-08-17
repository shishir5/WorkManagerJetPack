package com.shishir.workmanagerapplication.dashboard.pojo

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class DashboardItemVO (val header: DashboardHeaderItemVO, val jobsList: List<DashBoardJobItemVO>): Parcelable