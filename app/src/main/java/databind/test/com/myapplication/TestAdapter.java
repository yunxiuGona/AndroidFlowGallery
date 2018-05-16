package databind.test.com.myapplication;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Gallery;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import databind.test.com.myapplication.R;
import databind.test.com.myapplication.view.MyGalleryView;

/**
 * Created by wangyunxiu on 2018/5/16.
 */

public class TestAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnScrollChangeListener {

    private Context context;
    private RecyclerView recyclerView;
    private static final int TYPE_NORMAL=0x01;
    private static final int TYPE_GALLERY=0x02;
    private int screenHeight=0;
    List<Integer> array_pos=new ArrayList<>();
    public TestAdapter(Context context,RecyclerView recyclerView) {
        this.context = context;
        this.recyclerView=recyclerView;
        this.recyclerView.setOnScrollChangeListener(this);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        this.screenHeight= dm.heightPixels;
        //TESTDATA
        array_pos.add(30);
        array_pos.add(31);
        array_pos.add(32);
        array_pos.add(33);
        array_pos.add(50);
    }

    public boolean isGalleryPosition(int position){
        return array_pos.contains(position);
    }

    @Override
    public int getItemViewType(int position) {
        if(isGalleryPosition(position))
            return TYPE_GALLERY;
        else
            return TYPE_NORMAL;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==TYPE_NORMAL){
            return new ViewHolderNormal(LayoutInflater.from(context).inflate(R.layout.holder_normal,parent,false));
        }
        if(viewType==TYPE_GALLERY){
            return new ViewHolderGallery(LayoutInflater.from(context).inflate(R.layout.holder_gallery,parent,false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder hd, int position) {
        if(hd instanceof ViewHolderNormal){
            ViewHolderNormal holder=(ViewHolderNormal)hd;
            holder.tv_text.setText("Current Item Position Is:"+position);
        }
        if(hd instanceof ViewHolderGallery){
            ViewHolderGallery holder=(ViewHolderGallery)hd;
            holder.tv_text.setText("Current Item Position Is:"+position);
            holder.gallery.setFirstImageScal(0.95f);
        }
    }

    @Override
    public int getItemCount() {
        return 1000;
    }

    @Override
    public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        for (int i=0;i<array_pos.size();i++){
            int aimpositio=array_pos.get(i);
            int firstPosition = ((LinearLayoutManager)recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
            int lastPosition = ((LinearLayoutManager)recyclerView.getLayoutManager()).findLastVisibleItemPosition();
            if(aimpositio>firstPosition&&aimpositio<lastPosition){
                View vholder = recyclerView.getChildAt(aimpositio - firstPosition);
                if(vholder==null)
                    return;
                int location[]=new int[2];
                vholder.getLocationOnScreen(location);
                int dim_area_top=500;
                int dim_area_bottom=screenHeight-500;
                View view = vholder.findViewById(R.id.gallery);
                if(view!=null) {
                    int progress=(int) (((float) (dim_area_bottom - location[1]) / (float) (dim_area_bottom - dim_area_top)) * 100.0f);
                    ((MyGalleryView)view).setProcess(progress);
                }
            }
        }
    }
    public class ViewHolderNormal extends RecyclerView.ViewHolder{
        public TextView tv_text;
        public ViewHolderNormal(View itemView) {
            super(itemView);
            tv_text=(TextView)itemView.findViewById(R.id.tv_text);
        }
    }
    public class ViewHolderGallery extends RecyclerView.ViewHolder{
        public MyGalleryView gallery;
        public TextView tv_text;
        public ViewHolderGallery(View itemView) {
            super(itemView);
            gallery=(MyGalleryView)itemView.findViewById(R.id.gallery);
            tv_text=(TextView)itemView.findViewById(R.id.tv_text);
        }
    }
}
