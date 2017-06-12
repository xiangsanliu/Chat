package com.xiang.chat.NetWork;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;

/**
 * Created by xiang on 2017/4/12.
 *
 */

public class MyClient extends Socket implements Serializable {
    MyClient(String host, int port) throws IOException {
        super(host, port);
    }
}
