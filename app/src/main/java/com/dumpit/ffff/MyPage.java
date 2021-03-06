package com.dumpit.ffff;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;


import java.io.ByteArrayOutputStream;

public class MyPage extends Fragment {
    ViewGroup viewGroup;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    FirebaseUser user;

    TextView editInfo;
    TextView logout;
    TextView name;
    TextView point;
    TextView pointtxt;
    TextView storetxt;
    private AdView mAdview; //애드뷰 변수 선언
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewGroup = (ViewGroup) inflater.inflate(R.layout.mypage, container, false);
        MobileAds.initialize(this.getContext(), new OnInitializationCompleteListener() { //광고 초기화
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdview = viewGroup.findViewById(R.id.adView); //배너광고 레이아웃 가져오기
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdview.loadAd(adRequest);
        AdView adView = new AdView(this.getContext());
        adView.setAdSize(AdSize.BANNER); //광고 사이즈는 배너 사이즈로 설정
        adView.setAdUnitId("\n" + "ca-app-pub-5154428061719123/7769030105");

        // Firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        name = (TextView)viewGroup.findViewById(R.id.name);
        point = (TextView)viewGroup.findViewById(R.id.point);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String email = user.getEmail();
                int index = email.indexOf("@");
                String id = email.substring(0, index);
                String web = email.substring(index+1);
                int webidx = web.indexOf(".");
                String website = web.substring(0, webidx);
                String getname = snapshot.child("users").child(id+"_"+website).child("nickname").getValue(String.class);
                name.setText(getname);
                Integer getpoint = snapshot.child("users").child(id+"_"+website).child("Totalpoint").getValue(Integer.class);
                point.setText(String.valueOf(getpoint));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        editInfo = (TextView)viewGroup.findViewById(R.id.editInfo);
        editInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), UserInfo.class);
                startActivity(intent);
            }
        });
        logout = (TextView)viewGroup.findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getContext(), Login.class);
                startActivity(intent);
            }
        });

        pointtxt = (TextView)viewGroup.findViewById(R.id.pointButton);
        pointtxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), PointList.class);
                startActivity(intent);
            }
        });

        storetxt = (TextView)viewGroup.findViewById(R.id.storeButton);
        storetxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), BuyList.class);
                startActivity(intent);
            }
        });


        LinearLayout layout01 = (LinearLayout) viewGroup.findViewById(R.id.ask);
        layout01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(getContext());

                CharSequence contactArray[] = new CharSequence[]{"이메일 문의하기", "카톡 문의하기"};
                dlg.setItems(contactArray, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        switch(which){
                            case 0:
                                Intent email = new Intent(Intent.ACTION_SEND);
                                email.setType("plain/text");
                                String[] address = {"dumpit2021@gmail.com"};
                                email.putExtra(Intent.EXTRA_EMAIL, address);
                                email.putExtra(Intent.EXTRA_SUBJECT, "[덤프잇][문의하기] ");
                                email.putExtra(Intent.EXTRA_TEXT, "[문의 내용]\n");
                                startActivity(email);
                                break;
                            case 1:
                                String url = "http://pf.kakao.com/_xjgdLs";
                                Intent dial = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                startActivity(dial);
                                break;
                        }
                        dialog.dismiss();
                    }
                });
                dlg.show();


            }
        });
        LinearLayout layout02 = (LinearLayout) viewGroup.findViewById(R.id.notice);
        layout02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent t = new Intent(getContext(), Notice.class);
                startActivity(t);

            }
        });
        LinearLayout layout03 = (LinearLayout) viewGroup.findViewById(R.id.personal);
        layout03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent t = new Intent(Intent.ACTION_VIEW,Uri.parse("https://dumpit2021.blogspot.com/2021/06/blog-post.html"));
                startActivity(t);

            }
        });
        LinearLayout layout04 = (LinearLayout) viewGroup.findViewById(R.id.service);
        layout04.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent t = new Intent(Intent.ACTION_VIEW,Uri.parse("https://dumpit2021.blogspot.com/2021/06/blog-post_42.html"));
                startActivity(t);

            }
        });
        LinearLayout layout05 = (LinearLayout) viewGroup.findViewById(R.id.version);
        layout05.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent t = new Intent(getContext(), Version.class);
                startActivity(t);

            }
        });
        return viewGroup;
    }

}
