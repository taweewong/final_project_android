package kmitl.taweewong.teamtaskboard.controllers.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import kmitl.taweewong.teamtaskboard.R;
import kmitl.taweewong.teamtaskboard.adapters.TaskItemAdapter;
import kmitl.taweewong.teamtaskboard.adapters.TaskPagerAdapter;
import kmitl.taweewong.teamtaskboard.models.Tasks;
import kmitl.taweewong.teamtaskboard.services.DatabaseService;

public class TaskActivity extends AppCompatActivity implements
        DatabaseService.OnQueryTasksCompleteListener,
        TaskItemAdapter.OnClickTaskListener {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.tasksContainer) ViewPager viewPager;
    @BindView(R.id.tabs) TabLayout tabLayout;

    private Tasks tasks;
    private String projectId;
    private String itemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        ButterKnife.bind(this);

        projectId = getIntent().getStringExtra("projectId");
        itemId = getIntent().getStringExtra("itemId");

        DatabaseService databaseService = new DatabaseService();
        databaseService.queryTasks(projectId, itemId, this);
    }

    @Override
    public void onQueryTasksItemsSuccess(Tasks tasks) {
        this.tasks = tasks;
        initializeTab();
    }

    @Override
    public void onQueryTasksItemsFailed() {

    }

    private void initializeTab() {
        setSupportActionBar(toolbar);
        TaskPagerAdapter taskPagerAdapter = new TaskPagerAdapter(getSupportFragmentManager(), tasks, projectId, itemId);
        viewPager.setAdapter(taskPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(android.R.color.white));
    }

    @Override
    public void onClickTask(int position) {

    }

    @Override
    public void onLongClickTask(int position) {

    }
}
