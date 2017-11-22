package kmitl.taweewong.teamtaskboard.services;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import kmitl.taweewong.teamtaskboard.models.BacklogItem;
import kmitl.taweewong.teamtaskboard.models.Project;

public class DatabaseService {

    public interface OnQueryProjectsCompleteListener {
        void onQueryProjectsSuccess(ArrayList<Project> projects);
        void onQueryProjectsFailed();
    }

    public interface OnQueryBacklogItemsCompleteListener {
        void onQueryBacklogItemsSuccess(ArrayList<BacklogItem> backlogItems);
        void onQueryBacklogItemsFailed();
    }

    private DatabaseReference databaseReference;

    private static final String CHILD_USERS = "users";
    private static final String CHILD_PROJECTS = "projects";
    private static final String CHILD_BACKLOG_ITEMS = "backlogItems";

    public DatabaseService() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public void queryProjects(final List<String> projectIds, final OnQueryProjectsCompleteListener listener) {
        final ArrayList<Project> projects = new ArrayList<>();

        databaseReference.child(CHILD_PROJECTS)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (String id : projectIds) {
                            //TODO: fix database error!
                            projects.add(dataSnapshot.child(id).getValue(Project.class));
                        }
                        listener.onQueryProjectsSuccess(projects);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        listener.onQueryProjectsFailed();
                    }
                });
    }

    public void queryBacklogItems(final String projectId, final OnQueryBacklogItemsCompleteListener listener) {
        databaseReference.child(CHILD_PROJECTS).child(projectId).child(CHILD_BACKLOG_ITEMS)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ArrayList<BacklogItem> backlogItems = new ArrayList<>();

                        for (DataSnapshot backlogItemsSnapshot: dataSnapshot.getChildren()) {
                            backlogItems.add(backlogItemsSnapshot.getValue(BacklogItem.class));
                        }

                        listener.onQueryBacklogItemsSuccess(backlogItems);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        listener.onQueryBacklogItemsFailed();
                    }
                });
    }

    public Project createNewUserProject(String name, String userId, List<String> projects) {
        String projectId = databaseReference.push().getKey();
        Project project = new Project();
        project.setName(name);
        project.setProjectId(projectId);
        project.setMembers(new ArrayList<>(Collections.singletonList(userId)));

        insertProject(project, projectId);
        updateUserProject(projects, userId, projectId);

        return project;
    }

    private void insertProject(Project project, String projectId) {
        databaseReference.child(CHILD_PROJECTS).child(projectId).setValue(project);
    }

    private void updateUserProject(List<String> projects, String userId, String projectId) {
        if (projects == null) {
            projects = new ArrayList<>();
        }

        projects.add(projectId);
        databaseReference.child(CHILD_USERS).child(userId).child(CHILD_PROJECTS).setValue(projects);
    }

    public void addBacklogItem(BacklogItem backlogItem, String projectId) {
        databaseReference.child(CHILD_PROJECTS).child(projectId).child(CHILD_BACKLOG_ITEMS)
                .push().setValue(backlogItem);
    }
}
