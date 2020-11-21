package com.yxd.lvjie.test;

import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.yxd.lvjie.R;
import com.yxd.lvjie.bean.Option;
import com.yxd.lvjie.bluetooth.Utils;

import java.util.List;


/**
 * Created by liu on 15/8/9.
 */
public class OptionsMenu extends LinearLayout {
    private static final int CONTEXT_MENU_WIDTH = Utils.dpToPx(200);

    RecyclerView rv_options;

    private List<Option> list;
    private OptionsSelectAdapter.OptionsOnItemSelectedListener optionsOnItemSelectedListener;

    public OptionsMenu(Context context, List<Option> list, OptionsSelectAdapter.OptionsOnItemSelectedListener optionsOnItemSelectedListener) {
        super(context);
        this.list = list;
        this.optionsOnItemSelectedListener = optionsOnItemSelectedListener;
        init();
    }


    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.options_menu, this, true);
        setBackgroundResource(R.drawable.bg_container_shadow);
        setOrientation(VERTICAL);
        int height;
        if (list.size() == 2){
            height = Utils.dpToPx(61*list.size());
        }else {
            height = Utils.dpToPx(55*list.size());
        }
        setLayoutParams(new LayoutParams(CONTEXT_MENU_WIDTH,height));
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        initRecycleView();
    }


    private void initRecycleView(){
        OptionsSelectAdapter adapter = new OptionsSelectAdapter(getContext(),list);
        adapter.setConnectionsOnItemSelectedListener(optionsOnItemSelectedListener);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv_options = findViewById(R.id.rv_options);
        rv_options.setLayoutManager(llm);
        rv_options.setAdapter(adapter);
    }


    public void dismiss(){
        ((ViewGroup)getParent()).removeView(OptionsMenu.this);
    }

}
