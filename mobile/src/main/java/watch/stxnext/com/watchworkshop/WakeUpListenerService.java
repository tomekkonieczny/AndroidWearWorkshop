package watch.stxnext.com.watchworkshop;

import android.content.Intent;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

/**
 * @author Mieszko on 26-05-2015.
 */
public class WakeUpListenerService extends WearableListenerService {

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        if (!messageEvent.getPath().equals("/STX_wake_up_message")) {
            return;
        }

        Intent intent = new Intent();
        intent.setAction("watch.stxnext.com.watchworkshop.broadcast");
        sendBroadcast(intent);
    }
}
