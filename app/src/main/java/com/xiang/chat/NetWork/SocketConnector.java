package com.xiang.chat.NetWork;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by xiang on 2017/4/13.
 *
 */

public class SocketConnector {
    public static Observable<MyClient> getSocket(final String ip) {
        return Observable.create(new ObservableOnSubscribe<MyClient>() {
            @Override
            public void subscribe(ObservableEmitter<MyClient> e) throws Exception {
                e.onNext(new MyClient(ip, 4444));
                e.onComplete();
            }
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }
}
