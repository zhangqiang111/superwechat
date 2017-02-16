package cn.ucai.superwechat.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.domain.User;
import com.hyphenate.easeui.utils.EaseUserUtils;
import com.hyphenate.easeui.widget.EaseAlertDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.superwechat.R;
import cn.ucai.superwechat.SuperWeChatHelper;
import cn.ucai.superwechat.bean.Result;
import cn.ucai.superwechat.net.NetDao;
import cn.ucai.superwechat.net.OnCompleteListener;
import cn.ucai.superwechat.utils.DisplayUtils;
import cn.ucai.superwechat.utils.MFGT;
import cn.ucai.superwechat.utils.ResultUtils;
import cn.ucai.superwechat.widget.I;

public class ContactInfoActivity extends AppCompatActivity {
    private static final String TAG = "ContactInfoActivity";
    @BindView(R.id.user_head_avatar)
    ImageView mUserHeadAvatar;
    @BindView(R.id.tv_username)
    TextView mTvUsername;
    @BindView(R.id.tv_nick)
    TextView mTvNick;
    @BindView(R.id.btn_sendMessage)
    Button mBtnSendMessage;
    @BindView(R.id.btn_sendVideo)
    Button mBtnSendVideo;
    @BindView(R.id.btn_addContact)
    Button mBtnAddContact;
    private ProgressDialog progressDialog;
    User addU;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_info);
        ButterKnife.bind(this);
        DisplayUtils.initBackWithTitle(this, "详细资料");
        initData();
    }

    private void initData() {
        User user = (User) getIntent().getSerializableExtra(I.User.USER_NAME);
        if (user != null) {
            showInfo(user);
        }else {
            String username = getIntent().getStringExtra(I.User.TABLE_NAME);
            if (username==null){
                MFGT.finish(this);
            }else {
                syncInfo(username);
            }
        }
    }

    private void showInfo(User user) {
        mTvUsername.setText(user.getMUserName());
        EaseUserUtils.setAppUserAvatar(this, user.getMUserName(), mUserHeadAvatar);
        User u = SuperWeChatHelper.getInstance().getAppContactList().get(user.getMUserName());
        if (u != null) {
            EaseUserUtils.setAppUserNick(u.getMUserName(), mTvNick);
        } else {
            Log.e(TAG, "查找不到");
            mBtnSendMessage.setVisibility(View.GONE);
            mBtnSendVideo.setVisibility(View.GONE);
            mBtnAddContact.setVisibility(View.VISIBLE);
            addU = user;
        }
    }

    private void syncInfo(String username) {
        NetDao.getUserInfoByName(this, username, new OnCompleteListener<String>() {
            @Override
            public void onSuccess(String s) {
                if (s != null) {
                    Result result = ResultUtils.getResultFromJson(s, User.class);
                    if (result != null) {
                        if (result.isRetMsg()) {
                            User user = (User) result.getRetData();
                            if (user != null) {
                                showInfo(user);
                            }

                        }
                    }
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    @OnClick({R.id.btn_sendMessage, R.id.btn_sendVideo, R.id.btn_addContact})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_sendMessage:
                startActivity(new Intent(this, ChatActivity.class).putExtra("userId", mTvUsername.getText().toString()));
                break;
            case R.id.btn_sendVideo:
                startActivity(new Intent(this, VoiceCallActivity.class).putExtra("username", addU.getMUserName())
                        .putExtra("isComingCall", false));
                break;
            case R.id.btn_addContact:
                MFGT.gotAddFriendActivity(ContactInfoActivity.this, addU);
                break;
        }
    }
}
