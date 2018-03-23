package com.hesen.base;


import java.util.List;
import java.util.Map;

public interface BaseMapper<T> {

    public int addList(List list);

    public int add(T t);

    public int update(T t);

    public int updateByTo(T t);

    public int delete(Object id);

    public int deleteObj(T t);

    public int queryCount(BaseBean model);

    public List<T> queryListByPager(BaseBean model);

    public int queryCountMap(Map map);

    public List<T> queryListByPagerMap(Map map);

    public T queryById(Object id);

    public T queryByUUId(Object uuid);

    public List<T> queryListAll(BaseBean model);

    public List<T> queryListAllMap(Map map);
}
