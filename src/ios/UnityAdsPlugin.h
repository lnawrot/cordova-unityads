#import <Cordova/CDV.h>
#import <UnityAds/UnityAds.h>

@interface UnityAdsPlugin : CDVPlugin

@property NSString *gameId;
@property NSString *placement;
@property BOOL isTest;

@property id unityAdsDelegateId;
@property NSInteger videoOrRewardedVideo;

- (void) setup:(CDVInvokedUrlCommand*)command;
- (void) isReady:(CDVInvokedUrlCommand*)command;
- (void) isInitialized:(CDVInvokedUrlCommand*)command;
- (void) showVideoAd:(CDVInvokedUrlCommand*)command;

@end

@interface CustomUnityAdsDelegate : NSObject <UnityAdsExtendedDelegate>

@property UnityAdsPlugin *unityAdsPlugin;

- (id) initWithUnityAdsPlugin:(UnityAdsPlugin *)unityAdsPlugin_ ;

@end

