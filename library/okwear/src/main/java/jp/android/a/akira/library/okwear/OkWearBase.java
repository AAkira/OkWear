package jp.android.a.akira.library.okwear;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.MessageEvent;

import jp.android.a.akira.library.okwear.listener.NodeChangeListener;
import jp.android.a.akira.library.okwear.listener.WearReceiveListener;
import jp.android.a.akira.library.okwear.util.Util;

public class OkWearBase implements ConnectionHelper.CallBackListener {
    private static final String TAG = OkWearBase.class.getSimpleName();

    protected Context mContext;
    protected ConnectionHelper mHelper;
    protected WearReceiveListener mListener;

    public OkWearBase(final Context context) {
        mContext = context;
        mHelper = ConnectionHelper.getInstance(context);
        mHelper.setListener(this);
    }

    public OkWearBase(final Context context, final WearReceiveListener listener) {
        this(context);
        mListener = listener;
    }

    /**
     * if you want to receive message or data
     *
     * @param listener
     */
    public void registReceiver(final WearReceiveListener listener) {
        mListener = listener;
    }

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

    @Override
    public void onMessageReceived(final MessageEvent messageEvent) {
        if(BuildConfig.DEBUG) {
            Util.vibrate(mContext, 50);
            Log.v(TAG, "call onMessageReceived");
        }

        if (mListener != null) {
            mListener.onReceiveMessage(messageEvent);
        }
    }

    @Override
    public void onDataChanged(final DataEventBuffer dataEvents) {
        if(BuildConfig.DEBUG) {
            Util.vibrate(mContext, 50);
            Log.v(TAG, "call onDataChanged");
        }

        if (mListener != null) {
            mListener.onReceiveDataApi(dataEvents);
        }
    }

}
