package sang.com.xrecycleview;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import sang.com.freerecycleview.adapter.RefrushAdapter;
import sang.com.freerecycleview.adapter.XAdapter;
import sang.com.freerecycleview.holder.BaseHolder;
import sang.com.freerecycleview.view.RefrushRecycleView;
import sang.com.freerecycleview.view.SpringRecycleView;
import sang.com.freerecycleview.view.refrush.RefrushControl;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    RefrushRecycleView recyclerView;
    Button btRefrush;
    Button btLoadMore;
    List<String> list;
    private Context mContext;
    private RefrushAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        initView();


    }

    private void initView() {
        recyclerView = (RefrushRecycleView) findViewById(R.id.rv);
        btLoadMore = findViewById(R.id.bt_load);
        btRefrush = findViewById(R.id.bt_refrush);

        btRefrush.setOnClickListener(this);
        btLoadMore.setOnClickListener(this);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        list = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            list.add(getString(R.string.app_name) + i);
        }

        recyclerView.setListener(new RefrushControl.RefrushListener() {
            @Override
            public void onRefrush() {
                int size = list.size() - 1;
                list.clear();
                for (int i = 0; i < size - 1; i++) {
                    if (i == size - 1) {
                        list.add("更新数据" + i);
                    }
                }
                adapter.notifyDataSetChanged();
                recyclerView.refrushSuccess(true,200);
            }

            @Override
            public void onLoadMore() {
                list.add("新增数据了"+list.size());
                adapter.notifyDataSetChanged();
                recyclerView.loadMore(true,200);
            }
        });

        adapter = new RefrushAdapter<String>(this, list) {
            @Override
            protected BaseHolder initHolder(ViewGroup parent, int viewType) {
                return new BaseHolder<String>(parent, mContext, R.layout.item_textview) {
                    @Override
                    public void initView(View itemView, int position, String data) {
                        super.initView(itemView, position, data);
                        TextView textView = itemView.findViewById(R.id.item_text);
                        textView.setText(data);
                        if (position % 2 == 1) {
                            itemView.setBackgroundColor(Color.parseColor("#abcdef"));
                        } else {
                            itemView.setBackgroundColor(Color.parseColor("#fedcba"));

                        }
                    }
                };
            }
        };
        recyclerView.setAdapter(adapter);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_load:
                recyclerView.loadMore(true);
                break;
            case R.id.bt_refrush:
                recyclerView.refrushSuccess(true);
                break;
        }
    }
}
