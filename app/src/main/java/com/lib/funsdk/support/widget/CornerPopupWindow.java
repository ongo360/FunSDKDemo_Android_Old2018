package com.lib.funsdk.support.widget;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.funsdkdemo.R;

/**
 * Created by ccy on 2017-09-28.
 * 圆角有四周有margin，共3个item样式的底部弹窗popupWindow
 * （效果见视频预览界面巡航点编辑弹窗）
 */

public class CornerPopupWindow {

    private Activity mActivity;


    private PopupWindow pop;

    private View v;
    private TextView item1;
    private TextView item2;
    private TextView item3;

    private Object tag;

    public CornerPopupWindow(Activity activity) {
        this.mActivity = activity;
        createPopup();
    }

    private void createPopup() {
        v = LayoutInflater.from(mActivity).inflate(R.layout.item_corner_popup, null);
        item1 = (TextView) v.findViewById(R.id.pop_item_1);
        item2 = (TextView) v.findViewById(R.id.pop_item_2);
        item3 = (TextView) v.findViewById(R.id.pop_item_3);
        pop = new PopupWindow(v, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        pop.setBackgroundDrawable(new ColorDrawable());
        pop.setFocusable(true);
        pop.setOutsideTouchable(true);
        pop.setAnimationStyle(R.style.corner_popwindow_anim_style);
        pop.setOnDismissListener(mOnDismissListener);
    }


    public void setItem1(String text, View.OnClickListener click) {
        setItem1(text, mActivity.getResources().getColor(R.color.whitebg_text), click);
    }

    public void setItem1(String text, int textColor, final View.OnClickListener click) {
        item1.setText(text);
        item1.setTextColor(textColor);
        item1.setOnClickListener(click);
    }

    public void setItem2(String text, View.OnClickListener click) {
        setItem2(text, mActivity.getResources().getColor(R.color.whitebg_text), click);
    }

    public void setItem2(String text, int textColor, final View.OnClickListener click) {
        item2.setText(text);
        item2.setTextColor(textColor);
        item2.setOnClickListener(click);
    }

    public void setItem3(String text, View.OnClickListener click) {
        setItem3(text,mActivity.getResources().getColor(R.color.whitebg_text), click);
    }

    public void setItem3(String text, int textColor, final View.OnClickListener click) {
        item3.setText(text);
        item3.setTextColor(textColor);
        item3.setOnClickListener(click);
    }

    PopupWindow.OnDismissListener mOnDismissListener = new PopupWindow.OnDismissListener() {
        @Override
        public void onDismiss() {
            if (null == mActivity) {
                return;
            }
            WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
            lp.alpha = 1f;
            lp.dimAmount = 1f;
            mActivity.getWindow().setAttributes(lp);
        }
    };

    public void show() {
        if (pop.isShowing())
            return;
        WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
        lp.alpha = 0.5f;
        lp.dimAmount = 0.5f;
        mActivity.getWindow().setAttributes(lp);
        pop.showAtLocation(v, Gravity.BOTTOM, 0, 0);
    }

    public void dissmiss() {
        pop.dismiss();
    }

    public boolean isShowing(){
        return  pop.isShowing();
    }


    public TextView getItem1() {
        return item1;
    }

    public TextView getItem2() {
        return item2;
    }

    public TextView getItem3() {
        return item3;
    }

    public void setTag(Object o) {
        this.tag = o;
    }

    public Object getTag() {
        if(tag == null){
            tag = 1;
        }
        return tag;
    }


}
