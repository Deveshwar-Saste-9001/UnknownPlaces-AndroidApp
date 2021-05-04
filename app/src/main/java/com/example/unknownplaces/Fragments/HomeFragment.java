package com.example.unknownplaces.Fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unknownplaces.Adapters.CategoryAdapter;
import com.example.unknownplaces.DatabaseCodes;
import com.example.unknownplaces.R;

import static com.example.unknownplaces.DatabaseCodes.categoryModelList;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }

    private RecyclerView categoryRecyclerview;
    private CategoryAdapter categoryAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        categoryRecyclerview = view.findViewById(R.id.categoryRecyclerView);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        categoryRecyclerview.setLayoutManager(linearLayoutManager);


        if (categoryModelList.size() == 0) {
            DatabaseCodes.loadCategories(categoryRecyclerview, getContext());
        } else {
            categoryAdapter = new CategoryAdapter(categoryModelList);
            categoryAdapter.notifyDataSetChanged();
        }
        categoryRecyclerview.setAdapter(categoryAdapter);

        return view;


    }

}
