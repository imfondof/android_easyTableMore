package noteFragments;

import java.util.Date;

import utils.SQLiteDBUtil;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.example.easytablemore.BaseFragment;
import com.example.easytablemore.R;

public class NoteAddFragment extends BaseFragment {

	private View rootview;
	private EditText note_add_title, note_add_course, note_add_content;
	private Button note_add_btn;
	// 用于获取当前用户的账号--进一步来保存用户的相关课程--以及显示当前的课程是此用户的
	private SharedPreferences pref;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootview = inflater.inflate(R.layout.fragment_note_add, container,
				false);
		// TODO Auto-generated method stub
		return rootview;
	}

	@Override
	protected void initView(View view) {
		// TODO Auto-generated method stub
		super.initView(view);
		note_add_title = (EditText) rootview.findViewById(R.id.note_add_title);
		note_add_course = (EditText) rootview
				.findViewById(R.id.note_add_course);
		note_add_content = (EditText) rootview
				.findViewById(R.id.note_add_content);

		note_add_btn = (Button) rootview.findViewById(R.id.note_add_btn);

	}

	@Override
	protected void initEvents() {
		// TODO Auto-generated method stub
		super.initEvents();

		note_add_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				String title = note_add_title.getText().toString();
				pref = getActivity().getSharedPreferences("data",
						Context.MODE_PRIVATE);
				String account = pref.getString("name", "150060408");
				String course = note_add_course.getText().toString();
				String content = note_add_content.getText().toString();
				Date time = new Date();

				if("".equals(title)){
					show("标题为必输入项哦");
				}else{
					SQLiteDBUtil db = new SQLiteDBUtil(getActivity()
							.getApplicationContext());
					SQLiteDatabase sd = db.getWritableDatabase();
					String sql = "insert into note values(null,?,?,?,?,?)";
					sd.execSQL(sql, new Object[] { account, title, course, content,
							time });
					// 进行关闭
					db.close();
					show("添加成功！");
					
					
					
	                //发送广播
	                Intent broadCastIntent = new Intent("jack");
	                broadCastIntent.putExtra("change", "noteAdd");
	                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(broadCastIntent);
				    //自动返回
	                getFragmentManager().popBackStack();
				}
				


			}
		});
	}

	// 在onviewcreated里添加 左上角的单击 返回事件
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		view.findViewById(R.id.image_back).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						getFragmentManager().popBackStack();
					}
				});
	}
}
