package jp.android.a.akira.library.okwear.util;

import android.graphics.Bitmap;

import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;

import java.util.ArrayList;

import jp.android.a.akira.library.okwear.OkWear;

public class Payload {

    private String path;
    private PutDataMapRequest dataMapRequest;
    private DataMap dataMap;

    public static class Builder {
        private String path;
        private DataMap dataMap;
        private PutDataMapRequest dataMapRequest;

        public Builder() {
            path = OkWear.DEFAULT_MESSAGE_API_PATH;
            init();
        }

        public Builder(final String path) {
            this.path = path;
            init();
        }

        private void init() {
            dataMapRequest = PutDataMapRequest.create(path);
            dataMap = dataMapRequest.getDataMap();
        }

        public Builder addPayload(String name, Asset value) {
            dataMap.putAsset(name, value);
            return this;
        }

        public Builder addPayload(String name, Bitmap bitmap) {
            dataMap.putAsset(name, AssetUtil.createAssetFromBitmap(bitmap));
            return this;
        }

        public Builder addPayload(String name, Bitmap bitmap, Bitmap.CompressFormat format,
                                  int quality) {
            dataMap.putAsset(name, AssetUtil.createAssetFromBitmap(bitmap, format, quality));
            return this;
        }

        public Builder addPayload(String name, boolean value) {
            dataMap.putBoolean(name, value);
            return this;
        }

        public Builder addPayload(String name, byte value) {
            dataMap.putByte(name, value);
            return this;
        }

        public Builder addPayload(String name, byte[] value) {
            dataMap.putByteArray(name, value);
            return this;
        }

        public Builder addPayload(String name, DataMap value) {
            dataMap.putDataMap(name, value);
            return this;
        }

        public Builder addPayload(String name, double value) {
            dataMap.putDouble(name, value);
            return this;
        }

        public Builder addPayload(String name, float value) {
            dataMap.putFloat(name, value);
            return this;
        }

        public Builder addPayload(String name, float[] value) {
            dataMap.putFloatArray(name, value);
            return this;
        }

        public Builder addPayload(String name, int value) {
            dataMap.putInt(name, value);
            return this;
        }

        public Builder addPayload(String name, long value) {
            dataMap.putLong(name, value);
            return this;
        }

        public Builder addPayload(String name, long[] value) {
            dataMap.putLongArray(name, value);
            return this;
        }

        public Builder addPayload(String name, String value) {
            dataMap.putString(name, value);
            return this;
        }

        public Builder addPayload(String name, String[] value) {
            dataMap.putStringArray(name, value);
            return this;
        }

        public Builder addPayload(String name, ArrayList<?> value) throws Exception {
            if (value.get(0) == null) {
                throw new ClassCastException();
            }

            if (value.get(0) instanceof String) {
                dataMap.putStringArrayList(name, (ArrayList<String>) value);
            } else if (value.get(0) instanceof DataMap) {
                dataMap.putDataMapArrayList(name, (ArrayList<DataMap>) value);
            } else if (value.get(0) instanceof Integer) {
                dataMap.putIntegerArrayList(name, (ArrayList<Integer>) value);
            } else {
                throw new Exception(
                        "You can use only three type of String, Integer or DataMap.");
            }
            return this;
        }

        public Payload build() {
            if (dataMap == null) {
                throw new NullPointerException();
            }
            return new Payload(this);
        }
    }

    private Payload(Builder builder) {
        this.path = builder.path;
        this.dataMap = builder.dataMap;
        this.dataMapRequest = builder.dataMapRequest;
    }

    public PutDataRequest getPutDataRequest() {
        return dataMapRequest.asPutDataRequest();
    }

    public String getPath() {
        return path;
    }
}
