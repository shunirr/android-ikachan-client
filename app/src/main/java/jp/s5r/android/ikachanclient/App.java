package jp.s5r.android.ikachanclient;

import android.app.Application;

import com.squareup.okhttp.Authenticator;
import com.squareup.okhttp.Credentials;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;

import jp.s5r.android.ikachanclient.api.IkachanApi;
import jp.s5r.android.ikachanclient.util.Config;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

public class App extends Application {

    private static App sInstance;
    private IkachanApi mIkachanApi;
    private final OkHttpClient mOkHttpClient = new OkHttpClient();

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;

        initOkHttpClient();
    }

    private void initOkHttpClient() {
        if (Config.usesProxyAuthentication(this)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    mOkHttpClient.setProxy(new Proxy(
                            Proxy.Type.HTTP,
                            new InetSocketAddress(
                                    Config.getProxyHost(getApplicationContext()),
                                    Config.getProxyPort(getApplicationContext()))));

                    // reset client
                    mIkachanApi = null;
                }
            }).start();
        }

        mOkHttpClient.setAuthenticator(new Authenticator() {
            @Override
            public Request authenticate(Proxy proxy, Response response) throws IOException {
                if (Config.usesAuthentication(getApplicationContext())) {
                    String credential = Credentials.basic(
                            Config.getUsername(getApplicationContext()),
                            Config.getPassword(getApplicationContext()));
                    return response.request().newBuilder()
                            .header("Authorization", credential)
                            .build();
                }
                return null;
            }

            @Override
            public Request authenticateProxy(Proxy proxy, Response response) throws IOException {
                if (Config.usesProxyAuthentication(getApplicationContext())) {
                    String credential = Credentials.basic(
                            Config.getProxyUsername(getApplicationContext()),
                            Config.getProxyPassword(getApplicationContext()));
                    return response.request().newBuilder()
                            .header("Proxy-Authorization", credential)
                            .build();
                }
                return null;
            }
        });
    }

    public static App getInstance() {
        return sInstance;
    }

    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    public IkachanApi getIkachanApi() {
        if (mIkachanApi == null) {
            RestAdapter adapter = new RestAdapter.Builder()
                    .setEndpoint(Config.getEndpoint(this))
                    .setClient(new OkClient(getOkHttpClient()))
                    .build();
            mIkachanApi = adapter.create(IkachanApi.class);
        }

        return mIkachanApi;
    }
}
