package com.example.cw_comp1786;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cw_comp1786.adapter.YogaclassAdapter;
import com.example.cw_comp1786.db.databasehepler.YogaclassTableHelper;
import com.example.cw_comp1786.db.entity.Yogaclass;
import com.example.cw_comp1786.db.firebase.YogaFirebase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;

public class Allclass_Screen extends AppCompatActivity {

    private RecyclerView recyclerView;
    private YogaclassAdapter yogaClassAdapter;
    private YogaclassTableHelper yogaclassTableHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_allclass_screen);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        YogaclassTableHelper yogaclassTableHelper = new YogaclassTableHelper(this);
        yogaclassTableHelper.syncFromFirebaseToSQLite();

        List<Yogaclass> yogaClassList = yogaclassTableHelper.getAllClasses();

        yogaClassAdapter = new YogaclassAdapter(yogaClassList);
        recyclerView.setAdapter(yogaClassAdapter);
        CardView add = findViewById(R.id.cardaddnew);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Allclass_Screen.this, Addnewclass_Screen.class);
                startActivity(intent);
            }
        });

        yogaClassAdapter.setOnItemClickListener(new YogaclassAdapter.OnItemClickListener() {
            @Override
            public void onUpdateClick(Yogaclass yogaClass, int position) {
                Intent intent = new Intent(Allclass_Screen.this, Updateclass_Screen.class);
                intent.putExtra("class_id", yogaClass.getClassid());
                startActivity(intent);
            }

            @Override
            public void onDeleteClick(Yogaclass yogaClass, int position) {
                yogaClassList.remove(position);
                yogaclassTableHelper.deleteClass(
                        yogaClass.getClassid(),
                        new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("Yogaclass", "Class was deleted successfully from SQLite and Firebase.");
                                yogaClassAdapter.notifyItemRemoved(position);
                                yogaClassAdapter.notifyItemRangeChanged(position, yogaClassList.size()); // Cập nhật vị trí của các item sau vị trí bị xóa
                            }
                        },
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("Yogaclass", "Failed to delete class: " + e.getMessage());
                                yogaClassList.add(position, yogaClass);
                                yogaClassAdapter.notifyItemInserted(position);
                                yogaClassAdapter.notifyItemRangeChanged(position, yogaClassList.size());
                            }
                        }
                );
            }
        });
        findViewById(R.id.cardaddnew).setOnClickListener(view -> {
            Intent intent = new Intent(Allclass_Screen.this, Addnewclass_Screen.class);
            startActivity(intent);
        });


    }
}