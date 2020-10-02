# Fake GPS Detector, Emulator Device Detector

A Flutter plugin for detecting the emulator device and mock location / fake gps.


## Getting Started

In your flutter project add the dependency:

```yml
dependencies:
  ...
  fake_gps_detector: ^0.5.0
```


## Usage
#### Importing package
```
import 'package:fake_gps_detector/fake_gps_detector.dart';
```

Permission src/main/AndroidManifest.xml
```
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
```
#### Using it
(ANDROID ONLY) Checks whether device is real or emulator
```
bool isMock = await FakeGpsDetector.isFakeGps;
```
(ANDROID ONLY) Can this device mock location - no need to root!
```
bool isEmulator = await FakeGpsDetector.isEmulator;
```

# Note:
#### Add a location permission request before calling the "isFakeGps" method, make sure it has been approved to access the location. For more details, please open the example directory