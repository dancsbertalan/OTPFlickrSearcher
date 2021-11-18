package com.bertalandancs.otpflickrsearcher.data.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "photo", strict = false)
public class GetInfoPhoto {
    @Element(name = "owner", required = false)
    public Owner owner;

    @Element(name = "title", required = false)
    public String title;

    @Nullable
    @Element(name = "description", required = false)
    public String description;

    @Element(name = "visibility", required = false)
    public Visibility visibility;

    @Element(name = "dates", required = false)
    public Dates dates;

    @Element(name = "comments", required = false)
    public int commentCount;

    @Element(name = "tags", required = false)
    public Tags tags;

    @Attribute(name = "views", required = false)
    public int viewsCount;

    @Attribute(name = "secret", required = false)
    public String secret;

    @Attribute(name = "originalsecret", required = false)
    public String originalSecret;

    @Attribute(name = "server", required = false)
    public int server;

    @Attribute(name = "id", required = false)
    public long id;

    @NonNull
    @Override
    public String toString() {
        return "GetInfoPhoto{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                '}';
    }
}
