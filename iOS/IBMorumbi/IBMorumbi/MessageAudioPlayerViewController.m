//
//  MessageAudioPlayerViewController.m
//  IBMorumbi
//
//  Created by Daniel Arndt Alves on 3/26/14.
//  Copyright (c) 2014 ProgDan Software. All rights reserved.
//

#import "MessageAudioPlayerViewController.h"

@interface MessageAudioPlayerViewController ()

@property (strong, nonatomic) AVAudioSession *audioSession;

#pragma mark - Player Buttons
@property (weak, nonatomic) IBOutlet UIButton *btnBack;
@property (weak, nonatomic) IBOutlet UIButton *btnStop;
@property (weak, nonatomic) IBOutlet UIButton *btnPlay;
@property (weak, nonatomic) IBOutlet UIButton *btnPause;

#pragma mark - Info sobre a Mensagem
@property (weak, nonatomic) IBOutlet UILabel *tema;
@property (weak, nonatomic) IBOutlet UILabel *passagens;
@property (weak, nonatomic) IBOutlet UILabel *pregador;
@property (weak, nonatomic) IBOutlet UIImageView *imgPregador;
@property (weak, nonatomic) IBOutlet UILabel *data;
@property (weak, nonatomic) IBOutlet UILabel *cultos;

#pragma mark - Funções do Player
- (IBAction)voltar:(UIButton *)sender;
- (IBAction)stop:(UIButton *)sender;
- (IBAction)play:(UIButton *)sender;
- (IBAction)pause:(UIButton *)sender;


@end

@implementation MessageAudioPlayerViewController

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
    
    // Recuperando a referência da sessão compartilhada
    self.audioSession = [AVAudioSession sharedInstance];
    [self.audioSession setActive:YES error:nil];
    
    // Configurando as informações sobre a mensagem
    self.tema.text = self.mensagem[@"tema"];
    self.passagens.text = self.mensagem[@"passagens"];
    self.pregador.text = self.mensagem[@"pregador"];
    self.imgPregador.image = [[UIImage alloc] initWithData:[NSData dataWithContentsOfURL:[NSURL URLWithString:self.mensagem[@"pregador_img"]]]];
    self.data.text = self.mensagem[@"data"];
    self.cultos.text = self.mensagem[@"cultos"];
    
    // Configurando a música para o player
    [self playerPrepare];
}

-(void)observeValueForKeyPath:(NSString *)keyPath ofObject:(id)object change:(NSDictionary *)change context:(void *)context
{
    if (object == self.player && [keyPath isEqualToString:@"status"]) {
        if (self.player.status == AVPlayerStatusFailed) {
//            NSLog(@"AVPlayerFailed");
        }
        else if (self.player.status == AVPlayerStatusReadyToPlay) {
//            NSLog(@"AVPlayerStatusReadyToPlay");
            [self.player play];
        }
        else if (self.player.status == AVPlayerStatusUnknown){
//            NSLog(@"AVPlayer Unknown");
        }
    }
}

-(void) createNewPlayerForURL: (NSURL*) urlAudio
{
    self.player = [[AVPlayer alloc] initWithURL:urlAudio];
    [self.player addObserver:self forKeyPath:@"status" options:0 context:nil];
}

-(void) playerPrepare {
    if (self.player) {
        self.player = nil;
        
        // Ajustes de interface
        self.btnBack.enabled = NO;
        self.btnPlay.enabled = YES;
        self.btnPause.enabled = NO;
        self.btnStop.enabled = NO;
    }
    
    // Localizar a URL do arquivo a ser reproduzido
    NSURL *urlAudio = [[NSURL alloc] initWithString:self.mensagem[@"audio"]];
    [self createNewPlayerForURL:urlAudio];
    
    // Ajustes de interface
    self.btnBack.enabled = YES;
    self.btnPlay.enabled = NO;
    self.btnPause.enabled = YES;
    self.btnStop.enabled = YES;
}

- (IBAction)voltar:(UIButton *)sender {
    [self.player seekToTime:CMTimeMake(0, 1)];
    
    // Ajustes de interface
    self.btnBack.enabled = YES;
    self.btnPlay.enabled = NO;
    self.btnPause.enabled = YES;
    self.btnStop.enabled = YES;
}

- (IBAction)stop:(UIButton *)sender {
    self.player = nil;
    
    // Ajustes de interface
    self.btnBack.enabled = NO;
    self.btnPlay.enabled = YES;
    self.btnPause.enabled = NO;
    self.btnStop.enabled = NO;
}

- (IBAction)play:(UIButton *)sender {
    if (self.player) {
        [self.player play];
    }
    else {
        [self playerPrepare];
    }
    
    // Ajustes de interface
    self.btnBack.enabled = YES;
    self.btnPlay.enabled = NO;
    self.btnPause.enabled = YES;
    self.btnStop.enabled = YES;
}

- (IBAction)pause:(UIButton *)sender {
    [self.player pause];
    
    // Ajustes de interface
    self.btnBack.enabled = YES;
    self.btnPlay.enabled = YES;
    self.btnPause.enabled = NO;
    self.btnStop.enabled = YES;
}
@end
