package com.danny.whomi;

import java.util.concurrent.TimeUnit;

import rx.Observable;

public class PlaygroundRx {

    public static void  main(String[] args) throws InterruptedException {


        final Observable<Long> share = Observable.interval(1, TimeUnit.SECONDS)
                                                 .share();

        System.out.println("After");

        Thread.sleep(1000);

        share.subscribe(System.out::println);    

        while (true);

    }

}
