package kmitl.taweewong.teamtaskboard.controllers.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import kmitl.taweewong.teamtaskboard.R;
import kmitl.taweewong.teamtaskboard.adapters.ProjectItemAdapter;
import kmitl.taweewong.teamtaskboard.adapters.ProjectItemAdapter.OnClickProjectListener;
import kmitl.taweewong.teamtaskboard.models.Project;

import static kmitl.taweewong.teamtaskboard.models.Project.PROJECT_CLASS_KEY;

public class ShowProjectsFragment extends Fragment {
    private List<Project> projects;

    public ShowProjectsFragment() {
        // Required empty public constructor
    }

    public static ShowProjectsFragment newInstance(ArrayList<Project> projects) {
        Bundle args = new Bundle();
        args.putParcelableArrayList(PROJECT_CLASS_KEY, projects);
        
        ShowProjectsFragment fragment = new ShowProjectsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        projects = getArguments().getParcelableArrayList(PROJECT_CLASS_KEY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_show_projects, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ProjectItemAdapter projectItemAdapter = new ProjectItemAdapter(projects, (OnClickProjectListener) getContext());
        RecyclerView recyclerView = view.findViewById(R.id.showProjectsRecyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(projectItemAdapter);
    }
}
