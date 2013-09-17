package com.hoy.dto.message;

import java.io.Serializable;

/**
 * Se utiliza como estructura generica de mensaje para el envio de datos al servidor.
 *
 * @author LDicesaro
 */
public class MessageDTO<T> implements Serializable {

    private static final long serialVersionUID = 1410213698332113134L;
    private HeaderDTO headerDTO;
    private T bodyDTO;

    public HeaderDTO getHeaderDTO() {
        return headerDTO;
    }

    public void setHeaderDTO(HeaderDTO headerDTO) {
        this.headerDTO = headerDTO;
    }

    public T getBodyDTO() {
        return bodyDTO;
    }

    public void setBodyDTO(T bodyDTO) {
        this.bodyDTO = bodyDTO;
    }
}
