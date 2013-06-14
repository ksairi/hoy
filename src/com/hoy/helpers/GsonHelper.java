package com.hoy.helpers;

import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.hoy.constants.MilongaHoyConstants;
import com.hoy.utilities.DateUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ksairi
 * @date 4/25/12 2:32
 */
public class GsonHelper {

	private static final String TAG = GsonHelper.class.getSimpleName();
	private static Gson gson;

	private static Gson getGson() {
		if (gson == null) {
			gson = new Gson();
		}
		return gson;
	}

	public static String parseEntityToJson(Object obj) {

		Gson gson = getGson();
		String objParsed = gson.toJson(obj);

		return objParsed;
	}

	public static <T> T parseJsonToEntity(String jsonString, Class<T> clazz) {
		T obj = null;
		try {
			Gson gson = getGson();
			obj = gson.fromJson(jsonString, clazz);
		} catch (JsonSyntaxException e) {
			Log.e(TAG, e.getMessage());
		}
		return obj;
	}

	public static <T> List<T> parseJsonToArrayListEntity(String jsonString, Type listType) {

		// fuente: http://rishabhsays.wordpress.com/2011/02/24/parsing-list-of-json-objects-with-gson/
		List<T> result = new ArrayList<T>();
		try {
			Gson gson = getGson();
			result = gson.fromJson(jsonString, listType);
		} catch (JsonSyntaxException e) {
			Log.e(TAG, e.getMessage());
		}

		return result == null ? new ArrayList<T>() : result;
	}

	public static String parseResponse(String jsonString) {

		String resultData = null;
		try {
				if(jsonString != null){
					jsonString = jsonString.replace(MilongaHoyConstants.JSON_PREFIX_SUFIX,MilongaHoyConstants.EMPTY_STRING);
					JSONObject jsonObject = new JSONObject(jsonString);

					JSONObject resultSummary = jsonObject.getJSONObject("resultSummary");
					String errorNumber = resultSummary.getString("errorNumber");
					if (errorNumber.equals(MilongaHoyConstants.RESPONSE_OK)) {
						resultData = adaptJson(jsonObject);
					}
				}
			}catch (JSONException e) {
				resultData = null;
			}

		return resultData;

	}

	private static String adaptJson(JSONObject jsonObject){

		JSONArray jsonArray;
		try{
			jsonArray = jsonObject.getJSONArray("resultData");
			for(Integer i = 0;i< jsonArray.length();i++){
				JSONObject aux = (JSONObject)jsonArray.get(i);
				aux.put("date", DateUtils.changeDateFormat(aux.getString("date")));
			}
		}catch (JSONException e){
			return null;
		}
		return jsonArray.toString();
	}

}
