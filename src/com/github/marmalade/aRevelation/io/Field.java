package com.github.marmalade.aRevelation.io;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.github.marmalade.aRevelation.R;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Text;

/**
 * Created by sviro on 11/26/13.
 */
public class Field implements Parcelable {
    public static final String NAME = "generic-name";
    public static final String PASSWORD = "generic-password";
    public static final String EMAIL = "generic-email";
    public static final String USER_NAME = "generic-username";
    public static final String HOST_NAME = "generic-hostname";
    public static final String PORT = "generic-port";
    public static final String LOCATION = "generic-location";
    public static final String PIN = "generic-pin";
    public static final String DATABASE = "generic-database";
    public static final String URL = "generic-url";
    public static final String DOMAIN = "generic-domain";
    public static final String CODE = "generic-code";
    public static final String CREDIT_CARD_TYPE = "creditcard-cardtype";
    public static final String CREDIT_CARD_CCV = "creditcard-ccv";
    public static final String CREDIT_CARD_EXPIRY_DATE = "creditcard-expirydate";
    public static final String CREDIT_CARD_NUMBER = "creditcard-cardnumber";
    public static final String PHONE_NUMBER = "phone-phonenumber";

    @Attribute(name = "id")
    String fieldName;

    @Text(required = false)
    String value;

    public Field() {

    }

    public Field(Parcel in) {
        fieldName = in.readString();
        value = in.readString();
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getFieldName(Context context) {
        if (NAME.equals(fieldName)) {
            return context.getString(R.string.name);
        }
        if (PASSWORD.equals(fieldName)) {
            return context.getString(R.string.password);
        }
        if (EMAIL.equals(fieldName)) {
            return context.getString(R.string.email);
        }
        if (USER_NAME.equals(fieldName)) {
            return context.getString(R.string.username);
        }
        if (HOST_NAME.equals(fieldName)) {
            return context.getString(R.string.hostname);
        }
        if (PORT.equals(fieldName)) {
            return context.getString(R.string.port);
        }
        if (LOCATION.equals(fieldName)) {
            return context.getString(R.string.location);
        }
        if (PIN.equals(fieldName)) {
            return context.getString(R.string.pin);
        }
        if (DATABASE.equals(fieldName)) {
            return context.getString(R.string.database);
        }
        if (URL.equals(fieldName)) {
            return context.getString(R.string.url);
        }
        if (DOMAIN.equals(fieldName)) {
            return context.getString(R.string.domain);
        }
        if (CODE.equals(fieldName)) {
            return context.getString(R.string.code);
        }
        if (CREDIT_CARD_TYPE.equals(fieldName)) {
            return context.getString(R.string.cardtype);
        }
        if (CREDIT_CARD_CCV.equals(fieldName)) {
            return context.getString(R.string.ccv);
        }
        if (CREDIT_CARD_EXPIRY_DATE.equals(fieldName)) {
            return context.getString(R.string.expirydate);
        }
        if (CREDIT_CARD_NUMBER.equals(fieldName)) {
            return context.getString(R.string.cardnumber);
        }
        if (PHONE_NUMBER.equals(fieldName)) {
            return context.getString(R.string.phonenumber);
        }

        return fieldName;
    }

    public String getValue() {
        return value;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(fieldName);
        out.writeString(value);
    }

    public static final Parcelable.Creator<Field> CREATOR
            = new Parcelable.Creator<Field>() {
        public Field createFromParcel(Parcel in) {
            return new Field(in);
        }

        public Field[] newArray(int size) {
            return new Field[size];
        }
    };
}
