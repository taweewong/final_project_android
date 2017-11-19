package kmitl.taweewong.teamtaskboard.services;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import kmitl.taweewong.teamtaskboard.models.Project;

public class InsertDatabaseService {
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    public InsertDatabaseService() {
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
    }

    public void InsertProject(Project project) {
        String projectId = databaseReference.push().getKey();
        databaseReference.child("projects").child(projectId).setValue(project);
    }
}
