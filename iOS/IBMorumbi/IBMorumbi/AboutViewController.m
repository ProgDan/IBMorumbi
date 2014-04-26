//
//  AboutViewController.m
//  IBMorumbi
//
//  Created by Daniel Arndt Alves on 3/29/14.
//  Copyright (c) 2014 ProgDan Software. All rights reserved.
//

#import "AboutViewController.h"

@interface AboutViewController ()
- (IBAction)openFacebook:(UIButton *)sender;
- (IBAction)openTwitter:(UIButton *)sender;
@property (weak, nonatomic) IBOutlet UILabel *labelRelease;

@end

@implementation AboutViewController

- (void)viewDidLoad
{
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    NSString *appVersion = [NSBundle mainBundle].infoDictionary[@"CFBundleVersion"];
    self.labelRelease.text = [NSString stringWithFormat:@"v. %@", appVersion];
}

- (IBAction)openFacebook:(UIButton *)sender {
    // Para pegar o ID da página no Facebook: https://graph.facebook.com/yourappspage
    NSURL *facebookURL = [NSURL URLWithString:@"fb://profile/100002117943796"];
    if ([[UIApplication sharedApplication] canOpenURL:facebookURL]) {
        [[UIApplication sharedApplication] openURL:facebookURL];
    } else {
        [[UIApplication sharedApplication] openURL:[NSURL URLWithString:@"https://www.facebook.com/igreja.batistadomorumbi"]];
    }
}

- (IBAction)openTwitter:(UIButton *)sender {
    NSURL *twitterURL = [NSURL URLWithString:@"twitter://user?screen_name=ibmorumbi"];
    if ([[UIApplication sharedApplication] canOpenURL:twitterURL]) {
        [[UIApplication sharedApplication] openURL:twitterURL];
    } else {
        [[UIApplication sharedApplication] openURL:[NSURL URLWithString:@"https://twitter.com/ibmorumbi"]];
    }
}
@end
