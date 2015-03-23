package com.github.marmaladesky.data;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.util.List;

@Root
public class Entry implements Serializable {

    @Attribute(name="type")
    public String type;

    @Element(name = "name")
    private String name;
    private boolean isNameUpdated;

    @Element(name = "description", required = false)
    private String description;
    private boolean isDescriptionUpdated;

    @Element(name = "updated")
    public long updated;

    @Element(name = "notes", required = false)
    private String notes;
    private boolean isNotesUpdated;

    @ElementList(inline = true, required = false)
    public List<Field> fields;

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

    @Override
    public String toString() {
        return name;
    }
}
