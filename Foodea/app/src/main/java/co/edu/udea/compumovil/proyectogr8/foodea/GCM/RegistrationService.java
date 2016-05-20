package co.edu.udea.compumovil.proyectogr8.foodea.GCM;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.iid.InstanceID;

import co.edu.udea.compumovil.proyectogr8.foodea.R;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class RegistrationService extends IntentService {

    private static final String TAG = "RegIntentService";
    public RegistrationService() {
        super("RegistrationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(TAG, "onHandleIntent");
        String registrationToken = null;

        try {
            InstanceID myID = InstanceID.getInstance(this);
            registrationToken = myID.getToken(getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            Log.i(TAG,"Registration Token: "+ registrationToken);

            GcmPubSub subscription = GcmPubSub.getInstance(this);
            subscription.subscribe(registrationToken, "/topics/foodea", null);

        }catch(Exception e){
            Log.d(TAG, "Failed to complete token refresh", e);
        }

    }


}
