
package com.xwej.db.base;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author Evan 20 17-06-16
 */
@NoRepositoryBean
public interface BasicRepository<T> extends
        PagingAndSortingRepository<T, Long>, JpaSpecificationExecutor<T>, JpaRepository<T, Long> {


    default GetListResult<T> findAll(int page, int limit) {
        Page<T> pageBean = findAll(getPageRequest(page, limit));
        GetListResult getListResult = new GetListResult(pageBean);
        return getListResult;
    }


    default GetListResult<T> findAll(Specification<T> specifications, int page, int limit) {
        Page<T> pageBean = findAll(specifications, getPageRequest(page, limit));
        GetListResult getListResult = new GetListResult(pageBean);
        return getListResult;
    }

    /****
     * 防止页码传入错误，防止别人攻击传入过大的limit
     * 上面的分頁方法不滿足你的時候，機的用這個 生成pageRequest
     * @param page
     * @param limit
     * @return
     */
    default PageRequest getPageRequest(int page, int limit) {
        if (page < 1) {
            page = 1;
        }
        if (limit < 1) {
            limit = 20;
        }
        if (limit > 100) {
            limit = 100;
        }
        return new PageRequest(page - 1, limit);
    }


    default PageRequest getPageRequest(int page, int limit, Sort sort) {
        if (page < 1) {
            page = 1;
        }
        if (limit < 1) {
            limit = 20;
        }
        if (limit > 100) {
            limit = 100;
        }
        return new PageRequest(page - 1, limit,sort);
    }
}
  