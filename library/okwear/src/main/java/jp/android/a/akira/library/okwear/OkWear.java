package jp.android.a.akira.library.okwear;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;

import java.util.List;

import jp.android.a.akira.library.okwear.listener.NodeChangeListener;
import jp.android.a.akira.library.okwear.listener.WearReceiveListener;
import jp.android.a.akira.library.okwear.listener.SendResultListener;
import jp.android.a.akira.library.okwear.util.AssetUtil;
import jp.android.a.akira.library.okwear.util.ParseByteArray;
import jp.android.a.akira.library.okwear.util.Payload;

public class OkWear extends OkWearBase implements MessageApiImpl, DataApiImpl {
    private static final String TAG = OkWear.class.getSimpleName();

    public static final String DEFAULT_MESSAGE_API_PATH = "/path/message/api";
    public static final String DEFAULT_DATA_API_PATH = "/path/data/api";
    public static final String DEFAULT_DATA_API_KEY = "key_data_api";

    public OkWear(final Context context) {
        super(context);
    }

    public OkWear(final Context context, final WearReceiveListener listener) {
        super(context, listener);
    }

    public void sendMessageAll(@Nullable final String payload, @Nullable final String path) {
        sendMessageAll(ParseByteArray.fromString(payload), path, null);
    }

    public void sendMessageAll(@Nullable final int payload, @Nullable final String path) {
        sendMessageAll(ParseByteArray.fromInt(payload), path, null);
    }

    public void sendMessageAll(@Nullable final double payload, @Nullable final String path) {
        sendMessageAll(ParseByteArray.fromDouble(payload), path, null);
    }

    @Override
    public void sendMessageAll(@Nullable final byte[] payload, @Nullable final String path) {
        sendMessageAll(payload, path, null);
    }

    @Override
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

    @Override
    public void sendMessageAllAsync(@Nullable final byte[] payload, @Nullable final String path) {
        sendMessageAllAsync(payload, path, null);
    }

    @Override
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

    @Override
    public void sendMessage(@NonNull final Node node, @Nullable final byte[] payload, @Nullable final String path) {
        sendMessage(node, payload, path, null);
    }

    @Override
    public void sendMessage(@NonNull final Node node, @Nullable final byte[] payload,
                            @Nullable String path, @Nullable final SendResultListener<MessageApi.SendMessageResult> listener) {
        if (path == null) {
            path = DEFAULT_MESSAGE_API_PATH;
        }
        mHelper.sendMessage(node, payload, path, listener);
    }

    @Override
    public void sendMessageAsync(@NonNull final Node node, @Nullable final byte[] payload, @Nullable final String path) {
        sendMessageAsync(node, payload, path, null);
    }

    @Override
    public void sendMessageAsync(@NonNull final Node node, @Nullable final byte[] payload,
                                 @Nullable String path, @Nullable final SendResultListener<MessageApi.SendMessageResult> listener) {
        if (path == null) {
            path = DEFAULT_MESSAGE_API_PATH;
        }
        mHelper.sendMessageAsync(node, payload, path, listener);
    }

    public void syncData(@NonNull final String path, @NonNull final String key, @NonNull final String data) {
        final Payload payload =
                new Payload.Builder(path)
                        .addPayload(key, data)
                        .build();
        syncData(payload, null);
    }

    public void syncData(@NonNull final String path, @NonNull final String key, @NonNull final int data) {
        final Payload payload =
                new Payload.Builder(path)
                        .addPayload(key, data)
                        .build();
        syncData(payload, null);
    }

    public void syncData(@NonNull final String path, @NonNull final String key, @NonNull final double data) {
        final Payload payload =
                new Payload.Builder(path)
                        .addPayload(key, data)
                        .build();
        syncData(payload, null);
    }

    @Override
    public void syncData(@NonNull final Payload payload) {
        syncData(payload, null);
    }

    @Override
    public void syncData(@NonNull final Payload payload, @Nullable final SendResultListener<DataApi.DataItemResult> listener) {
        mHelper.syncData(payload.getPutDataRequest(), listener);
    }

    @Override
    public void syncDataAsync(@NonNull final Payload payload) {
        syncDataAsync(payload, null);
    }

    @Override
    public void syncDataAsync(@NonNull final Payload payload, @Nullable final SendResultListener<DataApi.DataItemResult> listener) {
        mHelper.syncDataAsync(payload.getPutDataRequest(), listener);
    }

    /**
     * load bitmap from asset
     *
     * @param asset
     * @return
     */
    public Bitmap loadBitmap(@NonNull final Asset asset) {
        return AssetUtil.loadBitmapFromAsset(asset, mHelper.getGoogleApiClient());
    }

}
