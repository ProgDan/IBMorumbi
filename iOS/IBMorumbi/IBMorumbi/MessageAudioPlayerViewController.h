//
//  MessageAudioPlayerViewController.h
//  IBMorumbi
//
//  Created by Daniel Arndt Alves on 3/26/14.
//  Copyright (c) 2014 ProgDan Software. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <AVFoundation/AVFoundation.h>
#import "GAITrackedViewController.h"

@interface MessageAudioPlayerViewController : GAITrackedViewController

// Dado recebido da View Controller de Lista de Mensagens
@property (strong, nonatomic) NSDictionary *mensagem;

@property (strong, nonatomic) AVPlayer *player;

@end
