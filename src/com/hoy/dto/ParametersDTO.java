package com.hoy.dto;

/**
 * Created with IntelliJ IDEA.
 * User: ksairi
 * Date: 1/2/13
 * Time: 5:33 AM
 * To change this template use File | Settings | File Templates.
 */
public class ParametersDTO {

	private static final String SYNC_EVENTS_SEL_PARAM_NAME = "sel";
	private static final String SYNC_EVENTS_FROM_PARAM_NAME = "from";
	private static final String SYNC_EVENTS_DATE_PARAM_NAME = "date";

	private String sel = null;
	private String from = "MEventFull";
	private String where = "date = CURDATE() AND areaId = 1";

	public String getSelName() {
		return sel;
	}

	public String getFromName() {
		return from;
	}

	public String getWhereName() {
		return where;
	}

	public String getSelValue() {
			return SYNC_EVENTS_SEL_PARAM_NAME;
		}

	public String getFromValue() {
			return SYNC_EVENTS_FROM_PARAM_NAME;
		}

	public String getWhereValue() {
			return SYNC_EVENTS_DATE_PARAM_NAME;
		}

}
