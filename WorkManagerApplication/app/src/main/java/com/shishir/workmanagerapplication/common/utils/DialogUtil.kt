package com.shishir.workmanagerapplication.common.utils

import android.content.Context
import android.content.DialogInterface
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class DialogUtil {
    companion object {
        fun showDialog(
            context: Context, title: String, msg: String, positiveText: String?, positiveListener: DialogInterface.OnClickListener?,
            negativeText: String?, negativeListener: DialogInterface.OnClickListener?
        ) {
            val alert = MaterialAlertDialogBuilder(context)
                .setTitle(title)
                .setMessage(msg)

            positiveText?.let {
                alert.setPositiveButton(positiveText, positiveListener)
            }
            negativeText?.let {
                alert.setNegativeButton(negativeText, negativeListener)
            }
            alert.show()
        }
    }
}