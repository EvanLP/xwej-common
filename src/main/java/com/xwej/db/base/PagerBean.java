package com.xwej.db.base;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.List;
import java.util.Map;

/**
 * @param <T>
 * @author Evan update 2017-06-16
 */
public abstract class PagerBean<T> {

	public GetListResult<T> getListData(int pageNumber, int pageSize) {
		return getListData(pageNumber, pageSize);
	}

	public GetListResult<T> getListData(int pageNumber, int pageSize, String sortType,
			Map<String, Object> searchParams) {
		Specification<T> spec = buildSpecification(searchParams);
		return getListData(pageNumber, pageSize, sortType);

	}

	public GetListResult<T> getListDataByParams(int pageNumber, int pageSize, String sortType,
			Map<String, Object> searchParams) {
		Specification<T> spec = buildSpecification(searchParams);
		return getListData(pageNumber, pageSize, sortType,spec);

	}

	public GetListResult<T> getListData(int pageNumber, int pageSize, String sortType, Map<String, Object> searchParams,
			String status) {
		Specification<T> spec = buildSpecificationNew(searchParams, status);
		return getListData(pageNumber, pageSize, sortType);

	}

	public GetListResult<T> getListData(int pageNumber, int pageSize, String sortType, Specification<T> spec) {
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
		Page<T> pg = getRepository().findAll(spec, pageRequest);

		GetListResult<T> rtn = new GetListResult<T>();

		rtn.setTotalpage(pg.getTotalPages());
		rtn.setHasNext(pg.hasNext());
		rtn.setCount((int) pg.getNumberOfElements());// 当前页条数
		rtn.setTotal((int) pg.getTotalElements());// 总条数
		int currentPage = (int) Math.min(pageNumber, pg.getTotalPages());
		rtn.setCurrPage(currentPage);
		rtn.setData(pg.getContent());
		return rtn;
	}

	public GetListResult<T> getListData(int pageNumber, int pageSize, String sortType) {
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);

		Page<T> pg = getRepository().findAll(pageRequest);

		GetListResult<T> rtn = new GetListResult<T>();

		rtn.setTotalpage(pg.getTotalPages());
		rtn.setHasNext(pg.hasNext());
		rtn.setCount((int) pg.getNumberOfElements());// 当前页条数
		rtn.setTotal((int) pg.getTotalElements());// 总条数
		int currentPage = (int) Math.min(pageNumber, pg.getTotalPages());
		rtn.setCurrPage(currentPage);
		rtn.setData(pg.getContent());
		return rtn;

	}

	public GetListResult<T> getListData(int pageNumber, int pageSize, Direction order, String sortType,
			Specification<T> spec) {
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, order, sortType);

		Page<T> pg = getRepository().findAll(spec, pageRequest);

		GetListResult<T> rtn = new GetListResult<T>();

		rtn.setTotalpage(pg.getTotalPages());
		rtn.setHasNext(pg.hasNext());
		rtn.setCount((int) pg.getNumberOfElements());// 当前页条数
		rtn.setTotal((int) pg.getTotalElements());// 总条数
		int currentPage = (int) Math.min(pageNumber, pg.getTotalPages());
		rtn.setCurrPage(currentPage);
		rtn.setData(pg.getContent());
		return rtn;

	}

	public List<T> getListData(Map<String, Object> searchParams) {
		Specification<T> spec = buildSpecification(searchParams);
		return getListData(spec);

	}

	public List<T> getListData(Specification<T> spec) {
		return getRepository().findAll(spec);

	}

	/**
	 * 创建动态查询条件组合.
	 */
	public Specification<T> buildSpecificationNew(Map<String, Object> searchParams, String status) {
		if (searchParams == null) {
			return null;
		}
		return new JLSpecificationNew(searchParams, status);
	}

	private class JLSpecificationNew implements Specification<T> {
		private Map<String, Object> searchParams;
		private String status;

		public JLSpecificationNew(Map<String, Object> searchParams, String status) {
			this.searchParams = searchParams;
			this.status = status;
		}

		public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

			Predicate[] predicates = new Predicate[searchParams.size()];
			int i = 0;
			for (String key : searchParams.keySet()) {
				Path<String> path = root.get(key);
				predicates[i] = cb.equal(path, searchParams.get(key));
				i++;
			}
			Predicate p1 = cb.equal(root.get(status), 1);
			Predicate p2 = cb.equal(root.get(status), 2);

			return cb.and(cb.and(predicates), cb.or(p1, p2));
		}
	}

	/**
	 * 创建动态查询条件组合.searchParams 为 “与”
	 */
	public Specification<T> buildSpecification(Map<String, Object> searchParams) {
		if (searchParams == null) {
			return null;
		}
		return new JLSpecification(searchParams);
	}

	private class JLSpecification implements Specification<T> {
		private Map<String, Object> searchParams;

		public JLSpecification(Map<String, Object> searchParams) {
			this.searchParams = searchParams;
		}

		public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

			Predicate[] predicates = new Predicate[searchParams.size()];
			int i = 0;
			for (String key : searchParams.keySet()) {
				Path<String> path = root.get(key);
				predicates[i] = cb.equal(path, searchParams.get(key));
				i++;
			}

			return cb.and(predicates);
		}
	}

	public abstract BasicRepository<T> getRepository();

	/**
	 * 创建分页请求.
	 */
	protected PageRequest buildPageRequest(int pageNumber, int pageSize, Direction order, String sortType) {
		if (pageNumber < 1) {
			pageNumber = 1;
		}
		if (pageSize < 1) {
			pageSize = 50;
		}
		Sort sort = new Sort(order, sortType);
		return new PageRequest(pageNumber - 1, pageSize, sort);
	}

	/**
	 * 创建分页请求.
	 */
	protected PageRequest buildPageRequest(int pageNumber, int pageSize, String sortType) {
		if (pageNumber < 1) {
			pageNumber = 1;
		}
		if (pageSize < 1) {
			pageSize = 50;
		}

		Sort sort = null;
		if ("desc".equals(sortType)) {
			sort = new Sort(Direction.DESC, "id");
		} else if ("name".equals(sortType)) {
			sort = new Sort(Direction.ASC, "name");
		}
		return new PageRequest(pageNumber - 1, pageSize, sort);
	}
}
