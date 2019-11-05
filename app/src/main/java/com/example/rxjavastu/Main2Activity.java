package com.example.rxjavastu;

import android.os.Build;
import android.os.StrictMode;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Main2Activity extends AppCompatActivity {

    private static final String TAG = "RxJava";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        if (Build.VERSION.SDK_INT > 9){
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        Observable.interval(2,1, TimeUnit.SECONDS)
                .doOnNext(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        Log.d(TAG,"第 " + aLong + " 次轮训");
                        Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("https://www.lloff.cn")
                            .addConverterFactory(GsonConverterFactory.create())
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            .build();

                        FlowerController flowerController = retrofit.create(FlowerController.class);
                        Observable<List<Flower>> observable = flowerController.orders(0);
                        //Call<List<Flower>> call = flowerController.orders(0);
                        observable.subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Observer<List<Flower>>() {
                                    @Override
                                    public void onSubscribe(Disposable d) {

                                    }

                                    @Override
                                    public void onNext(List<Flower> value) {
                                        for (Flower flower:value){
                                            System.out.println(flower);
                                        }
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        Log.d(TAG,"请求失败");
                                    }

                                    @Override
                                    public void onComplete() {

                                    }
                                });


                    }
                }).subscribe(new Observer<Long>() {
                    private Disposable disposable;
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(Long value) {
                        Log.d(TAG,"对" + value + "事件作出响应");
                        if (value > 3)
                            disposable.dispose();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG,"对Error事件作出响应");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG,"对Complete事件作出响应");
                    }
        });

//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("https://www.lloff.cn")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        FlowerController flowerController = retrofit.create(FlowerController.class);
//
//        Call<List<Flower>> call = flowerController.orders(0);
//
//        try {
//            List<Flower> list = call.execute().body();
//            for (Flower flower : list){
//                System.out.println(flower);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }

    public interface FlowerController{
        @GET("/flower/order/getByType")
        Observable<List<Flower>> orders(
                @Query("type") int type
        );
    }
}
