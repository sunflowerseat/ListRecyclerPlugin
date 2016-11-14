package com.fancy.adapterutil.util;

import android.content.res.ColorStateList;

/**
 * Created by fancy on 2016/9/28.
 */

public class BackgroundTintUtil {
    /** color 为16进制颜色，如 0xff000000*/
    public static ColorStateList getColorTint(int color) {
        int[] colors = new int[]{color};
        int[][] states = new int[1][];
        states[0] = new int[]{android.R.attr.state_enabled};
        return new ColorStateList(states, colors);
    }
}
