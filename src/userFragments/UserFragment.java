package userFragments;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import utils.HorizontalProgressBarWithNumber;
import utils.SQLiteDBUtil;
import utils.Utility;

import com.example.easytablemore.BaseFragment;
import com.example.easytablemore.LoginActivity;
import com.example.easytablemore.R;

import courseFragments.CourseShowFragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class UserFragment extends BaseFragment implements OnClickListener {

	private Button btn_exit;
	private Button btn_genggaixueqi;
	private Button btn_count_text;
	private Button btn_want;
	private Button btn_pifu;
	private Button btn_about_us;
	private Button btn_deleteAll;
	private TextView paomadeng;

	private TextView current_accoutnt;// 文本，用于显示当前的账号

	private SharedPreferences pref;// 用于获取键值数据

	long currentCount;
	// 这个是进度条
	private utils.HorizontalProgressBarWithNumber mProgress;

	private View rootview;
	private Button button;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootview = inflater.inflate(R.layout.fragment_user, container, false);// 获取视图
		return rootview;
	}

	@Override
	protected void initView(View view) {
		super.initView(view);

		btn_exit = (Button) rootview.findViewById(R.id.btn_exit);
		btn_about_us = (Button) rootview.findViewById(R.id.btn_aboutUs);
		btn_genggaixueqi = (Button) rootview
				.findViewById(R.id.btn_genggaixueqi);
		mProgress = (HorizontalProgressBarWithNumber) rootview
				.findViewById(R.id.myProgress);
		btn_count_text = (Button) rootview.findViewById(R.id.btn_count_text);
		btn_pifu = (Button) rootview.findViewById(R.id.btn_pifu);
		btn_deleteAll = (Button) rootview.findViewById(R.id.btn_deleteAll);
		paomadeng = (TextView) rootview.findViewById(R.id.paomadeng);

		current_accoutnt = (TextView) rootview
				.findViewById(R.id.current_account);

		// 初始化时间
		initTime();
	}

	@Override
	protected void initEvents() {
		super.initEvents();
		btn_pifu.setOnClickListener(this);
		btn_exit.setOnClickListener(this);
		btn_count_text.setOnClickListener(this);
		btn_genggaixueqi.setOnClickListener(this);
		btn_deleteAll.setOnClickListener(this);
		btn_about_us.setOnClickListener(this);

		// 将账号显示
		pref = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
		String name = pref.getString("name", "");
		current_accoutnt.setText("当前的账号：" + name);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.btn_aboutUs:
			FragmentTransaction transactionAbout = getActivity()
					.getSupportFragmentManager().beginTransaction();
			AboutUsFragment aboutUsFragment = new AboutUsFragment();
			transactionAbout.add(android.R.id.content, aboutUsFragment,
					"AboutUsFragment");
			transactionAbout.addToBackStack("AboutUsFragment");// 添加fragment到Activity的回退栈中
			transactionAbout.show(aboutUsFragment);
			transactionAbout.commit();
			break;
		case R.id.btn_pifu:
			show("使用皮肤功能，会员才能使用哦");
			break;
		case R.id.btn_count_text:
			FragmentTransaction transactionTimeDF = getActivity()
					.getSupportFragmentManager().beginTransaction();
			TimeDetailFragment timeDetailFragment = new TimeDetailFragment();
			transactionTimeDF.add(android.R.id.content, timeDetailFragment,
					"TimeDetailFragment");
			transactionTimeDF.addToBackStack("TimeDetailFragment");// 添加fragment到Activity的回退栈中

			// 传递值
			Bundle args = new Bundle();
			args.putInt("currentCount", getCounts());
			timeDetailFragment.setArguments(args);
			transactionTimeDF.show(timeDetailFragment);
			transactionTimeDF.commit();

			break;
		case R.id.btn_genggaixueqi:
			FragmentTransaction transactionTermDF = getActivity()
					.getSupportFragmentManager().beginTransaction();
			TermDetailFragment termDetailFragment = new TermDetailFragment();
			transactionTermDF.add(android.R.id.content, termDetailFragment,
					"TermDetailFragment");
			transactionTermDF.addToBackStack("TermDetailFragment");// 添加fragment到Activity的回退栈中
			transactionTermDF.show(termDetailFragment);
			transactionTermDF.commit();

			break;
		case R.id.btn_deleteAll:
			pref = getActivity().getSharedPreferences("data",
					Context.MODE_PRIVATE);
			final String name = pref.getString("name", "");
			new AlertDialog.Builder(rootview.getContext())
					// /在这里加入对话框，提醒用户在进行删除数据操作
					.setTitle("确认框")
					.setMessage("你真的确定要删除账号下所有的数据吗？请谨慎考虑后点再击确定！")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									SQLiteDBUtil db = new SQLiteDBUtil(
											getActivity()
													.getApplicationContext());
									SQLiteDatabase sd = db
											.getWritableDatabase();
									String sql = "delete from kebiao where account= "
											+ name;
									sd.execSQL(sql);
									// 这里进行关闭
									db.close();
									show("恭喜您刪除成功！");

									// //发送广播，更新数据
									Intent intent = new Intent("jerry");
									intent.putExtra("change", "deleteAll");
									LocalBroadcastManager.getInstance(
											getActivity())
											.sendBroadcast(intent);

									// 自动返回
									getFragmentManager().popBackStack();
								}
							})
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									show("恩，你点击了取消");
								}
							}).show();

			break;
		case R.id.btn_exit:
			new AlertDialog.Builder(rootview.getContext())
					// /在这里加入对话框，提醒用户在进行删除数据操作
					.setTitle("确认框")
					.setMessage("你确定要退出登录吗？")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									show("即将退出");
									changeLoginState(false);// 将登录状态改为false
															// 这里确认退出后才改变登录的状态
									startActivity(new Intent(getActivity(),
											LoginActivity.class));
								}
							})
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									show("恩，你点击了取消");
								}
							}).show();
			break;

		default:
			break;
		}
	}

	/************************************ 以下的都是关于time的相关方法 ***********************************/
	private void initTime() {
		// 获取当前进度条的百分比数
		int i = getCurrentcount();
		// 将数字显示在进度挑中
		mProgress.setProgress(i);
		String s = "大学生涯已经度过了" + i + "%,点击查看详情";
		btn_count_text.setText(s);
	}

	// 返回百分比
	private int getCurrentcount() {
		long count = getCounts();
		count = count * 100 / 1399;
		int c = (int) count;
		return c;
	}

	// 返回总天数
	private int getCounts() {
		// 这里默认自己是15级（默认开学时间是15.9.1）的，之后会传参数，
		Utility util = new Utility();
		pref = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
		String name = pref.getString("name", "");
		int year = util.getAccountYear(name);

		// String S15before = "Wed Sep 1 09:22:14 格林尼治标准时间+0800 2015";
		String S15before = "Wed Sep 1 09:22:14 格林尼治标准时间+0800 20" + year;// 这行代码代替了上一行代码，动态的获取用户的账号的年
		Date d1 = new Date();
		SimpleDateFormat sdf1 = new SimpleDateFormat("yy-MM-dd", Locale.CHINA);

		Date dNow = util.formatDate2(d1.toString());
		Date d15 = util.formatDate2(S15before);

		long count = (dNow.getTime() - d15.getTime());
		count = count / 24 / 60 / 60 / 1000;
		int c = (int) count;
		return c;
	}

	/************************************* 以上的都是关于time的相关方法 ************************************/

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);

		// fragment里对整个linearlayout进行监听
		view.findViewById(R.id.information).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						FragmentTransaction transaction = getActivity()
								.getSupportFragmentManager().beginTransaction();
						InformationFragment fragment = new InformationFragment();
						transaction.add(android.R.id.content, fragment,
								"InformationFragment");
						transaction.addToBackStack("InformationFragment");// 添加fragment到Activity的回退栈中
						transaction.show(fragment);
						transaction.commit();

					}
				});

		// 初始化时间
		initTime();
	}

	// 若登录了，则将登录状态设置为true，若退出登录则设置为false
	public void changeLoginState(Boolean b) {
		SharedPreferences.Editor editor = getActivity().getSharedPreferences(
				"data", Context.MODE_PRIVATE).edit();
		editor.putBoolean("LoginState", b);
		editor.apply();
	}

}
