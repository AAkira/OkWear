package jp.android.a.akira.library.okwear;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;

import jp.android.a.akira.library.okwear.listener.SendResultListener;

public interface MessageApiImpl {

    /**
     * send message to all connected devices without listener
     *
     * @param payload
     * @param path     if you don't distinguish messages, you can use default message api path
     */
    public void sendMessageAll(@Nullable final byte[] payload, @Nullable String path);

    /**
     * send message to all connected devices
     *
     * @param payload
     * @param path     if you don't distinguish messages, you can use default message api path
     * @param listener
     */
    public void sendMessageAll(@Nullable final byte[] payload, @Nullable String path,
                               @Nullable final SendResultListener<MessageApi.SendMessageResult> listener);

    /**
     * send message to all connected devices using AsyncTask without listener
     * if you want to call from UI thread
     *
     * @param payload
     * @param path     if you don't distinguish messages, you can use default message api path
     */
    public void sendMessageAllAsync(@Nullable final byte[] payload, @Nullable String path);

    /**
     * send message to all connected devices using AsyncTask
     * if you want to call from UI thread
     *
     * @param payload
     * @param path     if you don't distinguish messages, you can use default message api path
     * @param listener
     */
    public void sendMessageAllAsync(@Nullable final byte[] payload, @Nullable String path,
                                    @Nullable final SendResultListener<MessageApi.SendMessageResult> listener);

    /**
     * send message to specified device without listener
     *
     * @param node
     * @param payload
     * @param path     if you don't distinguish messages, you can use default message api path
     */
    public void sendMessage(@NonNull final Node node, @Nullable final byte[] payload, @Nullable String path);

    /**
     * send message to specified device
     *
     * @param node
     * @param payload
     * @param path     if you don't distinguish messages, you can use default message api path
     * @param listener
     */
    public void sendMessage(@NonNull final Node node, @Nullable final byte[] payload,
                            @Nullable String path, @Nullable final SendResultListener<MessageApi.SendMessageResult> listener);

    /**
     * send message to specified device using AsyncTask
     * if you want to call from UI thread
     *
     * @param node
     * @param payload
     * @param path     if you don't distinguish messages, you can use default message api path
     */
    public void sendMessageAsync(@NonNull final Node node, @Nullable final byte[] payload, @Nullable String path);

    /**
     * send message to specified device using AsyncTask
     * if you want to call from UI thread
     *
     * @param node
     * @param payload
     * @param path     if you don't distinguish messages, you can use default message api path
     * @param listener
     */
    public void sendMessageAsync(@NonNull final Node node, @Nullable final byte[] payload,
                                 @Nullable String path, @Nullable final SendResultListener<MessageApi.SendMessageResult> listener);
}
