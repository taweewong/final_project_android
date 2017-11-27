package kmitl.taweewong.teamtaskboard.controllers.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import kmitl.taweewong.teamtaskboard.R;
import kmitl.taweewong.teamtaskboard.adapters.TaskPagerAdapter;
import kmitl.taweewong.teamtaskboard.models.Tasks;
import kmitl.taweewong.teamtaskboard.services.DatabaseService;

public class TaskActivity extends AppCompatActivity implements
        DatabaseService.OnQueryTasksCompleteListener {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.tasksContainer) ViewPager viewPager;
    @BindView(R.id.tabs) TabLayout tabLayout;

    private Tasks tasks;
    private String projectId;
    private String itemId;

    public static final String TASK_LIST_KEY = "taskList";
    public static final String PROJECT_ID_KEY = "projectId";
    public static final String ITEM_ID_KEY = "itemId";
    public static final String TASK_TYPE_KEY = "taskType";
    public static final String TASK_KEY = "task";
    public static final String EDITED_TASK_KEY = "editedTask";
    public static final String POSITION_KEY = "position";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        ButterKnife.bind(this);

        projectId = getIntent().getStringExtra(PROJECT_ID_KEY);
        itemId = getIntent().getStringExtra(ITEM_ID_KEY);

        DatabaseService databaseService = new DatabaseService();
        databaseService.queryTasks(projectId, itemId, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.task_item_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logoutMenu:
                logout();
                startLoginActivity();
        }

        return super.onOptionsItemSelected(item);
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
        TaskPagerAdapter taskPagerAdapter = new TaskPagerAdapter(getSupportFragmentManager(), projectId, itemId);
        viewPager.setAdapter(taskPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(android.R.color.white));
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();
    }

    private void startLoginActivity() {
        startActivity(new Intent(this, LoginActivity.class));
    }
}
