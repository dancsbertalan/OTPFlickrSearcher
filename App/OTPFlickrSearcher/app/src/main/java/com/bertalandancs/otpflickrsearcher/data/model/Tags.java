package com.bertalandancs.otpflickrsearcher.data.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

@Root(name = "tags", strict = false)
public class Tags {
    @ElementList(name = "tag", inline = true, required = false)
    public List<Tag> tagList;
}
