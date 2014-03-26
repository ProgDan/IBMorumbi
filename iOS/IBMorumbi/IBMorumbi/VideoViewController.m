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
@property (weak, nonatomic) IBOutlet UITableView *table;

// Objeto responsável por executar arquivos de vídeo
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
    
    [self.spinner startAnimating];
    
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

#pragma mark Navigation

- (void)viewWillDisappear:(BOOL)animated
{
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
    [self.playerVideo stop];
}

#pragma mark UITableViewDataSource

-(NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 2;
}

-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    if (section) {
        return 1;
    }
    return 1;
}

-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    UITableViewCell *cell;
    if (indexPath.section == 0) {
        cell = [tableView dequeueReusableCellWithIdentifier:@"MorumbiPlus" forIndexPath:indexPath];
        cell.textLabel.text = @"Morumbi+";
        cell.imageView.image = [UIImage imageNamed:@"preview_morumbiplus_470.jpg"];
    }
    if (indexPath.section == 1) {
        cell = [tableView dequeueReusableCellWithIdentifier:@"VideoCell" forIndexPath:indexPath];
        cell.textLabel.text = @"Um dia a casa Cai";
        cell.detailTextLabel.text = @"(Lucas 15) - Pr. Lisânias Moura";
        cell.imageView.image = [UIImage imageNamed:@"preview_mensagens_470.jpg"];
    }
    return cell;
}

-(NSString *)tableView:(UITableView *)tableView titleForHeaderInSection:(NSInteger)section
{
    if (section) {
        return @"Mensagens";
    }
    else {
        return @"Morumbi+";
    }
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    // Localizar o arquivo
    NSURL *urlVideoRemoto;
    
    if (indexPath.section == 0 && indexPath.row == 0) {
        urlVideoRemoto = [NSURL URLWithString:@"http://www.ibmorumbi.org.br/audio/2014/morumbi_mais/morumbi_mais_hd.mp4"];
    }
    else {
        urlVideoRemoto = [NSURL URLWithString:@"http://www.ibmorumbi.com.br/audio/2014/video/mar1603m.mp4"];
    }
    
    
    [self criarNovoPlayer:urlVideoRemoto];
}

@end
