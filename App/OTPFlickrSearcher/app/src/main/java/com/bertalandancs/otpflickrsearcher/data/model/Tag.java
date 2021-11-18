package com.bertalandancs.otpflickrsearcher.data.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "tag", strict = false)
public class Tag {
    @Attribute(name = "raw", required = false)
    public String raw;

    @Element(name = "text", required = false)
    public String text;
}
