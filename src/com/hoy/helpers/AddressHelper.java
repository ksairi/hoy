package com.hoy.helpers;

import com.hoy.constants.MilongaHoyConstants;
import com.hoy.dto.EventDTO;

/**
 * Created with IntelliJ IDEA.
 * User: ksairi
 * Date: 6/10/13
 * Time: 3:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class AddressHelper {

    public static String getEventAddress(EventDTO eventDTO) {
        if (eventDTO != null && eventDTO.getStreetLine1() != null && eventDTO.getFamiliarNameOfArea() != null) {
            return eventDTO.getStreetLine1().concat("-").concat(eventDTO.getFamiliarNameOfArea());
        }
        return MilongaHoyConstants.EMPTY_STRING;
    }
}
