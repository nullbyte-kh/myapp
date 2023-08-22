package com.example.myapplication

import android.annotation.SuppressLint
import android.net.ConnectivityManager
import android.net.Network
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.HomeScreenBinding
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability

class HomeScreen : AppCompatActivity() {

    private lateinit var binding: HomeScreenBinding
    private var connectivityManager: ConnectivityManager? = null
    private var netCallBack:NetCallBack? = null
    private lateinit var updateManager:AppUpdateManager
    private lateinit var updateListener:InstallStateUpdatedListener


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = HomeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            netCallBack = NetCallBack()
            connectivityManager = getSystemService(ConnectivityManager::class.java)
            connectivityManager?.registerDefaultNetworkCallback(netCallBack!!)
        }
        //initial update manager.
        updateManager = AppUpdateManagerFactory.create(this)
        //initial install state update listener.
        updateListener = InstallStateUpdatedListener {
            if(it.installStatus() == InstallStatus.DOWNLOADING){
                val byteDownloaded = it.bytesDownloaded()
                val totalDownload = it.totalBytesToDownload()
                Log.d("UpdateApp", "Downloaded: ${(byteDownloaded / 100) * totalDownload} %")
            }
            else if (it.installStatus() == InstallStatus.DOWNLOADED) {
                Log.d("UpdateApp", "App Successfully in update.")
            }
        }
        //register update listener.
        updateManager.registerListener(updateListener)

    }

    override fun onPause() {
        super.onPause()
        if (netCallBack != null && connectivityManager != null){
            connectivityManager?.unregisterNetworkCallback(netCallBack!!)
        }
        updateManager.unregisterListener(updateListener)
    }

    inner class NetCallBack : ConnectivityManager.NetworkCallback() {

        override fun onAvailable(network: Network) {
            Log.d("Connect ManagerX", "Network active.")
        }

        override fun onLost(network: Network) {
            Log.d("Connect ManagerX", "Network not active.")
        }
    }

    private fun checkAppUpdate() {
        updateManager
            .appUpdateInfo
            .addOnSuccessListener {
            if(it.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && it.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)){
                updateManager.startUpdateFlowForResult(
                    it,
                    registerForResult(),
                    AppUpdateOptions.newBuilder(AppUpdateType.FLEXIBLE).build()
                )
            }
            else if (it.installStatus() == InstallStatus.DOWNLOADED){
                updateManager.unregisterListener(updateListener)
            }
        }
    }

    private fun registerForResult(): ActivityResultLauncher<IntentSenderRequest> {

        val launcher = registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()
        ) {
            if (it.resultCode == RESULT_OK){
                Log.d("Intent Result", "Result data: ${it.data}")
            }
        }
        return launcher


    }

    override fun onResume() {
        super.onResume()
        checkAppUpdate()
    }

}
