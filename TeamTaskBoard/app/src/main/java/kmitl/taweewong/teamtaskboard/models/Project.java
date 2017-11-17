package kmitl.taweewong.teamtaskboard.models;

import java.util.List;

public class Project {
    private String name;
    private List<String> members;
    private List<BacklogItem> backlogItems;

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
}
