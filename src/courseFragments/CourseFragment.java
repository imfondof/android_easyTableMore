package courseFragments;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


import message.Constant;

import utils.SQLiteDBUtil;
import utils.Utility;

import bean.Course;
import bean.Note;

import com.example.easytablemore.BaseFragment;
import com.example.easytablemore.MainActivity;
import com.example.easytablemore.R;

import adapter.CommonAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class CourseFragment extends BaseFragment implements OnClickListener {

	private View rootview;
	// 用于获取当前用户的账号--进一步来保存用户的相关课程--以及显示当前的课程是此用户的
	private SharedPreferences pref;
	

	
	LocalBroadcastManager broadcastManager;
	boolean isRegster = false;// 定义全局变量，判断是否注册了广播。

	private Button btn_1_1, btn_2_1, btn_3_1, btn_4_1, btn_5_1, btn_6_1,
			btn_7_1;
	private Button btn_1_2, btn_2_2, btn_3_2, btn_4_2, btn_5_2, btn_6_2,
			btn_7_2;
	private Button btn_1_3, btn_2_3, btn_3_3, btn_4_3, btn_5_3, btn_6_3,
			btn_7_3;
	private Button btn_1_4, btn_2_4, btn_3_4, btn_4_4, btn_5_4, btn_6_4,
			btn_7_4;
	private Button btn_1_5, btn_2_5, btn_3_5, btn_4_5, btn_5_5, btn_6_5,
			btn_7_5;
	private Button btn_1_6, btn_2_6, btn_3_6, btn_4_6, btn_5_6, btn_6_6,
			btn_7_6;
	private Button btn_1_7, btn_2_7, btn_3_7, btn_4_7, btn_5_7, btn_6_7,
			btn_7_7;
	private Button btn_1_8, btn_2_8, btn_3_8, btn_4_8, btn_5_8, btn_6_8,
			btn_7_8;
	private Button btn_1_9, btn_2_9, btn_3_9, btn_4_9, btn_5_9, btn_6_9,
			btn_7_9;
	private Button btn_1_10, btn_2_10, btn_3_10, btn_4_10, btn_5_10, btn_6_10,
			btn_7_10;
	private Button btn_1_11, btn_2_11, btn_3_11, btn_4_11, btn_5_11, btn_6_11,
			btn_7_11;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootview = inflater.inflate(R.layout.fragment_course, container, false);
		// TODO Auto-generated method stub

		// 注册广播 在这里注册，确保他只执行一次。。
		if (!isRegster) {// 如果没注册，就注册，注册过就不重复注册了。
			registerReceiver();
			isRegster = true;// 注册完后，更改为已经注册
		}
		return rootview;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);

		// 初始化接受数据
		receiveData();
		// 初始化按钮，这个按钮若没有课程的话，那么点击时，会跳到添加课程的界面
		initAllButton();

	}

	@Override
	protected void initView(View view) {
		super.initView(view);
		


		// 第1行按钮 id_btn_列_行
		btn_1_1 = (Button) rootview.findViewById(R.id.id_btn_1_1);
		btn_2_1 = (Button) rootview.findViewById(R.id.id_btn_2_1);
		btn_3_1 = (Button) rootview.findViewById(R.id.id_btn_3_1);
		btn_4_1 = (Button) rootview.findViewById(R.id.id_btn_4_1);
		btn_5_1 = (Button) rootview.findViewById(R.id.id_btn_5_1);
		btn_6_1 = (Button) rootview.findViewById(R.id.id_btn_6_1);
		btn_7_1 = (Button) rootview.findViewById(R.id.id_btn_7_1);
		// 第2行按钮 id_btn_列_行
		btn_1_2 = (Button) rootview.findViewById(R.id.id_btn_1_2);
		btn_2_2 = (Button) rootview.findViewById(R.id.id_btn_2_2);
		btn_3_2 = (Button) rootview.findViewById(R.id.id_btn_3_2);
		btn_4_2 = (Button) rootview.findViewById(R.id.id_btn_4_2);
		btn_5_2 = (Button) rootview.findViewById(R.id.id_btn_5_2);
		btn_6_2 = (Button) rootview.findViewById(R.id.id_btn_6_2);
		btn_7_2 = (Button) rootview.findViewById(R.id.id_btn_7_2);
		// 第3行按钮 id_btn_列_行
		btn_1_3 = (Button) rootview.findViewById(R.id.id_btn_1_3);
		btn_2_3 = (Button) rootview.findViewById(R.id.id_btn_2_3);
		btn_3_3 = (Button) rootview.findViewById(R.id.id_btn_3_3);
		btn_4_3 = (Button) rootview.findViewById(R.id.id_btn_4_3);
		btn_5_3 = (Button) rootview.findViewById(R.id.id_btn_5_3);
		btn_6_3 = (Button) rootview.findViewById(R.id.id_btn_6_3);
		btn_7_3 = (Button) rootview.findViewById(R.id.id_btn_7_3);
		// 第4行按钮 id_btn_列_行
		btn_1_4 = (Button) rootview.findViewById(R.id.id_btn_1_4);
		btn_2_4 = (Button) rootview.findViewById(R.id.id_btn_2_4);
		btn_3_4 = (Button) rootview.findViewById(R.id.id_btn_3_4);
		btn_4_4 = (Button) rootview.findViewById(R.id.id_btn_4_4);
		btn_5_4 = (Button) rootview.findViewById(R.id.id_btn_5_4);
		btn_6_4 = (Button) rootview.findViewById(R.id.id_btn_6_4);
		btn_7_4 = (Button) rootview.findViewById(R.id.id_btn_7_4);
		// 第5行按钮 id_btn_列_行
		btn_1_5 = (Button) rootview.findViewById(R.id.id_btn_1_5);
		btn_2_5 = (Button) rootview.findViewById(R.id.id_btn_2_5);
		btn_3_5 = (Button) rootview.findViewById(R.id.id_btn_3_5);
		btn_4_5 = (Button) rootview.findViewById(R.id.id_btn_4_5);
		btn_5_5 = (Button) rootview.findViewById(R.id.id_btn_5_5);
		btn_6_5 = (Button) rootview.findViewById(R.id.id_btn_6_5);
		btn_7_5 = (Button) rootview.findViewById(R.id.id_btn_7_5);
		// 第6行按钮 id_btn_列_行
		btn_1_6 = (Button) rootview.findViewById(R.id.id_btn_1_6);
		btn_2_6 = (Button) rootview.findViewById(R.id.id_btn_2_6);
		btn_3_6 = (Button) rootview.findViewById(R.id.id_btn_3_6);
		btn_4_6 = (Button) rootview.findViewById(R.id.id_btn_4_6);
		btn_5_6 = (Button) rootview.findViewById(R.id.id_btn_5_6);
		btn_6_6 = (Button) rootview.findViewById(R.id.id_btn_6_6);
		btn_7_6 = (Button) rootview.findViewById(R.id.id_btn_7_6);
		// 第7行按钮 id_btn_列_行
		btn_1_7 = (Button) rootview.findViewById(R.id.id_btn_1_7);
		btn_2_7 = (Button) rootview.findViewById(R.id.id_btn_2_7);
		btn_3_7 = (Button) rootview.findViewById(R.id.id_btn_3_7);
		btn_4_7 = (Button) rootview.findViewById(R.id.id_btn_4_7);
		btn_5_7 = (Button) rootview.findViewById(R.id.id_btn_5_7);
		btn_6_7 = (Button) rootview.findViewById(R.id.id_btn_6_7);
		btn_7_7 = (Button) rootview.findViewById(R.id.id_btn_7_7);
		// 第8行按钮 id_btn_列_行
		btn_1_8 = (Button) rootview.findViewById(R.id.id_btn_1_8);
		btn_2_8 = (Button) rootview.findViewById(R.id.id_btn_2_8);
		btn_3_8 = (Button) rootview.findViewById(R.id.id_btn_3_8);
		btn_4_8 = (Button) rootview.findViewById(R.id.id_btn_4_8);
		btn_5_8 = (Button) rootview.findViewById(R.id.id_btn_5_8);
		btn_6_8 = (Button) rootview.findViewById(R.id.id_btn_6_8);
		btn_7_8 = (Button) rootview.findViewById(R.id.id_btn_7_8);
		// 第9行按钮 id_btn_列_行
		btn_1_9 = (Button) rootview.findViewById(R.id.id_btn_1_9);
		btn_2_9 = (Button) rootview.findViewById(R.id.id_btn_2_9);
		btn_3_9 = (Button) rootview.findViewById(R.id.id_btn_3_9);
		btn_4_9 = (Button) rootview.findViewById(R.id.id_btn_4_9);
		btn_5_9 = (Button) rootview.findViewById(R.id.id_btn_5_9);
		btn_6_9 = (Button) rootview.findViewById(R.id.id_btn_6_9);
		btn_7_9 = (Button) rootview.findViewById(R.id.id_btn_7_9);
		// 第10行按钮 id_btn_列_行
		btn_1_10 = (Button) rootview.findViewById(R.id.id_btn_1_10);
		btn_2_10 = (Button) rootview.findViewById(R.id.id_btn_2_10);
		btn_3_10 = (Button) rootview.findViewById(R.id.id_btn_3_10);
		btn_4_10 = (Button) rootview.findViewById(R.id.id_btn_4_10);
		btn_5_10 = (Button) rootview.findViewById(R.id.id_btn_5_10);
		btn_6_10 = (Button) rootview.findViewById(R.id.id_btn_6_10);
		btn_7_10 = (Button) rootview.findViewById(R.id.id_btn_7_10);
		// 第11行按钮 id_btn_列_行
		btn_1_11 = (Button) rootview.findViewById(R.id.id_btn_1_11);
		btn_2_11 = (Button) rootview.findViewById(R.id.id_btn_2_11);
		btn_3_11 = (Button) rootview.findViewById(R.id.id_btn_3_11);
		btn_4_11 = (Button) rootview.findViewById(R.id.id_btn_4_11);
		btn_5_11 = (Button) rootview.findViewById(R.id.id_btn_5_11);
		btn_6_11 = (Button) rootview.findViewById(R.id.id_btn_6_11);
		btn_7_11 = (Button) rootview.findViewById(R.id.id_btn_7_11);
	}

	@Override
	protected void initEvents() {
		// TODO Auto-generated method stub
		super.initEvents();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	protected void receiveData() {

		SQLiteDBUtil db = new SQLiteDBUtil(getActivity()
				.getApplicationContext());
		SQLiteDatabase sd = db.getReadableDatabase();
		String sql = "select * from kebiao";
		Cursor cursor = sd.rawQuery(sql, null);
		while (cursor.moveToNext()) {
			// account必须是当前的账户。。。。从数据库中选择出来的数据，筛选出此账户的数据，然后显示在界面上-----
			// ---------------------学期也是这样，，没时间写呢

			pref = getActivity().getSharedPreferences("data",
					Context.MODE_PRIVATE);
			String name = pref.getString("name", "150060408");
			String receive_account = cursor.getString(1);
			int term = readCurrrentTerm();
			int receive_term = cursor.getInt(6);

			if (name.equals(receive_account) && term == receive_term) {// 判断读取出来的数据是不是当前登录用户的，是不是当前学期的课程。。是的话就显示
				int receive_courseId = cursor.getInt(2);
				String receive_name = cursor.getString(3);
				String receive_teacher = cursor.getString(4);
				String receive_place = cursor.getString(5);
				String receive_week = cursor.getString(7);

				Course receive_course = new Course(receive_account,
						receive_courseId, receive_name, receive_teacher,
						receive_place, receive_term, receive_week);
				showCourse(receive_course);
			}
		}
		// 关闭 不这样设置会报一大串错误，但运行并不会出错
		cursor.close();
		db.close();

	}

	protected void initAllButton() {
		Map<String, Button> map = new HashMap<String, Button>();
		// 将所有的button进行编号，以字符串的形式；
		String[] strings = { "101", "102", "103", "104", "105", "106", "107",
				"108", "109", "110", "111", "201", "202", "203", "204", "205",
				"206", "207", "208", "209", "210", "211", "301", "302", "303",
				"304", "305", "306", "307", "308", "309", "310", "311", "401",
				"402", "403", "404", "405", "406", "407", "408", "409", "410",
				"411", "501", "502", "503", "504", "505", "506", "507", "508",
				"509", "510", "511", "601", "602", "603", "604", "605", "606",
				"607", "608", "609", "610", "611", "701", "702", "703", "704",
				"705", "706", "707", "708", "709", "710", "711" };
		Button[] buttons = { btn_1_1, btn_1_2, btn_1_3, btn_1_4, btn_1_5,
				btn_1_6, btn_1_7, btn_1_8, btn_1_9, btn_1_10, btn_1_11,
				btn_2_1, btn_2_2, btn_2_3, btn_2_4, btn_2_5, btn_2_6, btn_2_7,
				btn_2_8, btn_2_9, btn_2_10, btn_2_11, btn_3_1, btn_3_2,
				btn_3_3, btn_3_4, btn_3_5, btn_3_6, btn_3_7, btn_3_8, btn_3_9,
				btn_3_10, btn_3_11, btn_4_1, btn_4_2, btn_4_3, btn_4_4,
				btn_4_5, btn_4_6, btn_4_7, btn_4_8, btn_4_9, btn_4_10,
				btn_4_11, btn_5_1, btn_5_2, btn_5_3, btn_5_4, btn_5_5, btn_5_6,
				btn_5_7, btn_5_8, btn_5_9, btn_5_10, btn_5_11, btn_6_1,
				btn_6_2, btn_6_3, btn_6_4, btn_6_5, btn_6_6, btn_6_7, btn_6_8,
				btn_6_9, btn_6_10, btn_6_11, btn_7_1, btn_7_2, btn_7_3,
				btn_7_4, btn_7_5, btn_7_6, btn_7_7, btn_7_8, btn_7_9, btn_7_10,
				btn_7_11 };
		// 将所有的button与编号存储在map里
		for (int i = 0; i < strings.length; i++) {
			map.put(strings[i], buttons[i]);
		}

		// 通过遍历所有的键，，遍历所有的button， 如果按钮的文本为空的话就跳转到添加课程的界面
		for (final String key : map.keySet()) {
			Button b = map.get(key);
			if ("".equals(b.getText())) {
				b.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {

						FragmentTransaction transaction = getActivity()
								.getSupportFragmentManager().beginTransaction();
						CourseAddFragment fragment = new CourseAddFragment();
						transaction.add(android.R.id.content, fragment, "aaa");
						transaction.addToBackStack("aaa");// 添加fragment到Activity的回退栈中
						transaction.show(fragment);

						// 传递值
						Bundle args = new Bundle();
						args.putString("extra_courseid", key);
						fragment.setArguments(args);

						transaction.commit();

					}
				});
			} else {// 若文本不是空的，那么说明这节有课。//暂时还不知道怎么写。。。
					// TODO
			}
		}

	}

	private void initAction(final Button btn, final Course c) {
		String content = c.getName();

		btn.setText(content);

		btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				FragmentTransaction transaction = getActivity()
						.getSupportFragmentManager().beginTransaction();
				CourseShowFragment fragment = new CourseShowFragment();
				transaction.add(android.R.id.content, fragment,
						"CourseShowFragment");
				transaction.addToBackStack("CourseShowFragment");// 添加fragment到Activity的回退栈中

				// 传递值
				Bundle args = new Bundle();
				args.putSerializable("course", c);
				fragment.setArguments(args);
				transaction.show(fragment);

				transaction.commit();
			}
		});
	}

	// 显示课程
	private void showCourse(final Course c) {
		if ("".equals(c.getName())) {
			return;
		}
		int day = c.getCourseId() / 100;
		int part = c.getCourseId() % 100;
		switch (day) {// 周几
		case 1:
			switch (part) {// 第几节课
			case 1:
				initAction(btn_1_1, c);
				break;
			case 2:
				initAction(btn_1_2, c);
				break;
			case 3:
				initAction(btn_1_3, c);
				break;
			case 4:
				initAction(btn_1_4, c);
				break;
			case 5:
				initAction(btn_1_5, c);
				break;
			case 6:
				initAction(btn_1_6, c);
				break;
			case 7:
				initAction(btn_1_7, c);
				break;
			case 8:
				initAction(btn_1_8, c);
				break;
			case 9:
				initAction(btn_1_9, c);
				break;
			case 10:
				initAction(btn_1_10, c);
				break;
			case 11:
				initAction(btn_1_11, c);
				break;

			default:
				break;
			}
			break;

		case 2:
			switch (part) {// 第几节课
			case 1:
				initAction(btn_2_1, c);
				break;
			case 2:
				initAction(btn_2_2, c);
				break;
			case 3:
				initAction(btn_2_3, c);
				break;
			case 4:
				initAction(btn_2_4, c);
				break;
			case 5:
				initAction(btn_2_5, c);
				break;
			case 6:
				initAction(btn_2_6, c);
				break;
			case 7:
				initAction(btn_2_7, c);
				break;
			case 8:
				initAction(btn_2_8, c);
				break;
			case 9:
				initAction(btn_2_9, c);
				break;
			case 10:
				initAction(btn_2_10, c);
				break;
			case 11:
				initAction(btn_2_11, c);
				break;

			default:
				break;
			}
			break;

		case 3:
			switch (part) {// 第几节课
			case 1:
				initAction(btn_3_1, c);
				break;
			case 2:
				initAction(btn_3_2, c);
				break;
			case 3:
				initAction(btn_3_3, c);
				break;
			case 4:
				initAction(btn_3_4, c);
				break;
			case 5:
				initAction(btn_3_5, c);
				break;
			case 6:
				initAction(btn_3_6, c);
				break;
			case 7:
				initAction(btn_3_7, c);
				break;
			case 8:
				initAction(btn_3_8, c);
				break;
			case 9:
				initAction(btn_3_9, c);
				break;
			case 10:
				initAction(btn_3_10, c);
				break;
			case 11:
				initAction(btn_3_11, c);
				break;

			default:
				break;
			}
			break;

		case 4:
			switch (part) {// 第几节课
			case 1:
				initAction(btn_4_1, c);
				break;
			case 2:
				initAction(btn_4_2, c);
				break;
			case 3:
				initAction(btn_4_3, c);
				break;
			case 4:
				initAction(btn_4_4, c);
				break;
			case 5:
				initAction(btn_4_5, c);
				break;
			case 6:
				initAction(btn_4_6, c);
				break;
			case 7:
				initAction(btn_4_7, c);
				break;
			case 8:
				initAction(btn_4_8, c);
				break;
			case 9:
				initAction(btn_4_9, c);
				break;
			case 10:
				initAction(btn_4_10, c);
				break;
			case 11:
				initAction(btn_4_11, c);
				break;

			default:
				break;
			}
			break;

		case 5:
			switch (part) {// 第几节课
			case 1:
				initAction(btn_5_1, c);
				break;
			case 2:
				initAction(btn_5_2, c);
				break;
			case 3:
				initAction(btn_5_3, c);
				break;
			case 4:
				initAction(btn_5_4, c);
				break;
			case 5:
				initAction(btn_5_5, c);
				break;
			case 6:
				initAction(btn_5_6, c);
				break;
			case 7:
				initAction(btn_5_7, c);
				break;
			case 8:
				initAction(btn_5_8, c);
				break;
			case 9:
				initAction(btn_5_9, c);
				break;
			case 10:
				initAction(btn_5_10, c);
				break;
			case 11:
				initAction(btn_5_11, c);
				break;

			default:
				break;
			}
			break;

		case 6:
			switch (part) {// 第几节课
			case 1:
				initAction(btn_6_1, c);
				break;
			case 2:
				initAction(btn_6_2, c);
				break;
			case 3:
				initAction(btn_6_3, c);
				break;
			case 4:
				initAction(btn_6_4, c);
				break;
			case 5:
				initAction(btn_6_5, c);
				break;
			case 6:
				initAction(btn_6_6, c);
				break;
			case 7:
				initAction(btn_6_7, c);
				break;
			case 8:
				initAction(btn_6_8, c);
				break;
			case 9:
				initAction(btn_6_9, c);
				break;
			case 10:
				initAction(btn_6_10, c);
				break;
			case 11:
				initAction(btn_6_11, c);
				break;

			default:
				break;
			}
			break;

		case 7:
			switch (part) {// 第几节课
			case 1:
				initAction(btn_7_1, c);
				break;
			case 2:
				initAction(btn_7_2, c);
				break;
			case 3:
				initAction(btn_7_3, c);
				break;
			case 4:
				initAction(btn_7_4, c);
				break;
			case 5:
				initAction(btn_7_5, c);
				break;
			case 6:
				initAction(btn_7_6, c);
				break;
			case 7:
				initAction(btn_7_7, c);
				break;
			case 8:
				initAction(btn_7_8, c);
				break;
			case 9:
				initAction(btn_7_9, c);
				break;
			case 10:
				initAction(btn_7_10, c);
				break;
			case 11:
				initAction(btn_7_11, c);
				break;

			default:
				break;
			}
			break;
		}
	}

	private int readCurrrentTerm() {
		pref = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
		String current_accoutnt = pref.getString("name", "150060408");// 这里name就是用户当前的账号啦
		String termKey = current_accoutnt + Constant.CURRENT_TERM; // /对学期的键进行加固，这样每个账号只会对应上自己的学期
		int currentTerm = pref.getInt(termKey, 1);// 键统一保存在constant类里边了
		return currentTerm;
	}

	/**
	 * 注册广播接收器
	 */
	private void registerReceiver() {
		broadcastManager = LocalBroadcastManager.getInstance(getActivity());
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("jerry");
		broadcastManager.registerReceiver(mAdDownLoadReceiver, intentFilter);
	}

	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
		broadcastManager.unregisterReceiver(mAdDownLoadReceiver);
	}

	private BroadcastReceiver mAdDownLoadReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String change = intent.getStringExtra("change");
			// 分别用这么多标签，是因为如果只用一个标签，每次点击其中一个，那么主界面将会更新好多号多次！！！你有几个界面是一样的标签他就更新几次！！
			if ("courseDelete".equals(change) // 删除课程传过来的广播
					|| "courseAdd".equals(change)// 添加课程传过来的广播
					|| "courseShow".equals(change)// 修改课程传过来的广播
					|| "deleteAll".equals(change)// 删除账号下所有课程传过来的广播
					// || "termDelete".equals(change)//
					// 删除选中的学期传过来的广播*****************这个目前不需要广播
					|| "termDetail".equals(change)) {// 修改当前学期传过来的广播
				// 这地方只能在主线程中刷新UI,子线程中无效，因此用Handler来实现
				new Handler().post(new Runnable() {
					public void run() {
						// 在这里来写你需要刷新的地方
						// 例如：testView.setText("恭喜你成功了");
						// show("啦啦啦啦，我要在主界面更新数据啦");
						clearAll();

						// 初始化接受数据
						receiveData();
						// 本来并不需要初始化所有button的，但是如果没有这个方法：：：：删除课程后，再点击删除课程的位置，会进入该课程详细信息界面！！！
						initAllButton();
					}
				});
			}
		}
	};

	// 进行广播后，需要对界面进行刷新，刷新的第一步是清除所有数据
	public void clearAll() {
		Map<String, Button> map = new HashMap<String, Button>();
		// 将所有的button进行编号，以字符串的形式；
		String[] strings = { "101", "102", "103", "104", "105", "106", "107",
				"108", "109", "110", "111", "201", "202", "203", "204", "205",
				"206", "207", "208", "209", "210", "211", "301", "302", "303",
				"304", "305", "306", "307", "308", "309", "310", "311", "401",
				"402", "403", "404", "405", "406", "407", "408", "409", "410",
				"411", "501", "502", "503", "504", "505", "506", "507", "508",
				"509", "510", "511", "601", "602", "603", "604", "605", "606",
				"607", "608", "609", "610", "611", "701", "702", "703", "704",
				"705", "706", "707", "708", "709", "710", "711" };
		Button[] buttons = { btn_1_1, btn_1_2, btn_1_3, btn_1_4, btn_1_5,
				btn_1_6, btn_1_7, btn_1_8, btn_1_9, btn_1_10, btn_1_11,
				btn_2_1, btn_2_2, btn_2_3, btn_2_4, btn_2_5, btn_2_6, btn_2_7,
				btn_2_8, btn_2_9, btn_2_10, btn_2_11, btn_3_1, btn_3_2,
				btn_3_3, btn_3_4, btn_3_5, btn_3_6, btn_3_7, btn_3_8, btn_3_9,
				btn_3_10, btn_3_11, btn_4_1, btn_4_2, btn_4_3, btn_4_4,
				btn_4_5, btn_4_6, btn_4_7, btn_4_8, btn_4_9, btn_4_10,
				btn_4_11, btn_5_1, btn_5_2, btn_5_3, btn_5_4, btn_5_5, btn_5_6,
				btn_5_7, btn_5_8, btn_5_9, btn_5_10, btn_5_11, btn_6_1,
				btn_6_2, btn_6_3, btn_6_4, btn_6_5, btn_6_6, btn_6_7, btn_6_8,
				btn_6_9, btn_6_10, btn_6_11, btn_7_1, btn_7_2, btn_7_3,
				btn_7_4, btn_7_5, btn_7_6, btn_7_7, btn_7_8, btn_7_9, btn_7_10,
				btn_7_11 };
		// 将所有的button与编号存储在map里
		for (int i = 0; i < strings.length; i++) {
			map.put(strings[i], buttons[i]);
		}
		// 通过遍历所有的键，，遍历所有的button， 清空所有内容
		for (final String key : map.keySet()) {
			Button b = map.get(key);
			b.setText("");
		}
	}
	
	

}
