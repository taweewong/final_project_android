package kmitl.taweewong.teamtaskboard.controllers.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import kmitl.taweewong.teamtaskboard.R;

public class ShowTasksFragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";

    public ShowTasksFragment() {
        // Required empty public constructor
    }

    public static ShowTasksFragment newInstance(int sectionNumber) {
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);

        ShowTasksFragment fragment = new ShowTasksFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_show_tasks, container, false);
        Toast.makeText(getContext(), "" + getArguments().getInt(ARG_SECTION_NUMBER), Toast.LENGTH_SHORT).show();
        return rootView;
    }

}
