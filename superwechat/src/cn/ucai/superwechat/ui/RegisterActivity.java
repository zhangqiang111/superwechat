/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.ucai.superwechat.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.domain.User;
import com.hyphenate.exceptions.HyphenateException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.superwechat.R;
import cn.ucai.superwechat.SuperWeChatHelper;
import cn.ucai.superwechat.bean.Result;
import cn.ucai.superwechat.db.SuperWeChatDBManager;
import cn.ucai.superwechat.db.UserDao;
import cn.ucai.superwechat.net.NetDao;
import cn.ucai.superwechat.net.OnCompleteListener;
import cn.ucai.superwechat.utils.CommonUtils;
import cn.ucai.superwechat.utils.DisplayUtils;
import cn.ucai.superwechat.utils.L;
import cn.ucai.superwechat.utils.MD5;
import cn.ucai.superwechat.utils.MFGT;
import cn.ucai.superwechat.utils.ResultUtils;
import cn.ucai.superwechat.widget.I;

/**
 * register screen
 */
public class RegisterActivity extends BaseActivity {
    final static String TAG = "RegisterActivity";
    @BindView(R.id.etUserName)
    EditText mEtUserName;
    @BindView(R.id.etNick)
    EditText mEtNick;
    @BindView(R.id.etPassword)
    EditText mEtPassword;
    @BindView(R.id.etConfirm)
    EditText mEtConfirm;
    String username;
    String nick;
    String password;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.em_activity_register);
        ButterKnife.bind(this);
        DisplayUtils.initBackWithTitle(this,"注册");
        pd = new ProgressDialog(this);
    }

    public void register() {
        username = mEtUserName.getText().toString().trim();
        nick = mEtNick.getText().toString().trim();
        password = mEtPassword.getText().toString().trim();
        String confirm_password = mEtConfirm.getText().toString().trim();
        if (TextUtils.isEmpty(username)) {
            Toast.makeText(this, getResources().getString(R.string.User_name_cannot_be_empty), Toast.LENGTH_SHORT).show();
            mEtUserName.requestFocus();
            return;
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, getResources().getString(R.string.Password_cannot_be_empty), Toast.LENGTH_SHORT).show();
            mEtPassword.requestFocus();
            return;
        } else if (TextUtils.isEmpty(confirm_password)) {
            Toast.makeText(this, getResources().getString(R.string.Confirm_password_cannot_be_empty), Toast.LENGTH_SHORT).show();
            mEtConfirm.requestFocus();
            return;
        } else if (!password.equals(confirm_password)) {
            Toast.makeText(this, getResources().getString(R.string.Two_input_password), Toast.LENGTH_SHORT).show();
            return;
        }
        nick = mEtNick.getText().toString().trim();
        if (TextUtils.isEmpty(nick)) {
            nick = "";
        }
        registerAppServer();
    }

    private void registerAppServer() {
        Log.e(TAG,"进入registerAppServer");
        pd.show();
        NetDao.register(this, username, nick, password, new OnCompleteListener<String>() {
            @Override
            public void onSuccess(String s) {
                if (s != null) {
                    Result result = ResultUtils.getResultFromJson(s, null);
                    if (result != null) {
                        if (result.isRetMsg()) {
                            Log.e(">>>>","进入registerEmServer");
                            registerEmServer();
                        }
                    } else {
                        pd.dismiss();
                        if (result.getRetCode() == I.MSG_REGISTER_USERNAME_EXISTS) {
                            CommonUtils.showShortToast(R.string.User_already_exists);
                        } else {
                            L.e(TAG,"到了");
                            CommonUtils.showShortToast(R.string.Registration_failed);
                        }
                    }

                } else {
                    pd.dismiss();
                    CommonUtils.showShortToast(R.string.Registration_failed);
                }
            }

            @Override
            public void onError(String error) {
                pd.dismiss();
                CommonUtils.showShortToast(R.string.Registration_failed);
            }
        });
    }

    private void registerEmServer() {
        if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
            pd.setMessage(getResources().getString(R.string.Is_the_registered));
            pd.show();

            new Thread(new Runnable() {
                public void run() {
                    try {
                        // call method in SDK
                        EMClient.getInstance().createAccount(username, MD5.getMessageDigest(password));
                        runOnUiThread(new Runnable() {
                            public void run() {
                                if (!RegisterActivity.this.isFinishing())
                                    pd.dismiss();
                                // save current user
                                SuperWeChatHelper.getInstance().setCurrentUserName(username);
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.Registered_successfully), Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                    } catch (final HyphenateException e) {
                        unRigsterAppServer();
                        runOnUiThread(new Runnable() {
                            public void run() {
                                if (!RegisterActivity.this.isFinishing())
                                    pd.dismiss();
                                int errorCode = e.getErrorCode();
                                if (errorCode == EMError.NETWORK_ERROR) {
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.network_anomalies), Toast.LENGTH_SHORT).show();
                                } else if (errorCode == EMError.USER_ALREADY_EXIST) {
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.User_already_exists), Toast.LENGTH_SHORT).show();
                                } else if (errorCode == EMError.USER_AUTHENTICATION_FAILED) {
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.registration_failed_without_permission), Toast.LENGTH_SHORT).show();
                                } else if (errorCode == EMError.USER_ILLEGAL_ARGUMENT) {
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.illegal_user_name), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.Registration_failed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            }).start();

        }
    }

    public void back(View view) {
        finish();
    }
    public void unRigsterAppServer(){
        NetDao.unRegister(this, username, new OnCompleteListener<String>() {
            @Override
            public void onSuccess(String result) {

            }

            @Override
            public void onError(String error) {

            }
        });
    }

    @OnClick({R.id.top_back, R.id.btn_login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.top_back:
//                MFGT.gotoGuideActivity(this);
                MFGT.finish(this);
                break;
            case R.id.btn_login:
                Log.e(">>>>>", "注册按钮");
                register();
                break;
        }
    }
}
