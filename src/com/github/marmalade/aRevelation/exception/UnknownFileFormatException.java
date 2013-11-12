package com.github.marmalade.aRevelation.exception;

import com.github.marmalade.aRevelation.R;

/**
 * Created by sviro on 11/12/13.
 */
public class UnknownFileFormatException extends ARevelationException {

    public UnknownFileFormatException() {
    }

    public UnknownFileFormatException(String detailMessage) {
        super(detailMessage);
    }

    public UnknownFileFormatException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public UnknownFileFormatException(Throwable throwable) {
        super(throwable);
    }

    @Override
    protected int getErrorStringResource() {
        return R.string.unsupported_file_format_exception;
    }
}
