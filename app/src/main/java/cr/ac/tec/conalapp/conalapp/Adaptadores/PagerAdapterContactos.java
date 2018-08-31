package cr.ac.tec.conalapp.conalapp.Adaptadores;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import cr.ac.tec.conalapp.conalapp.PantallaContactos.ContactoPoliciaFragment;
import cr.ac.tec.conalapp.conalapp.PantallaContactos.ContactoSaludFragment;

public class PagerAdapterContactos extends FragmentStatePagerAdapter {

    private int mNumOfTabs;

    public PagerAdapterContactos(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new ContactoPoliciaFragment();
            case 1:
                return new ContactoSaludFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
