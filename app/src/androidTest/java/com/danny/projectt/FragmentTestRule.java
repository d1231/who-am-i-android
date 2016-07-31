package com.danny.projectt;

import android.support.test.rule.ActivityTestRule;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import org.junit.Assert;

public class FragmentTestRule<F extends Fragment> extends ActivityTestRule<TestActivity> {


    private final Class<F> mFragmentClass;

    private final FragmentFactory<F> fragmentFactory;

    private F mFragment;

    public FragmentTestRule(final Class<F> fragmentClass) {

        this(fragmentClass, fragmentClass::newInstance);
    }

    public FragmentTestRule(final Class<F> fragmentClass, final FragmentFactory<F> fragmentFactory) {

        super(TestActivity.class, true, false);
        this.fragmentFactory = fragmentFactory;
        this.mFragmentClass = fragmentClass;
    }

    @Override
    protected void afterActivityLaunched() {

        super.afterActivityLaunched();

        System.out.println("HHH");

        getActivity().runOnUiThread(() -> {
            try {
                //Instantiate and insert the fragment into the container layout
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                mFragment = fragmentFactory.createFragment();
                transaction.replace(R.id.content, mFragment);
                transaction.commit();
            } catch (Exception e) {
                Assert.fail(String.format("%s: Could not insert %s into TestActivity: %s",
                        getClass().getSimpleName(),
                        mFragmentClass.getSimpleName(),
                        e.getMessage()));
            }
        });
    }

    public F getFragment() {

        return mFragment;
    }

    public interface FragmentFactory<T extends Fragment> {

        T createFragment() throws Exception;

    }
}
