package com.github.marmalade.aRevelation.io;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.github.marmalade.aRevelation.exception.UnknownEntryTypeException;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Commit;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.transform.Matcher;
import org.simpleframework.xml.transform.Transform;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by sviro on 11/25/13.
 */
public class Entry implements Parcelable {
    @Attribute
    EntryType type;
    @Element
    String name;
    @Element(required = false)
    String description;
    @Element
    String updated;
    @Element(required = false)
    String notes;
    @ElementList(entry = "field", inline = true)
    List<Field> fields;
    //TODO parse folder type
    List<Entry> children;
    Field secretFieldData;

    public Entry() {

    }

    public Entry(String name, String description,
                 String updated, String notes,
                 List<Field> fields, int type) throws UnknownEntryTypeException {
        this.name = name;
        this.description = description;
        this.updated = updated;
        this.notes = notes;
        this.fields = fields;
        this.type = new EntryType(type);
        buildSecretFieldData();
    }

    public Entry(String name, String description,
                 String updated, String notes,
                 List<Field> fields, int type,
                 List<Entry> entries) throws UnknownEntryTypeException {
        this(name, description, updated, notes, fields, type);
        this.children = entries;
    }

    public Entry(Parcel in) {
        type = in.readParcelable(EntryType.class.getClassLoader());
        name = in.readString();
        description = in.readString();
        updated = in.readString();
        notes = in.readString();
        fields = new ArrayList<>();
        in.readTypedList(fields, Field.CREATOR);
        children = new ArrayList<>();
        in.readTypedList(children, Entry.CREATOR);
        secretFieldData = in.readParcelable(Field.class.getClassLoader());
    }

    public static List<Entry> parseDecryptedXml(String rvlXml) throws Exception {
        Serializer serializer = new Persister(new EntryTypeMatcher());

        RevelationData revelationData = serializer.read(RevelationData.class, rvlXml);

        return revelationData.getEntries();
    }

    @Commit
    public void buildSecretFieldData() {
        if (type != null) {
            int entryType = type.getType();
            if (entryType == EntryType.TYPE_CREDIT_CARD) {
                secretFieldData = getField(Field.PIN);
            } else if (entryType == EntryType.TYPE_DOOR) {
                secretFieldData = getField(Field.CODE);
            } else if (entryType == EntryType.TYPE_PHONE) {
                secretFieldData = getField(Field.PIN);
            } else if (
                    entryType == EntryType.TYPE_DATABASE
                            || entryType == EntryType.TYPE_CRYPTO_KEY
                            || entryType == EntryType.TYPE_EMAIL
                            || entryType == EntryType.TYPE_GENERIC
                            || entryType == EntryType.TYPE_FTP
                            || entryType == EntryType.TYPE_REMOTE_DESKTOP
                            || entryType == EntryType.TYPE_SHELL
                            || entryType == EntryType.TYPE_VNC
                            || entryType == EntryType.TYPE_WEBSITE) {
                secretFieldData = getField(Field.PASSWORD);
            }
        }

    }

    private Field getField(String fieldName) {
        if (!TextUtils.isEmpty(fieldName)) {
            for (Field field : fields) {
                if (fieldName.equals(field.getFieldName())) {
                    return field;
                }
            }
        }

        return null;
    }


    public int getType() {
        return type.getType();
    }

