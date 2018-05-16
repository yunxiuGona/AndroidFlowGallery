package databind.test.com.myapplication.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;

import databind.test.com.myapplication.R;

/**
 * Created by wangyunxiu on 2018/5/15.
 */

public class MyGalleryView extends ViewGroup {

    private int width = 0;
    private int height = 0;

    private int final_size_one = 0;
    private int final_size_two = 0;
    private int final_size_three = 0;

    private float recent_one = 0.6f;//Compare to view
    private float recent_two = 0.8f;//Compare to one
    private float recent_three = 0.6f;//Compare to two

    private int part_padding = 70;
    private int process = 0;
    private ImageView iv_one, iv_two, iv_three;

    public void setFirstImageScal(float scal_compare_to_view){
        this.recent_one=scal_compare_to_view;
        requestLayout();
    }

    public MyGalleryView(Context context) {
        super(context);
        initView();
    }

    public MyGalleryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public MyGalleryView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public MyGalleryView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    private void initView() {
        iv_one = new ImageView(getContext());
        iv_two = new ImageView(getContext());
        iv_three = new ImageView(getContext());

        iv_one.setScaleType(ImageView.ScaleType.CENTER_CROP);
        iv_two.setScaleType(ImageView.ScaleType.CENTER_CROP);
        iv_three.setScaleType(ImageView.ScaleType.CENTER_CROP);

        MyGalleryView.this.addView(iv_three);
        MyGalleryView.this.addView(iv_two);
        MyGalleryView.this.addView(iv_one);

        iv_one.setImageResource(R.drawable.a);
        iv_two.setImageResource(R.drawable.b);
        iv_three.setImageResource(R.drawable.c);
    }

    public int getCurrentSize(int from, int to, int process) {
        return (int) (((float) to - (float) from) * ((float) process / 100.0f));
    }

    public float getFinalFirstLeft(int width) {
//        return (width - final_size_one) / 2.0f;
        return height*(1-recent_one)/2.0f;
    }

    public float getFinalFirstRight(int width) {
        return getFinalFirstLeft(width) + final_size_one;
    }

    public float getFinalSecondRight(int width) {
        return getFinalFirstRight(width) + part_padding;
    }

    public float getFinalSecondLeft(int width) {
        return getFinalSecondRight(width) - final_size_two;
    }

    public float getFinalThirdRight(int width) {
        return getFinalSecondRight(width) + part_padding;
    }

    public float getFinalThirdLeft(int width) {
        return getFinalThirdRight(width) - final_size_three;
    }

    public void bringToFront(ImageView iv1,ImageView iv2,ImageView iv3){
        float a=iv1.getLayoutParams().width;
        float b=iv2.getLayoutParams().width;
        float c=iv3.getLayoutParams().width;
        //MAX
        float arr[]={a,b,c};
        float vaules[] = bubble(arr);

        for (int i=0;i<vaules.length;i++){
            if(a==vaules[i]){
                iv1.bringToFront();
            }
            if(b==vaules[i]){
                iv2.bringToFront();
            }
            if(c==vaules[i]){
                iv3.bringToFront();
            }
        }
    }

    //冒泡排序
    public float[] bubble(float arr[]){
        for(int i=0;i<arr.length-1;i++){
            for(int j=0;j<arr.length-1-i;j++){
                if(arr[j]>arr[j+1]){
                    float temp=arr[j];
                    arr[j]=arr[j+1];
                    arr[j+1]=temp;
                }
            }
        }
        return arr;
    }

    public void processFirstToDismiss(ImageView view, int process) {
        int tmp_size = final_size_one;
        if(process<50){
            //减权
            view.setAlpha(1 - (float) process / 100.0f);//*2加权
        }else{
            //加权
            view.setAlpha(1 - (float) process / 100.0f);//*2加权
        }

        view.setLayoutParams(new LayoutParams(tmp_size, tmp_size));
        int left_tmp = (int) getFinalFirstLeft(width);
        int right_tmp = left_tmp + tmp_size;
        int top_tmp = (int) ((height - tmp_size) / 2.0f);
        int bottom_tmp = left_tmp + tmp_size;
        view.layout(left_tmp, top_tmp, right_tmp, bottom_tmp);
    }

    public void processThirdToAppear(ImageView view, int process) {
        int tmp_size = final_size_three;
        view.setAlpha((float) process / 100.0f);
        view.setLayoutParams(new LayoutParams(tmp_size, tmp_size));
        int right_tmp = (int)getFinalThirdRight(width);
        int left_tmp = (int)getFinalThirdLeft(width);
        int top_tmp = (int) ((height - tmp_size) / 2.0f);
        int bottom_tmp = top_tmp + tmp_size;
        view.layout(left_tmp, top_tmp, right_tmp, bottom_tmp);
    }

