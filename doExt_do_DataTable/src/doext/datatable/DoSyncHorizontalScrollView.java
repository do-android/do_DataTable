package doext.datatable;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.HorizontalScrollView;

public class DoSyncHorizontalScrollView extends HorizontalScrollView {
	private View mView;

	public DoSyncHorizontalScrollView(Context context) {
		super(context);
	}

	public DoSyncHorizontalScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		if (mView != null) {
			mView.scrollTo(l, t);
		}
	}

	public void setScrollView(View view) {
		mView = view;
	}

}
