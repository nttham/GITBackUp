//
//  ViewController.swift
//  PushNotification
//
//  Created by Srividya on 25/07/16.
//  Copyright © 2016 na. All rights reserved.
//

import UIKit

class ViewController: UIViewController {
    
    var container: UIView = UIView()
    var loadingView: UIView = UIView()
    var activityIndicator: UIActivityIndicatorView = UIActivityIndicatorView()
 
    override func viewDidLoad() {
//        print("came to viewDidLoad"
        NSLog("came to viewDidLoad")
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func showActivityIndicator(uiView: UIView) {
        NSLog("*********showActivityIndicatory***************")
        
        container.frame = uiView.frame
        container.center = uiView.center
        container.backgroundColor = UIColorFromHex(0xffffff, alpha: 0.3)
        
        loadingView.frame = CGRectMake(0, 0, 80, 80)
        loadingView.center = uiView.center
        loadingView.backgroundColor = UIColorFromHex(0x444444, alpha: 0.7)
        loadingView.clipsToBounds = true
        loadingView.layer.cornerRadius = 10
        
        activityIndicator.frame = CGRectMake(0.0, 0.0, 40.0, 40.0);
        activityIndicator.activityIndicatorViewStyle = UIActivityIndicatorViewStyle.WhiteLarge
        activityIndicator.center = CGPointMake(loadingView.frame.size.width / 2, loadingView.frame.size.height / 2);
        
        loadingView.addSubview(activityIndicator)
        container.addSubview(loadingView)
        uiView.addSubview(container)
        activityIndicator.startAnimating()
    }
    
    func hideActivityIndicator(uiView: UIView) {
        NSLog("*********hideActivityIndicator***************")
        dispatch_async(dispatch_get_main_queue()){
            self.activityIndicator.stopAnimating()
            self.container.removeFromSuperview()
        }
    }
    
    func UIColorFromHex(rgbValue:UInt32, alpha:Double=1.0)->UIColor {
        let red = CGFloat((rgbValue & 0xFF0000) >> 16)/256.0
        let green = CGFloat((rgbValue & 0xFF00) >> 8)/256.0
        let blue = CGFloat(rgbValue & 0xFF)/256.0
        return UIColor(red:red, green:green, blue:blue, alpha:CGFloat(alpha))
    }
    
    func updateDeviceToken(deviceID: String ,deviceToken: String,modelName: String) {
        NSLog("came to updateDeviceToken")
        
        let urlString = String(format: "http://pushnotificationservice.54.208.194.189.xip.io/devices/%@", deviceID)
        let updateUrl = NSURL(string: urlString)!
        
        let updateRequest = NSMutableURLRequest(URL: updateUrl)
        updateRequest.HTTPMethod = "PUT"
        updateRequest.setValue("application/json", forHTTPHeaderField: "Content-Type")
        
        //setting the body
        let body = NSString(format:"{\"userId\":\"%@\",\"deviceToken\":\"%@\"}", modelName,deviceToken) as String
        updateRequest.HTTPBody = body.dataUsingEncoding(NSUTF8StringEncoding)
        
        NSLog("***body*** %@",body)
        
        let updateSession = NSURLSession.sharedSession()
        let updateTask = updateSession.dataTaskWithRequest(updateRequest,completionHandler: {data, response, error -> Void in
            
            self.hideActivityIndicator(self.view)
            
            let updateStrData = NSString(data: data!, encoding: NSUTF8StringEncoding)!
            
            var updateAlert = UIAlertController();
            var updateAction = UIAlertAction();
            if let httpResponse = response as? NSHTTPURLResponse {
                if httpResponse.statusCode == 200 {
                    
                    do {
                        if let jsonResult = try NSJSONSerialization.JSONObjectWithData(data!, options: []) as? NSDictionary {
                            updateAlert = UIAlertController(title: "\(jsonResult["message"]!)", message: "", preferredStyle: .Alert)
                            updateAction = UIAlertAction(title: "ok", style: .Default, handler: nil);
                        }
                    } catch let error as NSError {
                        print(error.localizedDescription)
                        updateAlert = UIAlertController(title: "\(error.localizedDescription)", message: "", preferredStyle: .Alert)
                        updateAction = UIAlertAction(title: "ok", style: .Default, handler: nil);
                    }
                }
                else {
                    
                    //Kindly add error handling
                    updateAlert = UIAlertController(title: "\(updateStrData)", message: "", preferredStyle: .Alert)
                    updateAction = UIAlertAction(title: "ok", style: .Default, handler: nil);
                }
            }
            updateAlert.addAction(updateAction);
            self .presentViewController(updateAlert, animated: true, completion: nil);
            
        })
        updateTask.resume()
    }
    

    @IBAction func clickForRegisteringDevice(sender: UIButton) {
        self.showActivityIndicator(self.view)
        
        NSLog("came to clickForRegister");

        let url = NSURL(string: "http://pushnotificationservice.54.208.194.189.xip.io/devices")!
        let deviceID = UIDevice.currentDevice().identifierForVendor!.UUIDString
        
        let modelName = UIDevice.currentDevice().model
        let deviceToken = NSUserDefaults.standardUserDefaults().stringForKey("DEVICE-TOKEN")
        
        let request = NSMutableURLRequest(URL: url)
        request.HTTPMethod = "POST"
        request.setValue("application/json", forHTTPHeaderField: "Content-Type")

        //setting the body
        let body = NSString(format:"{\"deviceId\":\"%@\",\"userId\":\"%@\",\"deviceToken\":\"%@\",\"platform\":\"apns\",\"createdMode\":\"SDK\"}", deviceID,modelName,deviceToken!) as String
        request.HTTPBody = body.dataUsingEncoding(NSUTF8StringEncoding)
        
        
        NSLog("***body*** %@",body)
        
        
        let session = NSURLSession.sharedSession()
        
        
        let task = session.dataTaskWithRequest(request, completionHandler: {data, response, error -> Void in
           
            NSLog("HTTP request error")
            
            self.hideActivityIndicator(self.view)
            
            if let httpResponse = response as? NSHTTPURLResponse {
                NSLog("error \(httpResponse.statusCode)")
                let strData = NSString(data: data!, encoding: NSUTF8StringEncoding)!
                NSLog("HTTP request DATA = \(strData)")
                var alert = UIAlertController();
                var action = UIAlertAction();
                
                if httpResponse.statusCode == 200 {
                    alert = UIAlertController(title: "\(strData)", message: "", preferredStyle: .Alert)
                    action = UIAlertAction(title: "ok", style: .Default, handler: nil);
                    alert.addAction(action);
                }
                else if httpResponse.statusCode == 400{
                    // create the alert
                    alert = UIAlertController(title: "UIAlertController", message: "\(strData)", preferredStyle: UIAlertControllerStyle.Alert)
                    
                    // add the actions (buttons)
                    alert.addAction(UIAlertAction(title: "Update", style: UIAlertActionStyle.Default, handler: { action in
                        self.showActivityIndicator(self.view)
                        self.updateDeviceToken(deviceID,deviceToken: deviceToken!,modelName: modelName)
                        
                    }))
                    alert.addAction(UIAlertAction(title: "Cancel", style: UIAlertActionStyle.Cancel, handler: nil))
                }
                else {
                    //Kindly add error handling
                    alert = UIAlertController(title: "\(strData)", message: "\(strData)", preferredStyle: .Alert)
                    action = UIAlertAction(title: "OK", style: .Default, handler: nil);
                    alert.addAction(action);
                }
                self .presentViewController(alert, animated: true, completion: nil);
                
            }
    })
        task.resume()
    }
}

