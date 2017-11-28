package kmitl.taweewong.teamtaskboard.controllers.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import kmitl.taweewong.teamtaskboard.R;
import kmitl.taweewong.teamtaskboard.adapters.ProjectItemAdapter;
import kmitl.taweewong.teamtaskboard.adapters.ProjectItemAdapter.OnClickProjectListener;
import kmitl.taweewong.teamtaskboard.models.Project;
import kmitl.taweewong.teamtaskboard.services.DatabaseService;

import static kmitl.taweewong.teamtaskboard.models.Project.PROJECT_CLASS_KEY;

public class ShowProjectsFragment extends Fragment implements DatabaseService.OnQueryProjectsCompleteListener {
    private List<Project> projects;
    private ProjectItemAdapter projectItemAdapter;
    private String userId;

    private static final String USER_ID_KEY = "userId";

    public ShowProjectsFragment() {
        // Required empty public constructor
    }

    public static ShowProjectsFragment newInstance(String userId) {
        Bundle args = new Bundle();
        args.putString(USER_ID_KEY, userId);
        
        ShowProjectsFragment fragment = new ShowProjectsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        projects = new ArrayList<>();
        userId = getArguments().getString(USER_ID_KEY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_show_projects, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        projectItemAdapter = new ProjectItemAdapter(projects, (OnClickProjectListener) getContext());
        RecyclerView recyclerView = view.findViewById(R.id.showProjectsRecyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(projectItemAdapter);

        DatabaseService databaseService = new DatabaseService();
        databaseService.queryProjects(userId, this);
    }

    @Override
    public void onQueryProjectsSuccess(ArrayList<Project> projects) {
        this.projects.clear();
        this.projects.addAll(projects);
        projectItemAdapter.notifyDataSetChanged();
    }

    @Override
    public void onQueryProjectsFailed() {
        Toast.makeText(getContext(), "Query Failed", Toast.LENGTH_SHORT).show();
    }
}
