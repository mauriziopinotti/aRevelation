package com.github.marmalade.aRevelation.exception;

import android.content.Context;

/**
 * Created by sviro on 11/12/13.
 */
public abstract class ARevelationException extends Exception {

    public ARevelationException() {
    }

    public ARevelationException(String detailMessage) {
        super(detailMessage);
    }

    public ARevelationException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public ARevelationException(Throwable throwable) {
        super(throwable);
    }

    public String getErrorMessage(Context context) {
        return context.getString(getErrorStringResource());
    }

    protected abstract int getErrorStringResource();
}
