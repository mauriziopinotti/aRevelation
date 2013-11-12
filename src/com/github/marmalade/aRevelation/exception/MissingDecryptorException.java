package com.github.marmalade.aRevelation.exception;

import com.github.marmalade.aRevelation.R;

/**
 * Created by sviro on 11/12/13.
 */
public class MissingDecryptorException extends ARevelationException {

    public MissingDecryptorException() {
    }

    public MissingDecryptorException(String detailMessage) {
        super(detailMessage);
    }

    public MissingDecryptorException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public MissingDecryptorException(Throwable throwable) {
        super(throwable);
    }

    @Override
    protected int getErrorStringResource() {
        return R.string.unexpected_error;
    }
}
