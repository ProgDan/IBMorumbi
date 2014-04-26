//
//  BoletimViewController.m
//  IBMorumbi
//
//  Created by Daniel Arndt Alves on 3/27/14.
//  Copyright (c) 2014 ProgDan Software. All rights reserved.
//

#import "BoletimViewController.h"

@interface BoletimViewController ()

@property (weak, nonatomic) IBOutlet UITableView *boletimTable;

@property (strong, nonatomic) NSArray *boletimList;
@property(nonatomic, strong) UIDocumentInteractionController *docController;

@end

@implementation BoletimViewController

- (void)viewDidLoad
{
    [super viewDidLoad];
    // Do any additional setup after loading the view.

    // Carga do arquivo de configuração
    // Localizando o arquivo no projeto
    struct utsname systemInfo;
    uname(&systemInfo);
    NSString *device= [NSString stringWithCString:systemInfo.machine encoding:NSUTF8StringEncoding];
    NSString *ver = [[UIDevice currentDevice] systemVersion];
    NSString *appVersion = [NSBundle mainBundle].infoDictionary[@"CFBundleVersion"];
    NSString *path = [NSString stringWithFormat:@"http://mini.progdan.com/ibmorumbi/boletins.php?platform=iOS&device=%@&os=%@&client=%@", device, ver, appVersion];
    // Carregando os dados contidos no arquivo
    NSData *dadosArquivo = [NSData dataWithContentsOfURL:[NSURL URLWithString:path]];
    // Convertendo o arquivo para uma estrutura conhecida - vetor
    self.boletimList = [NSJSONSerialization JSONObjectWithData:dadosArquivo options:NSJSONReadingAllowFragments error:nil];
}

#pragma mark - Navigation

-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    // Inicia a animação de download...
    [[UIApplication sharedApplication] setNetworkActivityIndicatorVisible:YES];

    // Cria a URL para abertura do arquivo
    NSURL *url = [NSURL URLWithString:self.boletimList[indexPath.row][@"file"]];
    
    NSURLRequest *req = [[NSURLRequest alloc] initWithURL:url];
    [NSURLConnection sendAsynchronousRequest:req queue:[NSOperationQueue mainQueue] completionHandler:^(NSURLResponse *resp, NSData *respData, NSError *error){
//        NSLog(@"resp data length: %i", respData.length);
        NSArray *names = [self.boletimList[indexPath.row][@"file"] componentsSeparatedByString:@"/"];
        NSString *fileName =  names[names.count-1];
        NSString * path = [NSTemporaryDirectory() stringByAppendingPathComponent:fileName];
        NSError *errorC = nil;
        BOOL success = [respData writeToFile:path
                                     options:NSDataWritingFileProtectionComplete
                                       error:&errorC];
        if (success) {
            // Cria o document controller
            self.docController = [UIDocumentInteractionController interactionControllerWithURL:[NSURL fileURLWithPath:path]];
            self.docController.delegate = self;

            // isso serve para o iPhone sugerir mais aplicações para abrir o seu arquivo
            self.docController.UTI = @"com.adobe.pdf";
           
            // Encerra a animação de download
            [[UIApplication sharedApplication] setNetworkActivityIndicatorVisible:NO];
            
            [self.docController presentPreviewAnimated:YES];
        } else {
            NSLog(@"fail: %@", errorC.description);
        }
    }];
    
}

#pragma mark - UITableViewDataSource

-(NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}

-(NSString *)tableView:(UITableView *)tableView titleForHeaderInSection:(NSInteger)section
{
    return @"Boletins Informativos IBMorumbi";
}

-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    // Retorna a quantidade de boletins na lista
    return self.boletimList.count;
}

-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"Cell" forIndexPath:indexPath];
    cell.detailTextLabel.text = self.boletimList[indexPath.row][@"data"];
    return cell;
}

#pragma mark - UIDocumentInteractionControllerDelegate
// Este método informa em qual viewController o documento será visualizado
- (UIViewController *)documentInteractionControllerViewControllerForPreview:(UIDocumentInteractionController *)controller{
    return self;
}

@end
