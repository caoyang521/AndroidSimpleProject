package three.com.phoneservice;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.zip.Inflater;

import three.com.phoneservice.Db.DbHolder;
import three.com.phoneservice.Model.PeopleInfo;
import three.com.phoneservice.Utility.ViewHolder;

/**
 * Created by Administrator on 2015/9/24.
 */
public class PhoneServer extends Service{

    private TelephonyManager telephonyManager=null;
    private PeopleInfo peopleInfo=null;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("tag","service start");
        // 取得TelephonyManager对象
        telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        // 创建一个通话状态监听器
        PhoneStateListener listener = new PhoneStateListener()
        {
            @Override
            public void onCallStateChanged(int state, String number)
            {
                Log.d("tag","call state changed");
                switch (state)
                {
                    case TelephonyManager.CALL_STATE_IDLE:
                        remeView();
                        break;
                    case TelephonyManager.CALL_STATE_OFFHOOK:
                        break;
                    case TelephonyManager.CALL_STATE_RINGING:
                        Log.d("My Service", number);
                        peopleInfo= DbHolder.db.loadPersonByNumber(number);
                        if(peopleInfo!=null){
                            // textView.setText(peopleInfo.getName()+" "+peopleInfo.getNumber());
                            createFloatView(peopleInfo.getPeopleName(),peopleInfo.getPhoneNumber(),peopleInfo.getClassName(),peopleInfo.getPhoneNumber());
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"查询失败",Toast.LENGTH_SHORT).show();
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

        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private boolean isAdded = false; // 是否已增加悬浮窗
    private static WindowManager wm;
    private static WindowManager.LayoutParams params;
    private Button btn_floatView;
    private View floatView;
    /**
     * 创建悬浮窗
     */
    private void createFloatView(String name,String number,String className,String phoneNumber) {

        ViewHolder holder = null;
        if(floatView == null) {
            holder = new ViewHolder();
            LayoutInflater inf =  LayoutInflater.from(this);
            floatView = inf.inflate(R.layout.phone_item_list,null );
            holder.head_name_tv = (TextView) floatView.findViewById(R.id.head_name_tv);
            holder.class_name_tv = (TextView) floatView.findViewById(R.id.class_name_tv);
            holder.phone_number_tv = (TextView) floatView.findViewById(R.id.phone_number_tv);
            holder.name_tv = (TextView) floatView.findViewById(R.id.name_tv);
            //holder.school_number_tv= (TextView) floatView.findViewById(R.id.school_number_tv);
            floatView.setTag(holder);
        }
        else {
            holder = (ViewHolder) floatView.getTag();
        }
        int circleShape = 0;
        int color = 0;
        circleShape = R.drawable.circle_amber;
        color = getApplicationContext().getResources().getColor(R.color.amber_500);
        holder.head_name_tv.setBackgroundResource(circleShape);
        holder.head_name_tv.setText(name.substring(0, 1));
        holder.class_name_tv.setText(className);
        holder.phone_number_tv.setText(phoneNumber);
        holder.name_tv.setText(name);
        holder.school_number_tv.setText(number);
//       btn_floatView = new Button(getApplicationContext());
//       btn_floatView.setText(name+" "+number+" "+className);

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
        floatView.setBackground(getResources().getDrawable(R.drawable.style1));
        if(isAdded==false) {
            isAdded = true;
            wm.addView(floatView, params);

        }
//        else {
//            remeView();
//            wm.addView(floatView, params);
//            isAdded=true;
//        }


    }

    private void remeView(){
        Log.d("TAG", "OFF");
        if( isAdded && floatView != null)
        {
            //移除悬浮窗口
            wm.removeView(floatView);
            isAdded =false;
        }
    }


    @Override
    public void onDestroy() {
        Log.d("tag","service Destroy");
        Intent sevice = new Intent(this, PhoneServer.class);
        this.startService(sevice);

        super.onDestroy();
    }
}
