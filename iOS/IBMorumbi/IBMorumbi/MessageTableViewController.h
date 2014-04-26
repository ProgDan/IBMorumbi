//
//  MessageTableViewController.h
//  IBMorumbi
//
//  Created by Daniel Arndt Alves on 3/26/14.
//  Copyright (c) 2014 ProgDan Software. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <sys/utsname.h>
#import "MessageAudioPlayerViewController.h"
#import "MessageVideoPlayerViewController.h"
#import "GAITrackedViewController.h"

@interface MessageTableViewController : GAITrackedViewController <UITableViewDelegate, UITableViewDataSource>

@end
