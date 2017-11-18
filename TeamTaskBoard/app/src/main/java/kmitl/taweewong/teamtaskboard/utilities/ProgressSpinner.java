package kmitl.taweewong.teamtaskboard.utilities;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import com.victor.loading.rotate.RotateLoading;

import kmitl.taweewong.teamtaskboard.R;

public class ProgressSpinner {
    private Dialog progressDialog;
    private RotateLoading rotateLoading;

    public ProgressSpinner(Context context) {
        View progressSpinner = LayoutInflater.from(context).inflate(R.layout.progress_spinner, null);
        rotateLoading = progressSpinner.findViewById(R.id.rotateLoading);

        progressDialog = new Dialog(context);
        progressDialog.setCancelable(false);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.setContentView(progressSpinner);
    }

    public void show() {
        progressDialog.show();
        rotateLoading.start();
    }

    public void dismiss() {
        progressDialog.dismiss();
        rotateLoading.stop();
    }
}
