package com.example.tourlist.Main.ViewPager.Board.Comment;

// CommentAdapter.java

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tourlist.R;

import java.util.List;
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private List<Comment> commentList;

    public CommentAdapter(List<Comment> commentList) {
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment_board, parent, false);
        return new CommentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment comment = commentList.get(position);

        holder.userIdTextView.setText(comment.getUserName()); // 닉네임 사용
        holder.commentTextView.setText(comment.getContent());
        holder.timestampTextView.setText(comment.getTimestamp());
    }


    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {

        public TextView userIdTextView;
        public TextView commentTextView;
        public TextView timestampTextView;

        public CommentViewHolder(View itemView) {
            super(itemView);
            userIdTextView = itemView.findViewById(R.id.user_id_text_view);
            commentTextView = itemView.findViewById(R.id.comment_text_view);
            timestampTextView = itemView.findViewById(R.id.timestamp_text_view);
        }
    }
}
