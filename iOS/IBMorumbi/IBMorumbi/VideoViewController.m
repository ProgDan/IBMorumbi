//
//  VideoViewController.m
//  IBMorumbi
//
//  Created by Daniel Arndt Alves on 3/17/14.
//  Copyright (c) 2014 ProgDan Software. All rights reserved.
//

#import "VideoViewController.h"

@interface VideoViewController ()
@property (weak, nonatomic) IBOutlet UIView *areaVideo;
@property (weak, nonatomic) IBOutlet UIImageView *imgBackVideo;
@property (weak, nonatomic) IBOutlet UIActivityIndicatorView *spinner;

// Objetio responsável por executar arquivos de vídeo
@property (strong, nonatomic) MPMoviePlayerController *playerVideo;

@end

@implementation VideoViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    
    // Cadastrar a ViewController como ouvinte das mensagens do player de vídeo
    [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(playerVideoMudouEstado) name:MPMoviePlayerPlaybackStateDidChangeNotification object:nil];
    
    // Localizar o arquivo
    NSURL *urlVideoRemoto = [NSURL URLWithString:@"http://www.ibmorumbi.org.br/audio/2014/morumbi_mais/morumbi_mais_hd.mp4"];
    
    [self criarNovoPlayer:urlVideoRemoto];
    
    [self.spinner startAnimating];
    
    // Trazendo a view do spinner para o topo das views
    [self.areaVideo bringSubviewToFront:self.spinner];
    
}

- (void)criarNovoPlayer:(NSURL *)url {
    if (self.playerVideo) {
        [self.playerVideo stop];
        [self.playerVideo.view removeFromSuperview];
        self.playerVideo = nil;
    }
    
    self.playerVideo = [[MPMoviePlayerController alloc] initWithContentURL:url];
    self.playerVideo.view.frame = self.areaVideo.bounds;
    
    // adicionar na tela
    [self.areaVideo addSubview:self.playerVideo.view];
    
    [self.playerVideo play];
}

- (void) playerVideoMudouEstado {
    if (self.playerVideo.loadState == MPMovieLoadStatePlayable ||
        self.playerVideo.loadState == MPMovieLoadStatePlaythroughOK ||
        self.playerVideo.loadState == MPMovieLoadStateStalled ||
        self.playerVideo.loadState == MPMovieLoadStateUnknown)
    {
        [self.spinner stopAnimating];
    }
    else
    {
        [self.spinner startAnimating];
    }
    
}


- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
    [self.playerVideo stop];
}


@end
