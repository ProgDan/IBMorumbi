//
//  GiveViewController.m
//  IBMorumbi
//
//  Created by Daniel Arndt Alves on 16/01/15.
//  Copyright (c) 2015 ProgDan Software. All rights reserved.
//

#import "GiveViewController.h"

@interface GiveViewController ()

@property (weak, nonatomic) IBOutlet UIActivityIndicatorView *spinner;
@property (weak, nonatomic) IBOutlet UIWebView *web;

@end

@implementation GiveViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    
    [self openPageWithUrl:@"http://www.ibmorumbi.com.br/gestao/contribua.asp"];
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    self.screenName = @"Give Screen";
}

-(void) openPageWithUrl: (NSString*) strUrl
{
    if ([strUrl rangeOfString:@"http://"].location != 0) {
        strUrl = [@"http://" stringByAppendingString:strUrl];
    }
    
    // Cria uma URL com a string do endereço do site
    NSURL *url = [NSURL URLWithString:strUrl];
    
    // criam uma requisição web utilizando uma URL
    NSURLRequest *request = [NSURLRequest requestWithURL:url];
    
    // carrega um request na WebView
    [self.web loadRequest:request];
}

-(void)webViewDidStartLoad:(UIWebView *)webView
{
    [self.spinner startAnimating];
    [[UIApplication sharedApplication] setNetworkActivityIndicatorVisible:YES];
}

-(void)webViewDidFinishLoad:(UIWebView *)webView
{
    [self.spinner stopAnimating];
    [[UIApplication sharedApplication] setNetworkActivityIndicatorVisible:NO];
}

-(void)webView:(UIWebView *)webView didFailLoadWithError:(NSError *)error
{
    [self.spinner stopAnimating];
    [[UIApplication sharedApplication] setNetworkActivityIndicatorVisible:NO];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
