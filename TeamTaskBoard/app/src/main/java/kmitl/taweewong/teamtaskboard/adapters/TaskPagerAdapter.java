package kmitl.taweewong.teamtaskboard.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import kmitl.taweewong.teamtaskboard.controllers.fragments.ShowTasksFragment;

import static kmitl.taweewong.teamtaskboard.models.Tasks.TaskType.doingTasks;
import static kmitl.taweewong.teamtaskboard.models.Tasks.TaskType.doneTasks;
import static kmitl.taweewong.teamtaskboard.models.Tasks.TaskType.todoTasks;

public class TaskPagerAdapter extends FragmentPagerAdapter{

    private String projectId;
    private String itemId;

    public TaskPagerAdapter(FragmentManager fm, String projectId, String itemId) {
        super(fm);
        this.projectId = projectId;
        this.itemId = itemId;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return ShowTasksFragment.newInstance(projectId, itemId, todoTasks);
            case 1:
                return ShowTasksFragment.newInstance(projectId, itemId, doingTasks);
            default:
                return ShowTasksFragment.newInstance(projectId, itemId, doneTasks);
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "TODO";
            case 1:
                return "DOING";
            default:
                return "DONE";
        }
    }
}
