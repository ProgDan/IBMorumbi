//
//  MapViewController.m
//  IBMorumbi
//
//  Created by Daniel Arndt Alves on 3/17/14.
//  Copyright (c) 2014 ProgDan Software. All rights reserved.
//

#import "MapViewController.h"

@interface MapViewController ()

@property (weak, nonatomic) IBOutlet MKMapView *map;
@property (strong, nonatomic) CLLocationManager *locManager;
@property (strong, nonatomic) CLLocation *location;

- (IBAction)makeRoute:(UIButton *)sender;
- (IBAction)changeMapTYpe:(UISegmentedControl *)sender;

@end

@implementation MapViewController

- (void)viewDidLoad
{
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    
    [self showIBMorumbi];
    
    // Localizando o dispositivo via GPS
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

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    self.screenName = @"Map Screen";
}


- (void) showIBMorumbi
{
    MKPointAnnotation *pin = [MKPointAnnotation new];
    
    // Configura a coordenada do pino
    pin.coordinate = CLLocationCoordinate2DMake(-23.633165,-46.7394826);
    
    // Configura um título
    pin.title = @"IB Morumbi";
    pin.subtitle = @"Igreja Batista do Morumbi";
    
    // Adiciona no mapa
    [self.map addAnnotation:pin];
    
    // Define a quantidade de mapa que ficará visível na horizontal e na vertical
    MKCoordinateSpan zoom = MKCoordinateSpanMake(0.01, 0.01);
    
    // Define o local onde será aplicado o zoom
    MKCoordinateRegion region = MKCoordinateRegionMake(pin.coordinate, zoom);
    
    [self.map setRegion:region animated:YES];
    self.map.showsUserLocation = YES;
}

#pragma mark - Interface Buttons

- (IBAction)makeRoute:(UIButton *)sender
{
    // Como traçar rota no mapa
    CLLocationCoordinate2D ibmorumbi = CLLocationCoordinate2DMake(-23.633465,-46.7397026);
    
    MKPlacemark *locationPlace = [[MKPlacemark alloc] initWithCoordinate:self.location.coordinate addressDictionary:nil];
    MKPlacemark *ibmorumbiPlace = [[MKPlacemark alloc] initWithCoordinate:ibmorumbi addressDictionary:nil];
    
    // criando a requisição de busca
    MKDirectionsRequest *request = [MKDirectionsRequest new];
    
    // setando a origem
    request.source = [[MKMapItem alloc] initWithPlacemark:locationPlace];
    
    // setando o destino
    request.destination = [[MKMapItem alloc] initWithPlacemark:ibmorumbiPlace];
    
    // Configurando o tipo de transporte
    request.transportType = MKDirectionsTransportTypeAutomobile;
    
    MKDirections *router = [[MKDirections alloc] initWithRequest:request];
    
    // Mandar calcular a rota e quando terminar, executar o bloco
    [router calculateDirectionsWithCompletionHandler:^(MKDirectionsResponse *response, NSError *error) {
        if (error) {
            NSLog(@"%@", error);
        }
        else {
            // dentro do parâmetro response, temos a rota em questão
            MKRoute *rota = [response.routes firstObject];
            [self.map addOverlay:rota.polyline];
            
            [self.map setVisibleMapRect:rota.polyline.boundingMapRect animated:YES];
        }
    }];
}

- (IBAction)changeMapTYpe:(UISegmentedControl *)sender
{
    switch (sender.selectedSegmentIndex) {
        case 0:
            self.map.mapType = MKMapTypeStandard;
            break;
        case 1:
            self.map.mapType = MKMapTypeSatellite;
            break;
        case 2:
            self.map.mapType = MKMapTypeHybrid;
            break;
        default:
            break;
    }
 }

#pragma mark - CLLocationManagerDelegate

-(void)locationManager:(CLLocationManager *)manager didUpdateLocations:(NSArray *)locations
{
    [self.locManager stopUpdatingLocation];
    self.location = [locations lastObject];
}


#pragma mark - MKMapViewDelegate

// Método adicionado para definir o layout do overlay que vai ser exibido
// Estamos recebendo um overlay que não sabemos se é círculo, linha ou polígono

-(MKOverlayRenderer *)mapView:(MKMapView *)mapView rendererForOverlay:(id<MKOverlay>)overlay
{
    
    if ([overlay isKindOfClass:[MKPolyline class]])
    {
        // Linha
        MKPolylineRenderer *linha = [[MKPolylineRenderer alloc] initWithOverlay:overlay];
        linha.lineWidth = 3;
        linha.strokeColor = [UIColor blueColor];
        
        return linha;
    }
    return nil;
}


@end
