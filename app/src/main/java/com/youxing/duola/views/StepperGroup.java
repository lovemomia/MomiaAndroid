package com.youxing.duola.views;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jun Deng on 15/8/13.
 */
public class StepperGroup implements StepperView.OnNumberChangedListener {

    private List<StepperView> stepperList = new ArrayList<StepperView>();

    private int min;
    private int max;
    private int total;

    private StepperView.OnNumberChangedListener listener;

    public void addStepper(StepperView stepper) {
        stepper.setListener(this);
        stepperList.add(stepper);
    }

    public void clearStepper() {
        stepperList.clear();
    }

    public void setMax(int max) {
        this.max = max;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public List<StepperView> getStepperList() {
        return stepperList;
    }

    public int getNumberAtIndex(int index) {
        if (index >= stepperList.size()) {
            return 0;
        }
        return stepperList.get(index).getNumber();
    }

    public void setListener(StepperView.OnNumberChangedListener listener) {
        this.listener = listener;
    }

    @Override
    public void onNumberChanged(StepperView stepper) {
        updateTotal();
        for (StepperView sv : stepperList) {
            sv.setMax(max - total + sv.getNumber());
        }
        if (listener != null) {
            listener.onNumberChanged(stepper);
        }
    }

    private void updateTotal() {
        total = 0;
        for (StepperView sv : stepperList) {
            total += sv.getNumber();
        }
    }
}
