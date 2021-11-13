package com.bertalandancs.otpflickrsearcher.data.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(name = "err", strict = false)
public class Err {
    @Attribute(name = "code")
    public int code;

    @Attribute(name = "msg")
    public String msg;
}
