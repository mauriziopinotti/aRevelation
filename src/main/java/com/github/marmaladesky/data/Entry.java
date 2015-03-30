package com.github.marmaladesky.data;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Root
public class Entry implements Serializable {

    private final String uuid = UUID.randomUUID().toString();

    @Attribute(name="type")
    public String type;

    @Element(name = "name")
    private String name;
    private boolean isNameUpdated;
    private final String uuidName = UUID.randomUUID().toString();

    @Element(name = "description", required = false)
    private String description;
    private boolean isDescriptionUpdated;
    private final String uuidDescription = UUID.randomUUID().toString();

    @Element(name = "updated")
    public long updated;

    @Element(name = "notes", required = false)
    private String notes;
    private boolean isNotesUpdated;
    private final String uuidNotes = UUID.randomUUID().toString();

    @ElementList(inline = true, required = false)
    public List<Field> fields;

    @ElementList(name = "entry", inline = true, required = false)
    public List<Entry> list;

    public Entry(@Attribute(name="type") String type,
                 @Element(name = "name") String name,
                 @Element(name = "description", required = false) String description,
                 @Element(name = "notes", required = false) String notes,
                 @ElementList(inline = true, required = false) List<Field> fields,
                 @Element(name = "updated") long updated,
                 @ElementList(name = "entry", inline = true, required = false) List<Entry> list) {
        this.type = type;
        this.name = name;
        this.description = description;
        this.notes = notes;
        this.fields = fields;
        this.updated = updated;
        this.list = list;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        isDescriptionUpdated = true;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        isNameUpdated = true;
        this.name = name;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        isNotesUpdated = true;
        this.notes = notes;
    }

    public String getUuid() {
        return uuid;
    }

    public String getUuidName() {
        return uuidName;
    }

    public String getUuidDescription() {
        return uuidDescription;
    }

    public String getUuidNotes() {
        return uuidNotes;
    }

    @Override
    public String toString() {
        return name;
    }
}
