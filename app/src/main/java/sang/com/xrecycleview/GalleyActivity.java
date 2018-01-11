package sang.com.xrecycleview;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import sang.com.freerecycleview.adapter.XAdapter;
import sang.com.freerecycleview.holder.BaseHolder;
import sang.com.freerecycleview.view.GalleryRecycleView;

public class GalleyActivity extends AppCompatActivity {

    @BindView(R.id.rv)
    GalleryRecycleView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galley);
        ButterKnife.bind(this);

        LinearLayoutManager manager = new GalleyLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv.setLayoutManager(manager);

        List<String> list = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            list.add("测试数据"+i);
        }

        rv.setAdapter(new XAdapter<String>(this,list) {
            @Override
            protected BaseHolder initHolder(ViewGroup parent, int viewType) {
                return new BaseHolder<String>(parent,GalleyActivity.this,R.layout.item_img){
                    @Override
                    public void initView(View itemView, int position, String data) {
                        super.initView(itemView, position, data);
                        View view = itemView.findViewById(R.id.img);
                        if (position%2==0){
                            view.setBackgroundColor(Color.parseColor("#abcdef"));
                        }else {
                            view.setBackgroundColor(Color.parseColor("#fedcba"));
                        }
                    }
                };
            }
        });



    }
}
