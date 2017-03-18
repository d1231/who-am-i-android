package com.whomi;

import android.support.test.rule.ActivityTestRule;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import org.junit.Assert;

import static android.R.attr.fragment;

public class FragmentTestRule<F extends Fragment> extends ActivityTestRule<TestActivity> {


    private final Class<F> mFragmentClass;

    private final FragmentFactory<F> fragmentFactory;

    private F mFragment;


    public FragmentTestRule(final Class<F> fragmentClass, final FragmentFactory<F> fragmentFactory) {

        super(TestActivity.class, true, false);
        this.fragmentFactory = fragmentFactory;
        this.mFragmentClass = fragmentClass;
    }

    @Override
    protected void afterActivityLaunched() {

        super.afterActivityLaunched();

        TestActivity activity = getActivity();
        activity.runOnUiThread(() -> {
            try {

                FragmentManager manager = activity.getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                mFragment = createFragment();
                transaction.replace(R.id.content, mFragment, fragmentFactory.getTag());
                transaction.commit();

            } catch (Exception e) {

                Assert.fail(String.format("%s: Could not insert %s into TestActivity: %s",
                        getClass().getSimpleName(),
                        mFragmentClass.getSimpleName(),
                        e.getMessage()));

            }
        });
    }


    private F createFragment() {
        try {
            return fragmentFactory.createFragment();
        } catch (Exception e) {
            throw new AssertionError(String.format("%s: Could not insert fragment into %s: %s",
                    getClass().getSimpleName(),
                    getActivity().getClass().getSimpleName(),
                    e.getMessage()));
        }
    }

    public F getFragment() {
        return mFragment;
    }

    public void clear() {

        TestActivity activity = getActivity();

        activity.runOnUiThread(() -> {

            while (activity.getSupportFragmentManager().getBackStackEntryCount() > 0) {
                activity.getSupportFragmentManager().popBackStack();
            }
        });

    }

    public interface FragmentFactory<T extends Fragment> {

        T createFragment() throws Exception;

        String getTag();

    }
}
