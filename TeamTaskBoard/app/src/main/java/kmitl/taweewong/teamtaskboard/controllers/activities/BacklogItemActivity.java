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

import kmitl.taweewong.teamtaskboard.R;
import kmitl.taweewong.teamtaskboard.adapters.BacklogItemAdapter;
import kmitl.taweewong.teamtaskboard.controllers.fragments.AddBacklogItemFragment;
import kmitl.taweewong.teamtaskboard.controllers.fragments.EditBacklogItemFragment;
import kmitl.taweewong.teamtaskboard.controllers.fragments.ShowBacklogItemsFragment;
import kmitl.taweewong.teamtaskboard.models.BacklogItem;
import kmitl.taweewong.teamtaskboard.models.Project;
import kmitl.taweewong.teamtaskboard.services.DatabaseService;

public class BacklogItemActivity extends AppCompatActivity implements
        DatabaseService.OnQueryBacklogItemsCompleteListener,
        AddBacklogItemFragment.OnAddBacklogItemCompleteListener,
        BacklogItemAdapter.OnClickBacklogItemListener,
        EditBacklogItemFragment.OnEditBacklogItemCompleteListener {
    private ArrayList<BacklogItem> backlogItems;
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
        getMenuInflater().inflate(R.menu.backlog_item_menu, menu);
        menu.findItem(R.id.deleteBacklogItemMenu).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addBacklogItemMenu:
                replaceAddBacklogItemFragment();
                break;
            case R.id.logoutMenu:
                logout();
                startLoginActivity();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onQueryBacklogItemsSuccess(ArrayList<BacklogItem> backlogItems) {
        this.backlogItems = backlogItems;
        initializeFragment(backlogItems);
    }

    @Override
    public void onQueryBacklogItemsFailed() {
        Toast.makeText(this, "Query Failed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAddBacklogItemComplete(BacklogItem backlogItem) {
        this.backlogItems.add(backlogItem);
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onClickBacklogItem(int position) {
        Toast.makeText(this, "click " + backlogItems.get(position).getTitle(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLongClickBacklogItem(int position) {
        replaceEditBacklogItemFragment(position);
    }

    @Override
    public void onEditBacklogItemComplete(int position, BacklogItem editedBacklogItem) {
        this.backlogItems.set(position, editedBacklogItem);
        getSupportFragmentManager().popBackStack();
    }

    private void initializeFragment(ArrayList<BacklogItem> backlogItems) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_to_right)
                .add(R.id.backlogItemFragmentContainer, ShowBacklogItemsFragment.newInstance(backlogItems))
                .commit();
    }

    private void replaceAddBacklogItemFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in_from_right,
                        R.anim.slide_out_to_left,
                        R.anim.slide_in_from_left,
                        R.anim.slide_out_to_right)
                .replace(R.id.backlogItemFragmentContainer, AddBacklogItemFragment.newInstance(project))
                .addToBackStack(null)
                .commit();
    }

    private void replaceEditBacklogItemFragment(int position) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in_from_right,
                        R.anim.slide_out_to_left,
                        R.anim.slide_in_from_left,
                        R.anim.slide_out_to_right)
                .replace(R.id.backlogItemFragmentContainer, EditBacklogItemFragment.newInstance(project, position))
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
}
