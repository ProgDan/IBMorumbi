//
//  MapViewController.m
//  IBMorumbi
//
//  Created by Daniel Arndt Alves on 3/16/14.
//  Copyright (c) 2014 Daniel Arndt Alves. All rights reserved.
//

#import "MapViewController.h"

@interface MapViewController ()

@property (weak, nonatomic) IBOutlet MKMapView *mapa;
@property (strong, nonatomic) CLLocationManager *locManager;
@property (strong, nonatomic) CLLocation *localizacao;

- (IBAction)showLocation:(UIButton *)sender;
- (IBAction)makeRoute:(UIButton *)sender;
- (IBAction)changeMap:(UISegmentedControl *)sender;


@end

@implementation MapViewController

- (void)viewDidLoad
{
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    
    [self mostraIBMorumbi];
}

- (void) mostraIBMorumbi
{
    MKPointAnnotation *pino = [MKPointAnnotation new];
    
    // Configura a coordenada do pino
    pino.coordinate = CLLocationCoordinate2DMake(-23.633065,-46.7400826);
    
    // Configura um título
    pino.title = @"IB Morumbi";
    pino.subtitle = @"Igreja Batista do Morumbi";
    
    // Adiciona no mapa
    [self.mapa addAnnotation:pino];
    
    // Define a quantidade de mapa que ficará visível na horizontal e na vertical
    MKCoordinateSpan zoom = MKCoordinateSpanMake(0.01, 0.01);
    
    // Define o local onde será aplicado o zoom
    MKCoordinateRegion regiao = MKCoordinateRegionMake(pino.coordinate, zoom);
    
    [self.mapa setRegion:regiao animated:YES];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
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

#pragma mark - CLLocationManagerDelegate

-(void)locationManager:(CLLocationManager *)manager didUpdateLocations:(NSArray *)locations
{
    [self.locManager stopUpdatingLocation];
    
    self.localizacao = [locations lastObject];
    
    MKCoordinateSpan zoom = MKCoordinateSpanMake(0.005, 0.005);
    MKCoordinateRegion regiao = MKCoordinateRegionMake(self.localizacao.coordinate, zoom);
    
    [self.mapa setRegion:regiao animated:YES];
}

#pragma mark - Botões de Controle

- (IBAction)showLocation:(UIButton *)sender {
    if ([CLLocationManager locationServicesEnabled])
    {
        if (!self.locManager)
        {
            self.locManager = [CLLocationManager new];
            self.locManager.delegate = self;
        }
        [self.locManager startUpdatingLocation];
    }
    else
    {
        [[[UIAlertView alloc] initWithTitle:@"Erro" message:@"É necessário autorizar o aplicativo a buscar a localização" delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil] show];
    }
}

- (IBAction)makeRoute:(UIButton *)sender {
    // Como traçar rota no mapa
    CLLocationCoordinate2D ibmorumbi = CLLocationCoordinate2DMake(-23.633065,-46.7400826);
    
    MKPlacemark *localizacaoPlace = [[MKPlacemark alloc] initWithCoordinate:self.localizacao.coordinate addressDictionary:nil];
    MKPlacemark *ibmorumbiPlace = [[MKPlacemark alloc] initWithCoordinate:ibmorumbi addressDictionary:nil];
    
    // criando a requisição de busca
    MKDirectionsRequest *requisicao = [MKDirectionsRequest new];
    
    // setando a origem
    requisicao.source = [[MKMapItem alloc] initWithPlacemark:localizacaoPlace];
    
    // setando o destino
    requisicao.destination = [[MKMapItem alloc] initWithPlacemark:ibmorumbiPlace];
    
    // Configurando o tipo de transporte
    requisicao.transportType = MKDirectionsTransportTypeAutomobile;
    
    MKDirections *roteador = [[MKDirections alloc] initWithRequest:requisicao];
    
    // Mandar calcular a rota e quando terminar, executar o bloco
    [roteador calculateDirectionsWithCompletionHandler:^(MKDirectionsResponse *response, NSError *error) {
        if (error) {
            NSLog(@"%@", error);
        }
        else {
            // dentro do parâmetro response, temos a rota em questão
            MKRoute *rota = [response.routes firstObject];
            [self.mapa addOverlay:rota.polyline];
        }
    }];
}

- (IBAction)changeMap:(UISegmentedControl *)sender {
    if (sender.selectedSegmentIndex == 0)
    {
        self.mapa.mapType = MKMapTypeStandard;
    }
    else
    {
        self.mapa.mapType = MKMapTypeSatellite;
    }
}

// Método adicionado para definir o layout do overlay que vai ser exibido
// Estamos recebendo um overlay que não sabemos se é círculo, linha ou polígono
-(MKOverlayRenderer *)mapView:(MKMapView *)mapView rendererForOverlay:(id<MKOverlay>)overlay {
    
    if ([overlay isKindOfClass:[MKPolyline class]]) {
        // Linha
        MKPolylineRenderer *linha = [[MKPolylineRenderer alloc] initWithOverlay:overlay];
        linha.lineWidth = 3;
        linha.strokeColor = [UIColor blueColor];
        
        return linha;
    }
    return nil;
}


@end
