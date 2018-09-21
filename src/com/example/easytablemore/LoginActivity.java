package com.example.easytablemore;

import utils.Utility;

import com.example.easytablemore.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity implements View.OnClickListener {

	private Button btn;
	// 复选框，是否记住密码
	private CheckBox rememberPaw;
	// 记住密码的boolea值，默认为没有
	private Boolean isRememberPaw = false;
	// 用户名
	private EditText editUserName;
	// 密码
	private EditText editPassword;

	private TextView tvForgetPwd, rule;

	// 键值存储.
	private SharedPreferences pref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);
		// 如果已经登录过，则直接进入主页面
		if (LoginState()) {
			startActivity(new Intent(LoginActivity.this, MainActivity.class));
			finish();
		}

		// 初始化控件（绑定控件）
		initView();
		// 检查是否有已经保存了的账号和密码
		checkIsSaveNameAndPass();
		initEvent();

	}

	private void initEvent() {
		// 绑定单击事件
		tvForgetPwd.setOnClickListener(this);
		btn.setOnClickListener(this);
		rule.setOnClickListener(this);

		rememberPaw
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							isRememberPaw = true;
							changeRemeber(true);
						} else {
							isRememberPaw = false;
							changeRemeber(false);
						}
					}
				});
	}

	// 在这里统一添加单击事件
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tvForgetPwd:// 忘记密码的单击事件
			Toast.makeText(getApplicationContext(), "忘了就忘了吧",
					Toast.LENGTH_SHORT).show();
			break;
		case R.id.rule:// 忘记密码的单击事件
			Toast.makeText(
					getApplicationContext(),
					"账号必须九位数字，前两位为入学年，如15，代表的是2015年入学;之后三位为专业代码，如001;之后两位为班级代码，如01，最后两位为自己的序号，如01，加起来就是150010101",
					Toast.LENGTH_LONG).show();
			break;

		case R.id.btnLogin:// 登录按钮的单击事件
			Utility util = new Utility();
			if ("".equals(editUserName.getText().toString())
					|| "".equals(editPassword.getText().toString())) {
				Toast.makeText(getApplicationContext(), "学号或密码为空",
						Toast.LENGTH_SHORT).show();
			} else if (editUserName.getText().toString().length() != 9) {
				Toast.makeText(getApplicationContext(), "这位同志，学号长度必须为9位哦！",
						Toast.LENGTH_SHORT).show();
			} else if (!util.isOK(editUserName.getText().toString())) {// 此时用户输入的长度一定是9，进行判断：是不是现在的大学生
				Toast.makeText(getApplicationContext(),
						"你所输入的学号有问题哦，请点击最下边查看学号规则", Toast.LENGTH_SHORT).show();
			} else {// 到了这，那么说明用户的账号是正确的了
				checkPassword();
			}

			break;

		default:
			break;
		}
	}

	protected void checkPassword() {
		// 如果账号和密码不是一样的，就提示用户
		if (!editPassword.getText().toString()
				.equals(editUserName.getText().toString())) {
			Toast.makeText(getApplicationContext(), "这位同志，密码和账号是一样的才行哦",
					Toast.LENGTH_SHORT).show();
		} else {
			// 保存
			checkIsRememberPaw();
			Toast.makeText(getApplicationContext(), "恭喜您登录成功了哦！！",
					Toast.LENGTH_SHORT).show();
			Intent i = new Intent(LoginActivity.this, MainActivity.class);
			startActivity(i);
			finish();
		}
	}

	// 如果用户点击了保存密码，则将密码保存到prefer中
	private void checkIsRememberPaw() {
		String name = editUserName.getText().toString();
		String password = editPassword.getText().toString();
		SharedPreferences.Editor editor = getSharedPreferences("data",
				MODE_PRIVATE).edit();
		editor.putString("name", name);
		if (isRememberPaw) {
			editor.putString("password", password);
		} else {
			editor.putString("password", "");
		}
		editor.apply();
	}

	private void checkIsSaveNameAndPass() {
		pref = getSharedPreferences("data", MODE_PRIVATE);
		String name = pref.getString("name", "");
		String password = pref.getString("password", "");
		if (!"".equals(password)) {// 如果密码不是空的，说明上次保存密码了，即用户点击了记住密码了，那么设置记住密码为记住，
			rememberPaw.setChecked(true);
			// 下边这行代码很关键，倘若不写的话，退出登录，再次登录相同的账号，然后退出登录，再次登录的话，密码那就会清空！
			isRememberPaw = isRemeber();
		}
		editUserName.setText(name);
		editPassword.setText(password);

	}

	private void initView() {
		btn = (Button) findViewById(R.id.btnLogin);
		rule = (TextView) findViewById(R.id.rule);
		editUserName = (EditText) findViewById(R.id.edit_userName);
		editPassword = (EditText) findViewById(R.id.edit_passWord);
		rememberPaw = (CheckBox) findViewById(R.id.cb_rememberPsw);
		tvForgetPwd = (TextView) findViewById(R.id.tvForgetPwd);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	// 返回是否已登录的结果
	public Boolean LoginState() {
		pref = getSharedPreferences("data", MODE_PRIVATE);
		return pref.getBoolean("LoginState", false);
	}

	// 返回是否已登录的结果
	public Boolean isRemeber() {
		pref = getSharedPreferences("data", MODE_PRIVATE);
		return pref.getBoolean("isRemeber", false);
	}

	// 定义改变记住密码的布尔值
	public void changeRemeber(boolean b) {
		SharedPreferences.Editor editor = getSharedPreferences("data",
				MODE_PRIVATE).edit();
		editor.putBoolean("isRemeber", b);
		editor.apply();
	}

}
