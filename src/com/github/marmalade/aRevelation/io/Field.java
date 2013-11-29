package com.github.marmalade.aRevelation.io;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Text;

/**
 * Created by sviro on 11/26/13.
 */
public class Field {
    @Attribute(name = "id")
    String fieldName;

    @Text(required = false)
    String value;

    public Field() {

    }

    public String getFieldName() {
        return fieldName;
    }

    public String getValue() {
        return value;
    }
}
