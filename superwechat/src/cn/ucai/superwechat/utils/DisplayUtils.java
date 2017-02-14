package cn.ucai.superwechat.utils;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import cn.ucai.superwechat.R;


/**
 * Created by Administrator on 2017/1/18 0018.
 */

public class DisplayUtils {
    public static void initBack(final Activity activity) {
        activity.findViewById(R.id.top_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
    }

    public static void initBackWithTitle(Activity activity, String title) {
        TextView textView = (TextView) activity.findViewById(R.id.top_text);
        textView.setText(title);
        initBack(activity);
    }

    public static void setTextInVisible(Activity activity, boolean b) {
        if (b) {
            TextView textView = (TextView) activity.findViewById(R.id.top_text);
            textView.setVisibility(View.INVISIBLE);
        }
    }
    public static void setButtonInVisible(Activity activity, boolean b) {
        if (b) {
            Button button = (Button) activity.findViewById(R.id.btn_sendRequest);
            button.setVisibility(View.VISIBLE);
        }
    }
}
