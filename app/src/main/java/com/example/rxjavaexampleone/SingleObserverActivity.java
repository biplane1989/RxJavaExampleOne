package com.example.rxjavaexampleone;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleObserver;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SingleObserverActivity extends AppCompatActivity {

    private static final String TAG = SingleObserverActivity.class.getSimpleName();
    private Disposable disposable;

    /**
     * Single Observable emitting single Note
     * Single Observable is more useful in making network calls
     * where you expect a single response object to be emitted
     * -
     * Single : SingleObserver
     *
     * Single luôn phát ra chỉ một giá trị hoặc một lỗi. Observable có thể thực hiện được công việc này nhưng Single
     * luôn luôn đảm bảo rằng luôn luôn có 1 phần tử được trả về. Chính vì chỉ có 1 phần tử nên SingleObserver không
     * có onNext() mà chỉ có onSuccess() để nhận dữ liệu trả về.
     */

    // TODO - link to Retrofit  tutorial
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_observer);

        Single<Note> noteObservable = getNoteObservable();

        SingleObserver<Note> singleObserver = getSingleObserver();

        noteObservable
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(singleObserver);

    }

    private SingleObserver<Note> getSingleObserver() {
        return new SingleObserver<Note>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe");
                disposable = d;
            }

            @Override
            public void onSuccess(Note note) {
                Log.e(TAG, "onSuccess: " + note.getNote());
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: " + e.getMessage());
            }
        };
    }

    private Single<Note> getNoteObservable() {
        return Single.create(new SingleOnSubscribe<Note>() {
            @Override
            public void subscribe(SingleEmitter<Note> emitter) throws Exception {
                Note note = new Note(1, "Buy milk!");
                emitter.onSuccess(note);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
}
