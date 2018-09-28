package com.ailide.apartmentsabc.views.material;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.ailide.apartmentsabc.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.ImageViewState;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

public class MaterialPicFragment extends Fragment {

    private static final String URL = "URL";
    private SubsamplingScaleImageView ivPic;

    public static Fragment create(String url) {
        MaterialPicFragment fragment = new MaterialPicFragment();
        Bundle bundle = new Bundle();
        bundle.putString(URL, url);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_material_pic, container, false);
        ivPic = view.findViewById(R.id.iv_pic);
        ivPic.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CUSTOM);
        ivPic.setMinScale(1.0F);
        final Bundle bundle = getArguments();
        if (bundle != null) {
            try {
                ViewTreeObserver vto = view.getViewTreeObserver();
                vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        Glide.with(getActivity())
                                .load(bundle.getString(URL))
                                .asBitmap()
                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                .into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
                                    @Override
                                    public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
                                        try {
                                            int width = bitmap.getWidth();
                                            int height = bitmap.getHeight();
                                            float scaleWidth = ((float) (view.getWidth() - view.getPaddingLeft() - view.getPaddingRight())) / width;
                                            Matrix matrix = new Matrix();
                                            matrix.postScale(scaleWidth, scaleWidth);
                                            bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
                                            ivPic.setImage(ImageSource.bitmap(bitmap), new ImageViewState(0.5F, new PointF(0, 0), 0));
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return view;
    }
}