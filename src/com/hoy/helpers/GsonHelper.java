package com.hoy.helpers;

import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

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

}
