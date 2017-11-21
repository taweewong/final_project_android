package kmitl.taweewong.teamtaskboard.controllers.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kmitl.taweewong.teamtaskboard.R;
import kmitl.taweewong.teamtaskboard.models.Project;
import kmitl.taweewong.teamtaskboard.models.User;
import kmitl.taweewong.teamtaskboard.services.DatabaseService;

import static kmitl.taweewong.teamtaskboard.models.User.USER_CLASS_KEY;

public class AddProjectFragment extends Fragment {

    public interface OnAddProjectCompleteListener {
        void onAddProjectComplete(Project project);
    }

    @BindView(R.id.projectNameEditText) EditText projectNameEditText;

    private OnAddProjectCompleteListener listener;
    private User user;

    public AddProjectFragment() {
        // Required empty public constructor
    }

    public static AddProjectFragment newInstance(User user) {
        Bundle args = new Bundle();
        args.putParcelable(USER_CLASS_KEY, user);

        AddProjectFragment fragment = new AddProjectFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setOnAddProjectCompleteListener((OnAddProjectCompleteListener) getActivity());
        user = getArguments().getParcelable(USER_CLASS_KEY);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.findItem(R.id.addProjectMenu).setVisible(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_project, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @OnClick(R.id.addProjectButton)
    public void addProject() {
        String projectName = projectNameEditText.getText().toString();
        DatabaseService databaseService = new DatabaseService();

        Project newProject = databaseService.createNewUserProject(projectName, user.getUserId(), user.getProjects());
        listener.onAddProjectComplete(newProject);
    }

    public void setOnAddProjectCompleteListener(OnAddProjectCompleteListener listener) {
        this.listener = listener;
    }
}