    /**
     * Returns type of the entry in human readable format
     * @return type of the entry in human readable format
     */
    public String getTypeFormatted() {
        return type.toString();
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public long getUpdated() {
        return TimeUnit.SECONDS.toMillis(Long.parseLong(updated));
    }

    public String getNotes() {
        return notes;
    }

    public List<Field> getFields() {
        return fields;
    }

    public List<Entry> getChildren() {
        return children;
    }

    public String getSecretFieldData() {
        if (secretFieldData != null) {
            return secretFieldData.getValue();
        }

        return null;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeParcelable(type, 0);
        out.writeString(name);
        out.writeString(description);
        out.writeString(updated);
        out.writeString(notes);
        out.writeTypedList(fields);
        out.writeTypedList(children);
        out.writeParcelable(secretFieldData, 0);
    }

    public static final Parcelable.Creator<Entry> CREATOR
            = new Parcelable.Creator<Entry>() {
        public Entry createFromParcel(Parcel in) {
            return new Entry(in);
        }

        public Entry[] newArray(int size) {
            return new Entry[size];
        }
    };

    public static class EntryType implements Parcelable {

        private static final String FOLDER = "folder";
        private static final String CREDIT_CARD = "creditcard";
        private static final String CRYPTO_KEY = "cryptokey";
        private static final String DOOR = "door";
        private static final String DATABASE = "database";
        private static final String EMAIL = "email";
        private static final String FTP = "ftp";
        private static final String GENERIC = "generic";
        private static final String REMOTE_DESKTOP = "remotedesktop";
        private static final String SHELL = "shell";
        private static final String VNC = "vnc";
        private static final String WEBSITE = "website";
        private static final String PHONE = "phone";
        private static final String NON_REAL = "nonreal";

        public static final int TYPE_FOLDER = 0;
        public static final int TYPE_CREDIT_CARD = 1;
        public static final int TYPE_CRYPTO_KEY = 2;
        public static final int TYPE_DOOR = 3;
        public static final int TYPE_DATABASE = 4;
        public static final int TYPE_EMAIL = 5;
        public static final int TYPE_FTP = 6;
        public static final int TYPE_GENERIC = 7;
        public static final int TYPE_REMOTE_DESKTOP = 8;
        public static final int TYPE_SHELL = 9;
        public static final int TYPE_VNC = 10;
        public static final int TYPE_WEBSITE = 11;
        public static final int TYPE_PHONE = 12;
        public static final int TYPE_NON_REAL = 13;


        private int type;

        public EntryType(Parcel in) {
            type = in.readInt();
        }

        public static EntryType fromString(String value) throws UnknownEntryTypeException {
            int type;

            switch (value) {
                case FOLDER:
                    type = TYPE_FOLDER;
                    break;
                case CREDIT_CARD:
                    type = TYPE_CREDIT_CARD;
                    break;
                case CRYPTO_KEY:
                    type = TYPE_CRYPTO_KEY;
                    break;
                case DOOR:
                    type = TYPE_DOOR;
                    break;
                case DATABASE:
                    type = TYPE_DATABASE;
                    break;
                case EMAIL:
                    type = TYPE_EMAIL;
                    break;
                case FTP:
                    type = TYPE_FTP;
                    break;
                case GENERIC:
                    type = TYPE_GENERIC;
                    break;
                case REMOTE_DESKTOP:
                    type = TYPE_REMOTE_DESKTOP;
                    break;
                case SHELL:
                    type = TYPE_SHELL;
                    break;
                case VNC:
                    type = TYPE_VNC;
                    break;
                case WEBSITE:
                    type = TYPE_WEBSITE;
                    break;
                case PHONE:
                    type = TYPE_PHONE;
                    break;
                case NON_REAL:
                    type = TYPE_NON_REAL;
                    break;
                default:
                    throw new UnknownEntryTypeException(value);
            }

            return new EntryType(type);
        }

        public EntryType(int type) {
            this.type = type;
        }

        public int getType() {
            return type;
        }

        @Override
        public String toString() {
            switch (type) {
                case TYPE_FOLDER:
                    return FOLDER;
                case TYPE_CREDIT_CARD:
                    return CREDIT_CARD;
                case TYPE_CRYPTO_KEY:
                    return CRYPTO_KEY;
                case TYPE_DOOR:
                    return DOOR;
                case TYPE_DATABASE:
                    return DATABASE;
                case TYPE_EMAIL:
                    return EMAIL;
                case TYPE_FTP:
                    return FTP;
                case TYPE_GENERIC:
                    return GENERIC;
                case TYPE_REMOTE_DESKTOP:
                    return REMOTE_DESKTOP;
                case TYPE_SHELL:
                    return SHELL;
                case TYPE_VNC:
                    return VNC;
                case TYPE_WEBSITE:
                    return WEBSITE;
                case TYPE_PHONE:
                    return PHONE;
                case TYPE_NON_REAL:
                    return NON_REAL;
                default:
                    return null;
            }
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            out.writeInt(type);
        }

        public static final Parcelable.Creator<EntryType> CREATOR
                = new Parcelable.Creator<EntryType>() {
            public EntryType createFromParcel(Parcel in) {
                return new EntryType(in);
            }

            public EntryType[] newArray(int size) {
                return new EntryType[size];
            }
        };
    }

    private static class EntryTypeMatcher implements Matcher {

        @Override
        public Transform match(Class aClass) throws Exception {
            if (aClass.equals(EntryType.class)) {
                return new EntryTypeTransform();
            }

            return null;
        }
    }

    private static class EntryTypeTransform implements Transform<EntryType> {
        @Override
        public EntryType read(String s) throws Exception {
            return EntryType.fromString(s);
        }

        @Override
        public String write(EntryType entryType) throws Exception {
            return entryType.toString();
        }
    }
}
