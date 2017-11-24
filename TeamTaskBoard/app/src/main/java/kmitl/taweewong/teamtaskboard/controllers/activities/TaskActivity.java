package kmitl.taweewong.teamtaskboard.controllers.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import kmitl.taweewong.teamtaskboard.R;
import kmitl.taweewong.teamtaskboard.adapters.TaskPagerAdapter;

public class TaskActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TaskPagerAdapter taskPagerAdapter = new TaskPagerAdapter(getSupportFragmentManager());

        ViewPager viewPager = findViewById(R.id.container);
        viewPager.setAdapter(taskPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(android.R.color.white));
    }
}
