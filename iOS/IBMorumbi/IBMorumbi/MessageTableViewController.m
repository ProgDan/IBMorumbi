//
//  MessageTableViewController.m
//  IBMorumbi
//
//  Created by Daniel Arndt Alves on 3/26/14.
//  Copyright (c) 2014 ProgDan Software. All rights reserved.
//

#import "MessageTableViewController.h"

@interface MessageTableViewController ()

@property (weak, nonatomic) IBOutlet UIImageView *imgMsgSerie;
@property (weak, nonatomic) IBOutlet UISegmentedControl *mediaSelector;
@property (weak, nonatomic) IBOutlet UITableView *messagesTable;

- (IBAction)changeMediaType:(UISegmentedControl *)sender;


@property (strong, nonatomic) NSDictionary *ibmorumbiConfig;
@property (strong, nonatomic) NSArray *messageList;

@end

@implementation MessageTableViewController

- (void)viewDidLoad
{
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    
    // Carga do arquivo de configuração
    struct utsname systemInfo;
    uname(&systemInfo);
    NSString *device= [NSString stringWithCString:systemInfo.machine encoding:NSUTF8StringEncoding];
    NSString *ver = [[UIDevice currentDevice] systemVersion];
    NSString *appVersion = [NSBundle mainBundle].infoDictionary[@"CFBundleVersion"];
    NSString *path = [NSString stringWithFormat:@"http://mini.progdan.com/ibmorumbi/appsettings.php?platform=iOS&device=%@&os=%@&client=%@", device, ver, appVersion];
    // Carregando os dados contidos no arquivo
    NSData *dadosArquivo = [NSData dataWithContentsOfURL:[NSURL URLWithString:path]];
    // Convertendo o arquivo para uma estrutura conhecida - vetor
    self.ibmorumbiConfig = [NSJSONSerialization JSONObjectWithData:dadosArquivo options:NSJSONReadingAllowFragments error:nil];
    
    // Carga da lista de mensagens
    path = [NSString stringWithFormat:@"http://mini.progdan.com/ibmorumbi/messages.php?platform=iOS&device=%@&os=%@&client=%@", device, ver, appVersion];
    dadosArquivo = [NSData dataWithContentsOfURL:[NSURL URLWithString:path]];
    NSError *error;
    self.messageList = [NSJSONSerialization JSONObjectWithData:dadosArquivo options:NSJSONReadingAllowFragments error:&error];
    
    // Exibição da imagem da série de mensagens
    self.imgMsgSerie.image = [[UIImage alloc] initWithData:[NSData dataWithContentsOfURL:[NSURL URLWithString:self.ibmorumbiConfig[@"logo_mensagens"]]]];
    
    [self.messagesTable reloadData];
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    self.screenName = @"Message Screen";
}


#pragma mark - Navigation

-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    UIStoryboard *story;
    NSString *video_morumbiplus;
    
    if (UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiomPad) {
        story = [UIStoryboard storyboardWithName:@"Main_iPad" bundle:nil];
        video_morumbiplus = self.ibmorumbiConfig[@"video_morumbiplushd"];
    }
    else {
        story = [UIStoryboard storyboardWithName:@"Main_iPhone" bundle:nil];
        video_morumbiplus = self.ibmorumbiConfig[@"video_morumbiplus"];
    }
    
    if (self.mediaSelector.selectedSegmentIndex) {
        MessageAudioPlayerViewController *detalhe = [story instantiateViewControllerWithIdentifier:@"AudioPlayer"];
        detalhe.mensagem = self.messageList[indexPath.row];
        [self.navigationController pushViewController:detalhe animated:YES];
    }
    else {
        MessageVideoPlayerViewController *detalhe = [story instantiateViewControllerWithIdentifier:@"VideoPlayer"];
        if (indexPath.section) {
            detalhe.mensagem = self.messageList[indexPath.row];
        }
        else {
            detalhe.mensagem = @{@"tema": @"Morumbi+",@"video":video_morumbiplus};
        }
        [self.navigationController pushViewController:detalhe animated:YES];
    }
    
}

#pragma mark - UITableViewDataSource

-(NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    // Audio selecionado, somente mensagens
    if (self.mediaSelector.selectedSegmentIndex) {
        return 1;
    }
    
    // Vídeo selecionado, 2 seções - Morumbi+ e Mensagens
    return 2;
}

-(NSString *)tableView:(UITableView *)tableView titleForHeaderInSection:(NSInteger)section
{
    // Caso seja Audio, ou a seção 1 em Vídeo
    if (self.mediaSelector.selectedSegmentIndex || section) {
        return @"Cultos IB Morumbi";
    }
    
    // Caso contrário, é a seção do Morumbi+
    return @"Morumbi+";
}

-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    // Retorna a quantidade de mensagens na lista
    if (self.mediaSelector.selectedSegmentIndex || section) {
        return self.messageList.count;
    }
    // Seção 0: Morumbi+ (apenas vídeo)
    return 1;
}

-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    UITableViewCell *cell;
    // Seção das Mensagens
    if (self.mediaSelector.selectedSegmentIndex || indexPath.section) {
        cell = [tableView dequeueReusableCellWithIdentifier:@"MessageCell" forIndexPath:indexPath];
        cell.textLabel.text = self.messageList[indexPath.row][@"tema"];
        cell.detailTextLabel.text = self.messageList[indexPath.row][@"pregador"];
        
        // Verifica se a imagem do pastor já se encontra no aplicativo
        NSArray *names = [self.messageList[indexPath.row][@"pregador_img"] componentsSeparatedByString:@"/"];
        NSString *fileName =  names[names.count-1];
        names = [fileName componentsSeparatedByString:@"."];
        fileName =  names[0];
        NSString *path = [[NSBundle mainBundle] pathForResource:fileName ofType:@"jpg"];
        
        if (![[NSFileManager defaultManager] fileExistsAtPath:path]) {
            // O arquivo não existe
            cell.imageView.image = [[UIImage alloc] initWithData:[NSData dataWithContentsOfURL:[NSURL URLWithString:self.messageList[indexPath.row][@"pregador_img"]]]];
        }
        else {
            cell.imageView.image = [[UIImage alloc] initWithData:[NSData dataWithContentsOfURL:[NSURL fileURLWithPath:path]]];
        }
        
        
    }
    // Seção do Morumbi+
    else {
        cell = [tableView dequeueReusableCellWithIdentifier:@"MorumbiPlus" forIndexPath:indexPath];
        cell.textLabel.text = @"Morumbi+";
        cell.imageView.image = [UIImage imageNamed:@"preview_morumbiplus_470.jpg"];
        // cell.imageView.image = [[UIImage alloc] initWithData:[NSData dataWithContentsOfURL:[NSURL URLWithString:self.ibmorumbiConfig[@"logo_morumbiplus"]]]];
    }
    return cell;
}

#pragma mark - UISegmentedControl action
- (IBAction)changeMediaType:(UISegmentedControl *)sender {
    [self.messagesTable reloadData];
}

@end
