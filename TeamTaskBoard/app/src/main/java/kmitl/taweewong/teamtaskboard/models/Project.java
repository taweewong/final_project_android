package kmitl.taweewong.teamtaskboard.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Project implements Parcelable {
    private String projectId;
    private String name;
    private List<String> members;
    private List<BacklogItem> backlogItems;

    public static final String PROJECT_CLASS_KEY = "project";

    public Project() {

    }

    private Project(Parcel in) {
        projectId = in.readString();
        name = in.readString();
        members = in.createStringArrayList();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(projectId);
        dest.writeString(name);
        dest.writeStringList(members);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Project> CREATOR = new Creator<Project>() {
        @Override
        public Project createFromParcel(Parcel in) {
            return new Project(in);
        }

        @Override
        public Project[] newArray(int size) {
            return new Project[size];
        }
    };

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }

    public List<BacklogItem> getBacklogItems() {
        return backlogItems;
    }

    public void setBacklogItems(List<BacklogItem> backlogItems) {
        this.backlogItems = backlogItems;
    }
}
