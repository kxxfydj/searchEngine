package com.kxxfydj.utils;

import com.alibaba.fastjson.JSONObject;
import com.kaikai.entity.Location;
import com.kaikai.exception.RentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * create by kaiming_xu on 2017/10/11
 */
public class MapLocationUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(MapLocationUtil.class);

    public static Location getLngAndLat(String address){
        Location location = new Location();

        String json = "";
        String url = "http://api.map.baidu.com/geocoder/v2/?address="+address+"&output=json&ak=rM2jRXlxMCWUnw0EQ0FFMjNd6eEjupQk";
        try {
            StringResponse response = HttpUtils.get(url);
            if(response.getStatusCode() != 200){
                LOGGER.error("unknow status code! code:{}",response.getStatusCode());
                throw new RentException("unknow status code! code:" + response.getStatusCode());
            }
            json = response.getResponseBody();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
            return null;
        }

        JSONObject jsonObject = JSONObject.parseObject(json);
        if(jsonObject.get("status").toString().equals("0")) {
            double lng = jsonObject.getJSONObject("result").getJSONObject("location").getDouble("lng");
            double lat = jsonObject.getJSONObject("result").getJSONObject("location").getDouble("lat");
            location.setLat(lat);
            location.setLng(lng);
        }else {
            LOGGER.error("未知的json状态! 位置:{}",address);
            return null;
        }

        return location;
    }

    public static void main(String[] args){
        Location location = MapLocationUtil.getLngAndLat("深圳福田区梅林金丰花园4居室-北卧");
        System.out.println("lng:" + location.getLng() + " lat:" + location.getLat());
    }
}
