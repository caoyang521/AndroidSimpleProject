package three.com.phoneservice;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import three.com.phoneservice.Db.Db;
import three.com.phoneservice.Model.PeopleInfo;

/**
 * Created by Administrator on 2015/9/24.
 */
public class PhoneServer extends Service{

    private TelephonyManager telephonyManager=null;
    private final MyBinder myBinder=new MyBinder();
    private Db db=null;
    private PeopleInfo peopleInfo=null;

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
                        remeView();
                        break;
                    case TelephonyManager.CALL_STATE_OFFHOOK:
                        break;
                    case TelephonyManager.CALL_STATE_RINGING:
                        Log.d("My Service", number);
                        peopleInfo=db.loadPersonByNumber(number);
                        if(peopleInfo!=null){
                           // textView.setText(peopleInfo.getName()+" "+peopleInfo.getNumber());
                           // Toast.makeText(getApplicationContext(),peopleInfo.getName(),Toast.LENGTH_LONG).show();
                           createFloatView(peopleInfo.getPeopleName(),peopleInfo.getPhoneNumber());
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"查询失败",Toast.LENGTH_LONG).show();
                        }
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
        flags=START_STICKY;
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }

    class MyBinder extends Binder {

        public void getDb(Db mdb){
            db=mdb;
        }
    }

    private boolean isAdded = false; // 是否已增加悬浮窗
    private static WindowManager wm;
    private static WindowManager.LayoutParams params;
    private Button btn_floatView;
    /**
     * 创建悬浮窗
     */
    private void createFloatView(String name,String number) {

        btn_floatView = new Button(getApplicationContext());
        btn_floatView.setText(name+" "+number);

        wm = (WindowManager) getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE);
        params = new WindowManager.LayoutParams();

        // 设置window type
        params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;

        /*
         * 如果设置为params.type = WindowManager.LayoutParams.TYPE_PHONE;
         * 那么优先级会降低一些, 即拉下通知栏不可见
         */

        params.format = PixelFormat.RGBA_8888; // 设置图片格式，效果为背景透明

        // 设置Window flag
        params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        /*
         * 下面的flags属性的效果形同“锁定”。
         * 悬浮窗不可触摸，不接受任何事件,同时不影响后面的事件响应。
        wmParams.flags=LayoutParams.FLAG_NOT_TOUCH_MODAL
                               | LayoutParams.FLAG_NOT_FOCUSABLE
                               | LayoutParams.FLAG_NOT_TOUCHABLE;
         */

        //设置悬浮窗口长宽数据
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;

        wm.addView(btn_floatView, params);
        isAdded = true;
    }


    private void remeView(){
        Log.d("TAG","OFF");
        if(btn_floatView != null)
        {
            //移除悬浮窗口
            wm.removeView(btn_floatView);
        }
    }
}
