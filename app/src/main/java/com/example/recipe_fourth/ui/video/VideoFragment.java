package com.example.recipe_fourth.ui.video;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.recipe_fourth.Class.Step;
import com.example.recipe_fourth.Class.VideoPlayerConfig;
import com.example.recipe_fourth.R;
import com.example.recipe_fourth.ui.Activity.RecipeDetails;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultAllocator;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;


import butterknife.BindView;
import butterknife.ButterKnife;


public class VideoFragment extends Fragment  implements Player.EventListener{

    @BindView(R.id.recipe_video) @Nullable()
    SimpleExoPlayerView simpleExoPlayerView;
    @BindView(R.id.step_description_tv) @Nullable TextView stepDescriptionTv;
    @BindView(R.id.move_prev_img)@Nullable() ImageView movePrevImg;
    @BindView(R.id.move_next_img)@Nullable() ImageView moveNextImg;
    @BindView(R.id.spinnerVideoDetails)@Nullable ProgressBar mSpinnerVideoDetails;
    @BindView(R.id.step_wrong_message)@Nullable TextView mStep_wrong_message;
    int position;
    SimpleExoPlayer mSimpleExoPlayer;
    Handler mHandler;
    Runnable mRunnable;
    private static final String TAG = "ExoPlayerActivity";
    private static final String KEY_VIDEO_URI = "video_uri";
    private  String videoUri;
    public VideoFragment() {

    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        boolean tabletSize= getResources().getBoolean(R.bool.is_tablet);
        View root;
        if(!tabletSize&&getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE){
            root = inflater.inflate(R.layout.fragment_video_full, container, false);
        }else {

            root = inflater.inflate(R.layout.fragment_video, container, false);
        }
        ButterKnife.bind(this, root);
        ButterKnife.setDebug(true);
        Bundle bundle = getArguments();
        if (bundle != null) {
            position=bundle.getInt("POSITION");
            Step step=((RecipeDetails)getActivity()).getStep(position);
            stepDescriptionTv.setText(step.getDescription()!=null?step.getDescription():"No More Detail");
            videoUri= step.getVideoURL();
            setUp();
        }
        initilize();
        return root;
    }

    private  void initializePlayer(){
        if(mSimpleExoPlayer==null){

            LoadControl l=new DefaultLoadControl(new DefaultAllocator(true, 16),
                    VideoPlayerConfig.MIN_BUFFER_DURATION,
                    VideoPlayerConfig.MAX_BUFFER_DURATION,
                    VideoPlayerConfig.MIN_PLAYBACK_START_BUFFER,
                    VideoPlayerConfig.MIN_PLAYBACK_RESUME_BUFFER, -1, true);
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();


            TrackSelection.Factory videoTrackSelectionFactory =
                    new AdaptiveTrackSelection.Factory(bandwidthMeter);

            TrackSelector trackSelector=new DefaultTrackSelector(videoTrackSelectionFactory);

            mSimpleExoPlayer=    ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(getContext()), trackSelector, l);
            simpleExoPlayerView.setPlayer(mSimpleExoPlayer);


        }
    }   private void releazePlayer(){
        if(mSimpleExoPlayer!=null) {
            mSimpleExoPlayer.stop();
            mSimpleExoPlayer.release();
            mSimpleExoPlayer = null;
        }
    }
    private void setUp() {
        initializePlayer();
        if (videoUri == null||videoUri.isEmpty()) {
            mSpinnerVideoDetails.setVisibility(View.INVISIBLE);
            mStep_wrong_message.setText("Sorry the video is un avalabile for this step!");
            return;
        }

        mStep_wrong_message.setText("");
        buildMediaSource(Uri.parse(videoUri));
    }
    private void buildMediaSource(Uri mUri) {
        // Measures bandwidth during playback. Can be null if not required.
        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        MediaSource mediaSource=new ExtractorMediaSource(mUri,new DefaultDataSourceFactory(getContext(),getString(R.string.app_name)),
                new DefaultExtractorsFactory(),null,null);

        mSimpleExoPlayer.prepare(mediaSource);
        mSimpleExoPlayer.setPlayWhenReady(true);
        mSimpleExoPlayer.addListener(this);
    }
    private void releasePlayer() {
        if (mSimpleExoPlayer != null) {
            mSimpleExoPlayer.release();
            mSimpleExoPlayer = null;
        }
    }

    private void pausePlayer() {
        if (mSimpleExoPlayer != null) {
            mSimpleExoPlayer.setPlayWhenReady(false);
            mSimpleExoPlayer.getPlaybackState();
        }
    }

    private void resumePlayer() {
        if (mSimpleExoPlayer != null) {
            mSimpleExoPlayer.setPlayWhenReady(true);
            mSimpleExoPlayer.getPlaybackState();
        }
    }

    private  void initilize(){
        movePrevImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Step step=((RecipeDetails)getActivity()).getStepPrev(position);
                if(step!=null){
                    position--;
                    stepDescriptionTv.setText(step.getDescription()!=null?step.getDescription():"No More Detail");
                    videoUri= step.getVideoURL();
                    setUp();
                }else{
                    Toast.makeText(getContext(), "No Prevouis!", Toast.LENGTH_SHORT).show();

                }
            }
        });
        moveNextImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Step step=((RecipeDetails)getActivity()).getStepNext(position);
                if(step!=null){
                    releazePlayer();
                    stepDescriptionTv.setText(step.getDescription()!=null?step.getDescription():"No More Detail");
                    videoUri= step.getVideoURL();
                    setUp();
                    position++;
                }else{
                    Toast.makeText(getContext(), "No Next!!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        switch (playbackState) {

            case Player.STATE_BUFFERING:
                mSpinnerVideoDetails.setVisibility(View.VISIBLE);
                break;
            case Player.STATE_ENDED:
                // Activate the force enable
                break;
            case Player.STATE_IDLE:

                break;
            case Player.STATE_READY:
                mSpinnerVideoDetails.setVisibility(View.GONE);

                break;
            default:
                break;
        }
    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity(int reason) {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSeekProcessed() {

    }



    @Override
    public void onPause() {
        super.onPause();
        pausePlayer();
        if (mRunnable != null) {
            mHandler.removeCallbacks(mRunnable);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }
}