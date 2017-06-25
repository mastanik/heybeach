package com.daimler.heybeach.data.core;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface RowMapper<T> {
    List<T> map(ResultSet rs) throws Exception;
}
