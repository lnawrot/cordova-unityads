import exec from 'cordova/exec'

const PLUGIN = 'UnityAdsPlugin';
const DEFAULT_OPTIONS = {
  gameId: null,
  placement: '',
  isTest: false,
};

const isFunction = (f) => typeof f === 'function';
export function setup(opts, successCallback, failureCallback) {
  if (typeof opts === 'object') {
    const options = {
      ...DEFAULT_OPTIONS,
      ...opts,
    };

    if (options.gameId === null) {
      if (isFunction(failureCallback)) {
        failureCallback(`"gameId" must be specified.`);
      }
      return;
    }

    exec(successCallback, failureCallback, PLUGIN, 'setup', [options]);
  } else if (isFunction(failureCallback)) {
    failureCallback('Options must be specified.');
  }
}

export function isReady(successCallback, failureCallback) {
  exec(successCallback, failureCallback, PLUGIN, 'isReady', []);
}

export function isInitialized(successCallback, failureCallback) {
  exec(successCallback, failureCallback, PLUGIN, 'isInitialized', []);
}

export function showVideoAd(opts, successCallback, failureCallback) {
  let options = {};
  if (typeof opts === 'object') {
    options = opts;
  }
  exec(successCallback, failureCallback, PLUGIN, 'showVideoAd', [options]);
}

