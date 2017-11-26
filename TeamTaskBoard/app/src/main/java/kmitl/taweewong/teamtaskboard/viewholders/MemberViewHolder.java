package kmitl.taweewong.teamtaskboard.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import kmitl.taweewong.teamtaskboard.R;

public class MemberViewHolder extends RecyclerView.ViewHolder {
    private TextView memberNameText;

    public MemberViewHolder(View itemView) {
        super(itemView);
        memberNameText = itemView.findViewById(R.id.memberNameText);
    }

    public TextView getMemberNameText() {
        return memberNameText;
    }

    public void setMemberNameText(TextView memberNameText) {
        this.memberNameText = memberNameText;
    }
}
