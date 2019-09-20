function MotionActivity() {
    this.subscribers = [];
    this.MOTION_VEHICLE = 0;
    this.MOTION_STILL = 3;
    this.MOTION_WALKING = 7;
    this.MOTION_RUNNING = 8;
};

MotionActivity.prototype.startUpdates = function(success, error) {
    cordova.exec(success, error, 'MotionActivity', 'startUpdates');
};

MotionActivity.prototype.publish = function(activityType, transitionType) {
    var activity;
    switch (activityType) {
        case this.MOTION_VEHICLE:
            activity = this.MOTION_VEHICLE;
            break;
        case this.MOTION_STILL:
            activity = this.MOTION_STILL;
            break;
        case this.MOTION_WALKING:
            activity = this.MOTION_WALKING;
            break;
        case this.MOTION_RUNNING:
            activity = this.MOTION_RUNNING;
            break;
    }
    this.subscribers.forEach(function(callback){
        callback.call(null, activity);
    });
};

MotionActivity.prototype.onMotion = function(callback) {
    this.subscribers.push(callback);
    return {
        remove: function() {
            var index = this.subscribers.indexOf(callback);
            if (index > -1) {
                this.subscribers.splice(index, 1);
            }
        }
    }
};

MotionActivity.prototype.stopUpdates = function(success, error) {
    cordova.exec(success, error, 'MotionActivity', 'stopUpdates');
};

module.exports = new MotionActivity();
