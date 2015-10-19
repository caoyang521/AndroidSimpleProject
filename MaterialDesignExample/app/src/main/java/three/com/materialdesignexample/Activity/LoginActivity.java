package three.com.materialdesignexample.Activity;

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

import three.com.materialdesignexample.R;
import three.com.materialdesignexample.Util.SafeCodeUtil;
import three.com.materialdesignexample.Util.HttpUtil;

/**
 * Created by Administrator on 2015/10/14.
 */
public class LoginActivity extends Activity{
    private EditText loginuser=null;
    private EditText loginpass=null;
    private EditText passported=null;
    private Button loginbtn=null;
    private Button safecodebtn=null;
    private ImageView codeimg=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        loginuser= (EditText) findViewById(R.id.loginid_et);
        loginpass= (EditText) findViewById(R.id.loginpswd_et);
        passported=(EditText) findViewById(R.id.safecode_et);
        loginbtn= (Button) findViewById(R.id.login_ok_btn);
        safecodebtn=(Button) findViewById(R.id.safecode_btn);
        codeimg= (ImageView) findViewById(R.id.codeimg);

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user=loginuser.getText().toString();
                pass=loginpass.getText().toString();
                passport=passported.getText().toString();
                saveUserAndPass();
            }
        });

        safecodebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                user=loginuser.getText().toString();
                if(TextUtils.isEmpty(user)){
                    new AlertDialog.Builder(LoginActivity.this)
                            .setTitle("善意的提醒")
                            .setPositiveButton("确定", null)
                            .setMessage("请先填写学号")
                            .show();
                }
                else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            SafeCodeUtil.getLoginCookie(user);
                            final Bitmap codemap= SafeCodeUtil.getSafeCodePic();

                            // 通过runOnUiThread()方法回到主线程处理逻辑
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    codeimg.setImageBitmap(codemap);
                                }
                            });
                        }
                    }).start();
                }
            }
        });
    }
    private String user="";
    private String pass="";
    private String passport="";

    private void saveUserAndPass() {


        if(TextUtils.isEmpty(user)||TextUtils.isEmpty(pass)||TextUtils.isEmpty(passport)){
            new AlertDialog.Builder(this)
                    .setTitle("善意的提醒")
                    .setPositiveButton("确定", null)
                    .setMessage("请填写完整的学号,密码和验证码")
                    .show();
        }
        else{

            HttpUtil.userName=user;
            HttpUtil.password=pass;
            HttpUtil.passport=passport;
            Intent intent =new Intent(LoginActivity.this, DrawerLayoutActivity.class);
            startActivity(intent);
            finish();

        }
    }
}
