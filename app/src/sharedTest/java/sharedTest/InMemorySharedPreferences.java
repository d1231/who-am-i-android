package sharedTest;

import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class InMemorySharedPreferences implements SharedPreferences {

    private Map<String, Object> prefMap = new HashMap<>();

    @Override
    public Map<String, ?> getAll() {
        return prefMap;
    }

    private Object getOrDefault(String key, Object defValue) {

        Object val = prefMap.get(key);
        if (val == null) {
            return defValue;
        } else {
            return val;
        }

    }

    @Nullable
    @Override
    public String getString(String key, String defValue) {

        return (String) getOrDefault(key, defValue);
    }

    @Nullable
    @Override
    public Set<String> getStringSet(String key, Set<String> defValues) {
        return (Set<String>) getOrDefault(key, defValues);
    }

    @Override
    public int getInt(String key, int defValue) {
        return (int) getOrDefault(key, defValue);

    }

    @Override
    public long getLong(String key, long defValue) {
        return (long) getOrDefault(key, defValue);
    }

    @Override
    public float getFloat(String key, float defValue) {
        return (float) getOrDefault(key, defValue);
    }

    @Override
    public boolean getBoolean(String key, boolean defValue) {
        return (boolean) getOrDefault(key, defValue);
    }

    @Override
    public boolean contains(String key) {
        return prefMap.containsKey(key);
    }

    @Override
    public Editor edit() {
        return new MockedEditor(this);
    }

    @Override
    public void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {

    }

    @Override
    public void unregisterOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {

    }

    private static class MockedEditor implements Editor {

        private Map<String, Object> tempMap = new HashMap<>();

        private Set<String> keysToRemove = new HashSet<>();

        private boolean clearPrefs = false;

        private InMemorySharedPreferences sharedPreferences;

        public MockedEditor(InMemorySharedPreferences sharedPreferences) {

            this.sharedPreferences = sharedPreferences;
        }

        private Editor putValue(String key, Object val) {

            tempMap.put(key, val);

            return this;
        }

        @Override
        public Editor putString(String key, String value) {

            return putValue(key, value);
        }

        @Override
        public Editor putStringSet(String key, Set<String> values) {

            return putValue(key, values);
        }

        @Override
        public Editor putInt(String key, int value) {

            return putValue(key, value);
        }

        @Override
        public Editor putLong(String key, long value) {

            return putValue(key, value);
        }

        @Override
        public Editor putFloat(String key, float value) {

            return putValue(key, value);
        }

        @Override
        public Editor putBoolean(String key, boolean value) {

            return putValue(key, value);
        }

        @Override
        public Editor remove(String key) {

            keysToRemove.add(key);

            return this;
        }

        @Override
        public Editor clear() {
            clearPrefs = true;
            return this;
        }

        @Override
        public boolean commit() {

            saveChanges();


            return true;
        }

        private void saveChanges() {

            if (clearPrefs) {
                sharedPreferences.prefMap.clear();
            } else {
                for (String key : keysToRemove) {
                    sharedPreferences.prefMap.remove(key);
                }
            }

            sharedPreferences.prefMap.putAll(tempMap);

        }

        @Override
        public void apply() {

            saveChanges();

        }
    }

}
