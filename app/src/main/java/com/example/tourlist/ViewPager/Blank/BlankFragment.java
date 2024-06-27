package com.example.tourlist.ViewPager.Blank;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tourlist.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BlankFragment extends Fragment {

    private TextView tvSelectedCategory;
    private TextView tvSelectedService;
    private TextView tvSelectedArea;
    private Button btnSelectCategory;
    private Button btnSelectService;
    private Button btnSelectArea;

    private RecyclerView recyclerViewCategory;
    private CategoryAdapter categoryAdapter;
    private List<String> categoryList;

    private FirebaseDatabase database;
    private DatabaseReference categoryRef;

    private String selectedMainCategory;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blank, container, false);

        tvSelectedCategory = view.findViewById(R.id.tvSelectedCategory);
        tvSelectedService = view.findViewById(R.id.tvSelectedService);
        tvSelectedArea = view.findViewById(R.id.tvSelectedArea);
        btnSelectCategory = view.findViewById(R.id.btnSelectCategory);
        btnSelectService = view.findViewById(R.id.btnSelectService);
        btnSelectArea = view.findViewById(R.id.btnSelectArea);
        recyclerViewCategory = view.findViewById(R.id.recyclerViewCategory);

        categoryList = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(categoryList);

        recyclerViewCategory.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewCategory.setAdapter(categoryAdapter);

        database = FirebaseDatabase.getInstance();
        setButtonClickListeners();

        return view;
    }

    private void setButtonClickListeners() {
        btnSelectCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCategoryDialog();
            }
        });

        btnSelectService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showServiceDialog();
            }
        });

        btnSelectArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAreaDialog();
            }
        });
    }

    private void showCategoryDialog() {
        final String[] categories = {"관광지 (12)", "문화시설 (14)", "축제/공연/행사 (15)", "여행코스 (25)", "레포츠 (28)", "숙박 (32)", "쇼핑 (38)", "음식 (39)"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("관광타입 선택");
        builder.setItems(categories, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String selectedCategory = categories[which];
                tvSelectedCategory.setText(selectedCategory);
            }
        });
        builder.show();
    }

    private void showServiceDialog() {
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_service_selection);

        final ListView lvMainCategory = dialog.findViewById(R.id.lvMainCategory);
        final ListView lvSubCategory = dialog.findViewById(R.id.lvSubCategory);
        final ListView lvDetailCategory = dialog.findViewById(R.id.lvDetailCategory);

        String selectedCategory = tvSelectedCategory.getText().toString().split(" ")[1]; // 코드만 가져오기
        categoryRef = database.getReference("Tourist_Categories").child(selectedCategory);

        categoryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> mainCategories = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    mainCategories.add(dataSnapshot.getKey());
                }

                ArrayAdapter<String> mainCategoryAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, mainCategories);
                lvMainCategory.setAdapter(mainCategoryAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Error fetching data", error.toException());
            }
        });

        lvMainCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedMainCategory = (String) lvMainCategory.getItemAtPosition(position);
                categoryRef.child(selectedMainCategory).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<String> subCategories = new ArrayList<>();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            subCategories.add(dataSnapshot.getKey());
                        }

                        ArrayAdapter<String> subCategoryAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, subCategories);
                        lvSubCategory.setAdapter(subCategoryAdapter);
                        lvDetailCategory.setAdapter(null); // Clear detail category when main category changes
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("FirebaseError", "Error fetching data", error.toException());
                    }
                });
            }
        });

        lvSubCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedSubCategory = (String) lvSubCategory.getItemAtPosition(position);
                categoryRef.child(selectedMainCategory).child(selectedSubCategory).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<String> detailCategories = new ArrayList<>();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            detailCategories.add(dataSnapshot.getKey());
                        }

                        ArrayAdapter<String> detailCategoryAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, detailCategories);
                        lvDetailCategory.setAdapter(detailCategoryAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("FirebaseError", "Error fetching data", error.toException());
                    }
                });
            }
        });

        lvDetailCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedDetailCategory = (String) lvDetailCategory.getItemAtPosition(position);
                tvSelectedService.setText(selectedDetailCategory);
                dialog.dismiss(); // 선택 후 다이얼로그 닫기
            }
        });

        Button btnConfirm = dialog.findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void showAreaDialog() {
        final String[] areas = {"서울", "부산", "대구", "인천"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("지역 선택");
        builder.setItems(areas, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String selectedArea = areas[which];
                tvSelectedArea.setText(selectedArea);
            }
        });
        builder.show();
    }

    private class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
        private List<String> categoryList;

        public CategoryAdapter(List<String> categoryList) {
            this.categoryList = categoryList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            String category = categoryList.get(position);
            holder.textView.setText(category);
        }

        @Override
        public int getItemCount() {
            return categoryList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView textView;

            ViewHolder(View itemView) {
                super(itemView);
                textView = itemView.findViewById(android.R.id.text1);
            }
        }
    }
}
