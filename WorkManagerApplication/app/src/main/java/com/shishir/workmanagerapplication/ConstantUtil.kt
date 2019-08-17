package com.shishir.workmanagerapplication

class ConstantUtil {
    class JobTypes {
        companion object {
            val JOB_TYPE_CHANINED = "CHAINED JOBS"
            val JOB_TYPE_UNIQUE = "UNIQUE JOB"
            val JOB_TYPE_PARALLEL = "PARRALLEL JOBS"
            val DEFAULT = "DEFAULT"
        }
    }
    class WorkerKeys {
        companion object {
            val JOB_DURATION = "job_duration"
        }
    }
}