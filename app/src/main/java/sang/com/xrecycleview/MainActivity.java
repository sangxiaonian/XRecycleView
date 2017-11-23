package sang.com.xrecycleview;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import sang.com.freerecycleview.adapter.XAdapter;
import sang.com.freerecycleview.holder.BaseHolder;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext=this;
        initView();


    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.rv);
        LinearLayoutManager manager =new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        List<String> list =new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list.add(getString(R.string.app_name)+i);
        }

        recyclerView.setAdapter(new XAdapter<String>(this,list) {
            @Override
            protected BaseHolder initHolder(ViewGroup parent, int viewType) {
                return new BaseHolder<String>(parent,mContext,R.layout.item_textview){
                    @Override
                    public void initView(View itemView, int position, String data) {
                        super.initView(itemView, position, data);
                        TextView textView=itemView.findViewById(R.id.item_text);
                        textView.setText(data);
                    }
                };
            }
        });

    }
}
