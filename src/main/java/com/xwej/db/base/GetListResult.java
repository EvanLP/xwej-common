package com.xwej.db.base;

import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@SuppressWarnings("serial")
public class GetListResult<T> extends GetResult<List<T>> {

    public GetListResult() {
    }

    public GetListResult(Page<T> page) {
        if (page != null) {
            setTotalpage(page.getTotalPages());
            setHasNext(page.hasNext());
            setTotal(page.getTotalElements());
            setCount(page.getNumberOfElements());
            setCurrPage(page.getNumber() + 1);
            setData(page.getContent());
        }
    }



    /****
     * 数据格式转换的时候使用
     * 里面的data 需要你自己转换
     * @param listResult
     */
    public GetListResult(GetListResult listResult) {
        setCurrPage(listResult.getCurrPage());
        setTotal(listResult.getTotal());
        setTotalpage(listResult.getTotalpage());
        setCount(listResult.getCount());
        setHasNext(listResult.isHasNext());
    }

    /******
     * 计算过结果转换
     * @param source
     * @param function 转换的方法
     * @param <R>   要转换的结果 （返给客户端的结果）
     * @param <M> 要被转换的参数类型 （数据库查询结果）
     * @return
     */
    public static <R, M> GetListResult<R> result2Result(GetListResult<M> source, Function<M, R> function) {
        GetListResult<R> rtn = new GetListResult<R>();
        if (source == null) {
            return rtn;
        }
        rtn = new GetListResult<R>(source);
        List<R> collect = source.getData().stream().map(function).collect(Collectors.toList());
        rtn.setData(collect);
        return rtn;
    }

    /******
     * 计算过结果转换
     * @param source
     * @param function 转换的方法
     * @param <R>   要转换的结果 （返给客户端的结果）
     * @param <M> 要被转换的参数类型 （数据库查询结果）
     * @return
     */
    public static <R, M> GetListResult<R> result2Result(Page<M> source, Function<M, R> function) {
        GetListResult<R> rtn = new GetListResult<R>();
        if (source == null) {
            return rtn;
        }
        rtn.setTotalpage(source.getTotalPages());
        rtn.setHasNext(source.hasNext());
        rtn.setTotal(source.getTotalElements());
        rtn.setCount(source.getNumberOfElements());
        rtn.setCurrPage(source.getNumber() + 1);
        List<R> collect = source.getContent().stream().map(function).collect(Collectors.toList());
        rtn.setData(collect);
        return rtn;
    }


    /******
     * 计算过结果转换
     * @param source
     * @param function 转换的方法
     * @param <R>   要转换的结果 （返给客户端的结果）
     * @param <M> 要被转换的参数类型 （数据库查询结果）
     * @return
     */
    public <R> GetListResult<R> result2Result(Function<T, R> function) {
        GetListResult<R> rtn = new GetListResult<R>(this);
        List<R> collect = getData().stream().map(function).collect(Collectors.toList());
        rtn.setData(collect);
        return rtn;
    }


}