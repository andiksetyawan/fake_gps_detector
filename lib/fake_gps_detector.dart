
import 'dart:async';

import 'package:flutter/services.dart';

class FakeGpsDetector {
  static const MethodChannel _channel = const MethodChannel('fake_gps_detector');

  static Future<bool> get isFakeGps async {
    try{
      return await _channel.invokeMethod('isMock');
    }catch(err){
      return false;
    }
  }

  static Future<bool> get isEmulator async {
    try{
      return await _channel.invokeMethod('isEmulator');
    }catch(err){
      return false;
    }
  }
}
