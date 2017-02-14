package cn.ucai.superwechat.ui;

import android.app.ProgressDialog;
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
import cn.ucai.superwechat.utils.DisplayUtils;
import cn.ucai.superwechat.utils.MFGT;
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

    @OnClick({R.id.btn_sendMessage, R.id.btn_sendVideo, R.id.btn_addContact})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_sendMessage:
                break;
            case R.id.btn_sendVideo:
                break;
            case R.id.btn_addContact:
                MFGT.gotAddFriendActivity(ContactInfoActivity.this,addU);
                break;
        }
    }
}
