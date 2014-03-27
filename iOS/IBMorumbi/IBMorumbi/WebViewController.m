//
//  WebViewController.m
//  IBMorumbi
//
//  Created by Daniel Arndt Alves on 3/26/14.
//  Copyright (c) 2014 ProgDan Software. All rights reserved.
//

#import "WebViewController.h"

@interface WebViewController ()

@property (weak, nonatomic) IBOutlet UIActivityIndicatorView *spinner;
@property (weak, nonatomic) IBOutlet UIWebView *web;

- (IBAction)voltar:(UIBarButtonItem *)sender;
- (IBAction)avancar:(UIBarButtonItem *)sender;
- (IBAction)parar:(UIBarButtonItem *)sender;
- (IBAction)recarregar:(UIBarButtonItem *)sender;

@end

@implementation WebViewController

- (void)viewDidLoad
{
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    
    [self openPageWithUrl:@"http://www.ibmorumbi.com.br"];
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

- (IBAction)voltar:(UIBarButtonItem *)sender {
    if (self.web.canGoBack) {
        [self.web goBack];
    }
}

- (IBAction)avancar:(UIBarButtonItem *)sender {
    if (self.web.canGoForward) {
        [self.web goForward];
    }
}

- (IBAction)parar:(UIBarButtonItem *)sender {
    if (self.web.isLoading) {
        [self.web stopLoading];
    }
}

- (IBAction)recarregar:(UIBarButtonItem *)sender {
    [self.web reload];
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

@end
