package com.example.multidatasourcedemo.vo.sys;


import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * 封装分页结果集
 * @author gql
 */
@ApiModel(value = "分页结果")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> {

	@ApiModelProperty(value = "当前页")
	private int pageNum;
	@ApiModelProperty(value = "每页显示多少条")
	private int pageSize;
	@ApiModelProperty(value = "总页数")
	private int totalPage ;
	@ApiModelProperty(value = "总记录数")
	private long total;
	@ApiModelProperty(value = "结果集")
	private List<T> list;

	public static PageResult toPageResult(List list){
		PageInfo pageInfo = new PageInfo<>(list);
		return new PageResult<>(pageInfo.getPageNum(),pageInfo.getPageSize(),pageInfo.getPages(),pageInfo.getTotal(),list);
	}

}
