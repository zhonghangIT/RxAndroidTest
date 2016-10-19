package com.education.rxandroidtest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Action2;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    Subscription sub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sub = Observable.from(new String[]{"张三", "李四", "王五", "赵六"})
                .subscribeOn(Schedulers.io())//更改线程A,不加该属性,监听器默认在创建的线程中
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<String, Observable<List<String>>>() {

                    @Override
                    public Observable<List<String>> call(final String s) {

                        return Observable.create(new Observable.OnSubscribe<List<String>>() {
                            @Override
                            public void call(Subscriber<? super List<String>> subscriber) {
                                //传递List
                                if (s.equals("zhangsan")) {
                                    List<String> yuwen=Arrays.asList(new String[]{"1","2","3"});
                                    subscriber.onNext(yuwen);
                                    List<String> suxue=Arrays.asList(new String[]{"1","2","3"});
                                    subscriber.onNext(yuwen);
                                    List<String> wuli=Arrays.asList(new String[]{"1","2","3"});
                                    subscriber.onNext(yuwen);
                                }

                            }
                        });
                    }
                }).subscribe(new Subscriber<List<String>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<String> strings) {

                    }
                });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sub.unsubscribe();
    }

    private void firstRxJava() {
        //使用RxJava,这里按照观察者设计模式的方式,先创建观察者
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onCompleted() {
                //监听完成
                Log.d("MainActivity", "事件完成");
            }

            @Override
            public void onError(Throwable e) {
                //报错
                e.printStackTrace();
            }

            @Override
            public void onNext(String s) {
                //监听器传递的数据
                Log.d("MainActivity", "接受到数据" + s);
            }
        };
        //创建被观察者 和 监听器
        Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("第一次触发事件");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                subscriber.onNext("第二次触发事件");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
//                subscriber.onError(new NullPointerException());
                subscriber.onNext("第三次触发事件");

                subscriber.onCompleted();

            }
        });
        //绑定观察者和被观察者,为了代码的链式调用,这里是被观察者绑定观察者
        observable.subscribe(observer);
    }

}
