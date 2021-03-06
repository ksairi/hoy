package com.hoy.constants;

public class MilongaHoyConstants {

    public static final String EMPTY_STRING = "";

    //	public static final String HOST = "http://10.0.2.2";
//	public static final String HOST = "http://192.168.1.107";
    public static final String HOST = "http://www.milongaproject.com";
    public static final String IMG_HOST = "http://teachingtango.com";
//	public static final String IMG_HOST = "http://192.168.1.107";


    public static final String PROMO_IMG_URL = IMG_HOST.concat("/api/convert.php");
//	public static final String PROMO_IMG_URL = IMG_HOST.concat("/testImg.php");

    //	public static final String SYNC_EVENTS_URL = HOST.concat("/test.php");
    public static final String SYNC_EVENTS_URL = HOST.concat("/mp/prod/php/api/CRRetrieve.php");
    public static final String MILONGA_IMAGE_URL = HOST.concat("/mp/prod/images/");

    public static final String SHARED_PREFERENCES = "sharedPreferences";
    public static final String RESPONSE_OK = "0";
    public static final Integer MANUALLY_UPDATE_PERIOD = 15;
    public static final int TIME_WITH_SECONDS_LENGTH = 8;
    public static final String EVENT_DTO = "eventDTO";
    public static final String TRUE = "1";
    public static final String FALSE = "0";
    public static final String EVENT_ADDRESS = "eventAddress";
    public static final String EVENT_NAME = "eventName";
    public static final String EVENT_LONGITUDE = "eventLongitude";
    public static final String EVENT_LATITUDE = "eventLatitude";
    public static final String SAVE_PROMO_SUCCESS = "savePromoSuccess";
    public static final String SAVE_MILONGAS_SUCCESS = "saveMilongasSuccess";
    public static final String PROMO_IMG_UPDATED = "promoImgUpdated";
    public static final String CURRENT_IMG_PROMO_INDEX = "currentImgPromoIndex";
    public static final String PROMO_IMG_DATA = "promoImgData";
    public static final String JSON_PREFIX_SUFIX = ">>>HMPACKAGE<<<";
    public static final String EVENT_DTOS = "eventDTOs";
    public static final String NEW_MILONGAS_UPDATES = "newMilongasUpdates";
    public static final String SERVER_LAST_UPDATE_TIME = "serverLastUpdateTime";
    public static final Integer PROMO_IMG_BASE_64_INDEX_POSITION = 0;
    public static final Integer PROMO_IMG_URL_DESTINATION_INDEX_POSITION = 1;
    public static final String ERROR_SYNC_EVENTS = "errorSyncEvents";
    public static final String LAST_FULL_UPDATE_DATE = "lastFullUpdateDate";
    public static final String TODAY_DTOS = "TODAY_DTOS";
    public static final String HAS_CLEANED_VALUES = "HAS_CLEANED_VALUES";
    public static final String ANALYTICS_SCREEN_EVENTS_CATEGORY ="ANALYTICS_SCREEN_EVENTS_CATEGORY";
    public static final String ANALYTICS_OPEN_SCREEN_ACTION ="ANALYTICS_OPEN_SCREEN_ACTION";
    public static final String ANALYTICS_OPEN_LIST_EVENT_LABEL = "Milonga List";
    public static final String ANALYTICS_OPEN_DETAIL_EVENT_LABEL = "Milonga Detail";
    public static final String ANALYTICS_OPEN_TODAYS_MAP_LABEL = "Mapa de Hoy";
    public static final String ANALYTICS_OPEN_MILONGA_MAP_LABEL = "Mapa de Milonga";
    public static final String ANALYTICS_TRACK_ID = "UA-42599328-2";
    public static final Boolean DEBUG = false;
    public static final String LOCALE_SPANISH = "es";

    public static final String PREFIX_MILONGA_IMAGE = "ML";
    public static final Integer MILONGA_IMG_DAYS_LIMIT = 5;
    public static final String APP_VERSION_CODE = "APP_VERSION_CODE";
}
