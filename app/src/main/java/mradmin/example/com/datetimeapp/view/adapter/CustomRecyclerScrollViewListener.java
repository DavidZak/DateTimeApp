package mradmin.example.com.datetimeapp.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;

public abstract class CustomRecyclerScrollViewListener extends RecyclerView.OnScrollListener {
    int scrollDist = 0;
    boolean isVisible = true;
    static final float MINIMUM = 20;

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        if(isVisible && scrollDist>MINIMUM){
            hide();
            scrollDist = 0;
            isVisible = false;
        }
        else if(!isVisible && scrollDist < -MINIMUM){
            show();
            scrollDist = 0;
            isVisible = true;
        }
        if((isVisible && dy>0) || (!isVisible && dy<0)){
            scrollDist += dy;
        }
        if (recyclerView.getAdapter().getItemCount() < 10){
            show();
        }
    }
    public abstract void show();
    public abstract void hide();
}
