package com.github.marmaladesky.data;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;
import java.util.UUID;

@Root
public class RevelationData {

    private final String uuid = UUID.randomUUID().toString();

    @Attribute(name="version")
    private String version;

    @Attribute(name="dataversion")
    private String dataversion;

    @ElementList(inline = true)
    public List<Entry> list;

    public RevelationData(@Attribute(name="version") String version,
                          @Attribute(name="dataversion") String dataversion,
                          @ElementList(inline = true) List<Entry> list) {
        this.version = version;
        this.dataversion = dataversion;
        this.list = list;
    }

    public Entry getEntryById(String uuid) {
        return getEntryById(list, uuid);
    }

    private static Entry getEntryById(List<Entry> list, String uuid) {
        for(Entry e : list) {
            if(e.type.equals("folder")) {
                Entry n = getEntryById(e.list, uuid);
                if(n != null)
                    return n;
            }
            if(e.getUuid().equals(uuid))
                return e;
        }
        return null;
    }

    public FieldWrapper getFieldById(String uuid) throws Exception {
        for(Entry e : list) {
            if(e.fields != null) {
                for (Field f : e.fields) {
                        if (e.getUuidName().equals(uuid)) {
                            return new FieldWrapper("name", e);
                        } else if (e.getUuidDescription().equals(uuid)) {
                            return new FieldWrapper("description", e);
                        } else if (e.getUuidNotes().equals(uuid)) {
                            return new FieldWrapper("notes", e);
                        } else if (f != null && f.getUuid().equals(uuid))
                        return new FieldWrapper(f);
                }
            } else {
                if (e.getUuidName().equals(uuid)) {
                    return new FieldWrapper("name", e);
                } else if (e.getUuidDescription().equals(uuid)) {
                    return new FieldWrapper("description", e);
                } else if (e.getUuidNotes().equals(uuid)) {
                    return new FieldWrapper("notes", e);
                }
            }
        }
        throw new Exception("Cannot find field with id=" + uuid);
    }

    public List<Entry> getEntryGroupById(String uuid) {
        if(this.uuid.equals(uuid))
            return list;
        else
            return getEntryGroupById(list, uuid);
    }

    private List<Entry> getEntryGroupById(List<Entry> entries, String uuid) {
        for(Entry e : entries) {
            if(e.type.equals("folder")) {
                if(e.getUuid().equals(uuid)) {
                    return e.list;
                } else {
                    getEntryGroupById(e.list, uuid);
                }
            }
        }
        return null;
    }

    public String getUuid() {
        return uuid;
    }
}
