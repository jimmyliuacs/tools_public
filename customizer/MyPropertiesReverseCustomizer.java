package com.nationsky.cms.domain;

import org.apache.openjpa.jdbc.meta.ClassMapping;
import org.apache.openjpa.jdbc.meta.FieldMapping;
import org.apache.openjpa.jdbc.meta.PropertiesReverseCustomizer;

public class MyPropertiesReverseCustomizer extends PropertiesReverseCustomizer {

    @Override
    public void customize(ClassMapping cls) {
        super.customize(cls);
    }

    @Override
    public String getFieldCode(FieldMapping field) {
        return "";
    }
}
