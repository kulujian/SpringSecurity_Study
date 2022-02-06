package com.example.demo.repository;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;


// 這是沒有資料庫代替使用
@Repository
public class UserDao {

	public Map<String, Map<String, String>> users;
	{
		// A01, 1234, admin , normal, ROLE_manager
		Map<String, String> info1 = new LinkedHashMap<>();
		info1.put("password", "$2a$10$O/ycxI26BWNXDIBJprHJ9uHof5c.utNtQlPDwxw0eYb.lpg3GiRHq");
		info1.put("authority", "admin,normal,ROLE_manager");
		
		// A02, $2a$10$fxZv45YzIq07FEHNYX3RKO8WvnIfs4q6P94P6kKBchn1lFMEgfg62
		Map<String, String> info2 = new LinkedHashMap<>();
		info2.put("password", "$2a$10$4kOrHqe5ieKpx8l1ljpbiepRGBb1ofcxYe89rgtLIQMV.stTNMzXq");
		info2.put("authority", "normal,ROLE_employee");
		
		users = new LinkedHashMap<>();
		users.put("A01", info1);
		users.put("A02", info2);
		
		System.out.println(users);
	}
}
