package com.zxly.market.utils;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class GjsonUtil {
	   public static Gson gson;
	   
	   private GjsonUtil(){
		
	   }
	   
	   public  Gson getInstance(){
		   if(gson==null){
			   gson = new Gson();
		   }
		   return gson;
	   }
	   
	   public static <T> T json2Object(String json,Class<T> a){
		   if(gson==null){
			   gson = new Gson();
		   }
		   try {
			   
			   return gson.fromJson(json, a);
			   
			} catch (JsonSyntaxException e) {
				
			}
		    return null;
	   }
	   public static Object json2Object(String json,Type type){
		   if(gson==null){
			   gson = new Gson();
		   }
		   try {
			   
			   return gson.fromJson(json, type);
			   
			} catch (JsonSyntaxException e) {
				
			}
		    return null;
	   }
	   
	   public static String Object2Json(Object obj){
		   if(gson==null){
			   gson = new Gson();
		   }
		   return gson.toJson(obj);
	   }
	   
	   public static String Object2Json(Object obj,Type type){
		   if(gson==null){
			   gson = new Gson();
		   }
		   return gson.toJson(obj, type);
	   }
}
