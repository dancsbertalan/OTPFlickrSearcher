package com.bertalandancs.otpflickrsearcher.data.model;


import androidx.annotation.Nullable;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(name = "photo", strict = false)
public class SearchPhoto {
    @Attribute(name = "id")
    public long id;

    @Nullable
    @Attribute(name = "url_n", required = false)
    public String urlN;

    @Attribute(name = "url_t", required = false)
    public String urlT;

}