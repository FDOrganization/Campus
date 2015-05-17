package banner.ListAssistClass;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fydia.campus.R;

/**
 * @author SunnyCoffee
 * @create 2013-10-24
 * @version 1.0
 * @desc 鑷畾涔塋istview銆�涓嬫媺鍒锋柊,涓婃媺鍔犺浇鏇村�?
 */

public class myListView extends ListView implements OnScrollListener {

	// 鍖哄垎褰撳墠鎿嶄綔鏄埛鏂拌繕鏄姞杞�
	public static final int REFRESH = 0;
	public static final int LOAD = 1;

	// 鍖哄垎PULL鍜孯ELEASE鐨勮窛绂荤殑澶у皬
	private static final int SPACE = 20;

	// 瀹氫箟header鐨勫洓绉嶇姸鎬佸拰褰撳墠鐘舵��
	private static final int NONE = 0;
	private static final int PULL = 1;
	private static final int RELEASE = 2;
	private static final int REFRESHING = 3;
	private int state;

	private LayoutInflater inflater;
	private View header;
	private View footer;
	private TextView tip;
	private TextView lastUpdate;
	private ImageView arrow;
	private ProgressBar refreshing;

	private TextView noData;
	private TextView loadFull;
	private TextView more;
	private ProgressBar loading;

	private RotateAnimation animation;
	private RotateAnimation reverseAnimation;

	private int startY;

	private int firstVisibleItem;
	private int scrollState;
	private int headerContentInitialHeight;
	private int headerContentHeight;

	// 鍙湁鍦╨istview绗竴涓猧tem鏄剧ず鐨勬椂鍊欙紙listview婊戝埌浜嗛�?�閮級鎵嶈繘琛屼笅鎷夊埛鏂帮紝 鍚﹀垯姝ゆ椂鐨勪笅鎷夊彧鏄粦鍔╨istview
	private boolean isRecorded;
	private boolean isLoading;// 鍒ゆ柇鏄惁姝ｅ湪鍔犺浇
	private boolean loadEnable = true;// 寮�鍚垨鑰呭叧闂姞杞芥洿澶氬姛鑳�?
	private boolean isLoadFull;
	private int pageSize = 10;

	private OnRefreshListener onRefreshListener;
	private OnLoadListener onLoadListener;

	public myListView(Context context) {
		super(context);
		initView(context);
	}

