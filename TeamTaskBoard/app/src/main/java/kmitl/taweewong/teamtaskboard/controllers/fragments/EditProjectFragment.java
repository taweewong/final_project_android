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
import kmitl.taweewong.teamtaskboard.services.DatabaseService;

public class EditProjectFragment extends Fragment {

    public interface OnEditProjectCompleteListener {
        void onEditProjectComplete(int position, String editedProject);
    }

    @BindView(R.id.editProjectNameEditText) EditText editProjectNameEditText;

    private String projectId;
    private String projectName;
    private int position;
    private OnEditProjectCompleteListener listener;

    private static String PROJECT_ID_KEY = "projectId";
    private static String PROJECT_NAME_KEY = "projectName";
    private static String POSITION_KEY = "position";

    public EditProjectFragment() {
        // Required empty public constructor
    }

    public static EditProjectFragment newInstance(String projectId,
                                                  String projectName,
                                                  int position) {
        Bundle args = new Bundle();
        args.putString(PROJECT_ID_KEY, projectId);
        args.putString(PROJECT_NAME_KEY, projectName);
        args.putInt(POSITION_KEY, position);

        EditProjectFragment fragment = new EditProjectFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        projectId = getArguments().getString(PROJECT_ID_KEY);
        projectName = getArguments().getString(PROJECT_NAME_KEY);
        position = getArguments().getInt(POSITION_KEY);
        setOnEditProjectCompleteListener((OnEditProjectCompleteListener) getActivity());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.findItem(R.id.addProjectMenu).setVisible(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_edit_project, container, false);
        ButterKnife.bind(this, rootView);
        editProjectNameEditText.setText(projectName);

        return rootView;
    }

    @OnClick(R.id.editProjectButton)
    public void editProject() {
        String editedProject = editProjectNameEditText.getText().toString();
        DatabaseService databaseService = new DatabaseService();

        databaseService.editProject(projectId, editedProject);
        listener.onEditProjectComplete(position, editedProject);
    }

    public void setOnEditProjectCompleteListener(OnEditProjectCompleteListener listener) {
        this.listener = listener;
    }
}
