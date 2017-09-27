//Copyright (c) 2017 Łukasz Nawrot
//Email: lukasz@nawrot.me
//License: MIT (http://opensource.org/licenses/MIT)

package me.nawrot.cordova.plugin.unityads;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.PluginResult;
import org.apache.cordova.PluginResult.Status;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaWebView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import android.annotation.TargetApi;
import android.app.Activity;
import android.util.Log;

import android.content.SharedPreferences;
import android.content.res.Configuration;

import android.util.Log;
import android.view.View;

import com.unity3d.ads.mediation.IUnityAdsExtendedListener;
import com.unity3d.ads.UnityAds;

import me.nawrot.cordova.plugin.unityads.Actions;
import me.nawrot.cordova.plugin.unityads.UnityAdsListener;

public class UnityAdsPlugin extends CordovaPlugin {
	private static final String LOG_TAG = "UnityAdsPlugin";
	private CallbackContext callbackContextKeepCallback;

	protected String gameId;
	protected String placement;
	protected boolean isTest;

	@Override
	public void initialize(CordovaInterface cordova, CordovaWebView webView) {
		super.initialize(cordova, webView);
	}

	@Override
	public boolean execute(String action, JSONArray inputs, CallbackContext callbackContext) throws JSONException {
    PluginResult result = null;

		if (Actions.SETUP.equals(action)) {
			JSONObject options = inputs.optJSONObject(0);
			result = setup(options, callbackContext);

		} else if (Actions.IS_READY.equals(action)) {
			result = isReady(callbackContext);

		} else if (Actions.IS_INITIALIZED.equals(action)) {
			result = isInitialized(callbackContext);

		} else if (Actions.LOAD_VIDEO.equals(action)) {
			JSONObject options = inputs.optJSONObject(0);
			result = loadVideoAd(options, callbackContext);

		} else if (Actions.SHOW_VIDEO.equals(action)) {
			JSONObject options = inputs.optJSONObject(0);
			result = showVideoAd(options, callbackContext);

		} else {
			Log.d(LOG_TAG, String.format("Invalid action passed: %s", action));
			result = new PluginResult(Status.INVALID_ACTION);
		}

		if (result != null) {
			callbackContext.sendPluginResult(result);
		}

		return true;
	}

	@Override
	public void onPause(boolean multitasking) {
		super.onPause(multitasking);
	}

	@Override
	public void onResume(boolean multitasking) {
		super.onResume(multitasking);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}


	private PluginResult setup(final JSONObject options, CallbackContext callbackContext) {
		final CallbackContext delayCallback = callbackContext;
		cordova.getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				boolean result = _setup(options);
				if (result) {
					delayCallback.success();
				} else {
					delayCallback.error("Wrong options passed!");
				}
			}
		});

		return null;
	}

	private boolean _setup(final JSONObject options) {
		if (options == null) {
			return false;
		}

		if (options.has("gameId")) {
			this.gameId = options.optString("gameId");
		} else {
			return false;
		}

		if (options.has("placement")) {
			this.placement = options.optString("placement");
		}

		if (options.has("isTest")) {
			this.isTest = options.optBoolean("isTest");
		} else {
			this.isTest = false;
		}

		UnityAds.initialize(cordova.getActivity(), this.gameId, new UnityAdsListener(this), this.isTest);
		UnityAds.setDebugMode(true);

		return true;
	}

	private PluginResult isInitialized(final CallbackContext callbackContext) {
		cordova.getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				boolean result = UnityAds.isInitialized();
				callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, result));
			}
		});

		return null;
	}

	private PluginResult isReady(final CallbackContext callbackContext) {
		cordova.getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				boolean result = UnityAds.isReady();
				callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, result));
			}
		});

		return null;
	}

	private PluginResult loadVideoAd(final JSONObject options, final CallbackContext callbackContext) {
		return null;
	}

	private PluginResult showVideoAd(final JSONObject options, final CallbackContext callbackContext) {
		final CallbackContext delayCallback = callbackContext;
		cordova.getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				boolean result = _showVideoAd(options);
				if (result) {
					delayCallback.success();
				} else {
					delayCallback.error("Ad is not ready!");
				}
			}
		});

		return null;
	}

	private boolean _showVideoAd(JSONObject options) {
		String placement = this.placement;
		if (options.has("placement")) {
			placement = options.optString("placement");
		}

		if (!UnityAds.isReady()) {
			return false;
		}

		UnityAds.show(cordova.getActivity(), placement);
		return true;
	}
}
