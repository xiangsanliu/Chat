package com.xiang.chat.mvp.view;


/**
 * Created by xiang on 2017/4/13.
 *
 */

public interface ChatView extends BaseView {
    void refreshEditText();
    void showToast(String content);
    void finishActivity();
    void initRecycerView();
    void notifyRecyclerView();
    void showNameInputer();
    void showIpInputer();
    void initProgressDialog();
}
