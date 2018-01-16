package mradmin.example.com.datetimeapp.util;

import android.content.Context;
import android.content.res.TypedArray;

import mradmin.example.com.datetimeapp.R;

public class Utils {

    public static int getToolbarHeight(Context context) {
        final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(
                new int[]{R.attr.actionBarSize});
        int toolbarHeight = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();

        return toolbarHeight;
    }
}
