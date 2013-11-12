package com.github.marmalade.aRevelation.exception;

import com.github.marmalade.aRevelation.R;

/**
 * Created by sviro on 11/12/13.
 */
public class UnknownEntryTypeException extends ARevelationException {

    public UnknownEntryTypeException() {
    }

    public UnknownEntryTypeException(String detailMessage) {
        super(detailMessage);
    }

    public UnknownEntryTypeException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public UnknownEntryTypeException(Throwable throwable) {
        super(throwable);
    }

    @Override
    protected int getErrorStringResource() {
        return R.string.unexpected_error;
    }
}
