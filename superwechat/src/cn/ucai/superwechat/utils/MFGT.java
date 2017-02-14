package cn.ucai.superwechat.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import com.hyphenate.easeui.domain.User;

import cn.ucai.superwechat.R;
import cn.ucai.superwechat.ui.AddContactActivity;
import cn.ucai.superwechat.ui.AddFriendActivity;
import cn.ucai.superwechat.ui.CenterFragment;
import cn.ucai.superwechat.ui.ContactInfoActivity;
import cn.ucai.superwechat.ui.GuideActivity;
import cn.ucai.superwechat.ui.LoginActivity;
import cn.ucai.superwechat.ui.MainActivity;
import cn.ucai.superwechat.ui.RegisterActivity;
import cn.ucai.superwechat.ui.SettingsActivity;
import cn.ucai.superwechat.ui.UserProfileActivity;
import cn.ucai.superwechat.widget.I;


/**
 * Created by Administrator on 2017/1/10 0010.
 */

public class MFGT {
    public static void startActivity(Activity context, Class<?> cla) {
        context.startActivity(new Intent(context, cla));
        context.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    public static void finish(Activity activity) {
        activity.finish();
        activity.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    public static void startActivity(Activity context, Intent intent) {
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    public static void gotoRegisterActivity(Activity context) {
        startActivity(context, RegisterActivity.class);
    }

    public static void gotoLoginActivity(Activity context) {
        startActivity(context, LoginActivity.class);
    }

    public static void gotoGuideActivity(Activity context) {
        startActivity(context, GuideActivity.class);
    }


    public static void gotoSettings(FragmentActivity activity) {
        startActivity(activity, SettingsActivity.class);
    }

    public static void gotoLoginActivityClear(Activity activity) {
        startActivity(activity, new Intent(activity, LoginActivity.class).
                setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
    }

    public static void gotoUserProfile(FragmentActivity activity) {
        startActivity(activity, UserProfileActivity.class);
    }

    public static void gotoAddContact(Activity activity) {
        startActivity(activity, AddContactActivity.class);
    }

    public static void gotoFirent(Activity activity, User user) {
    }

    public static void gotoContactInfoActivity(Activity activity, User user) {
        Intent intent = new Intent(activity, ContactInfoActivity.class);
        intent.putExtra(I.User.USER_NAME,user);
        startActivity(activity,intent);
    }

    public static void gotAddFriendActivity(Activity activity, User addU) {
        Intent intent = new Intent(activity, AddFriendActivity.class);
        intent.putExtra(I.User.USER_NAME,addU);
        startActivity(activity,intent);
    }
}

