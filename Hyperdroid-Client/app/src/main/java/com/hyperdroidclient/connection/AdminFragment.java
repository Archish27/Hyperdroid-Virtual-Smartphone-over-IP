package com.hyperdroidclient.connection;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hyperdroidclient.R;
import com.hyperdroidclient.adapters.AdminAdapter;
import com.hyperdroidclient.common.BaseFragment;
import com.hyperdroidclient.data.local.remote.User;
import com.hyperdroidclient.widgets.BaseRadioButton;

import java.util.ArrayList;

/**
 * Created by Archish on 10/16/2017.
 */

public class AdminFragment extends BaseFragment implements AdminAdapter.OnHolderClickListener {

    RecyclerView rvHome;
    SwipeRefreshLayout srlHome;
    DatabaseReference databaseReference;
    ArrayList<User> arrayList;
    ProgressBar pgProgress;
    AdminAdapter adminAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_admin, container, false);
        initViews(rootView);
        fetchData();
        return rootView;
    }

    private void initViews(View view) {
        rvHome = (RecyclerView) view.findViewById(R.id.rvHome);
        srlHome = (SwipeRefreshLayout) view.findViewById(R.id.srlHome);
        pgProgress = (ProgressBar) view.findViewById(R.id.pgProgress);
        rvHome.setHasFixedSize(true);
        rvHome.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        databaseReference = FirebaseDatabase.getInstance().getReference();
        srlHome.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchData();

            }
        });
    }

    private void fetchData() {
        arrayList = new ArrayList<>();
        databaseReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    arrayList.add(new User(dataSnapshot1.getKey(), dataSnapshot1.child("Machine").getValue(String.class)
                            , dataSnapshot1.child("Name").getValue(String.class), dataSnapshot1.child("vmid").getValue(String.class), dataSnapshot1.child("TimeStamp").getValue(String.class)));

                }
                adminAdapter = new AdminAdapter(arrayList,AdminFragment.this);
                rvHome.setAdapter(adminAdapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        if(srlHome.isRefreshing())
            srlHome.setRefreshing(false);
        pgProgress.setVisibility(View.GONE);
    }


    @Override
    public void onHolderClicked(final User data) {
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        final View view = layoutInflater.inflate(R.layout.dialog_admin,null);
        final RadioGroup rgLevels = (RadioGroup) view.findViewById(R.id.rgLevels);
        final BaseRadioButton[] rbLevel = new BaseRadioButton[1];
        BaseRadioButton rbPrivileged = (BaseRadioButton) view.findViewById(R.id.rbPrivileged);
        BaseRadioButton rbNonPrivileged = (BaseRadioButton) view.findViewById(R.id.rbNonPrivileged);
        if(data.getMachine().equals(rbPrivileged.getText().toString())){
            rbPrivileged.setChecked(true);
        }else{
            rbNonPrivileged.setChecked(true);
        }
        rgLevels.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                int selectedId = rgLevels.getCheckedRadioButtonId();
                rbLevel[0] = (BaseRadioButton) view.findViewById(selectedId);
            }
        });

        new AlertDialog.Builder(getActivity())
                .setTitle("Select User levels")
                .setView(view)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        databaseReference.child("Users").child(data.getKey()).child("Machine").setValue(rbLevel[0].getText().toString());
                        fetchData();
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override   
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();
    }
}