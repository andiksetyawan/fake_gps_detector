import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:fake_gps_detector/fake_gps_detector.dart';

void main() {
  const MethodChannel channel = MethodChannel('fake_gps_detector');

  TestWidgetsFlutterBinding.ensureInitialized();

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getPlatformVersion', () async {
    expect(await FakeGpsDetector.platformVersion, '42');
  });
}
