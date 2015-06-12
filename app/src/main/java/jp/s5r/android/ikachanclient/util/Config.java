package jp.s5r.android.ikachanclient.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

public final class Config {
    private static final String PREFS_NAME = "config";

    private static final String KEY_ENDPOINT = "endpoint";
    private static final String KEY_PREFIX = "prefix";
    private static final String KEY_LAST_CHANNEL = "last_channel";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_PROXY_HOST = "proxy_host";
    private static final String KEY_PROXY_PORT = "proxy_port";
    private static final String KEY_PROXY_USERNAME = "proxy_username";
    private static final String KEY_PROXY_PASSWORD = "proxy_password";

    private Config() {
    }

    public static boolean hasEndpoint(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.contains(KEY_ENDPOINT) && !TextUtils.isEmpty(prefs.getString(KEY_ENDPOINT, null));
    }

    public static void setEndpoint(Context context, String endpoint) {
        putStringOrRemove(context, KEY_ENDPOINT, endpoint);
    }

    public static String getEndpoint(Context context) {
        return getString(context, KEY_ENDPOINT);
    }

    public static boolean hasPrefix(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.contains(KEY_PREFIX) && !TextUtils.isEmpty(prefs.getString(KEY_PREFIX, null));
    }

    public static void setPrefix(Context context, String endpoint) {
        putStringOrRemove(context, KEY_PREFIX, endpoint);
    }

    public static String getPrefix(Context context) {
        return getString(context, KEY_PREFIX);
    }

    public static boolean hasLastChannel(Context context) {
        return containsKeyAndNotEmptyValue(context, KEY_LAST_CHANNEL);
    }

    public static void setLastChannel(Context context, String channel) {
        putStringOrRemove(context, KEY_LAST_CHANNEL, channel);
    }

    public static String getLastChannel(Context context) {
        return getString(context, KEY_LAST_CHANNEL);
    }

    public static boolean usesAuthentication(Context context) {
        return containsKeyAndNotEmptyValue(context, KEY_USERNAME)
                && containsKeyAndNotEmptyValue(context, KEY_PASSWORD);
    }

    public static boolean hasUsername(Context context) {
        return containsKeyAndNotEmptyValue(context, KEY_USERNAME);
    }

    public static void setUsername(Context context, String username) {
        putStringOrRemove(context, KEY_USERNAME, username);
    }

    public static String getUsername(Context context) {
        return getString(context, KEY_USERNAME);
    }

    public static boolean hasPassword(Context context) {
        return containsKeyAndNotEmptyValue(context, KEY_PASSWORD);
    }

    public static void setPassword(Context context, String password) {
        putStringOrRemove(context, KEY_PASSWORD, password);
    }

    public static String getPassword(Context context) {
        return getString(context, KEY_PASSWORD);
    }

    public static boolean usesProxyAuthentication(Context context) {
        return containsKeyAndNotEmptyValue(context, KEY_PROXY_USERNAME)
                && containsKeyAndNotEmptyValue(context, KEY_PROXY_PASSWORD)
                && containsKeyAndNotEmptyValue(context, KEY_PROXY_HOST)
                && containsKeyAndPositiveValue(context, KEY_PROXY_PORT);
    }

    public static boolean hasProxyUsername(Context context) {
        return containsKeyAndNotEmptyValue(context, KEY_PROXY_USERNAME);
    }

    public static void setProxyUsername(Context context, String username) {
        putStringOrRemove(context, KEY_PROXY_USERNAME, username);
    }

    public static String getProxyUsername(Context context) {
        return getString(context, KEY_PROXY_USERNAME);
    }

    public static boolean hasProxyPassword(Context context) {
        return containsKeyAndNotEmptyValue(context, KEY_PROXY_PASSWORD);
    }

    public static void setProxyPassword(Context context, String password) {
        putStringOrRemove(context, KEY_PROXY_PASSWORD, password);
    }

    public static String getProxyPassword(Context context) {
        return getString(context, KEY_PROXY_PASSWORD);
    }

    public static boolean hasProxyHost(Context context) {
        return containsKeyAndNotEmptyValue(context, KEY_PROXY_HOST);
    }

    public static void setProxyHost(Context context, String host) {
        putStringOrRemove(context, KEY_PROXY_HOST, host);
    }

    public static String getProxyHost(Context context) {
        return getString(context, KEY_PROXY_HOST);
    }

    public static boolean hasProxyPort(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.contains(KEY_PROXY_PORT);
    }

    public static void setProxyPort(Context context, int port) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putInt(KEY_PROXY_PORT, port).apply();
    }

    public static int getProxyPort(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getInt(KEY_PROXY_PORT, -1);
    }

    ////////////

    private static void putStringOrRemove(Context context, String key, String value) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        if (TextUtils.isEmpty(value)) {
            prefs.edit().remove(key).apply();
        } else {
            prefs.edit().putString(key, value).apply();
        }
    }

    private static String getString(Context context, String key) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString(key, null);
    }

    private static boolean containsKeyAndNotEmptyValue(Context context, String key) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.contains(key) && !TextUtils.isEmpty(prefs.getString(key, null));
    }

    private static boolean containsKeyAndPositiveValue(Context context, String key) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.contains(key) && prefs.getInt(key, -1) > 0;
    }
}
