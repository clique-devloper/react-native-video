package com.brentvatne.react

import android.util.Log
import com.brentvatne.common.toolbox.ReactBridgeUtils
import com.brentvatne.exoplayer.ReactExoplayerView
import com.brentvatne.exoplayer.ReactExoplayerViewManager
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.UiThreadUtil
import com.facebook.react.uimanager.UIManagerHelper
import com.facebook.react.uimanager.common.UIManagerType
import kotlin.math.roundToInt

class VideoManagerModule(reactContext: ReactApplicationContext?) : ReactContextBaseJavaModule(reactContext) {
    override fun getName(): String = REACT_CLASS

    private fun performOnPlayerView(reactTag: Int, callback: (ReactExoplayerView?) -> Unit) {
        UiThreadUtil.runOnUiThread {
            try {
                val uiManager = UIManagerHelper.getUIManager(
                    reactApplicationContext,
                    if (BuildConfig.IS_NEW_ARCHITECTURE_ENABLED) UIManagerType.FABRIC else UIManagerType.DEFAULT
                )

                val view = uiManager?.resolveView(reactTag)

                if (view is ReactExoplayerView) {
                    callback(view)
                } else {
                    callback(null)
                }
            } catch (e: Exception) {
                callback(null)
            }
        }
    }

    @ReactMethod
    fun setPlayerPauseState(paused: Boolean?, reactTag: Int) {
        performOnPlayerView(reactTag) {
            it?.setPausedModifier(paused!!)
        }
    }

    @ReactMethod
    fun seek(info: ReadableMap, reactTag: Int) {
        if (!info.hasKey("time")) {
            return
        }

        val time = ReactBridgeUtils.safeGetInt(info, "time")
        performOnPlayerView(reactTag) {
            it?.seekTo((time * 1000f).roundToInt().toLong())
        }
    }

    @ReactMethod
    fun setVolume(volume: Float, reactTag: Int) {
        performOnPlayerView(reactTag) {
            it?.setVolumeModifier(volume)
        }
    }

    @ReactMethod
    fun getCurrentPosition(reactTag: Int, promise: Promise) {
        performOnPlayerView(reactTag) {
            it?.getCurrentPosition(promise)
        }
    }

    /**
     * Release all active ExoPlayer instances.
     * This is useful for freeing secure MediaCodec decoders (e.g., c2.qti.avc.decoder.secure)
     * which can only have one instance at a time.
     */
    @ReactMethod
    fun releaseAllPlayers(promise: Promise) {
        Log.d(REACT_CLASS, "releaseAllPlayers called")
        UiThreadUtil.runOnUiThread {
            try {
                val count = ReactExoplayerViewManager.getActivePlayerCount()
                Log.d(REACT_CLASS, "Releasing $count active players")
                ReactExoplayerViewManager.releaseAllPlayers()
                promise.resolve(count)
            } catch (e: Exception) {
                Log.e(REACT_CLASS, "Error releasing players: ${e.message}")
                promise.reject("RELEASE_ERROR", e.message, e)
            }
        }
    }

    /**
     * Get the count of active players
     */
    @ReactMethod
    fun getActivePlayerCount(promise: Promise) {
        UiThreadUtil.runOnUiThread {
            try {
                val count = ReactExoplayerViewManager.getActivePlayerCount()
                promise.resolve(count)
            } catch (e: Exception) {
                promise.reject("COUNT_ERROR", e.message, e)
            }
        }
    }

    companion object {
        private const val REACT_CLASS = "VideoManager"
    }
}
