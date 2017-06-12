package com.xiang.chat.mvp.presenter;


import android.util.Log;
import com.xiang.chat.mvp.model.ChatAdapter;
import com.xiang.chat.NetWork.MyClient;
import com.xiang.chat.NetWork.SocketConnector;
import com.xiang.chat.entities.Message;
import com.xiang.chat.mvp.view.ChatView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by xiang on 2017/4/13.
 *
 */

public class ChatPresenter extends BasePresenter<ChatView> {
    private boolean isKeepReading = true;
    private PrintWriter out;
    private BufferedReader inFromServer = null;
    private String name;
    public ChatAdapter adapter;
    private List<Message> messageList;
    private MyClient client;

    public ChatPresenter() {
        messageList = new ArrayList<>();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        adapter = new ChatAdapter(messageList);
        view.initProgressDialog();
        view.showIpInputer();
        view.initRecycerView();
    }

    public void setName(String userName) {
        this.name = userName;
    }

    public void getOutputStream(String ip){
        view.showProgress();
        SocketConnector.getSocket(ip).subscribe(new Observer<MyClient>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(MyClient myClient) {
                client = myClient;
                try {
                    inFromServer = new BufferedReader(new InputStreamReader(myClient.getInputStream()));
                    out = new PrintWriter(myClient.getOutputStream(), true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                view.hideProgress();
                view.showNameInputer();
                receice();
            }

            @Override
            public void onError(Throwable e) {
                view.showToast("连接服务器失败");
                view.finishActivity();
            }

            @Override
            public void onComplete() {

            }
        });
    }

    public void send(final String content) {
        if (content.length()>0) {
            if (content.equals("bye")) {
                isKeepReading = false;
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    out.println(content);
                    out.flush();
                    view.refreshEditText();
                }
            }).start();
        } else {
            if ( out != null) {
                view.showToast("请输入内容");
            } else {
                view.showToast("与服务器断开连接");
            }
        }
    }

    private void receice() {
        new Thread(new ClientThread()).start();
    }

    private class ClientThread implements Runnable {
        @Override
        public void run() {
            String receivedString = "";
            try {
                while (isKeepReading) {
                    receivedString = inFromServer.readLine();
                    String receivedName = receivedString.substring(0, receivedString.indexOf(":"));
                    String content = receivedString.substring(receivedString.indexOf(':')+1);
                    if (receivedName.equals(name)) {
                        messageList.add(new Message(receivedName, content, Message.TYPE_SENT));
                        Log.d("type", "sent");
                    } else {
                        Log.d("type", "received");
                        messageList.add(new Message(receivedName, content, Message.TYPE_RECEIVED));
                    }
                    Log.d("num", messageList.size()+"");
                    view.notifyRecyclerView();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
