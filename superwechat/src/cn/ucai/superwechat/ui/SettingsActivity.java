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
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.widget.EaseSwitchButton;
import com.hyphenate.util.EMLog;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.superwechat.R;
import cn.ucai.superwechat.SuperWeChatHelper;
import cn.ucai.superwechat.SuperWeChatModel;
import cn.ucai.superwechat.utils.DisplayUtils;
import cn.ucai.superwechat.utils.MFGT;

/**
 * settings screen
 */
@SuppressWarnings({"FieldCanBeLocal"})
public class SettingsActivity extends BaseActivity implements OnClickListener {

    @BindView(R.id.btn_logout)
    Button mBtnLogout;
    /**
     * new message notification
     */
    private RelativeLayout rl_switch_notification;
    /**
     * sound
     */
    private RelativeLayout rl_switch_sound;
    /**
     * vibration
     */
    private RelativeLayout rl_switch_vibrate;
    /**
     * speaker
     */
    private RelativeLayout rl_switch_speaker;


    /**
     * line between sound and vibration
     */
    private TextView textview1, textview2;

    private LinearLayout blacklistContainer;

    private LinearLayout userProfileContainer;

    /**
     * logout
     */
    private Button logoutBtn;

    private RelativeLayout rl_switch_chatroom_leave;

    private RelativeLayout rl_switch_delete_msg_when_exit_group;
    private RelativeLayout rl_switch_auto_accept_group_invitation;
    private RelativeLayout rl_switch_adaptive_video_encode;
    private RelativeLayout rl_custom_appkey;
    private RelativeLayout rl_custom_server;
    RelativeLayout rl_push_settings;
    private LinearLayout ll_call_option;

    /**
     * Diagnose
     */
    private LinearLayout llDiagnose;
    /**
     * display name for APNs
     */
    private LinearLayout pushNick;

