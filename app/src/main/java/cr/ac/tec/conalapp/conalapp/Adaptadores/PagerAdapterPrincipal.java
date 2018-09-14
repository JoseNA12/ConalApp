package cr.ac.tec.conalapp.conalapp.Adaptadores;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import cr.ac.tec.conalapp.conalapp.PantallaPrincipal.BoletinFragment;
import cr.ac.tec.conalapp.conalapp.PantallaPrincipal.ReunionFragment;

/**
 * A simple {@link Fragment} subclass.J
 */
public class PagerAdapterPrincipal extends FragmentStatePagerAdapter {

    private int mNumOfTabs;

    public PagerAdapterPrincipal(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new BoletinFragment();
            case 1:
                return new ReunionFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

}
