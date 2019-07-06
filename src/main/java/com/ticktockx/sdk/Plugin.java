package com.ticktockx.sdk;

public interface Plugin {
    String author();
    String Version();
    String name();

    void onLoad(API api);

    void onMessageHandler(QQMessage message);
}

