package com.zslin.web.dto;

/**
 * Created by zsl on 2018/10/15.
 */
public class DetailDto {

    /** 半票人数 */
    private Integer halfCount = 0;

    /** 全票人数 */
    private Integer fullCount = 0;

    /** 总人数 */
    private Integer totalCount = 0;

    public DetailDto() {
    }

    public DetailDto(Integer halfCount, Integer fullCount, Integer totalCount) {
        this.halfCount = halfCount;
        this.fullCount = fullCount;
        this.totalCount = totalCount;
    }

    public Integer getHalfCount() {
        return halfCount;
    }

    public void setHalfCount(Integer halfCount) {
        this.halfCount = halfCount;
    }

    public Integer getFullCount() {
        return fullCount;
    }

    public void setFullCount(Integer fullCount) {
        this.fullCount = fullCount;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }
}
