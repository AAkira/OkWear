package jp.android.a.akira.okwear;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.MessageEvent;

import jp.android.a.akira.library.okwear.OkWear;
import jp.android.a.akira.library.okwear.listener.ReceiveListener;


public class MainActivity extends ActionBarActivity implements ReceiveListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private OkWear mOkWear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mOkWear = new OkWear(this);
        mOkWear.registReceiver(this);
    }

    protected void onResume() {
        super.onResume();
        mOkWear.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mOkWear.disconnect();
    }

    @Override
    public void onReceiveMessage(MessageEvent messageEvent) {
        if (messageEvent.getPath().equals(OkWear.DEFAULT_MESSAGE_API_PATH)) {
            final String messagePayload = new String(messageEvent.getData());
            Log.v(TAG, messagePayload);
        }
    }

    @Override
    public void onReceiveDataApi(DataEventBuffer dataEventBuffer) {
        for (DataEvent event : dataEventBuffer) {
            DataMap dataMap = DataMap.fromByteArray(event.getDataItem().getData());
            final int data = dataMap.getInt(OkWear.DEFAULT_DATA_API_KEY);
            Log.v(TAG, "data: " + data);
        }
    }
}
