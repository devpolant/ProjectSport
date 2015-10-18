package com.polant.projectsport.fragment.step;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.glomadrian.dashedcircularprogress.DashedCircularProgress;
import com.polant.projectsport.R;

/**
 * Created by Антон on 18.10.2015.
 */
public class StepCounterFragment extends Fragment {

    private static final int LAYOUT = R.layout.fragment_step_counter;

    private View view;
    DashedCircularProgress circularProgress;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(LAYOUT, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initProgressView();
    }

    private void initProgressView(){
        circularProgress = (DashedCircularProgress) view.findViewById(R.id.progressSteps);

        //Устанавливаает значение, а не проценты.
        circularProgress.setValue(650);
    }
}
