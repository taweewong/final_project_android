package kmitl.taweewong.teamtaskboard.controllers.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import kmitl.taweewong.teamtaskboard.R;
import kmitl.taweewong.teamtaskboard.adapters.ProjectItemAdapter;
import kmitl.taweewong.teamtaskboard.controllers.fragments.AddProjectFragment;
import kmitl.taweewong.teamtaskboard.controllers.fragments.EditProjectFragment;
import kmitl.taweewong.teamtaskboard.controllers.fragments.ShowProjectsFragment;
import kmitl.taweewong.teamtaskboard.models.Project;
import kmitl.taweewong.teamtaskboard.models.User;
import kmitl.taweewong.teamtaskboard.services.DatabaseService;

import static kmitl.taweewong.teamtaskboard.models.User.USER_CLASS_KEY;

public class ProjectActivity extends AppCompatActivity implements
        AddProjectFragment.OnAddProjectCompleteListener,
        EditProjectFragment.OnEditProjectCompleteListener,
        ProjectItemAdapter.OnClickProjectListener {
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);
        setTitle(getString(R.string.project_activity));
        user = getIntent().getParcelableExtra(USER_CLASS_KEY);

        initializeFragment();
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
                replaceAddProjectFragment();
                break;
            case R.id.logoutMenu:
                logout();
                startLoginActivity();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAddProjectComplete(Project project) {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onEditProjectComplete(int position, String editedProject) {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onClickProject(int position, List<Project> projects) {
        startBacklogItemActivity(projects.get(position));
    }

    @Override
    public void onLongClickProject(int position, List<Project> projects) {
        replaceEditProjectFragment(position, projects.get(position));
    }

    private void initializeFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_to_right)
                .add(R.id.projectFragmentContainer, ShowProjectsFragment.newInstance(user.getUserId()))
                .commit();
    }

    private void replaceAddProjectFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in_from_right,
                        R.anim.slide_out_to_left,
                        R.anim.slide_in_from_left,
                        R.anim.slide_out_to_right)
                .replace(R.id.projectFragmentContainer,
                        AddProjectFragment.newInstance(user))
                .addToBackStack(null)
                .commit();
    }

    private void replaceEditProjectFragment(int position, Project project) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in_from_right,
                        R.anim.slide_out_to_left,
                        R.anim.slide_in_from_left,
                        R.anim.slide_out_to_right)
                .replace(R.id.projectFragmentContainer,
                        EditProjectFragment.newInstance(project.getProjectId(),
                                project.getName(),
                                position,
                                project.getMembers()))
                .addToBackStack(null)
                .commit();
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();
    }

    private void startLoginActivity() {
        startActivity(new Intent(this, LoginActivity.class));
    }

    private void startBacklogItemActivity(Project project) {
        Intent intent = new Intent(this, BacklogItemActivity.class);
        intent.putExtra("project", project);
        startActivity(intent);
    }
}
