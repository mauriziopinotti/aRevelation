package com.github.marmalade.aRevelation.io;

import android.app.Activity;

import com.github.marmalade.aRevelation.R;
import com.github.marmalade.aRevelation.exception.UnknownEntryTypeException;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.transform.Matcher;
import org.simpleframework.xml.transform.Transform;

import java.io.Serializable;
import java.util.List;

/**
* Created by sviro on 11/25/13.
*/
public class Entry implements Serializable {
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
    }

    public Entry(String name, String description,
                 String updated, String notes,
                 List<Field> fields, int type,
                 List<Entry> entries) throws UnknownEntryTypeException {
        this(name, description, updated, notes, fields, type);
        this.children = entries;
    }

    public static List<Entry> parseDecryptedXml(String rvlXml) throws Exception {
        Serializer serializer = new Persister(new EntryTypeMatcher());

        RevelationData revelationData = serializer.read(RevelationData.class, rvlXml);

        return revelationData.getEntries();
    }

    public int getType() {
        return type.getType();
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getUpdated() {
        return updated;
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

    @Override
    public String toString() {
        return name;
    }

    public String getSecretFieldData() {
//        if (entryType == EntryType.creditcard)
//            return fields.get("generic-pin");
//        else if (entryType == EntryType.door)
//            return fields.get("generic-code");
//        else if (entryType == EntryType.phone)
//            return fields.get("generic-pin");
//        else if (
//                entryType == EntryType.database
//                        || entryType == EntryType.cryptokey
//                        || entryType == EntryType.email
//                        || entryType == EntryType.generic
//                        || entryType == EntryType.ftp
//                        || entryType == EntryType.remotedesktop
//                        || entryType == EntryType.shell
//                        || entryType == EntryType.vnc
//                        || entryType == EntryType.website)
//            return fields.get("generic-password");
//        else
            return "";
    }

    public static String getFieldName(String fieldName, Activity activity) {
        if ("generic-name".equals(fieldName)) {
            return activity.getString(R.string.name);
        }
        if ("generic-password".equals(fieldName)) {
            return activity.getString(R.string.password);
        }
        if ("generic-email".equals(fieldName)) {
            return activity.getString(R.string.email);
        }
        if ("generic-username".equals(fieldName)) {
            return activity.getString(R.string.username);
        }
        if ("generic-hostname".equals(fieldName)) {
            return activity.getString(R.string.hostname);
        }
        if ("generic-port".equals(fieldName)) {
            return activity.getString(R.string.port);
        }
        if ("generic-location".equals(fieldName)) {
            return activity.getString(R.string.location);
        }
        if ("generic-pin".equals(fieldName)) {
            return activity.getString(R.string.pin);
        }
        if ("generic-database".equals(fieldName)) {
            return activity.getString(R.string.database);
        }
        if ("generic-url".equals(fieldName)) {
            return activity.getString(R.string.url);
        }
        if ("generic-domain".equals(fieldName)) {
            return activity.getString(R.string.domain);
        }
        if ("generic-code".equals(fieldName)) {
            return activity.getString(R.string.code);
        }
        if ("creditcard-cardtype".equals(fieldName)) {
            return activity.getString(R.string.cardtype);
        }
        if ("creditcard-ccv".equals(fieldName)) {
            return activity.getString(R.string.ccv);
        }
        if ("creditcard-expirydate".equals(fieldName)) {
            return activity.getString(R.string.expirydate);
        }
        if ("creditcard-cardnumber".equals(fieldName)) {
            return activity.getString(R.string.cardnumber);
        }
        if ("phone-phonenumber".equals(fieldName)) {
            return activity.getString(R.string.phonenumber);
        }

        return fieldName;
    }

    public static class EntryType {

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
