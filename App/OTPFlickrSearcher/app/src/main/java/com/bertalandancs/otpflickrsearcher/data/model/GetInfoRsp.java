package com.bertalandancs.otpflickrsearcher.data.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "rsp", strict = false)
public class GetInfoRsp {
    @Element(name = "photo", required = false)
    public GetInfoPhoto photo;

    @Attribute(name = "stat", required = false)
    public String stat;

    @Element(name = "err", required = false)
    public Err error;
}
