package com.max.tour.widget;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.max.tour.R;

import butterknife.BindView;
import razerdp.basepopup.BasePopupWindow;


/**
 * 输入法 输入框
 */
public class PopupInput extends BasePopupWindow {

    private EditText mEdInput;

    private SendListener listener;

    public PopupInput(Context context) {
        super(context);

        TextView mTvSend = findViewById(R.id.tv_send);
        mEdInput = findViewById(R.id.ed_input);

        mTvSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = mEdInput.getText().toString();
                if (TextUtils.isEmpty(str)) {
                    ToastUtils.showShort("评论不能为空");
                    return;
                }

                listener.onSend(str);
                mEdInput.setText("");
                mEdInput.clearFocus();
                dismiss();
            }
        });
    }

    public PopupInput setListener(SendListener listener) {
        this.listener = listener;
        return this;
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.popup_input);
    }

    @Override
    protected Animation onCreateShowAnimation() {
        return getTranslateVerticalAnimation(1f, 0f, 450);
    }


    @Override
    protected Animation onCreateDismissAnimation() {
        return getTranslateVerticalAnimation(0f, 1f, 450);
    }

    public interface SendListener {

        void onSend(String msg);
    }
}
