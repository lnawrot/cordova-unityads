#import "UnityAdsPlugin.h"

@implementation UnityAdsPlugin

@synthesize gameId;
@synthesize placement;
@synthesize isTest;

@synthesize unityAdsDelegateId;

- (void) pluginInitialize {
  [super pluginInitialize];
}

- (void) setup: (CDVInvokedUrlCommand*)command {
  NSLog(@"setOptions");

  CDVPluginResult *pluginResult;
  NSString *callbackId = command.callbackId;
  NSArray* args = command.arguments;

  NSUInteger argc = [args count];
  if( argc >= 1 ) {
    NSDictionary* options = [command argumentAtIndex:0 withDefault:[NSNull null]];

    [self _setup:options];
  }

  pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
  [self.commandDelegate sendPluginResult:pluginResult callbackId:callbackId];
}

- (void) _setup:(NSDictionary*)options {
  if ((NSNull *)options == [NSNull null]) return;

  NSString* str = nil;

  str = [options objectForKey:@"gameId"];
  if (str && [str length] > 0) {
    self.gameId = str;
  }

  str = [options objectForKey:@"placement"];
  if (str && [str length] > 0) {
    self.placement = str;
  }

  str = [options objectForKey:@"isTest"];
  if (str) {
    self.isTest = [str boolValue];
  }

  self.unityAdsDelegateId = [[CustomUnityAdsDelegate alloc] initWithUnityAdsPlugin:self];

  [UnityAds initialize:self.gameId delegate: self.unityAdsDelegateId testMode:self.isTest];
  [UnityAds setDebugMode:NO];
}

- (void)isReady:(CDVInvokedUrlCommand *)command {
    CDVPluginResult *pluginResult;
    NSString *callbackId = command.callbackId;

    BOOL isReady = [UnityAds isReady];
    pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsBool:isReady];

    [self.commandDelegate sendPluginResult:pluginResult callbackId:callbackId];
}

- (void)isInitialized:(CDVInvokedUrlCommand *)command {
    CDVPluginResult *pluginResult;
    NSString *callbackId = command.callbackId;

    BOOL isInitialized = [UnityAds isInitialized];
    pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsBool:isInitialized];

    [self.commandDelegate sendPluginResult:pluginResult callbackId:callbackId];
}

- (void) showVideoAd: (CDVInvokedUrlCommand*)command {
    CDVPluginResult *pluginResult;
    NSString *callbackId = command.callbackId;

    BOOL isReady = [UnityAds isReady];
    pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsBool:isReady];

    [self.commandDelegate runInBackground:^{
      [self _showVideoAd];
    }];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:callbackId];
}

- (void) _showVideoAd {
	if ([UnityAds isReady]) {
		[UnityAds show:self.viewController placementId:self.placement];
	}
}

- (void) fireEvent:(NSString *)obj event:(NSString *)eventName withData:(NSString *)jsonStr {
  NSString* js;
  if(obj && [obj isEqualToString:@"window"]) {
    js = [NSString stringWithFormat:@"var evt=document.createEvent(\"UIEvents\");evt.initUIEvent(\"%@\",true,false,window,0);window.dispatchEvent(evt);", eventName];
  } else if(jsonStr && [jsonStr length]>0) {
    js = [NSString stringWithFormat:@"javascript:cordova.fireDocumentEvent('%@',%@);", eventName, jsonStr];
  } else {
    js = [NSString stringWithFormat:@"javascript:cordova.fireDocumentEvent('%@');", eventName];
  }
  [self.commandDelegate evalJs:js];
}

@end


@implementation CustomUnityAdsDelegate

@synthesize unityAdsPlugin;

- (id) initWithUnityAdsPlugin:(UnityAdsPlugin *)unityAdsPlugin_ {
  self = [super init];
  if (self) {
    self.unityAdsPlugin = unityAdsPlugin_;
  }
  return self;
}

- (void) unityAdsReady:(NSString *)placementId {
  [self.unityAdsPlugin fireEvent:@"" event:@"unity.ads.ready" withData:nil];
}

- (void) unityAdsDidStart:(NSString *)placementId {
  [self.unityAdsPlugin fireEvent:@"" event:@"unity.ads.start" withData:nil];
}

- (void) unityAdsDidError:(UnityAdsError)error withMessage:(NSString *)message {
  NSString *stateString = @"UNKNOWN";
  switch (error) {
    case kUnityAdsErrorNotInitialized:
      stateString = @"NOT_INITIALIZED";
      break;
    case kUnityAdsErrorInitializedFailed:
      stateString = @"INITIALIZE_FAILED";
      break;
    case kUnityAdsErrorInvalidArgument:
      stateString = @"INVALID_ARGUMENT";
      break;
    case kUnityAdsErrorVideoPlayerError:
      stateString = @"VIDEO_PLAYER_ERROR";
      break;
    case kUnityAdsErrorInitSanityCheckFail:
      stateString = @"INIT_SANITY_CHECK_FAIL";
      break;
    case kUnityAdsErrorAdBlockerDetected:
      stateString = @"AD_BLOCKER_DETECTED";
      break;
    case kUnityAdsErrorFileIoError:
      stateString = @"FILE_IO_ERROR";
      break;
    case kUnityAdsErrorDeviceIdError:
      stateString = @"DEVICE_ID_ERROR";
      break;
    case kUnityAdsErrorShowError:
      stateString = @"SHOW_ERROR";
      break;
    case kUnityAdsErrorInternalError:
      stateString = @"INTERNAL_ERROR";
      break;
    default:
      break;
  }

  NSString* jsonData = [NSString stringWithFormat:@"{'error':'%@','message':'%@'}", stateString, message];
  [self.unityAdsPlugin fireEvent:@"" event:@"unity.ads.error" withData:jsonData];
}

- (void)unityAdsPlacementStateChanged:(NSString *)placementId oldState:(UnityAdsPlacementState)oldState newState:(UnityAdsPlacementState)newState {
  NSString *stateString = @"UNKNOWN";
  switch (newState) {
    case kUnityAdsPlacementStateReady:
      stateString = @"READY";
      break;
    case kUnityAdsPlacementStateNotAvailable:
      stateString = @"NOT_AVAILABLE";
      break;
    case kUnityAdsPlacementStateDisabled:
      stateString = @"DISABLED";
      break;
    case kUnityAdsPlacementStateWaiting:
      stateString = @"WAITING";
      break;
    case kUnityAdsPlacementStateNoFill:
      stateString = @"NO_FILL";
      break;
    default:
      break;
  }

  NSString* jsonData = [NSString stringWithFormat:@"{'state':'%@'}", stateString];
  [self.unityAdsPlugin fireEvent:@"" event:@"unity.ads.placement" withData:jsonData];
}

- (void) unityAdsDidClick:(NSString *)placementId {
  [self.unityAdsPlugin fireEvent:@"" event:@"unity.ads.click" withData:nil];
}

- (void) unityAdsDidFinish:(NSString *)placementId withFinishState:(UnityAdsFinishState)state {
  NSString *stateString = @"UNKNOWN";
  switch (state) {
    case kUnityAdsFinishStateError:
      stateString = @"ERROR";
      break;
    case kUnityAdsFinishStateSkipped:
      stateString = @"SKIPPED";
      break;
    case kUnityAdsFinishStateCompleted:
      stateString = @"COMPLETED";
      break;
    default:
      break;
  }

  NSString* jsonData = [NSString stringWithFormat:@"{'result':'%@'}", stateString];
  [self.unityAdsPlugin fireEvent:@"" event:@"unity.ads.finish" withData:jsonData];
}

@end

