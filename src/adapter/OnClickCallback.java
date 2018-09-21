package adapter;

import android.view.View;

/**
 * Created by lc on 2018/7/2.
 */

public interface OnClickCallback {

    //
    void onClick(View v, int position, BaseViewHolder viewHolder);

    //
    boolean onLongClick(View v, int position, BaseViewHolder viewHolder);
}
