package com.kopin.solos.customisableridemetrics;

import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes24.dex */
public class MultiLabelManager {
    private Map<Integer, LabelManager> labelManagerMap = new HashMap();

    public LabelManager get(int index) {
        if (index < 0) {
            return null;
        }
        if (!this.labelManagerMap.containsKey(Integer.valueOf(index))) {
            LabelManager labelManager = new LabelManager();
            this.labelManagerMap.put(Integer.valueOf(index), labelManager);
        }
        return this.labelManagerMap.get(Integer.valueOf(index));
    }

    public void setText(String text, int... indexes) {
        for (int index : indexes) {
            if (index >= 0) {
                LabelManager labelManager = this.labelManagerMap.get(Integer.valueOf(index));
                if (labelManager != null) {
                    labelManager.setText(text);
                }
            } else {
                return;
            }
        }
    }

    public void setText(int textRes, int... indexes) {
        for (int index : indexes) {
            if (index >= 0) {
                LabelManager labelManager = this.labelManagerMap.get(Integer.valueOf(index));
                if (labelManager != null) {
                    labelManager.setText(textRes);
                }
            } else {
                return;
            }
        }
    }

    public void setCompoundDrawablesWithIntrinsicBounds(int index, int left, int top, int right, int bottom) {
        LabelManager labelManager;
        if (index >= 0 && index < this.labelManagerMap.size() && (labelManager = this.labelManagerMap.get(Integer.valueOf(index))) != null) {
            labelManager.setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom);
        }
    }

    public int size() {
        return this.labelManagerMap.size();
    }
}
