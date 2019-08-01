function MotionActivity() {
    this.subscribers = [];
};

MotionActivity.prototype.startUpdates = function(success, error) {
    cordova.exec(success, error, 'MotionActivity', 'startUpdates');
};

MotionActivity.prototype.publish = function(activityType, transitionType) {
    var activityName;
    switch (activityType) {
        case 0:
            activityName = "IN_VEHICLE";
            break;
        case 3:
            activityName = "STILL";
            break;
        case 7:
            activityName = "WALKING";
            break;
        case 8:
            activityName = "RUNNING";
            break;
    }
    this.subscribers.forEach(function(callback){
        callback.call(null, activityName);
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
