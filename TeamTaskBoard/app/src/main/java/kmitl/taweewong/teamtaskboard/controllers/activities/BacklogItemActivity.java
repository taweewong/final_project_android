package kmitl.taweewong.teamtaskboard.controllers.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import kmitl.taweewong.teamtaskboard.R;
import kmitl.taweewong.teamtaskboard.models.BacklogItem;
import kmitl.taweewong.teamtaskboard.models.Project;
import kmitl.taweewong.teamtaskboard.services.DatabaseService;

public class BacklogItemActivity extends AppCompatActivity implements
        DatabaseService.OnQueryBacklogItemsCompleteListener {
    private List<BacklogItem> backlogItems;
    private Project project;
    private String projectId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backlog_item);

        project = getIntent().getParcelableExtra("project");
        setTitle(String.format("%s's backlog items", project.getName()));

        if (project != null) {
            projectId = project.getProjectId();
        }

        DatabaseService databaseService = new DatabaseService();
        databaseService.queryBacklogItems(projectId, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.project_page_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addProjectMenu:
                Toast.makeText(this, "add item is in progress", Toast.LENGTH_SHORT).show();
                break;
            case R.id.logoutMenu:
                logout();
                startLoginActivity();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onQueryBacklogItemsSuccess(List<BacklogItem> backlogItems) {
        this.backlogItems = backlogItems;

        for (BacklogItem item : backlogItems) {
            Log.d("DEBUG", item.getTitle());
        }
    }

    @Override
    public void onQueryBacklogItemsFailed() {
        Toast.makeText(this, "Query Failed", Toast.LENGTH_SHORT).show();
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();
    }

    private void startLoginActivity() {
        startActivity(new Intent(this, LoginActivity.class));
    }
}
