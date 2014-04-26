//
//  MessageVideoPlayerViewController.m
//  IBMorumbi
//
//  Created by Daniel Arndt Alves on 3/26/14.
//  Copyright (c) 2014 ProgDan Software. All rights reserved.
//

#import "MessageVideoPlayerViewController.h"

@interface MessageVideoPlayerViewController ()

@property (weak, nonatomic) IBOutlet UIView *videoArea;
@property (weak, nonatomic) IBOutlet UIActivityIndicatorView *spinner;


#pragma mark - Info sobre a Mensagem
@property (weak, nonatomic) IBOutlet UILabel *tema;
@property (weak, nonatomic) IBOutlet UILabel *passagens;
@property (weak, nonatomic) IBOutlet UILabel *pregador;
@property (weak, nonatomic) IBOutlet UIImageView *imgPregador;
@property (weak, nonatomic) IBOutlet UIImageView *imgLogoSmall;

@property (weak, nonatomic) IBOutlet UILabel *data;
@property (weak, nonatomic) IBOutlet UILabel *cultos;


@property (weak, nonatomic) IBOutlet UIImageView *imgLogo;

// Objetio responsável por executar arquivos de vídeo
@property (strong, nonatomic) MPMoviePlayerController *playerVideo;

@end

@implementation MessageVideoPlayerViewController

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
    
    if ([self.mensagem[@"tema"] isEqualToString:@"Morumbi+"]) {
        // Ajustes de interface
        self.tema.hidden = YES;
        self.passagens.hidden = YES;
        self.pregador.hidden = YES;
        self.imgPregador.hidden = YES;
        self.data.hidden = YES;
        self.cultos.hidden = YES;
        
        self.imgLogo.hidden = NO;
        if (UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiomPad) {
            self.imgLogoSmall.hidden = YES;
        }
    }
    else {
        // Configurando as informações sobre a mensagem
        self.tema.text = self.mensagem[@"tema"];
        self.passagens.text = self.mensagem[@"passagens"];
        self.pregador.text = self.mensagem[@"pregador"];
        self.imgPregador.image = [[UIImage alloc] initWithData:[NSData dataWithContentsOfURL:[NSURL URLWithString:self.mensagem[@"pregador_img"]]]];
        self.data.text = self.mensagem[@"data"];
        self.cultos.text = self.mensagem[@"cultos"];

        // Ajustes de interface
        self.tema.hidden = NO;
        self.passagens.hidden = NO;
        self.pregador.hidden = NO;
        self.imgPregador.hidden = NO;
        self.data.hidden = NO;
        self.cultos.hidden = NO;
        
        self.imgLogo.hidden = YES;
        if (UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiomPad) {
            self.imgLogoSmall.hidden = NO;
        }
    }
    
    // Cadastrar a ViewController como ouvinte das mensagens do player de vídeo
    [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(playerVideoMudouEstado) name:MPMoviePlayerPlaybackStateDidChangeNotification object:nil];
    
    NSURL *urlVideoRemoto = [NSURL URLWithString:self.mensagem[@"video"]];
    
    [self criarNovoPlayer:urlVideoRemoto];
    
    [self.spinner startAnimating];
//    self.spinner.hidden = NO;
    
    // Trazendo a view do spinner para o topo das views
    [self.videoArea bringSubviewToFront:self.spinner];
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    self.screenName = @"MessageVideoPlayer Screen";
}


- (void) playerVideoMudouEstado {
    if (self.playerVideo.loadState == MPMovieLoadStatePlayable || self.playerVideo.loadState == MPMovieLoadStatePlaythroughOK || self.playerVideo.loadState == MPMovieLoadStateUnknown) {
        [self.spinner stopAnimating];
//        self.spinner.hidden = YES;
    }
    
}

- (void)criarNovoPlayer:(NSURL *)url {
    if (self.playerVideo) {
        [self.playerVideo stop];
        [self.playerVideo.view removeFromSuperview];
        self.playerVideo = nil;
    }
    
    self.playerVideo = [[MPMoviePlayerController alloc] initWithContentURL:url];
    self.playerVideo.view.frame = self.videoArea.bounds;
    
    
    // adicionar na tela
    [self.videoArea addSubview:self.playerVideo.view];
    self.playerVideo.view.clipsToBounds = YES;
    self.playerVideo.view.autoresizingMask = UIViewAutoresizingFlexibleTopMargin | UIViewAutoresizingFlexibleBottomMargin | UIViewAutoresizingFlexibleLeftMargin | UIViewAutoresizingFlexibleHeight | UIViewAutoresizingFlexibleHeight | UIViewAutoresizingFlexibleWidth;
    
    [self.playerVideo play];
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
