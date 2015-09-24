package three.com.phoneservice;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by Administrator on 2015/9/24.
 */
public class PhoneServer extends Service{

    private TelephonyManager telephonyManager=null;
    private MyBinder myBinder=new MyBinder();

    @Override
    public void onCreate() {
        super.onCreate();
        // 取得TelephonyManager对象
        telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        // 创建一个通话状态监听器
        PhoneStateListener listener = new PhoneStateListener()
        {
            @Override
            public void onCallStateChanged(int state, String number)
            {
                switch (state)
                {
                    case TelephonyManager.CALL_STATE_IDLE:
                        break;
                    case TelephonyManager.CALL_STATE_OFFHOOK:
                        break;
                    case TelephonyManager.CALL_STATE_RINGING:
                        Log.d("My Service",number);
                        textView.setText(number);
                        break;
                    default:
                        break;
                }
                super.onCallStateChanged(state, number);
            }
        };
        // 监听电话通话状态的改变
        telephonyManager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }
    private TextView textView=null;

    class MyBinder extends Binder {

        public void getText(TextView mytextView){
            textView=mytextView;
        }
    }
}
