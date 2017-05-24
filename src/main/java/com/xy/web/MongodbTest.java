package com.xy.web;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.conversions.Bson;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongodbTest {

	public static void main(String[] args) throws UnknownHostException, MongoException {
		/*List<ServerAddress> seeds = new ArrayList<ServerAddress>();
		ServerAddress add1 = new ServerAddress("10.0.150.212", 9804);
		ServerAddress add2 = new ServerAddress("10.0.150.205", 9803);
		ServerAddress add3 = new ServerAddress("10.0.190.49", 9805);
		ServerAddress add4 = new ServerAddress("10.0.202.121", 9805);
		seeds.add(add1);
		seeds.add(add2);
		seeds.add(add3);
		seeds.add(add4);
		MongoClient client = new MongoClient(seeds);
		MongoDatabase db = client.getDatabase("blogdb");
		MongoCollection<Document> collection = db.getCollection("ArticleInfo");
		System.out.println(collection.find().first().get("_id"));
		System.out.println(collection.count());*/
		String s = "px_class, px_class_chapter, px_class_pack, px_class_plan, px_class_recommend, px_class_section, px_comment, px_comment_like, px_comment_rate, px_comment_reply, px_open_class, px_open_class_appointment, px_open_for_kj, px_pack_tag_group, px_tag_group, px_teacher, px_video_history, px_visit_info";
		String sql = "SELECT c.ORDINAL_POSITION 序号,c.COLUMN_NAME 字段名称,c.COLUMN_TYPE 字段类型,c.IS_NULLABLE 是否可空,c.COLUMN_COMMENT 字段描述,c.EXTRA 其他 FROM INFORMATION_SCHEMA.COLUMNS c WHERE table_name = '%s'  ORDER BY  c.ORDINAL_POSITION asc;";
		s = s.replaceAll(" ", "");
		StringBuilder sqlxxx = new StringBuilder();
		String[] ts = s.split(",");
		for (int i=0; i<ts.length; i++) {
			if (sqlxxx.length() > 0) {
				sqlxxx.append("\r\nunion all\r\n");
			}
			sqlxxx.append(String.format(sql, ts[i]));
		}
		System.out.println(sqlxxx);
	}
}