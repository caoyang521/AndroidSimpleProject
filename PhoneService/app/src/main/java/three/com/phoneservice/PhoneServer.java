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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

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
                        break;
                    case TelephonyManager.CALL_STATE_OFFHOOK:
                        break;
                    case TelephonyManager.CALL_STATE_RINGING:
                        Log.d("My Service", number);
                        peopleInfo=db.loadPerson(number);
                        if(peopleInfo!=null){
                           // textView.setText(peopleInfo.getName()+" "+peopleInfo.getNumber());
                           // Toast.makeText(getApplicationContext(),peopleInfo.getName(),Toast.LENGTH_LONG).show();
                           createFloatView(peopleInfo.getName(),peopleInfo.getNumber());
                        }
                        else
                          //  textView.setText("查询失败");
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
    private Context context=null;

    class MyBinder extends Binder {

        public void getDb(TextView mytextView,Db mdb,Context mcontext){
            textView=mytextView;
            db=mdb;
            context=mcontext;
        }
    }

    //定义浮动窗口布局
    LinearLayout mFloatLayout;
    WindowManager.LayoutParams wmParams;
    //创建浮动窗口设置布局参数的对象
    WindowManager mWindowManager;
    Button mFloatView;

    private void createFloatView(String number,String name)
    {
        wmParams = new WindowManager.LayoutParams();
        //获取的是WindowManagerImpl.CompatModeWrapper
        mWindowManager = (WindowManager)getApplication().getSystemService(getApplication().WINDOW_SERVICE);

        wmParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        //设置图片格式，效果为背景透明
        wmParams.format = PixelFormat.RGBA_8888;
        //设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        //调整悬浮窗显示的停靠位置为左侧置顶
        wmParams.gravity = Gravity.CENTER | Gravity.BOTTOM;
        // 以屏幕左上角为原点，设置x、y初始值，相对于gravity
        wmParams.x = 0;
        wmParams.y = 0;

        //设置悬浮窗口长宽数据
        wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        LayoutInflater inflater = LayoutInflater.from(getApplication());
        //获取浮动窗口视图所在布局
        mFloatLayout = (LinearLayout) inflater.inflate(R.layout.float_layout, null);
        //添加mFloatLayout
        mWindowManager.addView(mFloatLayout, wmParams);

        //浮动窗口按钮
        mFloatView = (Button)mFloatLayout.findViewById(R.id.floatbtn);
        mFloatView.setText(name +" "+number);
        mFloatLayout.measure(View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
                .makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

//        //设置监听浮动窗口的触摸移动
//        mFloatView.setOnTouchListener(new View.OnTouchListener()
//        {
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event)
//            {
//                //getRawX是触摸位置相对于屏幕的坐标，getX是相对于按钮的坐标
//                wmParams.x = (int) event.getX() - mFloatView.getMeasuredWidth()/2;
//                //减25为状态栏的高度
//                wmParams.y = (int) event.getY() - mFloatView.getMeasuredHeight()/2 - 25;
//                //刷新
//                mWindowManager.updateViewLayout(mFloatLayout, wmParams);
//                return false;  //此处必须返回false，否则OnClickListener获取不到监听
//            }
//        });

//        mFloatView.setOnClickListener(new OnClickListener()
//        {
//
//            @Override
//            public void onClick(View v)
//            {
//                Toast.makeText(FxService.this, "onClick", Toast.LENGTH_SHORT).show();
//            }
//        });
    }
}
