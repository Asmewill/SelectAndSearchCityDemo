package com.zaaach.citypicker.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.zaaach.citypicker.R;

public class SideLetterBar extends View {
    private  String[] bStr = {"定位", "热门", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    private int choose = -1;
    private Paint paint = new Paint();
    private boolean showBg = false;
    private OnLetterChangedListener onLetterChangedListener;
    private TextView overlay;

    public String[] getbStr() {
        return bStr;
    }

    public void setbStr(String[] bStr) {
        this.bStr = bStr;
        invalidate();
    }

    public SideLetterBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public SideLetterBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SideLetterBar(Context context) {
        super(context);
    }

    /**
     * 设置悬浮的textview
     * @param overlay
     */
    public void setOverlay(TextView overlay){
        this.overlay = overlay;
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (showBg) {
            canvas.drawColor(Color.TRANSPARENT);
        }
        //画出索引字母
        int height = getHeight();
        int width = getWidth();
        int singleHeight = height / bStr.length;
        for (int i = 0; i < bStr.length; i++) {
            paint.setTextSize(getResources().getDimension(R.dimen.side_letter_bar_letter_size));
            paint.setColor(getResources().getColor(R.color.gray));
            paint.setAntiAlias(true);
            //如果选中颜色变深
            if (i == choose) {
                Toast.makeText(getContext(), "choice", Toast.LENGTH_SHORT).show();
                paint.setColor(getResources().getColor(R.color.gray_deep));
                paint.setFakeBoldText(true);  //加粗
            }
            //计算相应字母的距离居中
            float xPos = width / 2 - paint.measureText(bStr[i]) / 2;
            float yPos = singleHeight * i + singleHeight;
            canvas.drawText(bStr[i], xPos, yPos, paint);
            paint.reset();
        }

    }

//    设置中间显示的结果
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        final float y = event.getY();
        final int oldChoose = choose;
        final OnLetterChangedListener listener = onLetterChangedListener;
        //相应高度的比例乘以字符数组长度就是我们要的字母
        final int c = (int) (y / getHeight() * bStr.length);

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                showBg = true;
                if (oldChoose != c && listener != null) {
                    if (c >= 0 && c < bStr.length) {
                        listener.onLetterChanged(bStr[c]);
                        choose = c;
                        invalidate();
                        if (overlay != null){
                            overlay.setVisibility(VISIBLE);
                            overlay.setText(bStr[c]);
                        }
                    }
                }

                break;
            case MotionEvent.ACTION_MOVE:
                if (oldChoose != c && listener != null) {
                    if (c >= 0 && c < bStr.length) {
                        listener.onLetterChanged(bStr[c]);
                        choose = c;
                        invalidate();
                        if (overlay != null){
                            overlay.setVisibility(VISIBLE);
                            overlay.setText(bStr[c]);
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                showBg = false;
                choose = -1;
                invalidate();
                if (overlay != null){
                    overlay.setVisibility(GONE);
                }
                break;
        }
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    public void setOnLetterChangedListener(OnLetterChangedListener onLetterChangedListener) {
        this.onLetterChangedListener = onLetterChangedListener;
    }

    public interface OnLetterChangedListener {
        void onLetterChanged(String letter);
    }

}
