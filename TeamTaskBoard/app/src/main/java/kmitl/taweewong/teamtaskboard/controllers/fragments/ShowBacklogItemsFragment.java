package kmitl.taweewong.teamtaskboard.controllers.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import kmitl.taweewong.teamtaskboard.R;
import kmitl.taweewong.teamtaskboard.adapters.BacklogItemAdapter;
import kmitl.taweewong.teamtaskboard.models.BacklogItem;

import static kmitl.taweewong.teamtaskboard.models.BacklogItem.BACKLOG_ITEM_CLASS_KEY;

public class ShowBacklogItemsFragment extends Fragment {
    List<BacklogItem> backlogItems;

    public ShowBacklogItemsFragment() {
        // Required empty public constructor
    }

    public static ShowBacklogItemsFragment newInstance(ArrayList<BacklogItem> backlogItems) {
        Bundle args = new Bundle();
        args.putParcelableArrayList(BACKLOG_ITEM_CLASS_KEY, backlogItems);

        ShowBacklogItemsFragment fragment = new ShowBacklogItemsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        backlogItems = getArguments().getParcelableArrayList(BACKLOG_ITEM_CLASS_KEY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_show_backlog_items, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        BacklogItemAdapter backlogItemAdapter = new BacklogItemAdapter(backlogItems, getContext());
        RecyclerView recyclerView = view.findViewById(R.id.showBacklogItemsRecyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(backlogItemAdapter);
    }
}
