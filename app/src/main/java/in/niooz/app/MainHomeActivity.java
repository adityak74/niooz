package in.niooz.app;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.transition.Scene;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;

/**
 * Created by aditya on 3/29/15.
 */
public class MainHomeActivity extends ActionBarActivity {
    private static final int NUM_PAGES = 4;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    private ActionBar actionBar;
    private PagerSlidingTabStrip tabs;
    private final int[] ICONS = { R.drawable.ic_blank_logo, R.drawable.ic_category_red,
            R.drawable.ic_search_white , R.drawable.ic_male_white };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        actionBar = getSupportActionBar();
        actionBar.hide();

        setContentView(R.layout.activity_main_slide);

        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        tabs = (PagerSlidingTabStrip) findViewById(R.id.mainTabs);
        tabs.setViewPager(mPager);

    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter implements PagerSlidingTabStrip.IconTabProvider {

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public int getPageIconResId(int position) {
            return ICONS[position];
        }


        @Override
        public Fragment getItem(int position) {
            
            if(position==3){
                return SearchFragment.newInstance(position);
            }
            if(position==2){
                return SearchFragment.newInstance(position);
            }
            if(position==1){
                return CategoryFragment.newInstance(position);
            }
            else{
                return HomeActivity.create(position);
            }
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
}
