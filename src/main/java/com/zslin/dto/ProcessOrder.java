package com.zslin.dto;

import com.zslin.model.BuffetOrder;

import java.util.List;

/**
 * Created by 钟述林 393156105@qq.com on 2017/4/17 11:09.
 */
public class ProcessOrder {
    private List<BuffetOrder> finishedList;
    private List<BuffetOrder> ingList;

    public ProcessOrder(List<BuffetOrder> finishedList, List<BuffetOrder> ingList) {
        this.finishedList = finishedList;
        this.ingList = ingList;
    }

    public List<BuffetOrder> getFinishedList() {
        return finishedList;
    }

    public void setFinishedList(List<BuffetOrder> finishedList) {
        this.finishedList = finishedList;
    }

    public List<BuffetOrder> getIngList() {
        return ingList;
    }

    public void setIngList(List<BuffetOrder> ingList) {
        this.ingList = ingList;
    }
}
