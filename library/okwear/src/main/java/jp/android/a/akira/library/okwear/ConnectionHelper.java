package jp.android.a.akira.library.okwear;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.util.ArrayList;
import java.util.List;

import jp.android.a.akira.library.okwear.listener.NodeChangeListener;
import jp.android.a.akira.library.okwear.listener.SendResultListener;
import jp.android.a.akira.library.okwear.task.SendDataTask;
import jp.android.a.akira.library.okwear.task.SendMessageTask;

public class ConnectionHelper implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        DataApi.DataListener, MessageApi.MessageListener, NodeApi.NodeListener {

    private static final String TAG = ConnectionHelper.class.getSimpleName();

    private static ConnectionHelper mConnectinoHelper;

    private GoogleApiClient mGoogleApiClient;
    private CallBackListener mListener;

    public static ConnectionHelper getInstance(final Context context) {
        if (mConnectinoHelper == null) {
            mConnectinoHelper = new ConnectionHelper(context);
        }
        return mConnectinoHelper;
    }

    private ConnectionHelper(final Context context) {
        mGoogleApiClient = buildGoogleApiClient(context);
    }

    public void setListener(final CallBackListener listener) {
        mListener = listener;
    }

    private GoogleApiClient buildGoogleApiClient(final Context context) {
        return new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addApi(Wearable.API)
                .build();
    }

    protected GoogleApiClient getGoogleApiClient() {
        return mGoogleApiClient;
    }

    @Override
    public void onConnected(final Bundle bundle) {
        Log.d(TAG, "onConnected");
        Wearable.DataApi.addListener(mGoogleApiClient, this);
        Wearable.MessageApi.addListener(mGoogleApiClient, this);
        Wearable.NodeApi.addListener(mGoogleApiClient, this);
    }

    @Override
    public void onConnectionSuspended(final int i) {
        Log.d(TAG, "connection suspend");
    }

    @Override
    public void onConnectionFailed(final ConnectionResult connectionResult) {
        Log.d(TAG, "connection failed");
    }

    public void connect() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    public void disconnect() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
            Wearable.DataApi.removeListener(mGoogleApiClient, this);
            Wearable.MessageApi.removeListener(mGoogleApiClient, this);
            Wearable.NodeApi.removeListener(mGoogleApiClient, this);
        }
    }

    @Override
    public void onMessageReceived(final MessageEvent messageEvent) {
        if (mListener != null) {
            mListener.onMessageReceived(messageEvent);
        }
    }

    @Override
    public void onDataChanged(final DataEventBuffer dataEventBuffer) {
        if (mListener != null) {
            mListener.onDataChanged(dataEventBuffer);
        }
    }

    @Override
    public void onPeerConnected(Node node) {
        Log.v(TAG, "peer connect" + node.getId());
    }

    @Override
    public void onPeerDisconnected(Node node) {
        Log.v(TAG, "peer disconnect" + node.getId());
    }

    public void getNodes(@NonNull final NodeChangeListener listener) {
        final List<Node> nodeList = new ArrayList<>();
        final PendingResult<NodeApi.GetConnectedNodesResult> nodes = Wearable.NodeApi.getConnectedNodes(mGoogleApiClient);
        nodes.setResultCallback(new ResultCallback<NodeApi.GetConnectedNodesResult>() {
            @Override
            public void onResult(NodeApi.GetConnectedNodesResult result) {
                for (Node node : result.getNodes()) {
                    nodeList.add(node);
                    listener.onReceiveNodes(nodeList);
                }
            }
        });
    }

    /**
     * send message
     * you don't have to send anything
     *
     * @param node    送信先node
     * @param payload
     * @param path
     * @param listener
     */
    public void sendMessage(@NonNull final Node node, @Nullable final byte[] payload,
                            @NonNull final String path, @Nullable final SendResultListener<MessageApi.SendMessageResult> listener) {
        final PendingResult<MessageApi.SendMessageResult> messageResult =
                Wearable.MessageApi.sendMessage(mGoogleApiClient, node.getId(), path, payload);
        messageResult.setResultCallback(new ResultCallback<MessageApi.SendMessageResult>() {
            @Override
            public void onResult(final MessageApi.SendMessageResult sendMessageResult) {
                Log.d(TAG, "Status: " + sendMessageResult.getStatus());
                if (listener != null) {
                    listener.onResult(sendMessageResult);
                }
            }
        });

    }

    /**
     *
     * send message using AsyncTask
     * you don't have to send anything
     *
     * @param node
     * @param payload
     * @param path
     * @param listener
     */
    public void sendMessageAsync(@NonNull final Node node, @Nullable final byte[] payload,
        @NonNull final String path, @Nullable final SendResultListener<MessageApi.SendMessageResult> listener) {

        if(mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            new SendMessageTask(mGoogleApiClient, node, payload, path, listener).execute();
        }
    }

    /**
     * you need to renew the data if it uses same key
     *
     * @param request
     * @param listener
     */
    public void syncData(@NonNull final PutDataRequest request, @Nullable final SendResultListener<DataApi.DataItemResult> listener) {
        final PendingResult<DataApi.DataItemResult> pendingResult = Wearable.DataApi.putDataItem(mGoogleApiClient, request);
        pendingResult.setResultCallback(new ResultCallback<DataApi.DataItemResult>() {
            @Override
            public void onResult(DataApi.DataItemResult dataItemResult) {
                if (listener != null) {
                    listener.onResult(dataItemResult);
                }
            }
        });
    }

    /**
     * you need to renew the data if it uses same key
     * using AsyncTask
     *
     * @param request
     * @param listener
     */
    public void syncDataAsync(@NonNull final PutDataRequest request, @Nullable final SendResultListener<DataApi.DataItemResult> listener) {
        if(mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            new SendDataTask(mGoogleApiClient, request, listener).execute();
        }
    }

    public interface CallBackListener {

        public void onMessageReceived(final MessageEvent messageEvent);

        public void onDataChanged(final DataEventBuffer dataEventBuffer);
    }
}
