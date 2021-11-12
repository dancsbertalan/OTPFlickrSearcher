package com.bertalandancs.otpflickrsearcher.data.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name = "photo")
class Photo {
    @Attribute(name = "id")
    public double id;

    @Attribute(name = "owner")
    public String owner;

    @Attribute(name = "secret")
    public String secret;

    @Attribute(name = "server")
    public int server;

    @Attribute(name = "farm")
    public int farm;

    @Attribute(name = "title")
    public String title;

    @Attribute(name = "ispublic")
    public int isPublic;

    @Attribute(name = "isfriend")
    public int isFriend;

    @Attribute(name = "isfamily")
    public int isFamily;
}

@Root(name = "photos")
class Photos {
    @ElementList(entry = "photo",inline = true)
    public List<Photo> photo;

    @Attribute(name = "page")
    public int page;

    @Attribute(name = "pages")
    public int pages;

    @Attribute(name = "perpage")
    public int perPage;

    @Attribute(name = "total")
    public int total;
}

@Root(name = "rsp")
public class Rsp {
    @Element(name = "photos")
    public Photos photos;

    @Attribute(name = "stat")
    public String stat;
}