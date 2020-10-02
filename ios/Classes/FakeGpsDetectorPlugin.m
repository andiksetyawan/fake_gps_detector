#import "FakeGpsDetectorPlugin.h"
#if __has_include(<fake_gps_detector/fake_gps_detector-Swift.h>)
#import <fake_gps_detector/fake_gps_detector-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "fake_gps_detector-Swift.h"
#endif

@implementation FakeGpsDetectorPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftFakeGpsDetectorPlugin registerWithRegistrar:registrar];
}
@end
