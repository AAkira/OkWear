package jp.android.a.akira.library.okwear.util;

import android.content.Context;
import android.os.Vibrator;
import android.widget.Toast;

public class Util {

    public static void showToast(final Context context, final String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void vibrate(final Context context, final long time) {
        final Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(time);
    }
}
