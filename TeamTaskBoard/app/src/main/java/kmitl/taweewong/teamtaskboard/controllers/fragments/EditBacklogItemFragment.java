package kmitl.taweewong.teamtaskboard.controllers.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kmitl.taweewong.teamtaskboard.R;
import kmitl.taweewong.teamtaskboard.models.BacklogItem;
import kmitl.taweewong.teamtaskboard.models.Project;
import kmitl.taweewong.teamtaskboard.services.DatabaseService;

import static kmitl.taweewong.teamtaskboard.models.Project.PROJECT_CLASS_KEY;

public class EditBacklogItemFragment extends Fragment {

    public interface OnEditBacklogItemCompleteListener {
        void onEditBacklogItemComplete(int position, BacklogItem editedBacklogItem);
        void onDeleteBacklogItemComplete(int position);
    }

    @BindView(R.id.editBacklogItemNameEditText) EditText editItemNameEditText;

    private Project project;
    private int position;
    private OnEditBacklogItemCompleteListener listener;

    private static String POSITION_KEY = "position";

    public EditBacklogItemFragment() {
        // Required empty public constructor
    }

    public static EditBacklogItemFragment newInstance(Project project, int position) {
        Bundle args = new Bundle();
        args.putParcelable(PROJECT_CLASS_KEY, project);
        args.putInt(POSITION_KEY, position);
        
        EditBacklogItemFragment fragment = new EditBacklogItemFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        project = getArguments().getParcelable(PROJECT_CLASS_KEY);
        position = getArguments().getInt(POSITION_KEY);
        setOnEditBacklogItemCompleteListener((OnEditBacklogItemCompleteListener) getActivity());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.findItem(R.id.addBacklogItemMenu).setVisible(false);
        menu.findItem(R.id.deleteBacklogItemMenu).setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.deleteBacklogItemMenu:
                deleteBacklogItem();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_edit_backlog_item, container, false);
        ButterKnife.bind(this, rootView);
        editItemNameEditText.setText(project.getBacklogItems().get(position).getTitle());

        return rootView;
    }

    @OnClick(R.id.editBacklogItemButton)
    public void editBacklogItem() {
        String editedTitle = editItemNameEditText.getText().toString();
        DatabaseService databaseService = new DatabaseService();

        BacklogItem editedItem = project.getBacklogItems().get(position);
        editedItem.setTitle(editedTitle);

        databaseService.editBacklogItem(editedItem, project.getProjectId(), position, project.getBacklogItems());
        listener.onEditBacklogItemComplete(position, editedItem);
    }

    private void deleteBacklogItem() {
        DatabaseService databaseService = new DatabaseService();
        databaseService.deleteBacklogItem(project.getProjectId(), position, project.getBacklogItems());
        listener.onDeleteBacklogItemComplete(position);
    }

    public void setOnEditBacklogItemCompleteListener(OnEditBacklogItemCompleteListener listener) {
        this.listener = listener;
    }
}
