package kmitl.taweewong.teamtaskboard.controllers.fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kmitl.taweewong.teamtaskboard.R;
import kmitl.taweewong.teamtaskboard.adapters.MemberAdapter;
import kmitl.taweewong.teamtaskboard.services.DatabaseService;

public class EditProjectFragment extends Fragment implements DatabaseService.OnQueryMembersCompleteListener {

    public interface OnEditProjectCompleteListener {
        void onEditProjectComplete(int position, String editedProject);
    }

    @BindView(R.id.editProjectNameEditText) EditText editProjectNameEditText;
    @BindView(R.id.memberRecyclerView) RecyclerView recyclerView;
    private EditText addMemberEditText;

    private String projectId;
    private String projectName;
    private int position;
    private OnEditProjectCompleteListener listener;
    private List<String> memberIds;

    private static String PROJECT_ID_KEY = "projectId";
    private static String PROJECT_NAME_KEY = "projectName";
    private static String POSITION_KEY = "position";
    private static String MEMBER_KEY = "member";

    public EditProjectFragment() {
        // Required empty public constructor
    }

    public static EditProjectFragment newInstance(String projectId,
                                                  String projectName,
                                                  int position,
                                                  List<String> memberIds) {
        Bundle args = new Bundle();
        args.putString(PROJECT_ID_KEY, projectId);
        args.putString(PROJECT_NAME_KEY, projectName);
        args.putInt(POSITION_KEY, position);
        args.putStringArrayList(MEMBER_KEY, (ArrayList<String>) memberIds);

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
        memberIds = getArguments().getStringArrayList(MEMBER_KEY);
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

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        DatabaseService databaseService = new DatabaseService();
        databaseService.queryMembers(memberIds, this);
    }

    @OnClick(R.id.editProjectButton)
    public void editProject() {
        String editedProject = editProjectNameEditText.getText().toString();
        DatabaseService databaseService = new DatabaseService();

        databaseService.editProject(projectId, editedProject);
        listener.onEditProjectComplete(position, editedProject);
    }

    @OnClick(R.id.addMemberText)
    public void addMember() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        View dialogView = getLayoutInflater().inflate(R.layout.add_member_dialog, null);
        dialogBuilder.setView(dialogView);
        addMemberEditText = dialogView.findViewById(R.id.addMemberEditText);

        dialogBuilder.setTitle("Add new member");
        dialogBuilder.setMessage("Enter new member's email");
        dialogBuilder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                inviteMember(addMemberEditText.getText().toString(), projectId, memberIds);

            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onQueryMembersSuccess(List<String> memberNames) {
        MemberAdapter memberAdapter = new MemberAdapter(memberNames);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(memberAdapter);
    }

    @Override
    public void onQueryMembersFailed() {

    }

    public void setOnEditProjectCompleteListener(OnEditProjectCompleteListener listener) {
        this.listener = listener;
    }

    private void inviteMember(String email, String projectId, List<String> memberIds) {
        DatabaseService databaseService = new DatabaseService();
        databaseService.inviteMember(email, projectId, memberIds);
    }
}
