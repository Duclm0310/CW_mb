    package com.example.cw_comp1786;

    import android.os.Bundle;
    import android.view.View;
    import android.widget.Button;

    import androidx.activity.EdgeToEdge;
    import androidx.activity.OnBackPressedCallback;
    import androidx.appcompat.app.AppCompatActivity;
    import androidx.fragment.app.Fragment;
    import androidx.fragment.app.FragmentManager;
    import androidx.fragment.app.FragmentTransaction;

    public class CalenderManagerActivity extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_calender_manager);

            if (savedInstanceState == null) {
                loadFragment(new ClassInstanceListFragment(), false);
            }

            OnBackPressedCallback callback = new OnBackPressedCallback(true) {
                @Override
                public void handleOnBackPressed() {
                    if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                        getSupportFragmentManager().popBackStack();
                    } else {
                        finish();
                    }
                }
            };

            getOnBackPressedDispatcher().addCallback(this, callback);
        }

        public void loadFragment(Fragment fragment, boolean addToBackStack) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container_classsession, fragment);
            if (addToBackStack) {
                fragmentTransaction.addToBackStack(null);
            }
            fragmentTransaction.commit();
        }

        public void goBackToFragment(Fragment fragment) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.popBackStack();
            loadFragment(fragment, false);
        }
    }