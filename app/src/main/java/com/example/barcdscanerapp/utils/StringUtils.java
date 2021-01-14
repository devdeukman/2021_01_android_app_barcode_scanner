package com.example.barcdscanerapp.utils;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 *  개발에 필요한 잡스러운 utils
 * @author LCY
 * @version 1.0.0
 * @since 2020-07-07 오전 11:21
 **/
public class StringUtils {


    public final static String noNull( Object string ) {
        return noNull( string, "" );
    }
    public final static String noNull( String string, String defaultString ) {
        return ( stringSet( string ) ) ? string : defaultString;
    }
    public final static Long noNull( Long vLong, Long defaultLong ) {
        return ( longSet( vLong ) ) ? vLong : defaultLong;
    }
    public final static Long noNull( Long vLong ) {
        return noNull( vLong, 0L );
    }
    public final static boolean longSet( Long vLong ) {
        return ( vLong != null ) && ( vLong != 0L );
    }

    public final static String noNull( Object string, String defaultString ) {
        String returnVal = "";
        if( string == null ){
            return defaultString;
        }else{
            returnVal = noNull( string.toString(), defaultString );//웹취약점 조치
        }
        return returnVal;
    }
    public final static boolean stringSet( String string ) {
        return ( string != null ) && !"".equals( string );
    }

    /**
     *  ArrayList<HashMap<String,Object>> To Json
     * @author LCY
     * @version 1.0.0
     * @since 2020-07-09 오전 11:41
     **/
    public static String listToJson(ArrayList<HashMap<String,Object>> datalist){
        JSONArray jArr = new JSONArray();
        HashMap<String,Object> data = new HashMap<>();
        Set key = null;
        String keyName = null;
        String valueName = null;
        try{
            for(int i=0; i<datalist.size() ; i++){
                JSONObject sObj = new JSONObject();
                data = (HashMap<String,Object>) datalist.get(i);
                key = data.keySet();
                for (Iterator iterator = key.iterator(); iterator.hasNext();) {
                    keyName = (String) iterator.next();
                    valueName = StringUtils.noNull(data.get(keyName));
                    sObj.put( keyName, valueName);
                }
                jArr.put(sObj);
            }
        }catch (Exception e){
            Log.d("List TO Json Exception" , e.getMessage());
        }
        return jArr.toString();
    }


    /**
     *  commandMap user_id , gusername add
     * @author LCY
     * @version 1.0.0
     * @since 2020-08-11 오후 5:33
     **/
    public static ContentValues setUserInfo(ContentValues commandMap ) {
//        commandMap.put("gUserId", noNull( memberVo.getUSER_ID() ) );//아이디
//        commandMap.put("gUserName", noNull( memberVo.getUSER_NAME() ) );//아이디
        return commandMap;
    };



    public static ArrayList<HashMap<String,Object>> strToArrList(String str){
        JSONArray jarray = null;
        JSONObject jObject = null;

        ArrayList<HashMap<String,Object>> resultsList = new ArrayList<HashMap<String,Object>>();
        if(str != null){
            try {
                jarray = new JSONArray(str);

                if (jarray != null) {
                    for (int i = 0; i < jarray.length(); i++) {
                        HashMap<String, Object> map = new HashMap<String, Object>();

                        try {
                            JSONObject c = (JSONObject) jarray.get(i);
                            //Fill map
                            Iterator iter = c.keys();
                            while(iter.hasNext())   {
                                String currentKey = (String) iter.next();
                                map.put(currentKey, c.getString(currentKey));
                            }
                            resultsList.add(map);
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        };
                    };
                };
            } catch (JSONException e) {
                e.printStackTrace();
            };
        };


        return resultsList;
    }

    public static void alarm(Context mContext){
        RingtoneManager.getRingtone(mContext, (  Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + mContext.getPackageName() + "/raw/alarm_ng") )).play();
    }

    public static boolean isNumeric(String input) {
        try {
            Double.parseDouble(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }

    }
}
