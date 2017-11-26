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
import kmitl.taweewong.teamtaskboard.models.Tasks;

import static kmitl.taweewong.teamtaskboard.models.Tasks.TASKS_CLASS_KEY;

public class BacklogItemActivity extends AppCompatActivity implements
        AddBacklogItemFragment.OnAddBacklogItemCompleteListener,
        BacklogItemAdapter.OnClickBacklogItemListener,
        EditBacklogItemFragment.OnEditBacklogItemCompleteListener {
    private ArrayList<BacklogItem> backlogItems;
    private String projectId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backlog_item);

        Project project = getIntent().getParcelableExtra("project");
        setTitle(String.format("%s's backlog items", project.getName()));

        projectId = project.getProjectId();
        backlogItems = new ArrayList<>();
        initializeFragment();
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
    public void onAddBacklogItemComplete(BacklogItem backlogItem) {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onClickBacklogItem(int position) {
        BacklogItem selectedItem = backlogItems.get(position);
        startTaskActivity(selectedItem.getTasks(), selectedItem.getId());
    }

    @Override
    public void onLongClickBacklogItem(int position) {
        replaceEditBacklogItemFragment(position);
    }

    @Override
    public void onEditBacklogItemComplete(int position) {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onDeleteBacklogItemComplete(int position) {
        getSupportFragmentManager().popBackStack();
    }

    private void initializeFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_to_right)
                .add(R.id.backlogItemFragmentContainer, ShowBacklogItemsFragment.newInstance(backlogItems, projectId))
                .commit();
    }

    private void replaceAddBacklogItemFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in_from_right,
                        R.anim.slide_out_to_left,
                        R.anim.slide_in_from_left,
                        R.anim.slide_out_to_right)
                .replace(R.id.backlogItemFragmentContainer,
                        AddBacklogItemFragment.newInstance(projectId, backlogItems))
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
                .replace(R.id.backlogItemFragmentContainer,
                        EditBacklogItemFragment.newInstance(projectId, backlogItems, position))
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

    private void startTaskActivity(Tasks tasks, String itemId) {
        Intent intent = new Intent(this, TaskActivity.class);
        intent.putExtra(TASKS_CLASS_KEY, tasks);
        intent.putExtra("projectId", projectId);
        intent.putExtra("itemId", itemId);

        startActivity(intent);
    }
}
