package com.ferry.tulen.presentation.business.bottomsheet;

import static java.security.AccessController.getContext;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ferry.tulen.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Objects;


public class BottomSheetFragment extends BottomSheetDialogFragment {

    public static BottomSheetFragment newInstance() {
        return new BottomSheetFragment();
    }
    private BottomSheetCallback mCallback;
    public void setBottomSheetCallback(BottomSheetCallback callback) {
        mCallback = callback;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        View view = View.inflate(getContext(), R.layout.bottom_sheet_dialog, null);
        dialog.setContentView(view);

        dialog.getBehavior().setPeekHeight(500);
        dialog.getBehavior().setState(BottomSheetBehavior.STATE_EXPANDED);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        view.findViewById(R.id.option1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onOption1Clicked();
                dismiss();
            }
        });

        view.findViewById(R.id.option2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onOption2Clicked();
                dismiss();
            }
        });

        view.findViewById(R.id.btnDissmis).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle option 2 click
                dismiss();
            }
        });

        return dialog;
    }
}