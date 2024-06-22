package com.example.tourlist.Main;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.tourlist.R;
import android.view.ViewGroup;

public class ResizableFragment2 extends Fragment {

    private static final String TAG = "ResizableFragment2";
    private int minHeight;
    private int maxHeight;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: called");
        View view = inflater.inflate(R.layout.resizable_fragment2, container, false);

        final FrameLayout resizableView = view.findViewById(R.id.resizable_view);
        ImageButton dragButton = view.findViewById(R.id.drag_button);
        Button registerButton = view.findViewById(R.id.register_button);
        EditText editText = view.findViewById(R.id.editText);
        ImageView imageView = view.findViewById(R.id.imageView);

        minHeight = (int) (200 * getResources().getDisplayMetrics().density); // 최소 높이를 200dp로 설정
        maxHeight = (int) (680 * getResources().getDisplayMetrics().density); // 최대 높이를 680dp로 설정

        dragButton.setOnTouchListener(createSimpleDragTouchListener(resizableView));

        return view;
    }

    // 단순한 드래그 기능을 위한 별도 함수
    private View.OnTouchListener createSimpleDragTouchListener(final FrameLayout resizableView) {
        return new View.OnTouchListener() {
            float initialY;
            float lastY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialY = event.getRawY();
                        lastY = initialY;
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        float currentY = event.getRawY();
                        float deltaY = lastY - currentY;
                        int newHeight = (int) (resizableView.getHeight() + deltaY);

                        // 최소 높이와 최대 높이 사이로 제한
                        if (newHeight < minHeight) {
                            newHeight = minHeight;
                        } else if (newHeight > maxHeight) {
                            newHeight = maxHeight;
                        }

                        ViewGroup.LayoutParams layoutParams = resizableView.getLayoutParams();
                        layoutParams.height = newHeight;
                        resizableView.setLayoutParams(layoutParams);

                        lastY = currentY;
                        return true;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        return true;
                }
                return false;
            }
        };
    }

    // 드래그 기능을 위한 별도 함수
    private View.OnTouchListener createDragTouchListener(final FrameLayout resizableView) {
        return new View.OnTouchListener() {
            float initialY;
            float lastY;
            boolean isDragging = false;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialY = event.getRawY();
                        lastY = initialY;
                        isDragging = true;
                        Log.d(TAG, "ACTION_DOWN: initialY = " + initialY);
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        if (isDragging) {
                            float currentY = event.getRawY();
                            float deltaY = lastY - currentY;
                            int newHeight = (int) (resizableView.getHeight() + deltaY);

                            // 최소 높이와 최대 높이 사이로 제한
                            if (newHeight < minHeight) {
                                newHeight = minHeight;
                            } else if (newHeight > maxHeight) {
                                newHeight = maxHeight;
                            }

                            ViewGroup.LayoutParams layoutParams = resizableView.getLayoutParams();
                            layoutParams.height = newHeight;
                            resizableView.setLayoutParams(layoutParams);

                            lastY = currentY;
                            Log.d(TAG, "ACTION_MOVE: newHeight = " + newHeight);
                        }
                        return true;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        isDragging = false;
                        Log.d(TAG, "ACTION_UP or ACTION_CANCEL");
                        return true;
                }
                return false;
            }
        };
    }
}