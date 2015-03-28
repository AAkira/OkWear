package jp.android.a.akira.library.okwear.task;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import jp.android.a.akira.library.okwear.listener.SendResultListener;

public class SendDataTask extends AsyncTask<Object, Void, DataApi.DataItemResult> {

    private GoogleApiClient googleApiClient;
    private PutDataRequest request;
    private SendResultListener<DataApi.DataItemResult> listener;

    public SendDataTask(@NonNull final GoogleApiClient googleApiClient,
                        @NonNull final PutDataRequest request,
                        @Nullable final SendResultListener<DataApi.DataItemResult> listener) {

        this.googleApiClient = googleApiClient;
        this.request = request;
        this.listener = listener;
    }

    @Override
    protected DataApi.DataItemResult doInBackground(Object... args) {
        final DataApi.DataItemResult dataResult = Wearable.DataApi.putDataItem(googleApiClient, request).await();

        return dataResult;
    }

    @Override
    protected void onPostExecute(final DataApi.DataItemResult result) {
        if (listener != null) {
            listener.onResult(result);
        }
        return;
    }
}
