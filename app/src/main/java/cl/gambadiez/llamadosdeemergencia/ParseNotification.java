package cl.gambadiez.llamadosdeemergencia;

import android.app.Application;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParsePush;
import com.parse.PushService;
import com.parse.SaveCallback;


/**
 * Created by Alfredo on 11-04-2015.
 */
    public class ParseNotification extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Add your initialization code here App ID -- Client Key
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "r20i16dy7yI4xph20fI2mqYlRDtPoOa5uccl0NUt", "dAiO5yi94O0Sl6EiHCaKxH27yVJBYyM3nTDQhm81");
        ParsePush.subscribeInBackground("", new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("com.parse.push", "successfully subscribed to the broadcast channel.");
                } else {
                    Log.e("com.parse.push", "failed to subscribe for push", e);
                }
            }
        });

    }
}
