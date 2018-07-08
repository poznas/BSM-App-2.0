package com.bsm.android.home;

import android.support.v7.util.DiffUtil;

import com.bsm.android.model.Privilege;

import java.util.List;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PrivilegeDiffUtil extends DiffUtil.Callback{

    private List<Privilege> oldList;
    private List<Privilege> newList;

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).getBrand()
                .equals(newList.get(newItemPosition).getBrand());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
    }
}
