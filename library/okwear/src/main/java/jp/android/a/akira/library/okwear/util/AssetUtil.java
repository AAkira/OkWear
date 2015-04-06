package jp.android.a.akira.library.okwear.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.Wearable;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class AssetUtil {

    private static String TAG = AssetUtil.class.getSimpleName();

    protected static Asset createAssetFromBitmap(final Bitmap bitmap) {
        return createAssetFromBitmap(bitmap, Bitmap.CompressFormat.PNG, 100);
    }

    protected static Asset createAssetFromBitmap(final Bitmap bitmap, final Bitmap.CompressFormat format,
                                              final int quality) {
        final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        bitmap.compress(format, quality, byteStream);
        return Asset.createFromBytes(byteStream.toByteArray());
    }

    public static Bitmap loadBitmapFromAsset(@NonNull final Asset asset, final GoogleApiClient googleApiClient) {
        final InputStream assetInputStream = Wearable.DataApi.getFdForAsset(
                googleApiClient, asset).await().getInputStream();
        if (assetInputStream == null) {
            Log.w(TAG, "Requested an unknown Asset.");
            return null;
        }
        return BitmapFactory.decodeStream(assetInputStream);
    }
}
