package com.xiang.chat.entities;

/**
 * Created by xiang on 2017/4/15.
 *
 */

public class Message {
    public static final int TYPE_RECEIVED = 0;
    public static final int TYPE_SENT = 1;
    private String name;

    public Message(String name, String content, int type) {
        this.name = name;
        this.content = content;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String content;
    private int type;

    public String getContent() {
        return content;
    }

    public int getType() {
        return type;
    }

    public Message(String content, int type) {
        this.content = content;
        this.type = type;

    }
}
