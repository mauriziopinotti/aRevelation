package com.github.marmaladesky.data;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

import java.io.Serializable;

@Root
public class Field implements Serializable {

    @Attribute(name="id")
    public String id;

    @Text(required = false)
    private String value;

    private boolean isUpdated;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        isUpdated = true;
        this.value = value;
    }
}
