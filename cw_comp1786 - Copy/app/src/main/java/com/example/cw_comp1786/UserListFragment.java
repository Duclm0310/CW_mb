package com.example.cw_comp1786;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.example.cw_comp1786.adapter.UserAdapter;
import com.example.cw_comp1786.db.databasehepler.UserTableHelper;
import com.example.cw_comp1786.db.entity.User;
import java.util.List;

public class UserListFragment extends Fragment {

    private RecyclerView recyclerViewUser;
    private RecyclerView recyclerViewTeacher;
    private UserAdapter userAdapter;
    private UserAdapter teacherAdapter;
    private List<User> userList;
    private List<User> teacherList;

    private ImageView addImage;
    private UserTableHelper userTableHelper;


    public UserListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_list, container, false);

        userTableHelper = new UserTableHelper(getContext());
        userTableHelper.syncFromFirebaseToSQLite();


        // user
        recyclerViewUser = view.findViewById(R.id.recycler_view_user);
        recyclerViewUser.setLayoutManager(new LinearLayoutManager(getContext()));
        userList = userTableHelper.getUsers();
        userAdapter = new UserAdapter(userList);
        recyclerViewUser.setAdapter(userAdapter);

        // teacher
        recyclerViewTeacher = view.findViewById(R.id.recycler_view_teacher);
        recyclerViewTeacher.setLayoutManager(new LinearLayoutManager(getContext()));
        teacherList = userTableHelper.getTeachers();
        teacherAdapter = new UserAdapter(teacherList);
        recyclerViewTeacher.setAdapter(teacherAdapter);


        addImage = view.findViewById(R.id.addImage);
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment userAddFragment = new UserAddFragment();
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, userAddFragment)
                        .addToBackStack(String.valueOf(userAddFragment))
                        .commit();
            }
        });

        teacherAdapter.setOnItemClickListener(new UserAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(User user, int position) {
                UserDetailFragment userDetailFragment = new UserDetailFragment();
                Bundle bundle = new Bundle();
                bundle.putString("user_id", user.getId());
                userDetailFragment.setArguments(bundle);

                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, userDetailFragment)
                        .addToBackStack(String.valueOf(userDetailFragment))
                        .commit();
            }
        });

        userAdapter.setOnItemClickListener(new UserAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(User user, int position) {
                UserDetailFragment userDetailFragment = new UserDetailFragment();
                Bundle bundle = new Bundle();
                bundle.putString("user_id", user.getId());
                userDetailFragment.setArguments(bundle);
                ((UserManagementActivity) requireActivity()).loadFragment(userDetailFragment, true);
            }
        });


        return view;
    }
}