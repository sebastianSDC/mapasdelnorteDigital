package com.example.pilasnotebook.mapasdelnortedigital.view.activity;


/**
 * Creada por Sebastián Suárez Da Costa y Pablo Herrera.
 */


import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.pilasnotebook.mapasdelnortedigital.R;
import com.example.pilasnotebook.mapasdelnortedigital.view.fragment.CarteleraClienteFragment;
import com.example.pilasnotebook.mapasdelnortedigital.view.fragment.CatalogoOnLineClienteFragment;
import com.example.pilasnotebook.mapasdelnortedigital.view.fragment.CuponeraClienteFragment;
import com.example.pilasnotebook.mapasdelnortedigital.view.fragment.DatosClienteFragment;
import com.example.pilasnotebook.mapasdelnortedigital.view.fragment.FotosClienteFragment;
import com.example.pilasnotebook.mapasdelnortedigital.view.fragment.MapaClienteFragment;

public class ViewPagerAdminActivity extends AppCompatActivity implements CarteleraClienteFragment.OnFragmentInteractionListener, CuponeraClienteFragment.OnFragmentInteractionListener, MapaClienteFragment.OnFragmentInteractionListener, FotosClienteFragment.OnFragmentInteractionListener, DatosClienteFragment.OnFragmentInteractionListener, CatalogoOnLineClienteFragment.OnFragmentInteractionListener {

    private PlaceholderFragment.SectionsPagerAdapter mSectionsPagerAdapter;
    //private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager_admin);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
         mSectionsPagerAdapter = new PlaceholderFragment.SectionsPagerAdapter(getSupportFragmentManager());
        //mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_pager_admin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    /**
     * A placeholder fragment containing a simple view.
     */

    public static class PlaceholderFragment extends Fragment {

        /*
         *The fragment argument representing the section number for this
         *fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /*
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static Fragment newInstance(int sectionNumber) {
            Fragment fragment = null;

            switch (sectionNumber) {

                case 1:
                    fragment = new DatosClienteFragment();
                    break;
                case 2:
                    fragment = new MapaClienteFragment();
                    break;
                case 3:
                    fragment = new CuponeraClienteFragment();
                    break;
                case 4:
                    fragment = new CarteleraClienteFragment();
                    break;
                case 5:
                    fragment = new FotosClienteFragment();
                    break;
                case 6:
                    fragment = new CatalogoOnLineClienteFragment();
                    break;
            }
            return fragment;
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                                 @Nullable Bundle savedInstanceState) {
            super.onCreateView(inflater, container, savedInstanceState);
            View view = inflater.inflate(R.layout.activity_view_pager_admin, container, false);

            return view;
        }


        public static class SectionsPagerAdapter extends FragmentPagerAdapter {
            public SectionsPagerAdapter(FragmentManager fm) {
                super(fm);
            }

            @Override
            public Fragment getItem(int position) {
                return PlaceholderFragment.newInstance(position + 1);

                /*Fragment fragment = null;

                switch (position) {

                    case 1:
                        return new DatosClienteFragment();
                    case 2:
                        return new MapaClienteFragment();
                    case 3:
                        return new CuponeraClienteFragment();
                    case 4:
                        return new CarteleraClienteFragment();
                    case 5:
                        return new FotosClienteFragment();
                    case 6:
                        return new CatalogoOnLineClienteFragment();
                }
                return fragment;*/
            }


            @Override
            public int getCount() {
                return 6;
            }


            @Override
            public CharSequence getPageTitle(int position) {
                switch (position) {
                    case 0:
                        return "Datos";
                    case 1:
                        return "Mapa";
                    case 2:
                        return "Cuponera";
                    case 3:
                        return "Cartelera";
                    case 4:
                        return "Fotos";
                    case 5:
                        return "Catalogo On-Line";
                }
                return null;
            }
        }
    }
}
