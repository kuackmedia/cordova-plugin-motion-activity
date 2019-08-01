#!/usr/bin/env node

'use strict';

var fs    = require('fs');
var plist = require('plist');  // www.npmjs.com/package/plist
var xml2js = require('xml2js');

module.exports = function (context) {
    var configPath = './config.xml';
    var configXml = fs.readFileSync(configPath).toString();
    xml2js.parseString(configXml, function(err, config){
        if (err) return console.error(err);
        var appName = config.widget.name[0];
        // plist
        var plistPath = context.opts.projectRoot + '/platforms/ios/'+ appName +'/'+ appName +'-Info.plist';
        var xml = fs.readFileSync(plistPath, 'utf8');
        var obj = plist.parse(xml);
        //
        if (!obj.hasOwnProperty('NSMotionUsageDescription') || obj.NSMotionUsageDescription === '') {
            obj.NSMotionUsageDescription = 'This app requires motion access to function properly';
        }
        // write
        xml = plist.build(obj);
        fs.writeFileSync(plistPath, xml, { encoding: 'utf8' });
    });
};
