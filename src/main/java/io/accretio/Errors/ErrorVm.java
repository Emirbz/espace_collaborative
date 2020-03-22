package io.accretio.Errors;

import java.io.Serializable;

/**
 * View Model for transferring error message with a list of field errors.
 */
public class ErrorVm implements Serializable {

    private static final long serialVersionUID = 1L;

    private  String message;




    public ErrorVm(String message) {
        this.message = message;

    }

    public String getMessage() {
        return message;
    }


}