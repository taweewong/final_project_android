package kmitl.taweewong.teamtaskboard.controllers.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import kmitl.taweewong.teamtaskboard.R;
import kmitl.taweewong.teamtaskboard.adapters.BacklogItemAdapter;
import kmitl.taweewong.teamtaskboard.adapters.BacklogItemAdapter.OnClickBacklogItemListener;
import kmitl.taweewong.teamtaskboard.models.BacklogItem;
import kmitl.taweewong.teamtaskboard.services.DatabaseService;

import static kmitl.taweewong.teamtaskboard.models.BacklogItem.BACKLOG_ITEM_CLASS_KEY;

public class ShowBacklogItemsFragment extends Fragment implements DatabaseService.OnQueryBacklogItemsCompleteListener {
    private List<BacklogItem> backlogItems;
    private String projectId;
    private BacklogItemAdapter backlogItemAdapter;

    private static String PROJECT_ID_KEY = "projectId";

    public ShowBacklogItemsFragment() {
        // Required empty public constructor
    }

    public static ShowBacklogItemsFragment newInstance(ArrayList<BacklogItem> backlogItems, String projectId) {
        Bundle args = new Bundle();
        args.putParcelableArrayList(BACKLOG_ITEM_CLASS_KEY, backlogItems);
        args.putString(PROJECT_ID_KEY, projectId);

        ShowBacklogItemsFragment fragment = new ShowBacklogItemsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        backlogItems = getArguments().getParcelableArrayList(BACKLOG_ITEM_CLASS_KEY);
        projectId = getArguments().getString(PROJECT_ID_KEY);

        DatabaseService databaseService = new DatabaseService();
        databaseService.queryBacklogItems(projectId, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_show_backlog_items, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ItemTouchHelper.Callback callback = createItemTouchHelperCallback(backlogItems);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);

        backlogItemAdapter = new BacklogItemAdapter(backlogItems,
                (OnClickBacklogItemListener) getContext(),
                itemTouchHelper);

        RecyclerView recyclerView = view.findViewById(R.id.showBacklogItemsRecyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(backlogItemAdapter);


        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private ItemTouchHelper.Callback createItemTouchHelperCallback(final List<BacklogItem> backlogItems) {
       return new ItemTouchHelper.Callback() {
           int sourcePosition;
           int targetPosition;
           DatabaseService databaseService = new DatabaseService();

           @Override
           public boolean isLongPressDragEnabled() {
               return false;
           }

           @Override
           public boolean isItemViewSwipeEnabled() {
                return true;
            }

           @Override
           public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
               int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
               int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
               return makeMovementFlags(dragFlags, swipeFlags);
           }

           @Override
           public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
               sourcePosition = viewHolder.getAdapterPosition();
               targetPosition = target.getAdapterPosition();
               Collections.swap(backlogItems, viewHolder.getAdapterPosition(), target.getAdapterPosition());
               recyclerView.getAdapter().notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
               return true;
           }

           @Override
           public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
               super.clearView(recyclerView, viewHolder);

               if (isDropItem()) {
                   databaseService.updateBacklogItems(backlogItems, projectId);
                   Toast.makeText(getContext(), "moved", Toast.LENGTH_SHORT).show();
               }
           }

           @Override
           public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
               Toast.makeText(getContext(), "on swipe", Toast.LENGTH_SHORT).show();
           }

           private boolean isDropItem() {
               return sourcePosition != targetPosition;
           }
        };
    }

    @Override
    public void onQueryBacklogItemsSuccess(ArrayList<BacklogItem> backlogItems) {
        this.backlogItems.clear();
        this.backlogItems.addAll(backlogItems);
        backlogItemAdapter.notifyDataSetChanged();
    }

    @Override
    public void onQueryBacklogItemsFailed() {

    }
}
