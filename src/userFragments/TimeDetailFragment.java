package userFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.example.easytablemore.BaseFragment;
import com.example.easytablemore.R;

public class TimeDetailFragment extends BaseFragment {

	private View rootview;

	private TextView timeDetailCurrent;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootview = inflater.inflate(R.layout.fragment_time_detail, container,
				false);
		// TODO Auto-generated method stub
		return rootview;
	}

	@Override
	protected void initView(View view) {
		// TODO Auto-generated method stub
		super.initView(view);

		timeDetailCurrent = (TextView) rootview
				.findViewById(R.id.timeDetailCurrent);
		int days = getArguments().getInt("currentCount");
		float count = (float) (days * 100 / 1399.00);
		String s = "你已经度过了" + "" + days + "天,准确的说已经度过了" + "" + count + "%;";
		timeDetailCurrent.setText(s);
	}

	@Override
	protected void initEvents() {
		// TODO Auto-generated method stub
		super.initEvents();
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