    private EaseSwitchButton notifySwitch;
    private EaseSwitchButton soundSwitch;
    private EaseSwitchButton vibrateSwitch;
    private EaseSwitchButton speakerSwitch;
    private EaseSwitchButton ownerLeaveSwitch;
    private EaseSwitchButton switch_delete_msg_when_exit_group;
    private EaseSwitchButton switch_auto_accept_group_invitation;
    private EaseSwitchButton switch_adaptive_video_encode;
    private EaseSwitchButton customServerSwitch;
    private EaseSwitchButton customAppkeySwitch;
    private SuperWeChatModel settingsModel;
    private EMOptions chatOptions;
    private EditText edit_custom_appkey;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.em_fragment_conversation_settings);
        ButterKnife.bind(this);
        DisplayUtils.initBackWithTitle(this, "设置");
        if (!TextUtils.isEmpty(EMClient.getInstance().getCurrentUser())) {
            mBtnLogout.setText(getString(R.string.button_logout) + "(" + EMClient.getInstance().getCurrentUser() + ")");
        }
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);//不让键盘自动弹出
        setListener();
    }

    private void setListener() {
        mBtnLogout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
         /*   //red packet code : 进入零钱页面
            case R.id.ll_change:
                RedPacketUtil.startChangeActivity(this);
                break;
            //end of red packet code*/
            case R.id.rl_switch_notification:
                if (notifySwitch.isSwitchOpen()) {
                    notifySwitch.closeSwitch();
                    rl_switch_sound.setVisibility(View.GONE);
                    rl_switch_vibrate.setVisibility(View.GONE);
                    textview1.setVisibility(View.GONE);
                    textview2.setVisibility(View.GONE);
                    settingsModel.setSettingMsgNotification(false);
                } else {
                    notifySwitch.openSwitch();
                    rl_switch_sound.setVisibility(View.VISIBLE);
                    rl_switch_vibrate.setVisibility(View.VISIBLE);
                    textview1.setVisibility(View.VISIBLE);
                    textview2.setVisibility(View.VISIBLE);
                    settingsModel.setSettingMsgNotification(true);
                }
                break;
            case R.id.rl_switch_sound:
                if (soundSwitch.isSwitchOpen()) {
                    soundSwitch.closeSwitch();
                    settingsModel.setSettingMsgSound(false);
                } else {
                    soundSwitch.openSwitch();
                    settingsModel.setSettingMsgSound(true);
                }
                break;
            case R.id.rl_switch_vibrate:
                if (vibrateSwitch.isSwitchOpen()) {
                    vibrateSwitch.closeSwitch();
                    settingsModel.setSettingMsgVibrate(false);
                } else {
                    vibrateSwitch.openSwitch();
                    settingsModel.setSettingMsgVibrate(true);
                }
                break;
            case R.id.rl_switch_speaker:
                if (speakerSwitch.isSwitchOpen()) {
                    speakerSwitch.closeSwitch();
                    settingsModel.setSettingMsgSpeaker(false);
                } else {
                    speakerSwitch.openSwitch();
                    settingsModel.setSettingMsgVibrate(true);
                }
                break;
            case R.id.rl_switch_chatroom_owner_leave:
                if (ownerLeaveSwitch.isSwitchOpen()) {
                    ownerLeaveSwitch.closeSwitch();
                    settingsModel.allowChatroomOwnerLeave(false);
                    chatOptions.allowChatroomOwnerLeave(false);
                } else {
                    ownerLeaveSwitch.openSwitch();
                    settingsModel.allowChatroomOwnerLeave(true);
                    chatOptions.allowChatroomOwnerLeave(true);
                }
                break;
            case R.id.rl_switch_delete_msg_when_exit_group:
                if (switch_delete_msg_when_exit_group.isSwitchOpen()) {
                    switch_delete_msg_when_exit_group.closeSwitch();
                    settingsModel.setDeleteMessagesAsExitGroup(false);
                    chatOptions.setDeleteMessagesAsExitGroup(false);
                } else {
                    switch_delete_msg_when_exit_group.openSwitch();
                    settingsModel.setDeleteMessagesAsExitGroup(true);
                    chatOptions.setDeleteMessagesAsExitGroup(true);
                }
                break;
            case R.id.rl_switch_auto_accept_group_invitation:
                if (switch_auto_accept_group_invitation.isSwitchOpen()) {
                    switch_auto_accept_group_invitation.closeSwitch();
                    settingsModel.setAutoAcceptGroupInvitation(false);
                    chatOptions.setAutoAcceptGroupInvitation(false);
                } else {
                    switch_auto_accept_group_invitation.openSwitch();
                    settingsModel.setAutoAcceptGroupInvitation(true);
                    chatOptions.setAutoAcceptGroupInvitation(true);
                }
                break;
            case R.id.rl_switch_adaptive_video_encode:
                EMLog.d("switch", "" + !switch_adaptive_video_encode.isSwitchOpen());
                if (switch_adaptive_video_encode.isSwitchOpen()) {
                    switch_adaptive_video_encode.closeSwitch();
                    settingsModel.setAdaptiveVideoEncode(false);
                    EMClient.getInstance().callManager().getCallOptions().enableFixedVideoResolution(true);

                } else {
                    switch_adaptive_video_encode.openSwitch();
                    settingsModel.setAdaptiveVideoEncode(true);
                    EMClient.getInstance().callManager().getCallOptions().enableFixedVideoResolution(false);
                }
                break;
            case R.id.btn_logout:
                logout();
                break;
            case R.id.ll_black_list:
                startActivity(new Intent(this, BlacklistActivity.class));
                break;
            case R.id.ll_diagnose:
                startActivity(new Intent(this, DiagnoseActivity.class));
                break;
            case R.id.ll_set_push_nick:
                startActivity(new Intent(this, OfflinePushNickActivity.class));
                break;
            case R.id.ll_call_option:
                startActivity(new Intent(this, CallOptionActivity.class));
                break;
            case R.id.ll_user_profile:
                startActivity(new Intent(this, UserProfileActivity.class).putExtra("setting", true)
                        .putExtra("username", EMClient.getInstance().getCurrentUser()));
                break;
            case R.id.switch_custom_server:
                if (customServerSwitch.isSwitchOpen()) {
                    customServerSwitch.closeSwitch();
                    settingsModel.enableCustomServer(false);
                } else {
                    customServerSwitch.openSwitch();
                    settingsModel.enableCustomServer(true);
                }
                break;
            case R.id.switch_custom_appkey:
                if (customAppkeySwitch.isSwitchOpen()) {
                    customAppkeySwitch.closeSwitch();
                    settingsModel.enableCustomAppkey(false);
                } else {
                    customAppkeySwitch.openSwitch();
                    settingsModel.enableCustomAppkey(true);
                }
                edit_custom_appkey.setEnabled(customAppkeySwitch.isSwitchOpen());
                break;
            case R.id.rl_custom_server:
                startActivity(new Intent(this, SetServersActivity.class));
                break;
            case R.id.rl_push_settings:
                startActivity(new Intent(this, OfflinePushSettingsActivity.class));
                break;
            default:
                break;
        }

    }

    void logout() {
        final ProgressDialog pd = new ProgressDialog(this);
        String st = getResources().getString(R.string.Are_logged_out);
        pd.setMessage(st);
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        SuperWeChatHelper.getInstance().logout(false, new EMCallBack() {

            @Override
            public void onSuccess() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        pd.dismiss();
                        // show login screen
                        finish();
                        MFGT.gotoLoginActivityClear(SettingsActivity.this);

                    }
                });
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        pd.dismiss();
                        Toast.makeText(SettingsActivity.this, "unbind devicetokens failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }


   /* @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (((MainActivity) this).isConflict) {
            outState.putBoolean("isConflict", true);
        } else if (((MainActivity) this).getCurrentAccountRemoved()) {
            outState.putBoolean(Constant.ACCOUNT_REMOVED, true);
        }
    }*/
}
