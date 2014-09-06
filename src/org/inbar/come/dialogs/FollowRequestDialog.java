package org.inbar.come.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import org.inbar.come.R;

/**
 * Created by inbar on 26/08/14.
 */
public class FollowRequestDialog extends DialogFragment {

    public interface FriendRequestDialogListener {
        public void onFollowRequestDialogPositiveClick(String email);
        public void onFollowRequestDialogNegativeClick();
    }

    public static final String TAG = "FollowRequestDialog";

    public static FollowRequestDialog newInstance() {

        FollowRequestDialog friendRequestDialog = new FollowRequestDialog();
        return friendRequestDialog;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        final LayoutInflater layoutInflater = getActivity().getLayoutInflater();


        final View dialog = layoutInflater.inflate(R.layout.dialog_friend_request, null);
        dialogBuilder.setView(dialog)
                     .setTitle(R.string.friend_request_dialog_title)
                     .setPositiveButton(R.string.send_friend_request_button_hint, new DialogInterface.OnClickListener() {
                         @Override
                         public void onClick(DialogInterface dialogInterface, int i) {

                             final FriendRequestDialogListener dialogListener = (FriendRequestDialogListener) getActivity();
                             final EditText emailField = (EditText) dialog.findViewById(R.id.friendRequestField);
                             final String email = emailField.getText().toString();
                             dialogListener.onFollowRequestDialogPositiveClick(email);
                         }
                     })
                     .setNegativeButton(R.string.cancle_button, new DialogInterface.OnClickListener() {
                         @Override
                         public void onClick(DialogInterface dialogInterface, int i) {

                         }
                     });

        return dialogBuilder.create();

    }

}
