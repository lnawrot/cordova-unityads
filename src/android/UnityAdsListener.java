package me.nawrot.cordova.plugin.unityads;

import org.json.JSONObject;
import org.json.JSONException;

import android.util.Log;

import com.unity3d.ads.UnityAds;
import com.unity3d.ads.mediation.IUnityAdsExtendedListener;

class UnityAdsListener implements IUnityAdsExtendedListener {
  private static final String LOG_TAG = "UnityAdsPlugin";
  private UnityAdsPlugin plugin;

  public UnityAdsListener(UnityAdsPlugin plugin) {
    this.plugin = plugin;
  }

  public void onUnityAdsReady(String placement) {
    Log.d(LOG_TAG, String.format("%s", "onUnityAdsReady"));
    fireEvent("unity.ads.ready", null);
  }

  public void onUnityAdsError(UnityAds.UnityAdsError error, String message) {
    Log.d(LOG_TAG, String.format("%s", "onUnityAdsError"));
    JSONObject data = new JSONObject();
    try {
      data.put("error", error);
    } catch (JSONException e) {
      e.printStackTrace();
    }
    fireEvent("unity.ads.error", data);
  }

  public void onUnityAdsStart(String placement) {
    Log.d(LOG_TAG, String.format("%s", "onUnityAdsStart"));
    fireEvent("unity.ads.start", null);
  }

  public void onUnityAdsFinish(String placement, UnityAds.FinishState result) {
    Log.d(LOG_TAG, String.format("%s", "onUnityAdsFinish"));
    JSONObject data = new JSONObject();
    try {
      data.put("result", result);
    } catch (JSONException e) {
      e.printStackTrace();
    }
    fireEvent("unity.ads.finish", data);
  }

  public void onUnityAdsPlacementStateChanged(String placementId, UnityAds.PlacementState oldState, UnityAds.PlacementState newState) {
    Log.d(LOG_TAG, String.format("%s", "onUnityAdsPlacementStateChanged"));
    JSONObject data = new JSONObject();
    try {
      data.put("state", newState);
      data.put("oldState", oldState);
    } catch (JSONException e) {
      e.printStackTrace();
    }
    fireEvent("unity.ads.placement", data);
  }

  public void onUnityAdsClick(String placementId) {
    Log.d(LOG_TAG, String.format("%s", "onUnityAdsClick"));
    fireEvent("unity.ads.click", null);
  }

  private void fireEvent(String eventName, JSONObject jsonObj) {
    String data = "";
    if (jsonObj != null) {
      data = jsonObj.toString();
    }

    StringBuilder js = new StringBuilder();
    js.append("javascript:cordova.fireDocumentEvent('");
    js.append(eventName);
    js.append("'");
    if (data != null && !"".equals(data)) {
      js.append(",");
      js.append(data);
    }
    js.append(");");

    plugin.webView.loadUrl(js.toString());
  }
}
