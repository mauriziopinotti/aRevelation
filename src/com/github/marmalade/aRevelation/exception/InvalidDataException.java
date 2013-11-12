package com.github.marmalade.aRevelation.exception;

import com.github.marmalade.aRevelation.R;

/**
 * Created by sviro on 11/12/13.
 */
public class InvalidDataException extends ARevelationException {

    public InvalidDataException() {
    }

    public InvalidDataException(String detailMessage) {
        super(detailMessage);
    }

    public InvalidDataException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public InvalidDataException(Throwable throwable) {
        super(throwable);
    }

    @Override
    protected int getErrorStringResource() {
        return R.string.invalid_data_error;
    }
}
