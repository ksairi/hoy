package com.hoy.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import com.hoy.constants.MilongaHoyConstants;

import java.util.Map;

/**
 * Encapsula las funciones para la administracion de Shared Preferences
 * agregandole a cada clave el prefijo del usuario logueado.
 */
public class SharedPreferencesHelper {

    public static Map<String, ?> getAll(Context context) {
        return context.getSharedPreferences(MilongaHoyConstants.SHARED_PREFERENCES, Context.MODE_PRIVATE).getAll();
    }

    /**
     * Se obtiene el valor correspondiente a <code>key</code> de Shared
     * Preferences. Si el parametro <code>loggedUserId</code> es nulo, se
     * considera que es una entrada en Shared Preferencesk, comun para todos los
     * usuarios.
     *
     * @param context
     * @param key
     * @return
     */
    public static String getValueInSharedPreferences(Context context, String key) {
        return context.getSharedPreferences(MilongaHoyConstants.SHARED_PREFERENCES, Context.MODE_PRIVATE).getString(key, MilongaHoyConstants.EMPTY_STRING);
    }

    /**
     * Se setea un valor en Shared Preferences. Si el parametro
     * <code>loggedUserId</code> es nulo, el valor sera comun para todos los
     * usuarios.
     *
     * @param context
     * @param key
     * @param value
     */
    public synchronized static void setValueSharedPreferences(Context context, String key, String value) {

        SharedPreferences sp = context.getSharedPreferences(MilongaHoyConstants.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        sp.edit().putString(key, value).commit();
    }

    /**
     * Se elimina la entrada correspondiente a <code>key</code> de Shared
     * Preferences. Si el parametro <code>loggedUserId</code> es nulo, se
     * considera que era una entrada en Shared Preferencesk, comun para todos los
     * usuarios.
     *
     * @param context
     * @param key
     */
    public synchronized static void removeValueSharedPreferences(Context context, String key) {

        SharedPreferences sp = context.getSharedPreferences(MilongaHoyConstants.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        sp.edit().remove(key).commit();
    }
}
