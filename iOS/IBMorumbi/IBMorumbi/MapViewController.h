//
//  MapViewController.h
//  IBMorumbi
//
//  Created by Daniel Arndt Alves on 3/17/14.
//  Copyright (c) 2014 ProgDan Software. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <CoreLocation/CoreLocation.h>
#import <MapKit/MapKit.h>
#import "GAITrackedViewController.h"

@interface MapViewController : GAITrackedViewController <MKMapViewDelegate, CLLocationManagerDelegate>

@end
