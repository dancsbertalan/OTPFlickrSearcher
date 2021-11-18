package com.bertalandancs.otpflickrsearcher.data.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(name = "dates", strict = false)
public class Dates {
    @Attribute(name = "posted", required = false)
    public int postedEpoch;
    @Attribute(name = "lastupdate", required = false)
    public int lastUpdateEpoch;
}
