package com.bertalandancs.otpflickrsearcher.data.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name = "photos", strict = false)
public class Photos {
    @ElementList(entry = "photo", inline = true, required = false)
    public List<Photo> photoList;

    @Attribute(name = "page")
    public int page;

    @Attribute(name = "pages")
    public int pages;

    @Attribute(name = "perpage")
    public int perPage;

    @Attribute(name = "total")
    public int total;
}
