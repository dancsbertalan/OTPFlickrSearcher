package com.bertalandancs.otpflickrsearcher.data.model;


import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(name = "photo", strict = false)
public class Photo {
    @Attribute(name = "id")
    public long id;

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

    @Attribute(name = "url_n", required = false)
    public String urlN;

    @Attribute(name = "url_t", required = false)
    public String urlT;

    @Attribute(name = "url_o", required = false)
    public String urlO;
}