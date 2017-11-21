package kmitl.taweewong.teamtaskboard.services;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import kmitl.taweewong.teamtaskboard.models.Project;

public class DatabaseService {

    public interface OnQueryProjectsCompleteListener {
        void onQueryProjectsSuccess(ArrayList<Project> projects);
        void onQueryProjectsFailed();
    }

    private DatabaseReference databaseReference;

    private static final String CHILD_USERS = "users";
    private static final String CHILD_PROJECTS = "projects";

    public DatabaseService() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public void queryProjects(final List<String> projectIds, final OnQueryProjectsCompleteListener listener) {
        final ArrayList<Project> projects = new ArrayList<>();

        databaseReference.child(CHILD_PROJECTS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (String id : projectIds) {
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
        projects.add(projectId);
        databaseReference.child(CHILD_USERS).child(userId).child(CHILD_PROJECTS).setValue(projects);
    }
}
