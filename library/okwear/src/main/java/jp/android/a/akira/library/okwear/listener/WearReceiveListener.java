package jp.android.a.akira.library.okwear.listener;

import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.MessageEvent;

public interface WearReceiveListener {

    void onReceiveMessage(final MessageEvent messageEvent);

    void onReceiveDataApi(final DataEventBuffer dataEventBuffer);
}
