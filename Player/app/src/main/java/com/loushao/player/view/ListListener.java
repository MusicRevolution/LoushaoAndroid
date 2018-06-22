package com.loushao.player.view;

import com.loushao.player.bean.ResourceList;

import java.util.List;

public interface ListListener {
    void onSuccess(List<ResourceList> list);
    void onFailure();
}
