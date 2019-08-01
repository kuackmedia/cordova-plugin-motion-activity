//  MotionActivity.h

#import <Foundation/Foundation.h>
#import <Cordova/CDVPlugin.h>

enum ActivityTypes {
    VEHICLE = 0,
    STILL = 3,
    WALKING = 7,
    RUNNING = 8
};
typedef NSUInteger ActivityTypes;


@interface MotionActivity : CDVPlugin {
}

- (void) startUpdates: (CDVInvokedUrlCommand*)command;
- (void) stopUpdates: (CDVInvokedUrlCommand*)command;

@end
