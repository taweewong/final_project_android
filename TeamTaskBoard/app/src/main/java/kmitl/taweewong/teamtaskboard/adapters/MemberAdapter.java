package kmitl.taweewong.teamtaskboard.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import kmitl.taweewong.teamtaskboard.R;
import kmitl.taweewong.teamtaskboard.viewholders.MemberViewHolder;

public class MemberAdapter extends RecyclerView.Adapter<MemberViewHolder> {
    private List<String> members;

    public MemberAdapter(List<String> members) {
        this.members = members;
    }

    @Override
    public MemberViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.member_item_view, parent, false);

        return new MemberViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MemberViewHolder holder, int position) {
        TextView memberNameText = holder.getMemberNameText();

        memberNameText.setText(members.get(position));
    }

    @Override
    public int getItemCount() {
        return members.size();
    }
}
