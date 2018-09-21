package courseFragments;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import noteFragments.NoteShowFragment;

import utils.SQLiteDBUtil;
import utils.Utility;
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
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import bean.Course;
import bean.Note;

import com.example.easytablemore.BaseFragment;
import com.example.easytablemore.R;

public class CourseShowFragment extends BaseFragment implements OnClickListener {

	private View rootview;

	private EditText name, teacher, place, weeks, week, jie;
	private Button delete_course;
	private Button save_course;

	// 用于获取当前用户的账号--进一步来保存用户的相关课程--以及显示当前的课程是此用户的
	private SharedPreferences pref;

	// 笔记
	private ListView noteList;
	CommonAdapter<Note> myAdapter;
	private static Context context;
	List<Note> notes;// 设为全局变量，便于引用

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootview = inflater.inflate(R.layout.fragment_course_show, container,
				false);
		// TODO Auto-generated method stub
		return rootview;
	}

	@Override
	protected void initView(View view) {
		// TODO Auto-generated method stub
		super.initView(view);

		noteList = (ListView) rootview.findViewById(R.id.courseNoteList);

		context = getActivity().getApplicationContext();
		name = (EditText) rootview.findViewById(R.id.id_txt_name);
		teacher = (EditText) rootview.findViewById(R.id.id_txt_teacher);
		place = (EditText) rootview.findViewById(R.id.id_txt_place);
		week = (EditText) rootview.findViewById(R.id.id_txt_week);
		weeks = (EditText) rootview.findViewById(R.id.id_txt_weeks);
		jie = (EditText) rootview.findViewById(R.id.id_txt_jie);

		delete_course = (Button) rootview.findViewById(R.id.delete_course);
		save_course = (Button) rootview.findViewById(R.id.save_course);

		// 将传过来的值初始化
		Course c = (Course) getArguments().getSerializable("course");
		name.setText(c.getName());
		teacher.setText(c.getTeacher());
		place.setText(c.getPlace());
		weeks.setText(c.getWeek());
		week.setText("第" + c.getCourseId() / 100 + "周");
		jie.setText("第" + c.getCourseId() % 100 + "节");
	}

	@Override
	protected void initEvents() {
		// TODO Auto-generated method stub
		super.initEvents();

		Course c = (Course) getArguments().getSerializable("course");

		final String myaccount = c.getAccount();
		final int courseid = c.getCourseId();
		final String getName = c.getName();
		final String getTeacher = c.getTeacher();
		final String getPlace = c.getPlace();
		final String getWeeks = c.getWeek();

		delete_course.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				new AlertDialog.Builder(rootview.getContext())
						// /在这里加入对话框，提醒用户在进行删除数据操作
						.setTitle("确认框")
						.setMessage("你确定要删除数据吗？")
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
										String sql = "delete from kebiao where courseId= "
												+ courseid
												+ " AND account= "
												+ myaccount;
										sd.execSQL(sql);
										// 这里进行关闭
										db.close();
										show("恭喜您刪除成功！");

										// //发送广播，更新数据
										Intent intent = new Intent("jerry");
										intent.putExtra("change",
												"courseDelete");
										LocalBroadcastManager.getInstance(
												getActivity()).sendBroadcast(
												intent);

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
			}
		});

		// 这里进行修改操作
		// 如果里边的文本信息没有进行修改操作，那么弹出提示信息：您没有修改
		// 如果文本信息进行了修改操作，那么保存修改信息，并返回
		save_course.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				final String editName = name.getText().toString();
				final String editTeacher = teacher.getText().toString();
				final String editPlace = place.getText().toString();
				final String editWeeks = weeks.getText().toString();

				if (getName.equals(editName) && getTeacher.equals(editTeacher)
						&& getPlace.equals(editPlace)
						&& getWeeks.equals(editWeeks)) {
					show("亲，你好像并没有修改信息哦");
				} else {
					new AlertDialog.Builder(rootview.getContext())
							// /在这里加入对话框，提醒用户在进行修改数据操作
							.setTitle("确认框")
							.setMessage("你确定要修改数据吗？")
							.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {
											SQLiteDBUtil db = new SQLiteDBUtil(
													getActivity()
															.getApplicationContext());
											SQLiteDatabase sd = db
													.getWritableDatabase();
											String sql = "update kebiao set name= ?"
													+ " ,teacher= ?"
													+ " ,place= ?"
													+ " ,week= ?"
													+ " where courseId= ?"
													+ " AND account= ?";
											sd.execSQL(sql, new Object[] {
													editName, editTeacher,
													editPlace, editWeeks,
													courseid, myaccount });
											// 这里进行关闭
											db.close();
											show("修改成功了哦！");

											// ////发送广播更新数据
											Intent intent = new Intent("jerry");
											intent.putExtra("change",
													"courseShow");
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
										public void onClick(
												DialogInterface dialog,
												int which) {
											// TODO Auto-generated method stub
											show("恩，你点击了取消");
										}
									}).show();
				}
			}
		});
		
		//查询到的笔记
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
		
		
		String text = name.getText().toString();
		notes = querryList(text);
		myAdapter.querryAppend(notes);
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

	// *********************************************

	
	
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

	private List<Note> querryList(String s) {
		List<Note> notes = new ArrayList<Note>();
		Utility util = new Utility();

		SQLiteDBUtil db = new SQLiteDBUtil(getActivity()
				.getApplicationContext());
		SQLiteDatabase sd = db.getReadableDatabase();
		String sql;
		Cursor cursor;

		// ********************************************

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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

}
