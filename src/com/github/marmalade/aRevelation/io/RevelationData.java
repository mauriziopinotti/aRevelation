package com.github.marmalade.aRevelation.io;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by sviro on 11/25/13.
 */
@Root(name = "revelationdata", strict = false)
public class RevelationData {

    @ElementList(inline = true)
    List<Entry> entries;

    public RevelationData() {

    }

    public List<Entry> getEntries() {
        return entries;
    }
}
