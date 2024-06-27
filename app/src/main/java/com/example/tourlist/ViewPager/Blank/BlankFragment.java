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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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

    private Map<String, Map<String, String[]>> dataMap;

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

        initializeData();

        setButtonClickListeners();

        return view;
    }

    // 초기 데이터 설정 부분 수정
    private void initializeData() {
        dataMap = new HashMap<>();
        dataMap.put("자연", new HashMap<String, String[]>() {{
            put("자연관광지", new String[]{
                    "국립공원", "도립공원", "군립공원", "산", "자연생태관광지",
                    "자연휴양림", "수목원", "폭포", "계곡", "약수터",
                    "해안절경", "해수욕장", "섬", "항구/포구", "등대",
                    "호수", "강", "동굴"
            });
            put("관광자원", new String[]{
                    "희귀동.식물", "기암괴석"
            });
        }});
        dataMap.put("인문(문화/예술/역사)", new HashMap<String, String[]>() {{
            put("역사관광지", new String[]{
                    "고궁", "성", "문", "고택", "생가",
                    "민속마을", "유적지/사적지", "사찰", "종교성지", "안보관광"
            });
            put("휴양관광지", new String[]{
                    "관광단지", "온천/욕장/스파", "이색찜질방", "헬스투어",
                    "테마공원", "공원", "유람선/잠수함관광"
            });
            put("체험관광지", new String[]{
                    "농.산.어촌 체험", "전통체험", "산사체험", "이색체험", "이색거리"
            });
            put("산업관광지", new String[]{
                    "발전소", "식음료", "기타", "전자-반도체", "자동차"
            });
            put("건축/조형물", new String[]{
                    "다리/대교", "기념탑/기념비/전망대", "분수", "동상", "터널", "유명건물"
            });
        }});
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


    //관광타입
    private void showCategoryDialog() {
        final String[] categories = {"관광지 (12)", "문화시설 (14)", "축제/공연/행사 (15)", "여행코스 (25)", "레포츠 (28)", "숙박 (32)", "쇼핑 (38)", "음식 (39)"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("관광타입 선택");
        builder.setItems(categories, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String selectedCategory = categories[which];
                tvSelectedCategory.setText(selectedCategory);
                showCategoryList(selectedCategory);
            }
        });
        builder.show();
    }

    //서비스분류
    // showServiceDialog 메소드 수정
    private void showServiceDialog() {
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_service_selection);

        final ListView lvMainCategory = dialog.findViewById(R.id.lvMainCategory);
        final ListView lvSubCategory = dialog.findViewById(R.id.lvSubCategory);
        final ListView lvDetailCategory = dialog.findViewById(R.id.lvDetailCategory);

        String selectedCategory = tvSelectedCategory.getText().toString().split(" ")[0];
        Map<String, String[]> subcategories = dataMap.get(selectedCategory);

        if (subcategories == null) {
            Log.e("ServiceDialogError", "subcategories is null for category: " + selectedCategory);
            return;
        }

        ArrayAdapter<String> mainCategoryAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, new ArrayList<>(subcategories.keySet()));
        lvMainCategory.setAdapter(mainCategoryAdapter);

        lvMainCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedMainCategory = (String) lvMainCategory.getItemAtPosition(position);
                String[] subCategoryArray = subcategories.get(selectedMainCategory);
                ArrayAdapter<String> subCategoryAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, subCategoryArray);
                lvSubCategory.setAdapter(subCategoryAdapter);
                lvDetailCategory.setAdapter(null); // Clear detail category when main category changes
            }
        });

        lvSubCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedSubCategory = (String) lvSubCategory.getItemAtPosition(position);
                String[] detailCategories = subcategories.get(selectedSubCategory);
                if (detailCategories == null) {
                    Log.e("DetailCategoryError", "detailCategories is null for subCategory: " + selectedSubCategory);
                    return;
                }
                ArrayAdapter<String> detailCategoryAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, detailCategories);
                lvDetailCategory.setAdapter(detailCategoryAdapter);
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

    //지역
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

    private void showCategoryList(String selectedCategory) {
        categoryList.clear();
        String[] parts = selectedCategory.split(" ");
        if (parts.length < 2) {
            return;
        }

        String categoryKey = parts[0];
        Map<String, String[]> subcategories = dataMap.get(categoryKey);
        if (subcategories != null) {
            categoryList.addAll(subcategories.keySet());
        }

        categoryAdapter.notifyDataSetChanged();
//        recyclerViewCategory.setVisibility(View.VISIBLE);
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