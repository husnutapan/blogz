package com.common;

import java.sql.SQLException;

public interface Commons<T1,T2> {
	
	public T1 kaydet(T2 t2) throws SQLException;

}
