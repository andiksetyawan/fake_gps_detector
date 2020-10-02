import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:fake_gps_detector/fake_gps_detector.dart';

import 'package:permission_handler/permission_handler.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  dynamic _isFakeGps = 'Unknown';
  dynamic _isEmulator = 'Unknown';

  @override
  void initState() {
    super.initState();
    initPlatformState();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    bool isMock;
    bool isEmulator;
    // Platform messages may fail, so we use a try/catch PlatformException.
    try {
      if (await Permission.location.request().isGranted) {
        isMock = await FakeGpsDetector.isFakeGps;
        isEmulator = await FakeGpsDetector.isEmulator;
      }
    } on PlatformException {
      print("failed");
    }

    if (!mounted) return;
    setState(() {
      _isFakeGps = isMock;
      _isEmulator = isEmulator;
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: Text('Fake GPS : $_isFakeGps , Emulator : $_isEmulator'),
        ),
      ),
    );
  }
}
