package dev.andiksetyawan.fake_gps_detector

import android.app.Activity
import android.content.Context
import android.os.Build
import android.provider.Settings
import androidx.annotation.NonNull
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry.Registrar

import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

/** FakeGpsDetectorPlugin */
public class FakeGpsDetectorPlugin: FlutterPlugin, MethodCallHandler, ActivityAware {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private lateinit var channel : MethodChannel
  private lateinit var activity: Activity
  private lateinit var context: Context
  var lastResult: MethodChannel.Result? = null
  private lateinit var fusedLocationClient: FusedLocationProviderClient

  override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    channel = MethodChannel(flutterPluginBinding.getFlutterEngine().getDartExecutor(), "fake_gps_detector")
    channel.setMethodCallHandler(this);
    context = flutterPluginBinding.applicationContext
  }

  // This static function is optional and equivalent to onAttachedToEngine. It supports the old
  // pre-Flutter-1.12 Android projects. You are encouraged to continue supporting
  // plugin registration via this function while apps migrate to use the new Android APIs
  // post-flutter-1.12 via https://flutter.dev/go/android-project-migration.
  //
  // It is encouraged to share logic between onAttachedToEngine and registerWith to keep
  // them functionally equivalent. Only one of onAttachedToEngine or registerWith will be called
  // depending on the user's project. onAttachedToEngine or registerWith must both be defined
  // in the same class.
  companion object {
    @JvmStatic
    fun registerWith(registrar: Registrar) {
      val channel = MethodChannel(registrar.messenger(), "fake_gps_detector")
      channel.setMethodCallHandler(FakeGpsDetectorPlugin())
    }
  }

  override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
    when (call.method) {
        "isMock" -> {
          lastResult = result
          GlobalScope.launch{
            checkMock()
          }
        }
        "isEmulator" -> {
          lastResult = result
          checkEmulator()
        }
        else -> result.notImplemented()
    }
  }

  override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    channel.setMethodCallHandler(null)
  }


  private suspend fun checkMock() {
    fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)
    withContext(Dispatchers.Main) {
      try {
        val location = fusedLocationClient.lastLocation.await()
        val isMock = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
          location.isFromMockProvider
        } else {
          !Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ALLOW_MOCK_LOCATION).equals("0")
        }
        lastResult!!.success(isMock)
        lastResult = null
      } catch (e: Throwable) {
        lastResult = null
        println("caught exception: $e");
      }
    }
  }

  private fun checkEmulator() {
    try {
      val isEmulator =  ((Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
              || Build.FINGERPRINT.startsWith("generic")
              || Build.FINGERPRINT.startsWith("unknown")
              || Build.HARDWARE.contains("goldfish")
              || Build.HARDWARE.contains("ranchu")
              || Build.MODEL.contains("google_sdk")
              || Build.MODEL.contains("Emulator")
              || Build.MODEL.contains("Android SDK built for x86")
              || Build.MANUFACTURER.contains("Genymotion")
              || Build.PRODUCT.contains("sdk_google")
              || Build.PRODUCT.contains("google_sdk")
              || Build.PRODUCT.contains("sdk")
              || Build.PRODUCT.contains("sdk_x86")
              || Build.PRODUCT.contains("vbox86p")
              || Build.PRODUCT.contains("emulator")
              || Build.PRODUCT.contains("simulator"))
      lastResult!!.success(isEmulator)
      lastResult = null
    }catch (e: Throwable){
      lastResult = null
    }
  }

  override fun onDetachedFromActivity() {
  }

  override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
  }

  override fun onAttachedToActivity(binding: ActivityPluginBinding) {
    activity = binding.activity
  }

  override fun onDetachedFromActivityForConfigChanges() {
  }
}
