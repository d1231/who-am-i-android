package com.whomi.services;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.whomi.R;
import com.whomi.adapters.TeamHistoryAdapter;
import com.whomi.model.objects.Player;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.inject.Inject;

public class ShareService {

    private Context context;

    @Inject
    public ShareService(Context context) {
        this.context = context;

    }

    public void sharePlayer(Player player) throws IOException {

        Bitmap screenShot = createScreenShot(player);

        Intent shareIntent = new Intent(Intent.ACTION_SEND);

        shareIntent.setType("image/*");

        File newFile = saveBitmapToFile(screenShot);

        Uri contentUri = FileProvider.getUriForFile(context, "com.whomi.fileprovider", newFile);

        shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);

        context.startActivity(Intent.createChooser(shareIntent, "Challenge your friends"));

    }

    @NonNull
    private File saveBitmapToFile(Bitmap screenShot) throws IOException {
        File cachePath = new File(context.getCacheDir(), "images");
        cachePath.mkdirs();
        FileOutputStream stream = new FileOutputStream(cachePath + "/shared_image.png"); // overwrites this image every time
        screenShot.compress(Bitmap.CompressFormat.PNG, 100, stream);

        stream.close();
        return new File(cachePath, "shared_image.png");
    }

    private Bitmap createScreenShot(Player player) {

        LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = vi.inflate(R.layout.screen_share, null);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.share_teamhistory);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        TeamHistoryAdapter teamHistoryAdapter = new TeamHistoryAdapter(context, player.teamHistory());
        recyclerView.setAdapter(teamHistoryAdapter);

        final Bitmap bmp = Bitmap.createBitmap(1200, 1500, Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bmp);

        view.setDrawingCacheEnabled(true);
        view.measure(View.MeasureSpec.makeMeasureSpec(canvas.getWidth(), View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(canvas.getHeight(), View.MeasureSpec.EXACTLY));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        canvas.drawBitmap(view.getDrawingCache(), 0, 0, new Paint());

        view.destroyDrawingCache();

        return bmp;

    }
}
