package com.company.testshiro4.support.dbhandler;

import com.alibaba.fastjson.JSON;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StringListHandler implements TypeHandler<List<String>> {

	@Override
	public void setParameter(PreparedStatement ps, int i, List<String> parameter, JdbcType jdbcType) throws SQLException {
		if (parameter != null && parameter.size() > 0) {
			String json = JSON.toJSONString(parameter);
			ps.setString(i, json);
		} else {
			ps.setNull(i, Types.VARCHAR);
		}
	}

	@Override
	public List<String> getResult(ResultSet rs, String columnName) throws SQLException {
		String columnValue = rs.getString(columnName);
		if (columnValue != null) {
			return JSON.parseArray(columnValue, String.class);
		}else{
			return new ArrayList<>();
		}
	}

	@Override
	public List<String> getResult(ResultSet rs, int columnIndex) throws SQLException {
		String columnValue = rs.getString(columnIndex);
		if (columnValue != null) {
			return JSON.parseArray(columnValue, String.class);
		}else{
			return new ArrayList<>();
		}
	}

	@Override
	public List<String> getResult(CallableStatement cs, int columnIndex) throws SQLException {
		String columnValue = cs.getString(columnIndex);
		if (columnValue != null) {
			return JSON.parseArray(columnValue, String.class);
		}else{
			return new ArrayList<>();
		}
	}

}
