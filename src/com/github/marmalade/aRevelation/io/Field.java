package com.github.marmalade.aRevelation.io;

import android.os.Parcel;
import android.os.Parcelable;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Text;

/**
 * Created by sviro on 11/26/13.
 */
public class Field implements Parcelable {
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
