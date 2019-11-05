package com.example.rxjavastu;

import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.*;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        Retrofit retrofit = new Retrofit.Builder()
                                    .baseUrl("https://fy.iciba.com/")
                                    .addConverterFactory(GsonConverterFactory.create())
                                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                                    .build();

        TranslateController controller = retrofit.create(TranslateController.class);

        Observable<Translate> observable = controller.translate("hello world");

        observable.subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe(new Observer<Translate>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG,"subscribe");
                    }

                    @Override
                    public void onNext(Translate value) {
                        Log.d(TAG, String.valueOf(value));
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG,"error:" + e);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG,"complete");
                    }
                });
    }

    public interface TranslateController{
        @GET("ajax.php?a=fy&f=auto&t=auto")
        Observable<Translate>translate(@Query("w") String word);
    }
}
