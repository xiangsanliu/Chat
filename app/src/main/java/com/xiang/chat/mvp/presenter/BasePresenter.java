package com.xiang.chat.mvp.presenter;

import com.xiang.chat.mvp.view.BaseView;

/**
 * Created by xiang on 2017/4/13.
 *
 */

public abstract class BasePresenter <T extends BaseView> {
    protected T view;

    public void attachView(T view) {
        this.view = view;
    }

    public void onCreate() {

    }
}
