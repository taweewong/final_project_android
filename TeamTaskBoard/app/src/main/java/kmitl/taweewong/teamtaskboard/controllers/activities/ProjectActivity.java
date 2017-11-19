package kmitl.taweewong.teamtaskboard.controllers.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import kmitl.taweewong.teamtaskboard.R;
import kmitl.taweewong.teamtaskboard.controllers.fragments.AddProjectFragment;
import kmitl.taweewong.teamtaskboard.controllers.fragments.ShowProjectsFragment;
import kmitl.taweewong.teamtaskboard.models.Project;
import kmitl.taweewong.teamtaskboard.models.User;
import kmitl.taweewong.teamtaskboard.services.ProjectQueryService;

import static kmitl.taweewong.teamtaskboard.models.User.USER_CLASS_KEY;

public class ProjectActivity extends AppCompatActivity implements ProjectQueryService.OnQueryProjectsCompleteListener {
    ProjectQueryService projectQueryService;
    ArrayList<Project> projects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);
        setTitle(getString(R.string.project_activity));

        User user = getIntent().getParcelableExtra(USER_CLASS_KEY);
        List<String> projectIds = new ArrayList<>();

        if (user.getProjects() != null) {
            projectIds = user.getProjects();
        }

        projectQueryService = new ProjectQueryService();
        projectQueryService.queryProjects(projectIds, this);
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
                updateFragment(new AddProjectFragment());
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onQueryProjectsSuccess(ArrayList<Project> projects) {
        this.projects = projects;
        initializeFragment(projects);
    }


    @Override
    public void onQueryProjectsFailed() {
        Toast.makeText(this, "Query Failed", Toast.LENGTH_SHORT).show();
    }

    private void initializeFragment(ArrayList<Project> projects) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.projectFragmentContainer, ShowProjectsFragment.newInstance(projects))
                .commit();
    }

    private void updateFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.projectFragmentContainer, fragment)
                .addToBackStack(null)
                .commit();
    }
}
