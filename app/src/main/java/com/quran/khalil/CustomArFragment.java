package com.quran.khalil;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;

import com.google.ar.sceneform.ux.ArFragment;

public class CustomArFragment extends ArFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FrameLayout frameLayout = (FrameLayout) super.onCreateView(inflater, container, savedInstanceState);

        // Ensure the ArSceneView is available
        if (getArSceneView() != null) {
            //Hide plane discovery by setting plane rendering properties
            getArSceneView().getPlaneRenderer().setEnabled(false);
            getArSceneView().getPlaneRenderer().setVisible(false);
        }

        return frameLayout;
    }
}
