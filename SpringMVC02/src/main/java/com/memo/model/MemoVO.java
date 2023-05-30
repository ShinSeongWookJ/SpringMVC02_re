package com.memo.model;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString(includeFieldNames = true)
@NoArgsConstructor
@AllArgsConstructor
public class MemoVO { 
	
	private int idx;
	private String name;
	private String msg;
	private Date wdate;

}
