package sang.com.xrecycleview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import sang.com.freerecycleview.adapter.RefrushAdapter;
import sang.com.freerecycleview.view.RefrushRecycleView;

public class MainActivity extends AppCompatActivity  {


    @BindView(R.id.bt_refrush)
    Button btRefrush;
    @BindView(R.id.bt_galley)
    Button btGalley;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

    }

    @OnClick({R.id.bt_refrush,R.id.bt_galley})
    void click(View view){
        switch (view.getId()){
            case R.id.bt_refrush:
                startActivity(new Intent(this,RefrushActivity.class));
                break;
            case R.id.bt_galley:
                startActivity(new Intent(this,GalleyActivity.class));
                break;
        }
    }


}
