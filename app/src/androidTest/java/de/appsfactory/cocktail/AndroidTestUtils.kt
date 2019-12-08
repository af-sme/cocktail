package de.appsfactory.cocktail

import android.app.Activity
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry
import androidx.test.runner.lifecycle.Stage
import androidx.test.uiautomator.UiDevice
import java.io.File
import java.text.SimpleDateFormat

private val formatter = SimpleDateFormat("yyyy.MM.dd HH:mm:ss.SSS")

fun makeScreenshot(info: String) {
    val uiDevice = UiDevice.getInstance(getInstrumentation())
    val appContext = getInstrumentation().targetContext
    val screenshotsDir = (File(
        appContext.getExternalFilesDir(null)!!.absolutePath,
        "/test-screenshots"
    ))
    if (!screenshotsDir.exists()) {
        screenshotsDir.mkdir()
    }
    val screenshot =
        File(screenshotsDir, info + "_" + formatter.format(System.currentTimeMillis()) + ".png")

    uiDevice.takeScreenshot(screenshot)
}

fun getCurrentActivity(): Activity? {
    var currentActivity: Activity? = null
    getInstrumentation().runOnMainSync {
        run {
            currentActivity = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(
                Stage.RESUMED
            ).elementAtOrNull(0)
        }
    }
    return currentActivity
}
