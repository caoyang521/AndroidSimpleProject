package three.com.phoneservice.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import three.com.phoneservice.CallBack;
import three.com.phoneservice.Params.AppParams;
import three.com.phoneservice.R;
import three.com.phoneservice.Utility.HttpUtility;
import three.com.phoneservice.Utility.ProgressDialogHelper;
import three.com.phoneservice.Utility.SharedPreferencesHelper;

/**
 * Created by Administrator on 2015/11/26.
 */
public class LoginActivity extends AppCompatActivity {


    private EditText loginid_et;
    private EditText loginpswd_et;
    private Button login_ok_btn;
    private String stNumber;
    private String stPassWord;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(SharedPreferencesHelper.getStdInfo(this)){
            Log.d("name", AppParams.name);
            Log.d("School",AppParams.School);
            Log.d("classRoom",AppParams.classRoom);
            finish();
            Intent intent=new Intent(LoginActivity.this,PhoneActivity.class);
            startActivity(intent);
        }
        setContentView(R.layout.avtivity_login);
        loginid_et= (EditText) findViewById(R.id.loginid_et);
        loginpswd_et= (EditText) findViewById(R.id.loginpswd_et);
        login_ok_btn= (Button) findViewById(R.id.login_ok_btn);

        login_ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stNumber = loginid_et.getText().toString();
                stPassWord = loginpswd_et.getText().toString();
                if (TextUtils.isEmpty(stNumber) || TextUtils.isEmpty(stNumber)) {
                    new AlertDialog.Builder(LoginActivity.this)
                            .setTitle("善意的提醒")
                            .setPositiveButton("确定", null)
                            .setMessage("请输入完整的学号或者密码")
                            .show();
                } else {
                    HttpUtility.loginHttpRequest(AppParams.newLoginAddress, stNumber, stPassWord, new CallBack() {
                        @Override
                        public void onStart() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ProgressDialogHelper.showProgressDialog(LoginActivity.this, "正在登陆...");
                                }
                            });

                        }

                        @Override
                        public void onFinsh(final String response) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if ("ok".equals(response)) {
                                        ProgressDialogHelper.closeProgressDialog();
                                        SharedPreferencesHelper.saveStdInfo(LoginActivity.this);
                                        finish();
                                        Intent intent = new Intent(LoginActivity.this, PhoneActivity.class);
                                        startActivity(intent);
                                    } else {
                                        ProgressDialogHelper.closeProgressDialog();
                                        new AlertDialog.Builder(LoginActivity.this)
                                                .setTitle("善意的提醒")
                                                .setPositiveButton("确定", null)
                                                .setMessage("密码错误")
                                                .show();
                                    }
                                }
                            });
                        }
                    });
                }
            }
        });


    }
}
