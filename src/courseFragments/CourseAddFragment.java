package courseFragments;

import message.Constant;
import userFragments.InformationFragment;
import utils.SQLiteDBUtil;

import com.example.easytablemore.BaseFragment;
import com.example.easytablemore.R;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class CourseAddFragment extends BaseFragment {

	private View rootview;
	// 用于获取当前用户账号，来保存此账号的课程
	private SharedPreferences pref;
	private EditText add_txt_name, add_txt_place, add_txt_teacher,
			add_txt_weeks, add_txt_week, add_txt_jie;

	private Button btn_add;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootview = inflater.inflate(R.layout.fragment_course_add, container,
				false);
		// TODO Auto-generated method stub
		return rootview;
	}

	@Override
	protected void initView(View view) {
		// TODO Auto-generated method stub
		super.initView(view);

		add_txt_name = (EditText) rootview.findViewById(R.id.add_txt_name);
		add_txt_place = (EditText) rootview.findViewById(R.id.add_txt_place);
		add_txt_teacher = (EditText) rootview
				.findViewById(R.id.add_txt_teacher);
		add_txt_weeks = (EditText) rootview.findViewById(R.id.add_txt_weeks);
		add_txt_week = (EditText) rootview.findViewById(R.id.add_txt_week);
		add_txt_jie = (EditText) rootview.findViewById(R.id.add_txt_jie);

		btn_add = (Button) rootview.findViewById(R.id.btn_add);
	}

	@Override
	protected void initEvents() {
		// TODO Auto-generated method stub
		super.initEvents();

		String courseId = getArguments().getString("extra_courseid");
		final int getCourseId = Integer.valueOf(courseId);
		int text_week = getCourseId / 100;
		int text_jie = getCourseId % 100;
		add_txt_week.setText("第" + text_week + "周");
		add_txt_jie.setText("第" + text_jie + "节");

		pref = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
		final String current_accoutnt = pref.getString("name", "150060408");// 这里name就是用户当前的账号啦

		btn_add.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String add_name = add_txt_name.getText().toString();
				String add_place = add_txt_place.getText().toString();
				String add_teacher = add_txt_teacher.getText().toString();
				String add_weeks = add_txt_weeks.getText().toString();

				if (!"".equals(add_name)) {// 如果用户连课程名都没有输入的话，那就必须提示用户
					SQLiteDBUtil db = new SQLiteDBUtil(getActivity()
							.getApplicationContext());
					SQLiteDatabase sd = db.getWritableDatabase();
					String sql = "insert into kebiao values(null,?,?,?,?,?,?,?)";
					// sd.execSQL(sql, new
					// Object[]{account,id,add_name,add_teacher,add_place,term,add_weeks});
					sd.execSQL(sql, new Object[] { current_accoutnt,
							getCourseId, add_name, add_teacher, add_place,
							readCurrrentTerm(),// 这个学期是当前学期哦
							add_weeks });
					// 进行关闭
					db.close();
					show("添加成功！");
					Intent intent = new Intent("jerry");
					intent.putExtra("change", "courseAdd");
					LocalBroadcastManager.getInstance(getActivity())
							.sendBroadcast(intent);

					// 自动返回
					getFragmentManager().popBackStack();
				} else {// 如果为空的话，就提示用户没有输入东西
					show("请务必输入添加的课程名字哦！");
				}
			}
		});

	}

	private int readCurrrentTerm() {
		pref = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
		String current_accoutnt = pref.getString("name", "150060408");// 这里name就是用户当前的账号啦
		String termKey = current_accoutnt + Constant.CURRENT_TERM; // /对学期的键进行加固，这样每个账号只会对应上自己的学期
		int currentTerm = pref.getInt(termKey, 1);// 键统一保存在constant类里边了
		return currentTerm;
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
