package jp.android.a.akira.library.okwear;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;

import java.util.List;

import jp.android.a.akira.library.okwear.listener.NodeChangeListener;
import jp.android.a.akira.library.okwear.listener.ReceiveListener;
import jp.android.a.akira.library.okwear.listener.SendResultListener;
import jp.android.a.akira.library.okwear.util.AssetUtil;
import jp.android.a.akira.library.okwear.util.Payload;
import jp.android.a.akira.library.okwear.util.Util;

public class OkWear implements ConnectionHelper.CallBackListener {
    private static final String TAG = OkWear.class.getSimpleName();

    public static final String DEFAULT_MESSAGE_API_PATH = "/path/message/api";
    public static final String DEFAULT_DATA_API_PATH = "/path/data/api";
    public static final String DEFAULT_DATA_API_KEY = "key_data_api";

    private Context mContext;
    private ConnectionHelper mHelper;
    private ReceiveListener mListener;

    public OkWear(final Context context) {
        init(context);
    }

    public OkWear(final Context context, final ReceiveListener listener) {
        init(context);
        mListener = listener;
    }

    private void init(final Context context) {
        mContext = context;
        mHelper = ConnectionHelper.getInstance(context);
        mHelper.setListener(this);
    }

    /**
     * if you want to receive message or data
     *
     * @param listener
     */
    public void registReceiver(final ReceiveListener listener) {
        mListener = listener;
    }

    //TODO readme

    /**
     * you should call this method at onResume.
     * if you don't call this method, you cannot use this api.
     */
    public void connect() {
        mHelper.connect();
    }

    /**
     * you should call this method at onResume.
     * if you don't call this method, this listener keeps running.
     */
    public void disconnect() {
        mHelper.disconnect();
    }

    public void getNodeList(@NonNull final NodeChangeListener listener) {
        mHelper.getNodes(listener);
    }

    /**
     * send message to all connected devices
     *
     * @param payload
     * @param path    if you don't distinguish messages, you can use default message api path
     * @param listener
     */
    public void sendMessageAll(@Nullable final byte[] payload, @Nullable String path,
                               @Nullable final SendResultListener<MessageApi.SendMessageResult> listener) {
        if (path == null) {
            path = DEFAULT_MESSAGE_API_PATH;
        }
        final String finalPath = path;
        mHelper.getNodes(new NodeChangeListener() {
            @Override
            public void onReceiveNodes(List<Node> nodes) {
                for (Node node : nodes) {
                    sendMessage(node, payload, finalPath, listener);
                }
            }
        });
    }

    /**
     * send message to all connected devices using AsyncTask
     * if you want to call from UI thread
     *
     * @param payload
     * @param path    if you don't distinguish messages, you can use default message api path
     * @param listener
     */
    public void sendMessageAllAsync(@Nullable final byte[] payload, @Nullable String path,
                               @Nullable final SendResultListener<MessageApi.SendMessageResult> listener) {
        if (path == null) {
            path = DEFAULT_MESSAGE_API_PATH;
        }
        final String finalPath = path;
        mHelper.getNodes(new NodeChangeListener() {
            @Override
            public void onReceiveNodes(List<Node> nodes) {
                for (Node node : nodes) {
                    sendMessageAsync(node, payload, finalPath, listener);
                }
            }
        });

    }

    /**
     * send message to specified device
     *
     * @param node
     * @param payload
     * @param path    if you don't distinguish messages, you can use default message api path
     * @param listener
     */
    public void sendMessage(@NonNull final Node node, @Nullable final byte[] payload,
                            @Nullable String path, @Nullable final SendResultListener<MessageApi.SendMessageResult> listener) {
        if (path == null) {
            path = DEFAULT_MESSAGE_API_PATH;
        }
        mHelper.sendMessage(node, payload, path, listener);
    }

    /**
     * send message to specified device using AsyncTask
     * if you want to call from UI thread
     *
     * @param node
     * @param payload
     * @param path    if you don't distinguish messages, you can use default message api path
     * @param listener
     */
    public void sendMessageAsync(@NonNull final Node node, @Nullable final byte[] payload,
                            @Nullable String path, @Nullable final SendResultListener<MessageApi.SendMessageResult> listener) {
        if (path == null) {
            path = DEFAULT_MESSAGE_API_PATH;
        }
        mHelper.sendMessageAsync(node, payload, path, listener);
    }

    /**
     * sync data
     *
     * @param payload
     * @param listener
     */
    public void syncData(@NonNull final Payload payload, @Nullable final SendResultListener<DataApi.DataItemResult> listener) {
        mHelper.syncData(payload.getPutDataRequest(), listener);
    }

    /**
     * sync data using AsyncTask
     * if you want to call from UI thread
     *
     * @param payload
     * @param listener
     */
    public void syncDataAsync(@NonNull final Payload payload, @Nullable final SendResultListener<DataApi.DataItemResult> listener) {
        mHelper.syncDataAsync(payload.getPutDataRequest(), listener);
    }

    /**
     * load bitmap from asset
     * @param asset
     * @return
     */
    public Bitmap loadBitmap(@NonNull Asset asset) {
        return AssetUtil.loadBitmapFromAsset(asset, mHelper.getGoogleApiClient());
    }

    @Override
    public void onMessageReceived(final MessageEvent messageEvent) {
        Util.vibrate(mContext, 50);
        // call from only UI Thread
//        Util.showToast(mContext, "Message:" + messageEvent.getPath());

        if (messageEvent.getPath().equals(DEFAULT_MESSAGE_API_PATH)) {
            final String messagePayload = new String(messageEvent.getData());
            Log.v(TAG, messagePayload);
        }

        if (mListener != null) {
            mListener.onReceiveMessage(messageEvent);
        }
    }

    @Override
    public void onDataChanged(final DataEventBuffer dataEvents) {
        Util.vibrate(mContext, 50);
        // call from only UI Thread
//        Util.showToast(mContext, "get data");

        Log.v(TAG, "receive data api");

        for (DataEvent event : dataEvents) {
            if (event.getType() == DataEvent.TYPE_DELETED) {
                Log.d(TAG, "DataItem deleted: " + event.getDataItem().getUri());

            } else if (event.getType() == DataEvent.TYPE_CHANGED) {
                Log.d(TAG, "DataItem changed: " + event.getDataItem().getUri());
                DataMap dataMap = DataMap.fromByteArray(event.getDataItem().getData());
//                final DataMap dataMap = DataMapItem.fromDataItem(event.getDataItem()).getDataMap();
                final int data = dataMap.getInt("key1");
                final String data2 = dataMap.getString("key2");
                Log.v(TAG, "data: " + data);
                Log.v(TAG, "data2: " + data2);
            }
        }

        if (mListener != null) {
            mListener.onReceiveDataApi(dataEvents);
        }
    }

}
