package com.indev.chaol;

import android.app.job.JobParameters;
import android.app.job.JobService;

/**
 * Created by jvier on 05/07/2017.
 */

public class MyJobService extends JobService {

    private static final String TAG = MyJobService.class.getName();
    @Override
    public boolean onStartJob(JobParameters params) {
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
