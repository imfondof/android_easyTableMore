package adapter;

import android.content.Context;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class BaseViewHolder implements View.OnClickListener, View.OnLongClickListener{
	private static String tag = BaseViewHolder.class.getSimpleName();
    private SparseArray<View> views = new SparseArray<View>();//用来缓存view的容器*******************
    private int position;
    private OnClickCallback callback;
    private Context context;
    private int layoutId;
    private View convertView;

    public BaseViewHolder(ViewGroup parent, Context context, int layoutId, OnClickCallback callback) {
        this.callback = callback;
        this.layoutId = layoutId;
        this.context = context;
        convertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        convertView.setTag(this);
    }

    public View getConvertView() {
        return convertView;
    }


    //泛型方法,用来找到viewId所对应的view,然后缓存,为了以后使用
    public <T extends View> T getView(int viewId) {
        View view = views.get(viewId);
        if (view == null) {
            view = convertView.findViewById(viewId);
            views.put(viewId, view);
        }

        return (T) views.get(viewId);
    }

    public void setText(int viewId, int textRes) {
        View view = getView(viewId);
        if (isIllegal(viewId, view)) {
            return;
        }
        //view 他爹是不是TextView或者TextView儿子,孙子....
        if (view instanceof TextView) {//instanceof: 判断前面的对象是不是后面的类或子类的对象
            TextView textView = (TextView) view;
            textView.setText(textRes);
        }
    }

    public void setImg(int viewId, int imgRes) {
        View view = getView(viewId);
        if (isIllegal(viewId, view)) {
            return;
        }
        if (view instanceof ImageView) {
            ImageView imageView = (ImageView) view;
            imageView.setImageResource(imgRes);
        }
    }

    public void setOnClickListener(int viewId) {
        View view = getView(viewId);
        if (isIllegal(viewId, view)) {
            return;
        }
       view.setOnClickListener( this);
    }

    public void setOnLongClickListener(int viewId) {
        View view = getView(viewId);
        if (isIllegal(viewId, view)) {
            return;
        }
        view.setOnLongClickListener( this);
    }

    //给整个convertView设置长按事件
    public void setOnItemLongClickListener() {
        convertView.setOnLongClickListener( this);
    }


    //给整个convertView设置短按事件
    public void setOnItemClickListener() {
        convertView.setOnClickListener( this);
    }

    private boolean isIllegal(int viewId, View view) {
        if (view == null) {
            Log.e(tag, "error>>>  is incorrect " + viewId);
            return true;
        }
        return false;
    }


    public void setText( int viewId, String text) {
        View view = getView(viewId);
        if (isIllegal(viewId, view)) {
            return;
        }
        //view 他爹是不是TextView或者TextView儿子,孙子....
        if (view instanceof TextView) {//instanceof: 判断前面的对象是不是后面的类或子类的对象
            TextView textView = (TextView) view;
            textView.setText(text);
        }
    }


    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void onClick(View v) {
        if (callback != null) {
            callback.onClick(v, position, this);
        }
    }

    public boolean onLongClick(View v) {
        return callback != null && callback.onLongClick(v, position, this);
    }
}