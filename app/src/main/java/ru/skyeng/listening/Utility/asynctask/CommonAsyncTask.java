package ru.skyeng.listening.Utility.asynctask;

import android.os.AsyncTask;

/**
 * ---------------------------------------------------
 * Created by Sermilion on 18/02/2017.
 * Project: Listening
 * ---------------------------------------------------
 * <a href="http://www.skyeng.ru">www.skyeng.ru</a>
 * <a href="http://www.github.com/sermilion>github</a>
 * ---------------------------------------------------
 */

public class CommonAsyncTask<A, B, C> extends AsyncTask<A, B, C> {

    private Producer<A,C> mDoInBackground;
    private Consumer<C> mConsumer;

    @SafeVarargs
    @Override
    protected final C doInBackground(A... params) {
        A param = null;
        if(params.length>0){
            param = params[0];
        }
        mDoInBackground.produceFrom(param);
        return null;
    }

    @Override
    protected void onPostExecute(C c) {
        super.onPostExecute(c);
        mConsumer.consume(c);
    }

    public void setDoInBackground(Producer<A, C> mDoInBackground) {
        this.mDoInBackground = mDoInBackground;
    }

    public void setConsumer(Consumer<C> mConsumer) {
        this.mConsumer = mConsumer;
    }
}
