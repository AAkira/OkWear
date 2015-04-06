package jp.android.a.akira.library.okwear.task;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;

import jp.android.a.akira.library.okwear.listener.SendResultListener;

public class SendMessageTask extends AsyncTask<Object, Void, MessageApi.SendMessageResult> {

    private GoogleApiClient googleApiClient;
    private String id;
    private String path;
    private byte[] payload;
    private SendResultListener<MessageApi.SendMessageResult> listener;

    public SendMessageTask(@NonNull final GoogleApiClient googleApiClient, @NonNull final Node node,
                           @Nullable final byte[] payload, @NonNull final String path,
                           @Nullable final SendResultListener<MessageApi.SendMessageResult> listener) {

        this.googleApiClient = googleApiClient;
        this.id = node.getId();
        this.payload = payload;
        this.path = path;
        this.listener = listener;
    }

    @Override
    protected MessageApi.SendMessageResult doInBackground(Object... args) {

        final MessageApi.SendMessageResult messageResult =
                Wearable.MessageApi.sendMessage(googleApiClient, id, path, payload).await();

        return messageResult;
    }

    @Override
    protected void onPostExecute(final MessageApi.SendMessageResult result) {
        if (listener != null) {
            listener.onResult(result);
        }
        return;
    }
}
