//
//  MessageVideoPlayerViewController.h
//  IBMorumbi
//
//  Created by Daniel Arndt Alves on 3/26/14.
//  Copyright (c) 2014 ProgDan Software. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <MediaPlayer/MediaPlayer.h>
#import "GAITrackedViewController.h"

@interface MessageVideoPlayerViewController : GAITrackedViewController

// Dado recebido da View Controller de Lista de Mensagens
@property (strong, nonatomic) NSDictionary *mensagem;

@end
