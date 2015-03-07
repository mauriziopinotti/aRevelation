package com.github.marmaladesky.data;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root
public class RevelationData {

    @Attribute(name="version")
    private String version;

    @Attribute(name="dataversion")
    private String dataversion;

    @ElementList(inline = true)
    public List<Entry> list;
}
