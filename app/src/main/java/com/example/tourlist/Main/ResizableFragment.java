package com.example.tourlist.Main;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.tourlist.R;

public class ResizableFragment extends Fragment {

    private static final String TAG = "ResizableFragment";
    private int minHeight; // px 단위의 최소 높이
    private int maxHeight; // 화면 높이에 대한 최대 높이
    private int initialHeight; // px 단위의 초기 높이

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: called");
        View view = inflater.inflate(R.layout.fragment_resizable, container, false);

        final FrameLayout resizableView = view.findViewById(R.id.resizable_view);
        ImageButton dragButton = view.findViewById(R.id.drag_button);

        // 최소 높이를 200dp로 설정
        minHeight = (int) (40 * getResources().getDisplayMetrics().density);
//        initialHeight= (int) (40 * getResources().getDisplayMetrics().density);

        // 레이아웃이 그려진 후에 maxHeight를 설정
        // 레이아웃이 그려진 후에 초기 높이 및 maxHeight를 설정
        view.post(new Runnable() {
            @Override
            public void run() {
                maxHeight = view.getHeight();
                Log.d(TAG, "Max height set to " + maxHeight);

                // 초기 높이 설정
//                if (resizableView.getHeight() < initialHeight) {
//                    ViewGroup.LayoutParams layoutParams = resizableView.getLayoutParams();
//                    layoutParams.height = initialHeight;
//                    resizableView.setLayoutParams(layoutParams);
//                    Log.d(TAG, "Initial height set to " + initialHeight);
//                }
            }
        });

        dragButton.setOnTouchListener(new OnTouchListener() {
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
        });

        return view;
    }
}
