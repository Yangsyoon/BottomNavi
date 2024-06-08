package com.example.tourlist.Tourist_Detail_Activity.Comment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tourlist.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentViewHolder> {
    private List<Comment> commentList;
    private Context context;
    private Activity activity;

    public CommentsAdapter(List<Comment> commentList, Context context, Activity activity) {
        this.commentList = commentList;
        this.context = context;
        this.activity = activity;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment comment = commentList.get(position);
        holder.userNameTextView.setText(comment.getUserName());
        holder.contentTextView.setText(comment.getContent());

        // 더보기 버튼 클릭 리스너 설정
        holder.commentOptionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context wrapper = new ContextThemeWrapper(context, R.style.PopupMenuStyle);
                PopupMenu popupMenu = new PopupMenu(wrapper, holder.commentOptionsButton);
                popupMenu.inflate(R.menu.comment_options_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int itemId = item.getItemId();
                        if (itemId == R.id.edit_comment) {
                            // 댓글 수정 로직
                            editComment(comment, holder.getAdapterPosition());
                            return true;
                        } else if (itemId == R.id.delete_comment) {
                            // 댓글 삭제 로직
                            deleteComment(comment, holder.getAdapterPosition());
                            return true;
                        } else {
                            return false;
                        }
                    }
                });
                popupMenu.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView userNameTextView;
        TextView contentTextView;
        ImageButton commentOptionsButton;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            userNameTextView = itemView.findViewById(R.id.userNameTextView);
            contentTextView = itemView.findViewById(R.id.contentTextView);
            commentOptionsButton = itemView.findViewById(R.id.commentOptionsButton);
        }
    }

    // 댓글 수정 로직
    private void editComment(Comment comment, int position) {
        if (comment.getPlaceName() == null || comment.getCommentId() == null) {
            Toast.makeText(context, "댓글 정보를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("댓글 수정");

        // 커스텀 레이아웃을 사용하여 다이얼로그 구성
        View viewInflated = LayoutInflater.from(context).inflate(R.layout.dialog_edit_comment, null, false);
        final EditText input = viewInflated.findViewById(R.id.editText);
        input.setText(comment.getContent());

        builder.setView(viewInflated);

        // 다이얼로그 버튼 설정
        builder.setPositiveButton(android.R.string.ok, (dialog, which) -> {
            dialog.dismiss();
            String newContent = input.getText().toString();
            if (!newContent.isEmpty()) {
                comment.setContent(newContent);
                notifyItemChanged(position);

                // Firebase 업데이트
                DatabaseReference commentRef = FirebaseDatabase.getInstance().getReference("comments")
                        .child(comment.getPlaceName()).child(comment.getCommentId());
                commentRef.setValue(comment)
                        .addOnSuccessListener(aVoid -> Toast.makeText(context, "댓글이 수정되었습니다.", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e -> Toast.makeText(context, "댓글 수정에 실패했습니다.", Toast.LENGTH_SHORT).show());
            }
        });

        builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> dialog.cancel());

        builder.show();
    }

    // 댓글 삭제 로직
    private void deleteComment(Comment comment, int position) {
        if (comment.getPlaceName() == null || comment.getCommentId() == null) {
            Toast.makeText(context, "댓글 정보를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("댓글 삭제");
        builder.setMessage("정말로 댓글을 삭제하시겠습니까?");
        builder.setPositiveButton(android.R.string.ok, (dialog, which) -> {
            dialog.dismiss();

            // Firebase 삭제
            DatabaseReference commentRef = FirebaseDatabase.getInstance().getReference("comments")
                    .child(comment.getPlaceName()).child(comment.getCommentId());
            commentRef.removeValue()
                    .addOnSuccessListener(aVoid -> {
                        if (position < commentList.size()) {
                            commentList.remove(position);
//                            notifyItemRemoved(position);
//                            notifyItemRangeChanged(position, commentList.size());
                        }
                        Toast.makeText(context, "댓글이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> Toast.makeText(context, "댓글 삭제에 실패했습니다.", Toast.LENGTH_SHORT).show());
        });

        builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> dialog.cancel());

        builder.show();
    }
}
