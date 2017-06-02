package cn.kk20.lib.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import cn.kk20.lib.R;

/**
 * @Description 通用版提示框
 * @Author kk20
 * @Date 2017/5/15
 * @Version V1.0.0
 */
public class CommonTipDialog extends Dialog {
    private TextView tv_title, tv_msg;
    private Button btn_left, btn_right;

    private OnBtnClickListener listener;

    public CommonTipDialog(Context context) {
        this(context, R.style.StyleCustomDialog);
        init(context);
    }

    public CommonTipDialog(Context context, int theme) {
        super(context, theme);

        init(context);
    }

    public void init(Context c) {
        View dialogView = LayoutInflater.from(c).inflate(R.layout.layout_dialog_default, null);
        tv_title = (TextView) dialogView.findViewById(R.id.tv_title);
        tv_msg = (TextView) dialogView.findViewById(R.id.tv_msg);
        btn_left = (Button) dialogView.findViewById(R.id.btn_left);
        btn_right = (Button) dialogView.findViewById(R.id.btn_right);
        tv_title.setVisibility(View.GONE);
        tv_msg.setText("正在处理，请稍后...");

        btn_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonTipDialog.this.dismiss();
                if (listener != null) {
                    listener.onLeftBtnClickListener();
                }
            }
        });
        btn_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonTipDialog.this.dismiss();
                if (listener != null) {
                    listener.onRightBtnClickListener();
                }
            }
        });

        setContentView(dialogView);
        setCanceledOnTouchOutside(false);
    }

    public void setTitleVisiable() {
        tv_title.setVisibility(View.VISIBLE);
    }

    public void setTitle(String title) {
        tv_title.setText(title);
    }

    public void setMessage(String msg) {
        tv_msg.setText(msg);
    }

    public void setLeftBtnText(String text) {
        btn_left.setText(text);
    }

    public void setRightBtnText(String text) {
        btn_right.setText(text);
    }

    public void setListener(OnBtnClickListener listener) {
        this.listener = listener;
    }

    public interface OnBtnClickListener {
        void onLeftBtnClickListener();

        void onRightBtnClickListener();
    }

}