    public void processSecondToFirst(ImageView view, int proc) {
        view.setAlpha(1.0f);
        float process = proc / 100.0f;
        int tmp_size = (int) (final_size_two + (final_size_one - final_size_two) * process);
        view.setLayoutParams(new LayoutParams(tmp_size, tmp_size));
        int left_tmp = (int) (getFinalSecondLeft(width) - (getFinalSecondLeft(width) - getFinalFirstLeft(width) )* process);
        int right_tmp = (int) (getFinalSecondRight(width) - (getFinalSecondRight(width) - getFinalFirstRight(width)) * process);
        int top_tmp = (int) ((height - tmp_size) / 2.0f);
        int bottom_tmp = top_tmp + tmp_size;
        view.layout(left_tmp, top_tmp, right_tmp, bottom_tmp);
    }

    public void processThirdToSecond(ImageView view, int proc) {
        view.setAlpha(1.0f);
        float process = proc / 100.0f;
        int tmp_size = (int) (final_size_three + (final_size_two - final_size_three) * process);
        view.setLayoutParams(new LayoutParams(tmp_size, tmp_size));
        int left_tmp = (int) (getFinalThirdLeft(width) - ((getFinalThirdLeft(width) - getFinalSecondLeft(width)) * process));
        int right_tmp = (int) (getFinalThirdRight(width) - (getFinalThirdRight(width) - getFinalSecondRight(width)) * process);
        int top_tmp = (int) ((height - tmp_size) / 2.0f);
        int bottom_tmp = top_tmp + tmp_size;
        view.layout(left_tmp, top_tmp, right_tmp, bottom_tmp);
    }

    private int getPersent(int min,int max,int process){
        return (int)((((float)process-(float)min)/((float)max-(float)min))*100.0f);
    }

    public void setUpView(int process, int l, int t, int r, int b) {
        final_size_one = (int) (height * recent_one);
        final_size_two = (int) ((float)final_size_one * (float)recent_two);
        final_size_three = (int) ((float)final_size_one * (float)recent_three);

        if(process>=0&&process<=16){
            //1-16  iv1-disappear  iv2-to1  iv3-to2
            processFirstToDismiss(iv_one,getPersent(0,16,process));
            processSecondToFirst(iv_two,getPersent(0,34,process));
            processThirdToSecond(iv_three,getPersent(0,34,process));
        }else if(process<=34){
            //17-34  iv1-appear  iv2-to1  iv3-to2
            processThirdToAppear(iv_one, getPersent(17,34,process));
            processSecondToFirst(iv_two,getPersent(0,34,process));
            processThirdToSecond(iv_three,getPersent(0,34,process));
            iv_three.bringToFront();
            iv_two.bringToFront();
        }else if(process<=51){
            //35-51  iv2-disappear iv1-to2  iv3-to1
            processFirstToDismiss(iv_two,getPersent(35,51,process));
            processSecondToFirst(iv_three,getPersent(35,68,process));
            processThirdToSecond(iv_one,getPersent(35,68,process));
        }else if(process<=68){
            //52-68  iv2-appear  iv1-to2  i3-to1
            processThirdToAppear(iv_two,getPersent(52,68,process));
            processSecondToFirst(iv_three,getPersent(35,68,process));
            processThirdToSecond(iv_one,getPersent(35,68,process));
            iv_one.bringToFront();
            iv_three.bringToFront();
        }else if(process<=85){
            //69-85  iv3-disappear iv2-to2  iv1-to1
            processFirstToDismiss(iv_three,getPersent(69,85,process));
            processSecondToFirst(iv_one,getPersent(69,100,process));
            processThirdToSecond(iv_two,getPersent(69,100,process));
        }else if(process<=100){
            //86-100  iv3-appear iv2-to2  iv1-to1
            processThirdToAppear(iv_three,getPersent(86,100,process));
            processSecondToFirst(iv_one,getPersent(69,100,process));
            processThirdToSecond(iv_two,getPersent(69,100,process));
            iv_two.bringToFront();
            iv_one.bringToFront();
        }
        bringToFront(iv_one,iv_two,iv_three);
    }

    public void setProcess(int process) {
        if(process<0){process=0;}
        if(process>100){process=100;}
        this.process = process;
       requestLayout();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        width = r - l;
        height = b - t;
        setUpView(this.process, l, t, r, b);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }
}
