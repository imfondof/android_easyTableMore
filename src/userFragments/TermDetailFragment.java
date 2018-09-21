package userFragments;

import java.util.ArrayList;
import java.util.List;

import message.Constant;
import utils.SQLiteDBUtil;
import adapter.BaseViewHolder;
import adapter.CommonAdapter;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import bean.Course;

import com.example.easytablemore.BaseFragment;
import com.example.easytablemore.R;

public class TermDetailFragment extends BaseFragment implements OnClickListener {

	private View rootview;

	private static Context context;
	private ListView term_list;
	private TextView text_currentTerm;
	private EditText edit_Term;
	private Button btn_addTerm;
	CommonAdapter<Course> myAdapter;
	private SharedPreferences pref;// 用于保存当前学期

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootview = inflater.inflate(R.layout.fragment_term_detail, container,
				false);
		// TODO Auto-generated method stub
		return rootview;
	}

	@Override
	protected void initView(View view) {
		// TODO Auto-generated method stub
		super.initView(view);

		context = getActivity().getApplicationContext();
		btn_addTerm = (Button) rootview.findViewById(R.id.btn_addTerm);
		edit_Term = (EditText) rootview.findViewById(R.id.edit_Term);
		term_list = (ListView) rootview.findViewById(R.id.term_list);
		text_currentTerm = (TextView) rootview
				.findViewById(R.id.text_currentTerm);
	}

	@Override
	protected void initEvents() {
		// TODO Auto-generated method stub
		super.initEvents();

		btn_addTerm.setOnClickListener(this);// 时间在onclik里边编写
		int term = readCurrrentTerm();
		text_currentTerm.setText("当前学期为：" + term);

		pref = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
		final String current_accoutnt = pref.getString("name", "150060408");// 这里name就是用户当前的账号啦

		List<Course> courses = new ArrayList<Course>();

		// select distinct term from kebiao;
		// 从数据库中查询出一共有哪些学期
		SQLiteDBUtil db = new SQLiteDBUtil(getActivity()
				.getApplicationContext());
		SQLiteDatabase sd = db.getReadableDatabase();
		// String sql =
		// "select distinct term from kebiao";////////////这行代码被下一行所替代，因为这行代码的话，，患一个账号登录，学期还是之前的学期。
		String sql = "select distinct term from kebiao where account="
				+ current_accoutnt;
		Cursor cursor = sd.rawQuery(sql, null);
		while (cursor.moveToNext()) {
			int receive_term = cursor.getInt(0);
			Course c = new Course("", 101, "", "", "", receive_term, "");
			courses.add(c);
		}
		db.close();
		cursor.close();

		term_list.setAdapter(myAdapter = new CommonAdapter<Course>(courses,
				context, R.layout.list_item) {
			@Override
			public void setListener(BaseViewHolder holder) {
				holder.setOnItemClickListener();
				holder.setOnItemLongClickListener();
			}

			@Override
			public void setData(BaseViewHolder holder, Course item) {
				holder.setText(R.id.list_item_tv, item.getTerm() + "");
			}

			@Override
			public void onClickCallback(View v, int position,
					BaseViewHolder viewHolder) {
				switch (v.getId()) {

				default:
					Course c = (Course) term_list.getItemAtPosition(position);
					int term = c.getTerm();
					String s = "您点击了" + term + "，当前学期设置为" + term;
					show(s);
					updateTerm(term);// 保存设置当前学期
					text_currentTerm.setText("当前学期为：" + term);// 更新界面显示学期

					// 发送广播，更新数据
					Intent intent = new Intent("jerry");
					intent.putExtra("change", "termDetail");
					LocalBroadcastManager.getInstance(getActivity())
							.sendBroadcast(intent);
					break;
				}
			}

			@Override
			public boolean onLonClickCallback(View v, int position,
					BaseViewHolder viewHolder) {
				// TODO Auto-generated method stub

				Course c = (Course) term_list.getItemAtPosition(position);
				final int term = c.getTerm();

				int currentterm = readCurrrentTerm();
				if (term == currentterm) {// 如果想要删除的学期，是正在使用的学期，那么提示用户不能删除，
					show("你所删除的学期是正在使用的学期哦，请更改当前的学期再删除哦");
				} else {
					new AlertDialog.Builder(rootview.getContext())
							// /在这里加入对话框，提醒用户在进行删除数据操作
							.setTitle("确认框")
							.setMessage("你确定要删除该学期吗？你将删除此账号下所有该学期的课程哦！！请慎重")
							.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											SQLiteDBUtil db = new SQLiteDBUtil(
													getActivity()
															.getApplicationContext());
											SQLiteDatabase sd = db
													.getWritableDatabase();
											String sql = "delete from kebiao where term= "
													+ term
													+ " AND account= "
													+ current_accoutnt;
											sd.execSQL(sql);
											// 这里进行关闭
											db.close();
											show("恭喜您刪除学期成功！");

											// 这个，好像不用发送广播。。
											// //发送广播，更新数据
											// Intent intent = new
											// Intent("jerry");
											// intent.putExtra("change",
											// "termDelete");
											// LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);

											// 自动返回
											getFragmentManager().popBackStack();
										}
									})
							.setNegativeButton("取消",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											show("恩，你点击了取消");
										}
									}).show();
				}

				return true;// 此处返回真，，那么在长按事件触发后，就不会触发短按事件了
			}
		});
	}

	private static class MyAdapter extends BaseAdapter {
		private List<Course> courses = new ArrayList<Course>();

		public MyAdapter(List<Course> courses) {
			if (courses != null) {
				this.courses.addAll(courses);
			}
		}

		@Override
		public int getCount() {
			return courses.size();
		}

		@Override
		public Course getItem(int position) {
			if (position > -1 && position < getCount()) {
				return courses.get(position);
			}
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			ViewHolder holder;
			if (convertView != null) {
				holder = (ViewHolder) convertView.getTag();
				// holder.btn.setOnClickListener(new View.OnClickListener() {
				// @Override
				// public void onClick(View v) {
				// Toast.makeText(context, position + "",
				// Toast.LENGTH_SHORT).show();
				// Log.v(tag, position + "");
				// }
				// });
			} else {
				convertView = LayoutInflater.from(context).inflate(
						R.layout.list_item, parent, false);
				holder = new ViewHolder(convertView);

			}
			Course item = getItem(position);
			if (item != null) {
				holder.tv.setText(item.getTerm() + "");
			}

			return convertView;
		}
	}

	private static class ViewHolder {
		TextView tv;

		public ViewHolder(View convertView) {
			if (convertView != null) {
				convertView.setTag(this);
				tv = (TextView) convertView.findViewById(R.id.list_item_tv);
			}

		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_addTerm:

			pref = getActivity().getSharedPreferences("data",
					Context.MODE_PRIVATE);
			String current_accoutnt = pref.getString("name", "150060408");// 这里name就是用户当前的账号啦

			String currentTerm = edit_Term.getText().toString();
			int term = Integer.valueOf(currentTerm);
			SQLiteDBUtil db = new SQLiteDBUtil(getActivity()
					.getApplicationContext());
			SQLiteDatabase sd = db.getWritableDatabase();
			String sql = "insert into kebiao values(null," + current_accoutnt
					+ ",'','','',''," + term + ",'')";
			// sd.execSQL(sql, new
			// Object[]{account,id,add_name,add_teacher,add_place,term,add_weeks});
			sd.execSQL(sql, new Object[] {});
			// 进行关闭
			db.close();
			show("添加成功！");
			edit_Term.setText("");// 将文本框设置为空

			// 接下来跟新界面显示
			List<Course> cs = new ArrayList<Course>();
			Course c = new Course(current_accoutnt, 222222, "", "", "", term,
					"");
			cs.add(c);
			myAdapter.append(cs);

			break;

		default:
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

	private void updateTerm(int term) {
		SharedPreferences.Editor editor = getActivity().getSharedPreferences(
				"data", Context.MODE_PRIVATE).edit();
		pref = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
		String current_accoutnt = pref.getString("name", "150060408");// 这里name就是用户当前的账号啦
		String termKey = current_accoutnt + Constant.CURRENT_TERM; // /对学期的键进行加固，这样每个账号只会对应上自己的学期
		editor.putInt(termKey, term);
		editor.apply();
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
