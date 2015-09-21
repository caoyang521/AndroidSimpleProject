package three.com.countdown;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;



public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button start;
    private Button over;
    private EditText editText;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        start.setOnClickListener(this);
        over.setOnClickListener(this);
    }

    public void initView(){
        start= (Button) findViewById(R.id.startTime);
        over= (Button) findViewById(R.id.overTime);
        editText= (EditText) findViewById(R.id.inputtime);
        textView= (TextView) findViewById(R.id.timetext);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.startTime:
                i=Integer.parseInt(editText.getText().toString());
                textView.setText(i.toString());
                if(i!=null)
                    start();
                break;
            case R.id.overTime:
                over();
                break;
        }
    }
    private Timer time;
    private TimerTask timerTask;
    private Integer i;

    private Handler handler=new Handler() {
        @Override
        public void handleMessage(Message msg) {
            textView.setText(msg.arg1 + "");
            if(msg.arg1>0)
                start();
        }
    };

    public void start(){

        time =new Timer();
        timerTask =new TimerTask() {
            @Override
            public void run() {
                Message message =handler.obtainMessage();
                i--;
                message.arg1=i;
                handler.sendMessage(message);
            }
        };
        time.schedule(timerTask,1000);
    }

    public void over (){
        if(timerTask!=null)
            timerTask.cancel();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
