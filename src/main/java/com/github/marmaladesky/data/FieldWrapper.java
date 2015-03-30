package com.github.marmaladesky.data;

import java.io.Serializable;

public class FieldWrapper implements Serializable {

    private Field field;

    private String property;
    private Entry entry;

    public FieldWrapper(Field field) {
        this.field = field;
    }

    public FieldWrapper(String property, Entry entry) {
        this.property = property;
        this.entry = entry;
    }

    public String getFieldValue() {
        if(field != null) {
            return field.getValue();
        } else if (entry != null) {
            switch (property) {
                case "name": return entry.getName();
                case "description": return entry.getDescription();
                case "notes": return entry.getNotes();
            }
            return null;
        } else
            return "";
    }

    public void setFieldValue(String newVal) throws Exception {
        if(field != null) {
            field.setValue(newVal);
        } else if (entry != null) {
            switch (property) {
                case "name":
                    entry.setName(newVal); break;
                case "description":
                    entry.setDescription(newVal); break;
                case "notes":
                    entry.setNotes(newVal); break;
                default: throw new Exception("Unknown Entity property " + property);
            }
        }
    }

    public String getUuid() throws Exception {
        if(field != null) {
            return field.getUuid();
        } else {
            switch (property) {
                case "name": return entry.getUuidName();
                case "description": return entry.getUuidDescription();
                case "notes": return entry.getUuidNotes();
            }
        }
        throw new Exception("Unknown state");
    }

}
