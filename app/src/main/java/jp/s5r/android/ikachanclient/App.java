package jp.s5r.android.ikachanclient;

import android.app.Application;
import android.content.SharedPreferences;
import android.text.TextUtils;

import jp.s5r.android.ikachanclient.api.IkachanApi;
import retrofit.RestAdapter;

public class App extends Application {

    private static App sInstance;
    private IkachanApi mIkachanApi;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }

    public static App getInstance() {
        return sInstance;
    }

    public IkachanApi getIkachanApi() {
        if (mIkachanApi == null) {
            RestAdapter adapter = new RestAdapter.Builder()
                    .setEndpoint(getEndpoint())
                    .build();
            mIkachanApi = adapter.create(IkachanApi.class);
        }

        return mIkachanApi;
    }

    public boolean hasEndpoint() {
        SharedPreferences prefs = getSharedPreferences("config", MODE_PRIVATE);
        return prefs.contains("endpoint") && !TextUtils.isEmpty(prefs.getString("endpoint", null));
    }

    public void setEndpoint(String endpoint) {
        SharedPreferences prefs = getSharedPreferences("config", MODE_PRIVATE);
        prefs.edit().putString("endpoint", endpoint).apply();
    }

    public String getEndpoint() {
        SharedPreferences prefs = getSharedPreferences("config", MODE_PRIVATE);
        return prefs.getString("endpoint", null);
    }

    public boolean hasLastChannel() {
        SharedPreferences prefs = getSharedPreferences("config", MODE_PRIVATE);
        return prefs.contains("last_channel") && !TextUtils.isEmpty(prefs.getString("last_channel", null));
    }

    public void setLastChannel(String channel) {
        SharedPreferences prefs = getSharedPreferences("config", MODE_PRIVATE);
        prefs.edit().putString("last_channel", channel).apply();
    }

    public String getLastChannel() {
        SharedPreferences prefs = getSharedPreferences("config", MODE_PRIVATE);
        return prefs.getString("last_channel", null);
    }
}
