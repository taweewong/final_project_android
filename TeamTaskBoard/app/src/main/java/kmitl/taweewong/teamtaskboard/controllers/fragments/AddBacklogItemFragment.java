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

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kmitl.taweewong.teamtaskboard.R;
import kmitl.taweewong.teamtaskboard.models.BacklogItem;
import kmitl.taweewong.teamtaskboard.services.DatabaseService;

import static kmitl.taweewong.teamtaskboard.models.Project.PROJECT_CLASS_KEY;

public class AddBacklogItemFragment extends Fragment {

    public interface OnAddBacklogItemCompleteListener {
        void onAddBacklogItemComplete(BacklogItem backlogItem);
    }

    @BindView(R.id.backlogItemNameEditText) EditText backlogItemNameEditText;

    private String projectId;
    private ArrayList<BacklogItem> backlogItems;
    private OnAddBacklogItemCompleteListener listener;

    private static String BACKLOG_ITEMS_KEY = "backlogItems";

    public AddBacklogItemFragment() {
        // Required empty public constructor
    }

    public static AddBacklogItemFragment newInstance(String projectId, ArrayList<BacklogItem> backlogItems) {
        Bundle args = new Bundle();
        args.putString(PROJECT_CLASS_KEY, projectId);
        args.putParcelableArrayList(BACKLOG_ITEMS_KEY, backlogItems);

        AddBacklogItemFragment fragment = new AddBacklogItemFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setOnAddBacklogItemCompleteListener((OnAddBacklogItemCompleteListener) getActivity());
        projectId = getArguments().getString(PROJECT_CLASS_KEY);
        backlogItems = getArguments().getParcelableArrayList(BACKLOG_ITEMS_KEY);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.findItem(R.id.addBacklogItemMenu).setVisible(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_backlog_item, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @OnClick(R.id.addBacklogItemButton)
    public void addBacklogItem() {
        String backlogItemTitle = backlogItemNameEditText.getText().toString();
        DatabaseService databaseService = new DatabaseService();

        BacklogItem newBacklogItem = createNewBacklogItem(backlogItemTitle);

        databaseService.addBacklogItem(newBacklogItem, projectId, backlogItems);
        listener.onAddBacklogItemComplete(newBacklogItem);
    }

    private void setOnAddBacklogItemCompleteListener(OnAddBacklogItemCompleteListener listener) {
        this.listener = listener;
    }

    private BacklogItem createNewBacklogItem(String title) {
        BacklogItem backlogItem = new BacklogItem();
        backlogItem.setTitle(title);
        return backlogItem;
    }
}
