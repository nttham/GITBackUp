//
//  AppDelegate.swift
//  PushNotification
//
//  Created by Srividya on 25/07/16.
//  Copyright © 2016 na. All rights reserved.
//

import UIKit

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate {

    var window: UIWindow?


    func application(application: UIApplication, didFinishLaunchingWithOptions launchOptions: [NSObject: AnyObject]?) -> Bool {
        // Override point for customization after application launch.
        
        //Registering for push notifications.
        let notificationTypes: UIUserNotificationType = [UIUserNotificationType.Alert, UIUserNotificationType.Badge, UIUserNotificationType.Sound]
        let pushNotificationSettings = UIUserNotificationSettings(forTypes: notificationTypes, categories: nil)
        
        application.registerUserNotificationSettings(pushNotificationSettings)
        application.registerForRemoteNotifications()
        
        return true
    }

    func applicationWillResignActive(application: UIApplication) {
        // Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
        // Use this method to pause ongoing tasks, disable timers, and throttle down OpenGL ES frame rates. Games should use this method to pause the game.
    }

    func applicationDidEnterBackground(application: UIApplication) {
        // Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later.
        // If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.
    }

    func applicationWillEnterForeground(application: UIApplication) {
        // Called as part of the transition from the background to the inactive state; here you can undo many of the changes made on entering the background.
    }

    func applicationDidBecomeActive(application: UIApplication) {
        // Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
    }

    func applicationWillTerminate(application: UIApplication) {
        // Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
    }

    
    //Push notification delegate methods
    func application(application: UIApplication, didRegisterForRemoteNotificationsWithDeviceToken deviceToken: NSData) {
        
        let deviceTokenOrigin = deviceToken.description.stringByTrimmingCharactersInSet(NSCharacterSet(charactersInString: "<>"))
        let deviceTokenFiltered = deviceTokenOrigin.stringByReplacingOccurrencesOfString(" ", withString: "")
        NSLog("DeviceToken : %@", deviceTokenFiltered)
        
        NSUserDefaults.standardUserDefaults().setObject(deviceTokenFiltered, forKey: "DEVICE-TOKEN")
        NSUserDefaults.standardUserDefaults().synchronize()
//        
//        let deviceTok = NSUserDefaults.standardUserDefaults().objectForKey("DEVICE-TOKEN")
//        NSLog("DeviceToken deviceTok : %@", deviceTok)
        
    }
    
    func application(application: UIApplication, didFailToRegisterForRemoteNotificationsWithError error: NSError) {
        print(error)
    }
    
    func application(application: UIApplication, didReceiveRemoteNotification userInfo: [NSObject : AnyObject]) {
        print(userInfo)
    }

}

