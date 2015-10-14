package three.com.materialdesignexample;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import three.com.materialdesignexample.Activity.DrawerLayoutActivity;
import three.com.materialdesignexample.Util.HttpUtil;

/**
 * Created by Administrator on 2015/10/14.
 */
public class LoginActivity extends Activity{
    private EditText loginuser=null;
    private EditText loginpass=null;
    private Button loginbtn=null;
    private ImageView codeimg=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        loginuser= (EditText) findViewById(R.id.loginid_et);
        loginpass= (EditText) findViewById(R.id.loginpswd_et);
        loginbtn= (Button) findViewById(R.id.login_ok_btn);
        codeimg= (ImageView) findViewById(R.id.codeimg);

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user=loginuser.getText().toString();
                pass=loginpass.getText().toString();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SafeCodeHelp.getLoginCookie(user);
                        final Bitmap codemap=SafeCodeHelp.getSafeCodePic();

                        // 通过runOnUiThread()方法回到主线程处理逻辑
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                codeimg.setImageBitmap(codemap);
                            }
                        });
                    }
                }).start();



                saveUserAndPass();
            }
        });
    }
    private String user="";
    private String pass="";

    private void saveUserAndPass() {


        if(TextUtils.isEmpty(user)||TextUtils.isEmpty(pass)){
            new AlertDialog.Builder(this)
                    .setTitle("善意的提醒")
                    .setPositiveButton("确定", null)
                    .setMessage("请填写完整的学号和密码")
                    .show();
        }
        else{
            HttpUtil.userName=user;
            HttpUtil.password=pass;
            Intent intent =new Intent(LoginActivity.this, DrawerLayoutActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
