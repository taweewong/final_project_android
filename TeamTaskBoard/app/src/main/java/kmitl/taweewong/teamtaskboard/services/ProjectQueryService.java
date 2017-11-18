package kmitl.taweewong.teamtaskboard.services;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import kmitl.taweewong.teamtaskboard.models.Project;

public class ProjectQueryService {

    public interface OnQueryProjectsCompleteListener {
        void onQueryProjectsSuccess(ArrayList<Project> projects);
        void onQueryProjectsFailed();
    }

    private DatabaseReference databaseReference;

    public ProjectQueryService() {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("projects");
    }

    public void queryProjects(final List<String> projectIds, final OnQueryProjectsCompleteListener listener) {
        final ArrayList<Project> projects = new ArrayList<>();

        databaseReference.addValueEventListener(new ValueEventListener() {
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
}
