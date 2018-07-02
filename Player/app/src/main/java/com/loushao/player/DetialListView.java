package com.loushao.player;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class DetialListView extends ListView {
    public DetialListView(Context context) {
        super(context);
    }

    public DetialListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DetialListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    //ScrollView 嵌套listview只显示一行  重写onMeasure
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //测量的大小由一个32位的数字表示，前两位表示测量模式，后30位表示大小，这里需要右移两位才能拿到测量的大小
        int heightSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightSpec);
    }
}
