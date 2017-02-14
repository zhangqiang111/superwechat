package cn.ucai.superwechat.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.domain.User;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.superwechat.R;
import cn.ucai.superwechat.utils.DisplayUtils;
import cn.ucai.superwechat.utils.PreferenceManager;
import cn.ucai.superwechat.widget.I;

public class AddFriendActivity extends BaseActivity {
    @BindView(R.id.et_msgOfQequest)
    EditText mEtMsgOfQequest;
    private ProgressDialog progressDialog;
    User addU;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        ButterKnife.bind(this);
        DisplayUtils.initBackWithTitle(this, "验证申请");
        DisplayUtils.setButtonInVisible(this, true);
        initData();
    }

    private void initData() {
        addU = (User) getIntent().getSerializableExtra(I.User.USER_NAME);
        mEtMsgOfQequest.setText("我是"+ PreferenceManager.getInstance().getCurrentUsername());
    }

    public void addContact() {
        /*if (EMClient.getInstance().getCurrentUser().equals(nameText.getText().toString())) {
            new EaseAlertDialog(this, R.string.not_add_myself).show();
            return;
        }

        if (SuperWeChatHelper.getInstance().getContactList().containsKey(nameText.getText().toString())) {
            //let the user know the contact already in your contact list
            if (EMClient.getInstance().contactManager().getBlackListUsernames().contains(nameText.getText().toString())) {
                new EaseAlertDialog(this, R.string.user_already_in_contactlist).show();
                return;
            }
            new EaseAlertDialog(this, R.string.This_user_is_already_your_friend).show();
            return;
        }*/

        progressDialog = new ProgressDialog(this);
        String stri = getResources().getString(R.string.Is_sending_a_request);
        progressDialog.setMessage(stri);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        new Thread(new Runnable() {
            public void run() {

                try {
                    //demo use a hardcode reason here, you need let user to input if you like
//                    String s = getResources().getString(R.string.Add_a_friend);
                    String s = mEtMsgOfQequest.getText().toString();
                    EMClient.getInstance().contactManager().addContact(addU.getMUserName(), s);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            progressDialog.dismiss();
                            String s1 = getResources().getString(R.string.send_successful);
                            Toast.makeText(getApplicationContext(), s1, Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (final Exception e) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            progressDialog.dismiss();
                            String s2 = getResources().getString(R.string.Request_add_buddy_failure);
                            Toast.makeText(getApplicationContext(), s2 + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finish();
            }
        }).start();
    }

    @OnClick(R.id.btn_sendRequest)
    public void onClick() {
        addContact();
    }
}
