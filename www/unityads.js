'use strict';

Object.defineProperty(exports, '__esModule', { value: true });

function _interopDefault (ex) { return (ex && (typeof ex === 'object') && 'default' in ex) ? ex['default'] : ex; }

var exec = _interopDefault(require('cordova/exec'));

var _typeof = typeof Symbol === "function" && typeof Symbol.iterator === "symbol" ? function (obj) {
  return typeof obj;
} : function (obj) {
  return obj && typeof Symbol === "function" && obj.constructor === Symbol && obj !== Symbol.prototype ? "symbol" : typeof obj;
};





var _extends = Object.assign || function (target) {
  for (var i = 1; i < arguments.length; i++) {
    var source = arguments[i];

    for (var key in source) {
      if (Object.prototype.hasOwnProperty.call(source, key)) {
        target[key] = source[key];
      }
    }
  }

  return target;
};

var PLUGIN = 'UnityAdsPlugin';
var DEFAULT_OPTIONS = {
  gameId: null,
  placement: '',
  isTest: false
};

var isFunction = function isFunction(f) {
  return typeof f === 'function';
};
function setup(opts, successCallback, failureCallback) {
  if ((typeof opts === 'undefined' ? 'undefined' : _typeof(opts)) === 'object') {
    var options = _extends({}, DEFAULT_OPTIONS, opts);

    if (options.gameId === null) {
      if (isFunction(failureCallback)) {
        failureCallback('"gameId" must be specified.');
      }
      return;
    }

    exec(successCallback, failureCallback, PLUGIN, 'setup', [options]);
  } else if (isFunction(failureCallback)) {
    failureCallback('Options must be specified.');
  }
}

function isReady(successCallback, failureCallback) {
  exec(successCallback, failureCallback, PLUGIN, 'isReady', []);
}

function isInitialized(successCallback, failureCallback) {
  exec(successCallback, failureCallback, PLUGIN, 'isInitialized', []);
}

function showVideoAd(opts, successCallback, failureCallback) {
  var options = {};
  if ((typeof opts === 'undefined' ? 'undefined' : _typeof(opts)) === 'object') {
    options = opts;
  }
  exec(successCallback, failureCallback, PLUGIN, 'showVideoAd', [options]);
}

exports.setup = setup;
exports.isReady = isReady;
exports.isInitialized = isInitialized;
exports.showVideoAd = showVideoAd;
//# sourceMappingURL=unityads.js.map
