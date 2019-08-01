//
//  MotionActivity.m
//  Música VIVA
//
//  Created by Nico on 11/12/18.
//

#import <Foundation/Foundation.h>
#import "MotionActivity.h"
#import <CoreMotion/CoreMotion.h>

NSInteger lastActivityType = -1;

@interface MotionActivity ()
@property (nonatomic, retain) CMMotionActivityManager* activityManager;
@end

@implementation MotionActivity;

- (void) startUpdates: (CDVInvokedUrlCommand*)command
{
    BOOL isActivityAvailable = [CMMotionActivityManager isActivityAvailable];
    if(isActivityAvailable)
    {
        _activityManager = [[CMMotionActivityManager alloc] init];
        
        [self.commandDelegate runInBackground:^{
            [self.activityManager startActivityUpdatesToQueue:[NSOperationQueue mainQueue] withHandler:^(CMMotionActivity *activity) {
                NSInteger activityType = -1;
                if (activity.automotive) {
                    activityType = VEHICLE;
                } else if (activity.stationary) {
                    activityType = STILL;
                } else if (activity.walking) {
                    activityType = WALKING;
                } else if (activity.running) {
                    activityType = RUNNING;
                }
                if (activity.confidence != CMMotionActivityConfidenceLow && activityType != -1 && activityType != lastActivityType) {
                    lastActivityType = activityType;
                    [self.commandDelegate evalJs:[NSString stringWithFormat:@"plugins.motion.MotionActivity.publish(%ld)", (long)activityType]];
                }
            }];
        }];
    }
    else
    {
        [self.commandDelegate sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"CMMotionActivityManager is not available"] callbackId:command.callbackId];
    }
}

- (void) stopUpdates: (CDVInvokedUrlCommand*)command
{
    BOOL isActivityAvailable =  [CMMotionActivityManager isActivityAvailable];
    
    if(isActivityAvailable){
        [self.activityManager stopActivityUpdates];
        [self.commandDelegate sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:@"stopped"] callbackId:command.callbackId];
    }
    else
    {
        NSLog(@"CMMotionActivityManager is not available");
        [self.commandDelegate sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"CMMotionActivityManager is not available"] callbackId:command.callbackId];
    }
}

@end
