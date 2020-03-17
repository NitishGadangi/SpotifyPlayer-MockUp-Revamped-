package nitish.build.com.musictest2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.PendingIntent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.ui.PlayerNotificationManager;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;

public class MainActivity extends AppCompatActivity implements ExoPlayer.EventListener {
    private Handler mainHandler;
    private RenderersFactory renderersFactory;
    private BandwidthMeter bandwidthMeter;
    private LoadControl loadControl;
    private DataSource.Factory dataSourceFactory;
    private ExtractorsFactory extractorsFactory;
    private MediaSource mediaSource;
    private TrackSelection.Factory trackSelectionFactory;
    private SimpleExoPlayer player;
    private final String streamUrl = "https://aac.saavncdn.com/426/f64ddaeb6ff9142d2aade5fe57f06f55_96.mp4"; //bbc world service url
    private TrackSelector trackSelector;
    PlayerControlView pcv;
    PlayerNotificationManager playerNotificationManager;
    TextView tv_alb_head,tv_lrc_head;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pcv = findViewById(R.id.playerView_exo);
        tv_alb_head = findViewById(R.id.tv_alb_head);
        tv_lrc_head = findViewById(R.id.tv_lrc_head);

        ViewPager viewPager = findViewById(R.id.viewPager_player);
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.i("VPG_T","P1:"+position);
            }

            @Override
            public void onPageSelected(int position) {
                Log.i("VPG_T","P2:"+position);
                if (position==0){
                    tv_alb_head.setTextColor(Color.WHITE);
                    tv_lrc_head.setTextColor(Color.parseColor("#FFBEBEBE"));
                }else if (position==1){
                    tv_lrc_head.setTextColor(Color.WHITE);
                    tv_alb_head.setTextColor(Color.parseColor("#FFBEBEBE"));
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.i("VPG_T","S:"+state);
            }
        });


        renderersFactory = new DefaultRenderersFactory(getApplicationContext());
        bandwidthMeter = new DefaultBandwidthMeter();
        trackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        trackSelector = new DefaultTrackSelector(trackSelectionFactory);
        loadControl = new DefaultLoadControl();

        player = ExoPlayerFactory.newSimpleInstance(getApplicationContext(),renderersFactory, trackSelector, loadControl);
        player.addListener(this);

            dataSourceFactory = new DefaultDataSourceFactory(getApplicationContext(), "ExoplayerDemo");
            extractorsFactory = new DefaultExtractorsFactory();
            mainHandler = new Handler();
            mediaSource = new ExtractorMediaSource(Uri.parse(streamUrl),
                    dataSourceFactory,
                    extractorsFactory,
                    mainHandler,
                    null);
            pcv.setPlayer(player);
            pcv.setShowTimeoutMs(-1);
            player.prepare(mediaSource);
            player.setPlayWhenReady(false);







    }

    public class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position)
            {
                case 0:
                    return new AlbumFragmentPlayer(); //ChildFragment1 at position 0
                case 1:
                    return new LyricsFragmentPlayer(); //ChildFragment2 at position 1

            }
            return null; //does not happen
        }

        @Override
        public int getCount() {
            return 2; //three fragments
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        player.setPlayWhenReady(true);
//        Toast.makeText(MainActivity.this, "Ready", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        player.setPlayWhenReady(false);
        Toast.makeText(MainActivity.this, "Exoplayer is on pause.", Toast.LENGTH_SHORT).show();
    }



    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onSeekProcessed() {

    }

    @Override
    public void onTimelineChanged(Timeline timeline, @Nullable Object manifest, int reason) {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }
}
