package com.laugh.timeline;
import android.app.*;
import android.os.*;
import android.support.v7.widget.RecyclerView;
import android.graphics.Paint;
import android.text.TextPaint;
import android.graphics.Rect;
import android.view.View;
import android.graphics.Canvas;
import java.text.SimpleDateFormat;
import android.widget.TextView;
import java.util.List;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import java.util.Date;
import android.text.StaticLayout;
import android.text.Layout;
import java.text.ParseException;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import com.laugh.timeline.MainActivity.NormalAdapter;
import java.util.ArrayList;
import com.laugh.timeline.MainActivity.Fruit;
public class MainActivity extends Activity {
	private LinearLayoutManager layoutManager;
	private List<Fruit> fruits = new ArrayList<Fruit>();
	private MainActivity.NormalAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
		layoutManager = new LinearLayoutManager(this);
		//设置布局管理器
		mRecyclerView.setLayoutManager(layoutManager);  
		mRecyclerView.addItemDecoration(new RVItemDecoration());
		//设置为垂直布局，这也是默认的  
		layoutManager.setOrientation(OrientationHelper.VERTICAL);

		for (int i = 0;i < 12;i++) {
			MainActivity.Fruit fruit = new Fruit();
			fruit.setName(String.valueOf(i));
			fruit.setIntroduce(String.valueOf(i+1));
			fruit.setTime("2018-0"+i+"-01 12:12:12");
			fruits.add(fruit);
		}
		adapter = new NormalAdapter(fruits);
		mRecyclerView.setAdapter(adapter);
    }


	private class RVItemDecoration extends RecyclerView.ItemDecoration {

		private Paint mPaint;
		private int mDividerHeight,mOffsetLeft;
		private float mRadiu;
		private TextPaint textPaint;
		public RVItemDecoration() {

			mPaint = new Paint();
			mPaint.setColor(getColor(R.color.colorAccent));
			mPaint.setAntiAlias(true);
			mPaint.setStyle(Paint.Style.FILL);
			mPaint.setStrokeWidth(5);
			mPaint.setTextSize(getResources().getDimension(R.dimen.sp_12));
			mRadiu = 16;

			textPaint = new TextPaint();
			textPaint.setColor(getResources().getColor(R.color.colorAccent));
			textPaint.setTextSize(getResources().getDimension(R.dimen.sp_12));
			textPaint.setAntiAlias(true);
		}

		@Override
		public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
			super.getItemOffsets(outRect, view, parent, state);

			if (parent.getChildAdapterPosition(view) == 0) {
				outRect.top = 0;
			} else {
				outRect.top = 5;
				mDividerHeight = 5;
			}

			//view的左右的padding
			outRect.left = 160;
			mOffsetLeft = 160;
		}

		/**
		 * 在item下方画
		 * @param c
		 * @param parent
		 * @param state
		 */

		@Override
		public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
			super.onDraw(c, parent, state);
			if (parent.getChildCount() == 0) return;
			
			for (int i = 0; i <parent.getChildCount(); i++) {
				View view = parent.getChildAt(i);
				float circleX = (view.getLeft()-mOffsetLeft / 2) + 30;
				float circleY = (view.getHeight() / 2) + view.getTop();
				try {
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:m:s");
					Date Date = format.parse(((NormalAdapter)parent.getAdapter()).getItem(parent.getChildAdapterPosition(view)).getTime());
					if (RelativeDateFormat.format(Date).length() > 4) {
						StringBuilder stringBuilder = new StringBuilder(RelativeDateFormat.format(Date));
						stringBuilder.insert(4, "\n");
						StaticLayout layout = new StaticLayout(stringBuilder.toString(), textPaint, 80,
															   Layout.Alignment.ALIGN_NORMAL, 1.0F, 0.0F, true);
						c.save();
						c.translate(18, ((view.getHeight() / 2) + view.getTop()) - 25);
						layout.draw(c);
						c.restore();
						
					} else {
						c.drawText(RelativeDateFormat.format(Date), 18, ((view.getHeight() / 2) + view.getTop()) + 6, mPaint);
					}
				} catch (ParseException e) {}
				if (parent.getChildCount() != 1) {
					if(parent.getChildAdapterPosition(view)==0)
					{
						c.drawLine(circleX, (view.getHeight() / 2) + view.getTop(), circleX, view.getBottom()*2, mPaint);//画轴线
					}else if(parent.getChildAdapterPosition(view)==parent.getAdapter().getItemCount()-1){
						c.drawLine(circleX, (view.getHeight() / 2) + view.getTop(), circleX, view.getBottom()/2, mPaint);
					}else{
						c.drawLine(circleX, view.getTop(), circleX, view.getBottom()+5, mPaint);
					}
				}
				c.drawLine(circleX, circleY, mOffsetLeft + 30, circleY, mPaint);
				
				c.drawCircle(circleX, circleY, mRadiu, mPaint);//画小圆点
			}
		}
	}


	class NormalAdapter extends RecyclerView.Adapter<NormalAdapter.HV> {

		public int type;
		public class HV extends RecyclerView.ViewHolder {
			public TextView Name;
			public TextView Introduce;
			public HV(View view) {
				super(view);
				Name = (TextView) view.findViewById(R.id.title);
				Introduce = (TextView) view.findViewById(R.id.Introduce);
			}
		}
		public List<Fruit> mDatas;
		public NormalAdapter(List<Fruit> data) {
			this.mDatas = data;
		}

		//③ 在Adapter中实现3个方法
		@Override
		public void onBindViewHolder(final HV holder, int position) {
			Fruit fruit = mDatas.get(position);
			holder.Name.setText(fruit.getName());
			holder.Introduce.setText(fruit.getIntroduce());
		}

		@Override
		public int getItemCount() {
			return mDatas.size();
		}

		public List<Fruit> getItem() {
			return mDatas;
		}

		public Fruit getItem(int position) {
			return mDatas.get(position);
		}
		@Override
		public HV onCreateViewHolder(ViewGroup parent, int viewType) {
			View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listviewitem, parent, false);
			return new HV(v);
		}
	}

	public class Fruit {

		private String Name;
		private String Introduce;
		private String time;
		public Fruit() {
		}
		public Fruit(String Name, String Introduce) {
			this.Name = Name;
			this.Introduce = Introduce;
		}

		public void setTime(String time) {
			this.time = time;
		}

		public String getTime() {
			return time;
		}

		public String getName() {
			return Name;
		}
		public void setName(String Name) {
			this.Name  = Name;
		}
		public String getIntroduce() {
			return Introduce;
		}
		public void setIntroduce(String Introduce) {
			this.Introduce = Introduce;
		}
	}
}
