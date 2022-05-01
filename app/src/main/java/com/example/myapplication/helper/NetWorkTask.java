package com.example.myapplication.helper;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;

import com.example.myapplication.R;
import com.example.myapplication.bean.Option;
import com.example.myapplication.bean.Quest;
import com.example.myapplication.bean.SubQuestion;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NetWorkTask {
    static String testUrl = "http://localhost:8080/app";
    static String baseUrl = "http://120.53.104.59:8080/app";
    static HashMap<String, String> urlMap = new HashMap<>();
    static OkHttpClient client;

    static {
        client = new OkHttpClient();

        urlMap.put("logon", "/user/logon");
        urlMap.put("register", "/user/register");
        urlMap.put("userinfo", "/user/userinfo");
        urlMap.put("sign in", "/user/signin");
        urlMap.put("all quest", "/quest/allquest");
        urlMap.put("not do", "/quest/notdo");
        urlMap.put("purse", "/quest/purse");
        urlMap.put("having", "/quest/having");
        urlMap.put("buy", "/quest/buy");
        urlMap.put("get question", "/quest/getquestion");
        urlMap.put("get option", "/quest/getoption");
        urlMap.put("get data", "/data/get");
        urlMap.put("insert data", "/data/insert");
    }



    public static String GetTestUrl(String urlType)
    {
        if (urlMap.containsKey(urlType))
            return testUrl + urlMap.get(urlType);
        else
            return null;
    }

    public static String GetBaseUrl(String urlType)
    {
        if (urlMap.containsKey(urlType))
        {
            Log.d("URL", "GetBaseUrl: " + baseUrl + urlMap.get(urlType));
            return baseUrl + urlMap.get(urlType);
        }
        else
            return null;
    }

    public static void log(String tag, String mes)
    {
        Log.d(tag, mes);
    }

    public static HashMap<String, Integer> LogOn(String username, String password)
    {
        HashMap<String, Integer> hashMap = new HashMap<>();
        Request request = new Request.Builder()
                .url(GetBaseUrl("logon"))
                .addHeader("username", username)
                .addHeader("password", password)
                .build();

        try {
            Response response = client.newCall(request).execute();
            //Log.d("Test", "logon: " + response.header("logon").trim().toString());
            hashMap.put("logon", Integer.parseInt(response.header("logon").trim().toString()));
            hashMap.put("userid", Integer.parseInt(response.header("userid").trim().toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return hashMap;
    }

    public static boolean Register(String username, String password)
    {
        boolean success = false;
        Request request = new Request.Builder()
                .url(GetBaseUrl("register"))
                .addHeader("username", username)
                .addHeader("password", password)
                .build();
        try {
            Response response = client.newCall(request).execute();
            success = response.isSuccessful();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return success;
    }

    public static List<Quest> GetQuestNotDo(int userid)
    {
        List<Quest> list = null;
        Request request = new Request.Builder()
                .url(GetBaseUrl("not do"))
                .addHeader("userid", String.valueOf(userid))
                .build();

        try {
            Response response = client.newCall(request).execute();
            Gson gson = new Gson();
            String jsonStr = response.body().string();
            list = gson.fromJson(jsonStr, new TypeToken<List<Quest>>(){}.getType());

//            log("userid", userid + " ");
//            log("response str", jsonStr);
//            log("list title:", list.get(0).getTitle());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<Quest> GetNotHavingQuest(int userid)
    {
        List<Quest> list = null;
        Request request = new Request.Builder()
                .url(GetBaseUrl("purse"))
                .addHeader("userid", String.valueOf(userid))
                .build();

        try {
            Response response = client.newCall(request).execute();
            Gson gson = new Gson();
            String jsonStr = response.body().string();
            list = gson.fromJson(jsonStr, new TypeToken<List<Quest>>(){}.getType());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<Quest> GetHavingQuest(int userid)
    {
        List<Quest> list = null;
        Request request = new Request.Builder()
                .url(GetBaseUrl("having"))
                .addHeader("userid", String.valueOf(userid))
                .build();

        try {
            Response response = client.newCall(request).execute();
            Gson gson = new Gson();
            String jsonStr = response.body().string();
            list = gson.fromJson(jsonStr, new TypeToken<List<Quest>>(){}.getType());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static HashMap<String, String> GetUserInfo(int userid)
    {
        HashMap<String, String> hashMap = new HashMap<>();
        Request request = new Request.Builder()
                .url(GetBaseUrl("userinfo"))
                .addHeader("userid", String.valueOf(userid))
                .build();
        try {
            Response response = client.newCall(request).execute();
            hashMap.put("name", response.header("username"));
            hashMap.put("score", response.header("score"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return hashMap;
    }

    public static int SignIn(int userid)
    {
        Request request = new Request.Builder()
                .url(GetBaseUrl("sign in"))
                .addHeader("userid", String.valueOf(userid))
                .build();
        int successful = 0;

        try {
            Response response = client.newCall(request).execute();
            successful = Integer.parseInt(response.header("signin").toString());

        } catch (IOException e) {
            e.printStackTrace();
        }

        return successful;
    }

    public static List<SubQuestion> GetSubQuestion(int questid)
    {
        List<SubQuestion> list = null;
        Request request = new Request.Builder()
                .url(GetBaseUrl("get question"))
                .addHeader("questid", String.valueOf(questid))
                .build();

        try {
            Response response = client.newCall(request).execute();
            String jsonStr = response.body().string();
            Gson gson = new Gson();
            list = gson.fromJson(jsonStr, new TypeToken<List<SubQuestion>>(){}.getType());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<Option> GetOption(int questid)
    {
        List<Option> list = null;
        Request request = new Request.Builder()
                .url(GetBaseUrl("get option"))
                .addHeader("questid", String.valueOf(questid))
                .build();

        try {
            Response response = client.newCall(request).execute();
            String jsonStr = response.body().string();
            Gson gson = new Gson();
            list = gson.fromJson(jsonStr, new TypeToken<List<Option>>(){}.getType());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void BuyQuest(int userid, int questid)
    {
        Request request = new Request.Builder()
                .url(GetBaseUrl("buy"))
                .addHeader("questid", String.valueOf(questid))
                .addHeader("userid", String.valueOf(userid))
                .build();

        try {
            Response response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void InsertData(int userid, int questid, int reward, String dataJsonStr)
    {
        Request request = new Request.Builder()
                .url(GetBaseUrl("insert data"))
                .addHeader("questid", String.valueOf(questid))
                .addHeader("userid", String.valueOf(userid))
                .addHeader("reward", String.valueOf(reward))
                .addHeader("data", dataJsonStr)
                .build();

        try {
            Response response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String GetData(int questid)
    {

        Request request = new Request.Builder()
                .url(GetBaseUrl("get data"))
                .addHeader("questid", String.valueOf(questid))
                .build();
        String strJson = null;
        try {
            Response response = client.newCall(request).execute();
            strJson = response.body().string().trim();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return strJson;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static boolean isNetConnection(Context context) {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        Network network = cm.getActiveNetwork();
        if (null == network) {
            return false;
        }
        NetworkCapabilities capabilities = cm.getNetworkCapabilities(network);
        if (null == capabilities) {
            return false;
        }
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
    }
}
