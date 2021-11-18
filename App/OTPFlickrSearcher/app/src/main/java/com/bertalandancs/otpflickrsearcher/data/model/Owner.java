package com.bertalandancs.otpflickrsearcher.data.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(name = "owner", strict = false)
public class Owner {
    @Attribute(name = "username", required = false)
    public String username;
    @Attribute(name = "location", required = false)
    public String location;
}
