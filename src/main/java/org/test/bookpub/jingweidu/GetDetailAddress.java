package org.test.bookpub.jingweidu;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class GetDetailAddress {
    public static void main(String[] args) {
        String url = "http://restapi.amap.com/v3/geocode/regeo?output=json&key=20ff603c30311c7b3a6d103178b1f8d3&radius=1000&extensions=all" +
                "&batch=true&location=";
        OkHttpClient client = new OkHttpClient();

        File file = new File("D:\\gps.txt");

        File finalResult = new File("D:\\finalResult1.txt");

        if (!finalResult.exists())
            try {
                finalResult.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        File middleResult = new File("D:\\finalResult.txt");


        try {
            List<String> locations = Files.readLines(file, Charsets.UTF_8);
            int i = 0;
            StringBuilder sb = new StringBuilder();
            for (String s : locations) {
                if (i > 0 && i % 17 == 0){
                    i = 0;
                    Request request = new Request.Builder()
                            .url(url + sb.toString())
                            .build();

                    Response response = null;
                    try {
                        response = client.newCall(request).execute();
                        String res = response.body().string();

                        JSONObject jsonObject = JSONObject.parseObject(res);
                        JSONArray jsonArray = jsonObject.getJSONArray("regeocodes");
                        for (int j = 0; j< jsonArray.size();j++) {
                            JSONObject q = jsonArray.getJSONObject(j);
                            Files.append(q.getString("formatted_address") + "\n", finalResult, Charsets.UTF_8);
                        }
//                        System.out.println(jsonObject.toJSONString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    sb = new StringBuilder(s.split(" ")[1] + "," + s.split(" ")[0] + "|");

                }else {
                    sb.append(s.split(" ")[1] + "," + s.split(" ")[0] + "|");
                }
                i++;
            }
            Request request = new Request.Builder()
                    .url(url + sb.toString())
                    .build();

            Response response = null;
            try {
                response = client.newCall(request).execute();
                String res = response.body().string();

                JSONObject jsonObject = JSONObject.parseObject(res);
                JSONArray jsonArray = jsonObject.getJSONArray("regeocodes");
                for (int j = 0; j< jsonArray.size();j++) {
                    JSONObject q = jsonArray.getJSONObject(j);
                    Files.append(q.getString("formatted_address") + "\n", finalResult, Charsets.UTF_8);
                }
//                        System.out.println(jsonObject.toJSONString());
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }






    }
}
