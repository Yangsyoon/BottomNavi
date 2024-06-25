package com.example.tourlist.ViewPager.Board;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tourlist.R;

import java.util.List;


public class BoardAdapter extends RecyclerView.Adapter<BoardAdapter.BoardViewHolder> {

    private List<BoardItem> boardItemList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(String postTitle);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public BoardAdapter(List<BoardItem> boardItemList) {
        this.boardItemList = boardItemList;
    }

    @NonNull
    @Override
    public BoardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_board, parent, false);
        return new BoardViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BoardViewHolder holder, int position) {
        BoardItem boardItem = boardItemList.get(position);
        holder.titleTextView.setText(boardItem.getTitle());
        Log.d("BoardAdapter", "Image URL: " + boardItem.getTitle());
        if (boardItem.getImageUrl() != null && !boardItem.getImageUrl().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(boardItem.getImageUrl())
                    .into(holder.boardImageView);
        } else {
            holder.boardImageView.setImageResource(R.drawable.ic_camera); // 기본 이미지 설정
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(boardItem.getTitle());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return boardItemList.size();
    }

    public static class BoardViewHolder extends RecyclerView.ViewHolder {

        public TextView titleTextView;
        public ImageView boardImageView;

        public BoardViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.title_text_view);
            boardImageView = itemView.findViewById(R.id.board_image);
        }
    }
}
