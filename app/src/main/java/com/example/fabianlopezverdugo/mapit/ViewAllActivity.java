package com.example.fabianlopezverdugo.mapit;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import java.util.List;

public class ViewAllActivity extends ListActivity {

    private VisitorDataSource dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all);

        dataSource = new VisitorDataSource(this);
        dataSource.open();

        List<Visitor> values = dataSource.getAllVisitors();

        ArrayAdapter<Visitor> adapter = new ArrayAdapter<Visitor>(this,android.R.layout.simple_list_item_1,values);

        setListAdapter(adapter);
    }

    public void onCliclkMapButton(View view){
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }
}
