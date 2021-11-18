package com.bertalandancs.otpflickrsearcher.data.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(name = "visibility", strict = false)
public class Visibility {
    @Attribute(name = "ispublic", required = false)
    public int isPublic;
    @Attribute(name = "isfriend", required = false)
    public int isFriend;
    @Attribute(name = "isfamily", required = false)
    public int isFamily;
}
