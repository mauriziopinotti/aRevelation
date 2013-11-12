package com.github.marmalade.aRevelation.exception;

import com.github.marmalade.aRevelation.R;

/**
 * Created by sviro on 11/12/13.
 */
public class WrongPasswordException extends ARevelationException {

    public WrongPasswordException() {
    }

    public WrongPasswordException(String detailMessage) {
        super(detailMessage);
    }

    public WrongPasswordException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public WrongPasswordException(Throwable throwable) {
        super(throwable);
    }

    @Override
    protected int getErrorStringResource() {
        return R.string.wrong_password_error;
    }
}
