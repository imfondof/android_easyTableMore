package noteFragments;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import userFragments.InformationFragment;
import utils.SQLiteDBUtil;
import utils.Utility;

import bean.Note;

import com.example.easytablemore.BaseFragment;
import com.example.easytablemore.R;

import adapter.BaseViewHolder;
import adapter.CommonAdapter;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class NoteFragment extends BaseFragment implements OnClickListener {

	private View rootview;
	private Button querryBtn;
	private EditText querryText;
	private ListView noteList;
	ImageView noteAdd;
	CommonAdapter<Note> myAdapter;

	List<Note> notes;// 设为全局变量，便于引用
	boolean isRegster = false;// 定义全局变量，判断是否注册了广播。

	LocalBroadcastManager broadcastManager;

	private static Context context;
	// 用于获取当前用户的账号--进一步来保存用户的相关课程--以及显示当前的课程是此用户的
	private SharedPreferences pref;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootview = inflater.inflate(R.layout.fragment_note, container, false);

		// 注册广播 在这里注册，确保他只执行一次。。
		if (!isRegster) {// 如果没注册，就注册，注册过就不重复注册了。
			registerReceiverNote();
			isRegster = true;// 注册完后，更改为已经注册
		}
		return rootview;
	}

	@Override
	protected void initView(View view) {
		// TODO Auto-generated method stub
		super.initView(view);
		context = getActivity().getApplicationContext();
		querryBtn = (Button) rootview.findViewById(R.id.querryBtn);
		querryText = (EditText) rootview.findViewById(R.id.querryText);
		noteList = (ListView) rootview.findViewById(R.id.noteList);
		noteAdd = (ImageView) rootview.findViewById(R.id.noteAdd);
	}

	@Override
	protected void initEvents() {
		// TODO Auto-generated method stub
		super.initEvents();
		noteAdd.setOnClickListener(this);
		querryBtn.setOnClickListener(this);

		notes = new ArrayList<Note>();
		// for (int i = 1; i < 15; i++) {
		// Note note = new Note(i, "150060408", "这是第一个文章" + i, "安卓课设" + i,
		// "正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文正文"
		// + i, "2018.07." + i);
		// notes.add(note);
		// }

		Utility util = new Utility();

		SQLiteDBUtil db = new SQLiteDBUtil(getActivity()
				.getApplicationContext());
		SQLiteDatabase sd = db.getReadableDatabase();
		String sql = "select * from note order by id desc";// 查询的结果按照时间排序
																// 从大到小(最新的在最上边)
		Cursor cursor = sd.rawQuery(sql, null);
		while (cursor.moveToNext()) {
			// account必须是当前的账户。。。。从数据库中选择出来的数据，筛选出此账户的数据，然后显示在界面上-----

			pref = getActivity().getSharedPreferences("data",
					Context.MODE_PRIVATE);
			String name = pref.getString("name", "150060408");
			int receive_id = cursor.getInt(0);
			String receive_account = cursor.getString(1);

			if (name.equals(receive_account)) {// 判断读取出来的数据是不是当前登录用户的，是不是当前学期的课程。。是的话就显示
				String receive_title = cursor.getString(2);
				String receive_course = cursor.getString(3);
				String receive_content = cursor.getString(4);
				// 将数据库中的时间（字符串）格式转化为相应的格式
				String str = "";
				SimpleDateFormat sdf1 = new SimpleDateFormat(
						"yyyy/MM/dd HH:mm", Locale.CHINA);
				Date d = util.formatDate2(cursor.getString(5));
				str = sdf1.format(d);

				Note note = new Note(receive_id, name, receive_title,
						receive_course, receive_content, str);
				notes.add(note);
			}
		}
		// 关闭 不这样设置会报一大串错误，但运行并不会出错
		cursor.close();
		db.close();

		noteList.setAdapter(myAdapter = new CommonAdapter<Note>(notes, context,
				R.layout.note_item) {

			@Override
			public void setListener(BaseViewHolder holder) {

				holder.setOnItemClickListener();
				holder.setOnItemLongClickListener();
			}

			@Override
			public void setData(BaseViewHolder holder, Note item) {
				// TODO Auto-generated method stub
				holder.setText(R.id.title, item.getTitle());
				holder.setText(R.id.course, item.getCourse());
				holder.setText(R.id.content, item.getContent());
				holder.setText(R.id.time, item.getTime());
			}

			@Override
			public void onClickCallback(View v, int position,
					BaseViewHolder viewHolder) {
				switch (v.getId()) {

				default:

					Note note = (Note) noteList.getItemAtPosition(position);
					int id = note.getId();

					FragmentTransaction transaction = getActivity()
							.getSupportFragmentManager().beginTransaction();
					NoteShowFragment fragment = new NoteShowFragment();
					transaction.add(android.R.id.content, fragment,
							"InformationFragment");
					transaction.addToBackStack("InformationFragment");// 添加fragment到Activity的回退栈中

					// 传递值
					Bundle args = new Bundle();
					args.putSerializable("note", note);
					fragment.setArguments(args);

					transaction.show(fragment);
					transaction.commit();

					break;
				}
			}

			@Override
			public boolean onLonClickCallback(View v, final int position,
					BaseViewHolder viewHolder) {
				switch (v.getId()) {

				default:
					final Note note = (Note) noteList
							.getItemAtPosition(position);
					final int id = note.getId();// /用于删除该笔记的时候用

					new AlertDialog.Builder(rootview.getContext())
							// /在这里加入对话框，提醒用户在进行删除数据操作
							.setTitle("确认框")
							.setMessage("你确定要删除数据吗？")
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
											String sql = "delete from note where id= "
													+ id;
											sd.execSQL(sql);
											// 这里进行关闭
											db.close();

											update(note);
											show("恭喜您刪除成功！");
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

					break;
				}
				return true;// //返回true之后就不会触发短按事件
			}
		});
	}

	private static class MyAdapter extends BaseAdapter {

		public List<Note> notes = new ArrayList<Note>();

		public MyAdapter(List<Note> notes) {
			if (notes != null) {
				this.notes.addAll(notes);
			}
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return notes.size();
		}

		@Override
		public Note getItem(int position) {
			if (position > -1 && position < getCount()) {
				return notes.get(position);
			}
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView != null) {
				holder = (ViewHolder) convertView.getTag();
			} else {
				convertView = LayoutInflater.from(context).inflate(
						R.layout.note_item, parent, false);
				holder = new ViewHolder(convertView);
			}
			Note item = getItem(position);
			if (item != null) {
				// holder.tv.setText(item.getName());
				holder.title.setText(item.getTitle());
				holder.course.setText(item.getCourse());
				holder.content.setText(item.getContent());
				holder.time.setText(item.getTime());
			}
			return convertView;
		}
	}

	private static class ViewHolder {
		TextView title, course, content, time;

		public ViewHolder(View convertView) {
			if (convertView != null) {
				convertView.setTag(this);
				title = (TextView) convertView.findViewById(R.id.title);
				course = (TextView) convertView.findViewById(R.id.course);
				content = (TextView) convertView.findViewById(R.id.content);
				time = (TextView) convertView.findViewById(R.id.time);
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.querryBtn:

			String text = querryText.getText().toString();
			notes = querryList(text);
			myAdapter.querryAppend(notes);
			show("查询到" + notes.size() + "条数据哦");

			break;
		case R.id.noteAdd:

			FragmentTransaction transaction = getActivity()
					.getSupportFragmentManager().beginTransaction();
			NoteAddFragment fragment = new NoteAddFragment();
			transaction.add(android.R.id.content, fragment,
					"InformationFragment");
			transaction.addToBackStack("InformationFragment");// 添加fragment到Activity的回退栈中
			transaction.show(fragment);
			transaction.commit();

			break;
		default:
			break;
		}

	}

	// 非常的代码》》》
	private List<Note> querryList(String s) {
		List<Note> notes = new ArrayList<Note>();
		Utility util = new Utility();

		SQLiteDBUtil db = new SQLiteDBUtil(getActivity()
				.getApplicationContext());
		SQLiteDatabase sd = db.getReadableDatabase();
		String sql;
		Cursor cursor;
		
		// ********************************************
				sql = "select * from note " + "where time like '%" + s + "%'"
						+ "order by id desc";// 查询的结果按照时间排序
												// 从大到小(最新的在最上边)
				cursor = sd.rawQuery(sql, null);
				while (cursor.moveToNext()) {
					// account必须是当前的账户。。。。从数据库中选择出来的数据，筛选出此账户的数据，然后显示在界面上-----

					pref = getActivity().getSharedPreferences("data",
							Context.MODE_PRIVATE);
					String name = pref.getString("name", "150060408");
					int receive_id = cursor.getInt(0);
					String receive_account = cursor.getString(1);

					if (name.equals(receive_account)) {// 判断读取出来的数据是不是当前登录用户的，是不是当前学期的课程。。是的话就显示
						String receive_title = cursor.getString(2);
						String receive_course = cursor.getString(3);
						String receive_content = cursor.getString(4);

						// 将数据库中的时间（字符串）格式转化为相应的格式
						String str = "";
						SimpleDateFormat sdf1 = new SimpleDateFormat(
								"yyyy/MM/dd HH:mm", Locale.CHINA);
						Date d = util.formatDate2(cursor.getString(5));
						str = sdf1.format(d);

						Note note = new Note(receive_id, name, receive_title,
								receive_course, receive_content, str);
						if (!isIsexist(notes, note)) {
							notes.add(note);
						}
					}
				}
		//***********************************************************************
		sql = "select * from note " + "where title like '%" + s + "%'"
				+ "order by id desc";// 查询的结果按照时间排序
										// 从大到小(最新的在最上边)
		cursor = sd.rawQuery(sql, null);
		while (cursor.moveToNext()) {
			// account必须是当前的账户。。。。从数据库中选择出来的数据，筛选出此账户的数据，然后显示在界面上-----

			pref = getActivity().getSharedPreferences("data",
					Context.MODE_PRIVATE);
			String name = pref.getString("name", "150060408");
			int receive_id = cursor.getInt(0);
			String receive_account = cursor.getString(1);

			if (name.equals(receive_account)) {// 判断读取出来的数据是不是当前登录用户的，是不是当前学期的课程。。是的话就显示
				String receive_title = cursor.getString(2);
				String receive_course = cursor.getString(3);
				String receive_content = cursor.getString(4);

				// 将数据库中的时间（字符串）格式转化为相应的格式
				String str = "";
				SimpleDateFormat sdf1 = new SimpleDateFormat(
						"yyyy/MM/dd HH:mm", Locale.CHINA);
				Date d = util.formatDate2(cursor.getString(5));
				str = sdf1.format(d);

				Note note = new Note(receive_id, name, receive_title,
						receive_course, receive_content, str);
				if (!isIsexist(notes, note)) {
					notes.add(note);
				}
			}
		}

		// ***************************************************************************
		sql = "select * from note " + "where course like '%" + s + "%'"
				+ "order by id desc";// 查询的结果按照时间排序
										// 从大到小(最新的在最上边)
		cursor = sd.rawQuery(sql, null);
		while (cursor.moveToNext()) {
			// account必须是当前的账户。。。。从数据库中选择出来的数据，筛选出此账户的数据，然后显示在界面上-----

			pref = getActivity().getSharedPreferences("data",
					Context.MODE_PRIVATE);
			String name = pref.getString("name", "150060408");
			int receive_id = cursor.getInt(0);
			String receive_account = cursor.getString(1);

			if (name.equals(receive_account)) {// 判断读取出来的数据是不是当前登录用户的，是不是当前学期的课程。。是的话就显示
				String receive_title = cursor.getString(2);
				String receive_course = cursor.getString(3);
				String receive_content = cursor.getString(4);

				// 将数据库中的时间（字符串）格式转化为相应的格式
				String str = "";
				SimpleDateFormat sdf1 = new SimpleDateFormat(
						"yyyy/MM/dd HH:mm", Locale.CHINA);
				Date d = util.formatDate2(cursor.getString(5));
				str = sdf1.format(d);

				Note note = new Note(receive_id, name, receive_title,
						receive_course, receive_content, str);
				if (!isIsexist(notes, note)) {
					notes.add(note);
				}
			}
		}
		// ///**********************************************
		sql = "select * from note " + "where content like '%" + s + "%'"
				+ "order by id desc";// 查询的结果按照时间排序
										// 从大到小(最新的在最上边)
		cursor = sd.rawQuery(sql, null);
		while (cursor.moveToNext()) {
			// account必须是当前的账户。。。。从数据库中选择出来的数据，筛选出此账户的数据，然后显示在界面上-----

			pref = getActivity().getSharedPreferences("data",
					Context.MODE_PRIVATE);
			String name = pref.getString("name", "150060408");
			int receive_id = cursor.getInt(0);
			String receive_account = cursor.getString(1);

			if (name.equals(receive_account)) {// 判断读取出来的数据是不是当前登录用户的，是不是当前学期的课程。。是的话就显示
				String receive_title = cursor.getString(2);
				String receive_course = cursor.getString(3);
				String receive_content = cursor.getString(4);

				// 将数据库中的时间（字符串）格式转化为相应的格式
				String str = "";
				SimpleDateFormat sdf1 = new SimpleDateFormat(
						"yyyy/MM/dd HH:mm", Locale.CHINA);
				Date d = util.formatDate2(cursor.getString(5));
				str = sdf1.format(d);

				Note note = new Note(receive_id, name, receive_title,
						receive_course, receive_content, str);
				if (!isIsexist(notes, note)) {
					notes.add(note);
				}
			}
		}
		

		// 关闭 不这样设置会报一大串错误，但运行并不会出错
		cursor.close();
		db.close();

		return notes;
	}

	// 非常的代码《《《

	private boolean isIsexist(List<Note> notes, Note note) {// 判断集合中是否已经包含了这个元素
		for (Note n : notes) {
			if (note.getId() == n.getId()) {
				return true;
			}
		}
		return false;
	}

	// ///**************************一整套通过广播，实现添加笔记 自动跟新界面******************功能
	// 在onViewCreate注册的广播

	/**
	 * 注册广播接收器
	 */
	private void registerReceiverNote() {
		broadcastManager = LocalBroadcastManager.getInstance(getActivity());
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("jack");
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
			if ("noteAdd".equals(change) || "noteEdit".equals(change)) {// 添加笔记传过来的广播
				// 这地方只能在主线程中刷新UI,子线程中无效，因此用Handler来实现
				new Handler().post(new Runnable() {
					public void run() {
						// 在这里来写你需要刷新的地方
						// 例如：testView.setText("恭喜你成功了");
						// show("啦啦啦啦，我要在主界面更新数据啦");
						initEvents();
					}
				});
			}
		}
	};
	// ///**************************一整套通过广播，实现添加笔记 自动跟新界面******************功能
}