	public myListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public myListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	// 涓嬫媺鍒锋柊鐩戝�?
	public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
		this.onRefreshListener = onRefreshListener;
	}

	// 鍔犺浇鏇村鐩戝�?
	public void setOnLoadListener(OnLoadListener onLoadListener) {
		this.loadEnable = true;
		this.onLoadListener = onLoadListener;
	}

	public boolean isLoadEnable() {
		return loadEnable;
	}

	// 杩欓噷鐨勫紑鍚垨鑰呭叧闂姞杞芥洿澶氾紝骞朵笉�?寔鍔ㄦ�佽皟鏁�?
	public void setLoadEnable(boolean loadEnable) {
		this.loadEnable = loadEnable;
		this.removeFooterView(footer);
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	// 鍒濆鍖栫粍浠�
	private void initView(Context context) {

		// 璁剧疆绠ご鐗规�?
		animation = new RotateAnimation(0, -180,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		animation.setInterpolator(new LinearInterpolator());
		animation.setDuration(100);
		animation.setFillAfter(true);

		reverseAnimation = new RotateAnimation(-180, 0,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		reverseAnimation.setInterpolator(new LinearInterpolator());
		reverseAnimation.setDuration(100);
		reverseAnimation.setFillAfter(true);

		inflater = LayoutInflater.from(context);
		/*footer = inflater.inflate(R.layout.listview_footer, null);
		loadFull = (TextView) footer.findViewById(R.id.loadFull);
		noData = (TextView) footer.findViewById(R.id.noData);
		more = (TextView) footer.findViewById(R.id.more);
		loading = (ProgressBar) footer.findViewById(R.id.loading);*/

		header = inflater.inflate(R.layout.header, null);
		arrow = (ImageView) header.findViewById(R.id.arrow);
		tip = (TextView) header.findViewById(R.id.tip);
		lastUpdate = (TextView) header.findViewById(R.id.lastUpdate);
		refreshing = (ProgressBar) header.findViewById(R.id.refreshing);

		// 涓簂istview娣诲姞澶撮儴鍜屽熬閮紝骞惰繘琛屽垵濮嬪�?
		headerContentInitialHeight = header.getPaddingTop();
		measureView(header);
		headerContentHeight = header.getMeasuredHeight();
		topPadding(-headerContentHeight);
		this.addHeaderView(header);
//		this.addFooterView(footer);
		this.setOnScrollListener(this);
	}

	public void onRefresh() {
		if (onRefreshListener != null) {
			onRefreshListener.onRefresh();
		}
	}

	public void onLoad() {
		if (onLoadListener != null) {
			onLoadListener.onLoad();
		}
	}

	public void onRefreshComplete(String updateTime) {
		lastUpdate.setText(this.getContext().getString(R.string.lastUpdateTime,
				Utils.getCurrentTime()));
		state = NONE;
		refreshHeaderViewByState();
	}

	// 鐢ㄤ簬涓嬫媺鍒锋柊缁撴潫鍚庣殑鍥炶皟
	public void onRefreshComplete() {
		String currentTime = Utils.getCurrentTime();
		onRefreshComplete(currentTime);
	}

	// 鐢ㄤ簬鍔犺浇鏇村缁撴潫鍚庣殑鍥炶皟
	public void onLoadComplete() {
		isLoading = false;
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		this.firstVisibleItem = firstVisibleItem;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		this.scrollState = scrollState;
		ifNeedLoad(view, scrollState);
	}

	// 鏍规嵁listview婊戝姩鐨勭姸鎬佸垽鏂槸鍚﹂渶瑕佸姞杞芥洿澶�?
	private void ifNeedLoad(AbsListView view, int scrollState) {
		if (!loadEnable) {
			return;
		}
		try {
			if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
					&& !isLoading
					&& view.getLastVisiblePosition() == view
							.getPositionForView(footer) && !isLoadFull) {
				onLoad();
				isLoading = true;
			}
		} catch (Exception e) {
		}
	}

	/**
	 * 鐩戝惉瑙︽懜浜嬩欢锛岃В璇绘墜鍔�
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (firstVisibleItem == 0) {
				isRecorded = true;
				startY = (int) ev.getY();
			}
			break;
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			if (state == PULL) {
				state = NONE;
				refreshHeaderViewByState();
			} else if (state == RELEASE) {
				state = REFRESHING;
				refreshHeaderViewByState();
				onRefresh();
			}
			isRecorded = false;
			break;
		case MotionEvent.ACTION_MOVE:
			whenMove(ev);
			break;
		}
		return true;
//		return super.onTouchEvent(ev);
	}

	// 瑙ｈ鎵嬪娍锛屽埛鏂癶eader鐘舵��
	private void whenMove(MotionEvent ev) {
		if (!isRecorded) {
			return;
		}
		int tmpY = (int) ev.getY();
		int space = tmpY - startY;
		int topPadding = space - headerContentHeight;
		switch (state) {
		case NONE:
			if (space > 0) {
				state = PULL;
				refreshHeaderViewByState();
			}
			break;
		case PULL:
			topPadding(topPadding);
			if (scrollState == SCROLL_STATE_TOUCH_SCROLL
					&& space > headerContentHeight + SPACE) {
				state = RELEASE;
				refreshHeaderViewByState();
			}
			break;
		case RELEASE:
			topPadding(topPadding);
			if (space > 0 && space < headerContentHeight + SPACE) {
				state = PULL;
				refreshHeaderViewByState();
			} else if (space <= 0) {
				state = NONE;
				refreshHeaderViewByState();
			}
			break;
		}

	}

	// 璋冩暣header鐨勫ぇ灏忋�傚叾瀹炶皟鏁寸殑鍙槸璺濈椤堕儴鐨勯珮搴︺��
	private void topPadding(int topPadding) {
		header.setPadding(header.getPaddingLeft(), topPadding,
				header.getPaddingRight(), header.getPaddingBottom());
		header.invalidate();
	}

	/**
	 * 杩欎釜鏂规硶鏄牴鎹粨鏋滅殑澶у皬鏉ュ喅瀹歠ooter鏄剧ず鐨勩��?
	 * <p>
	 * 杩欓噷鍋囧畾姣忔璇锋眰鐨勬潯鏁颁负10銆傚鏋滆姹傚埌浜�?10鏉°�傚垯璁や负杩樻湁鏁版嵁銆傚杩囩粨鏋�?笉瓒�?10鏉★紝鍒欒涓烘暟鎹凡缁忓叏閮ㄥ姞杞斤紝杩欐椂footer鏄剧ず宸茬粡鍏ㄩ儴鍔犺浇
	 * </p>
	 * 
	 * @param resultSize
	 */
	public void setResultSize(int resultSize) {
		if (resultSize == 0) {
			isLoadFull = true;
			loadFull.setVisibility(View.GONE);
			loading.setVisibility(View.GONE);
			more.setVisibility(View.GONE);
			noData.setVisibility(View.VISIBLE);
		} else if (resultSize > 0 && resultSize < pageSize) {
			isLoadFull = true;
			loadFull.setVisibility(View.VISIBLE);
			loading.setVisibility(View.GONE);
			more.setVisibility(View.GONE);
			noData.setVisibility(View.GONE);
		} else if (resultSize == pageSize) {
			isLoadFull = false;
			loadFull.setVisibility(View.GONE);
			loading.setVisibility(View.VISIBLE);
			more.setVisibility(View.VISIBLE);
			noData.setVisibility(View.GONE);
		}

	}

	// 鏍规嵁褰撳墠鐘舵�侊紝璋冩暣header
	private void refreshHeaderViewByState() {
		switch (state) {
		case NONE:
			topPadding(-headerContentHeight);
			tip.setText(R.string.pull_to_refresh);
			refreshing.setVisibility(View.GONE);
			arrow.clearAnimation();
			arrow.setImageResource(R.drawable.pull_to_refresh_arrow);
			break;
		case PULL:
			arrow.setVisibility(View.VISIBLE);
			tip.setVisibility(View.VISIBLE);
			lastUpdate.setVisibility(View.VISIBLE);
			refreshing.setVisibility(View.GONE);
			tip.setText(R.string.pull_to_refresh);
			arrow.clearAnimation();
			arrow.setAnimation(reverseAnimation);
			break;
		case RELEASE:
			arrow.setVisibility(View.VISIBLE);
			tip.setVisibility(View.VISIBLE);
			lastUpdate.setVisibility(View.VISIBLE);
			refreshing.setVisibility(View.GONE);
			tip.setText(R.string.pull_to_refresh);
			tip.setText(R.string.release_to_refresh);
			arrow.clearAnimation();
			arrow.setAnimation(animation);
			break;
		case REFRESHING:
			topPadding(headerContentInitialHeight);
			refreshing.setVisibility(View.VISIBLE);
			arrow.clearAnimation();
			arrow.setVisibility(View.GONE);
			tip.setVisibility(View.GONE);
			lastUpdate.setVisibility(View.GONE);
			break;
		}
	}

	// 鐢ㄦ潵璁＄畻header澶у皬鐨勩�傛瘮杈冮殣鏅︺�傚洜涓篽eader鐨勫垵濮嬮珮搴﹀氨鏄�?0,璨屼�?鍙互涓嶇敤銆�
	private void measureView(View child) {
		ViewGroup.LayoutParams p = child.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		int lpHeight = p.height;
		int childHeightSpec;
		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
					MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0,
					MeasureSpec.UNSPECIFIED);
		}
		child.measure(childWidthSpec, childHeightSpec);
	}

	/*
	 * 瀹氫箟涓嬫媺鍒锋柊鎺ュ彛
	 */
	public interface OnRefreshListener {
		public void onRefresh();
	}

	/*
	 * 瀹氫箟鍔犺浇鏇村鎺ュ彛
	 */
	public interface OnLoadListener {
		public void onLoad();
	}

}
