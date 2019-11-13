package com.zc.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.zc.model.Access;

public class AccessRowMapper implements RowMapper<Access> {

	@Override
	public Access mapRow(ResultSet rs, int rowNum) throws SQLException {
		Access access = new Access();
		access.setId(rs.getInt("id"));
		access.setUsername(rs.getString("username"));
		access.setShopName(rs.getString("shop_name"));
		return access;
	}

}
