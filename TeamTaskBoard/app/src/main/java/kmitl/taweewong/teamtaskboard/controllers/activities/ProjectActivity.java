package kmitl.taweewong.teamtaskboard.controllers.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import kmitl.taweewong.teamtaskboard.R;
import kmitl.taweewong.teamtaskboard.controllers.fragments.ShowProjectsFragment;

public class ProjectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);
        setTitle(getString(R.string.project_activity));

        initializeFragment();
    }

    private void initializeFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.projectFragmentContainer, new ShowProjectsFragment())
                .commit();
    }
}
