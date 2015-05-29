package jp.s5r.android.ikachanclient.api;

import org.json.JSONObject;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

public interface IkachanApi {
    @FormUrlEncoded
    @POST("/notice")
    void notice(@Field("channel") String channel,
                @Field("message") String message,
                Callback<JSONObject> callback);

    @FormUrlEncoded
    @POST("/privmsg")
    void privmsg(@Field("channel") String channel,
                 @Field("message") String message,
                 Callback<JSONObject> callback);
}
