package jp.android.a.akira.okwear;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;

import java.util.List;

import jp.android.a.akira.library.okwear.OkWear;
import jp.android.a.akira.library.okwear.listener.NodeChangeListener;
import jp.android.a.akira.library.okwear.listener.SendResultListener;
import jp.android.a.akira.library.okwear.util.Payload;

public class MainActivity extends Activity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private Button mSendButton;
    private OkWear mOkWear;
    private int mPayload = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mOkWear = new OkWear(this);

        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mSendButton = (Button) stub.findViewById(R.id.activity_main_send_button);
                mSendButton.setOnClickListener(MainActivity.this);
            }
        });
    }

    @Override
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
    public void onClick(View v) {
//        sampleSendData();
//        sampleSendMessageAll();
        sampleSendMessageAllAsync();
    }

    private void sampleSendMessage() {
        final byte[] bs = ("hello message").getBytes();

        mOkWear.getNodeList(new NodeChangeListener() {
            @Override
            public void onReceiveNodes(List<Node> nodes) {
                mOkWear.sendMessage(nodes.get(0), bs, null, null);
            }
        });
    }

    private void sampleSendMessageAll() {
        final byte[] bs = ("hello message").getBytes();

        mOkWear.sendMessageAll(bs, null, new SendResultListener<MessageApi.SendMessageResult>() {
            @Override
            public void onResult(MessageApi.SendMessageResult result) {
                Log.v(TAG, "Status: " + result.getStatus());
            }
        });
    }

    private void sampleSendMessageAllAsync() {
        final byte[] bs = ("hello message").getBytes();
        mOkWear.sendMessageAllAsync(bs, null, new SendResultListener<MessageApi.SendMessageResult>() {
            @Override
            public void onResult(MessageApi.SendMessageResult result) {
                Log.v(TAG, "Status: " + result.getStatus());
            }
        });
    }

    private void sampleSendData() {
        final Payload payload =
                new Payload.Builder(OkWear.DEFAULT_DATA_API_PATH)
                        .addPayload("key1", mPayload++)
                        .addPayload("key2", "hello")
                        .build();

        mOkWear.syncData(payload, new SendResultListener<DataApi.DataItemResult>() {
            @Override
            public void onResult(DataApi.DataItemResult result) {
                Log.v(TAG, "Status: " + result.getStatus());
            }
        });
    }
}
