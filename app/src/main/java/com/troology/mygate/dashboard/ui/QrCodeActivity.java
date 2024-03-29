package com.troology.mygate.dashboard.ui;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.troology.mygate.R;
import com.troology.mygate.utils.ApplicationConstant;

import java.io.ByteArrayOutputStream;

public class QrCodeActivity extends AppCompatActivity {
    ImageView qr_code_img, back, img;
    String name, image="";
    String passcode;
    Bitmap bitmap;
    TextView tvName, tvCode;
    Button btnshare;
    LinearLayout share;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_code);

        tvName = findViewById(R.id.qrName);
        tvCode = findViewById(R.id.qrpascode);
        back = findViewById(R.id.backqr);
        btnshare = findViewById(R.id.btnshare);
        share = findViewById(R.id.share);
        img = findViewById(R.id.img);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initView();
    }

    private void initView() {
        Intent intent = getIntent();
        name = (String) intent.getExtras().get("name");
        image = (String) intent.getExtras().get("image");
        passcode = intent.getStringExtra("passcode").replace("#","");
        qr_code_img = findViewById(R.id.qr_code_img);
        tvName.setText(name);
        tvCode.setText("#"+passcode);

        Glide.with(this).load(ApplicationConstant.INSTANCE.baseUrl+"/"+image)
                .centerCrop()
                .apply(RequestOptions.circleCropTransform())
                .error(getResources().getDrawable(R.drawable.logo))
                .into(img);

        String text = passcode.replace("#",""); // Whatever you need to encode in the QR code
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE, 200, 200);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            bitmap = barcodeEncoder.createBitmap(bitMatrix);
            qr_code_img.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        btnshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onClickApp(bitmap);
            }
        });
    }


    public void onClickApp(Bitmap bitmap) {
        try {
            String path = MediaStore.Images.Media.insertImage(this.getContentResolver(), bitmap, name+", "+passcode, null);
            Uri imageUri = Uri.parse(path);

            Intent waIntent = new Intent(Intent.ACTION_SEND);
            waIntent.putExtra(Intent.EXTRA_TEXT, name+", Please use this QR code for Entry/Exit.");
            waIntent.setType("image/*");
            waIntent.putExtra(android.content.Intent.EXTRA_STREAM, imageUri);
            startActivity(Intent.createChooser(waIntent, "Share with"));
        } catch (Exception e) {
//            Log.e("Error on sharing", e + " ");
            Toast.makeText(this, getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
        }
    }

}



