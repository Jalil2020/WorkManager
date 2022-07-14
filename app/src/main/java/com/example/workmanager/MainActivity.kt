package com.example.workmanager

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.work.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        simpleWorker()
        myWorkManager()
    }

    @SuppressLint("InvalidPeriodicWorkRequestInterval")
    private fun myWorkManager() {

        val userData = "Developer";
        val userPerfect = false

        val inputWorkData =
            workDataOf(Pair("user_data", userData), Pair("user_perfect", userPerfect))

        val inputData =
            Data.Builder().putString("user_data", userData).putBoolean("user_perfect", userPerfect)
                .build()


        val constraint = Constraints.Builder().setRequiresCharging(false)
            .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
            .setRequiresBatteryNotLow(true)
            .build()

        val mRequest = PeriodicWorkRequest.Builder(MyWorker::class.java, 2, TimeUnit.HOURS)
            .setInputData(inputData)
            .setConstraints(constraint)
            .build()

        WorkManager.getInstance(this)
            .enqueueUniquePeriodicWork("my_id", ExistingPeriodicWorkPolicy.KEEP, mRequest)


        val workInfo =
            WorkManager.getInstance(applicationContext).getWorkInfoByIdLiveData(mRequest.id)
                .observe(this) { workInfo ->
                    val wasSuccess =
                        if (workInfo != null && workInfo.state == WorkInfo.State.SUCCEEDED) {
                            workInfo.outputData.getBoolean("is_success", false)
                        } else {
                            false
                        }
                    Toast.makeText(
                        applicationContext,
                        "Was work successful message $wasSuccess",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.d("TAG", "myWorkManager: Was work successful message $wasSuccess")
                }
    }

    private fun simpleWorker() {

        val mRequest: WorkRequest = OneTimeWorkRequestBuilder<MyWorker>().build()
        WorkManager.getInstance(this).enqueue(mRequest)

    }
}