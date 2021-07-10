package com.knowit.profile.exceptions;

import com.knowit.profile.constants.ExceptionConstants;

public class UserDoesNotExistException extends Exception {

    public UserDoesNotExistException() {
        super(ExceptionConstants.USER_NOT_FOUND);
    }
}
