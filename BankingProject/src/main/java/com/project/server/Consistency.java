package com.project.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class Consistency {
	
	private static final String FILE_NAME = "./consistence.txt";
	private static Map<String, Integer[]> lastModifiedDb = new HashMap<>();
	
	public static void genConsistency() {
		  BufferedReader br = null;
	      try {
	         br = new BufferedReader(new FileReader(FILE_NAME));
	         String line;
	         while ((line = br.readLine()) != null) {
	            String[] part = line.split("\\$");
	            String key = part[0];
	            Integer[] value = {Integer.parseInt(part[1]),Integer.parseInt(part[2])};
	            lastModifiedDb.put(key, value); 
	         }
	      } catch (Exception e) {
	         return;
	      } finally {
	         try {
	            if(br != null)
	               br.close();
	         } catch (IOException e) {
	            //
	         }
	      }
	}
	
	private static void updateConsistency(String acc_num, int a, int b) {
		  String tmpFileName = "tmp";
		  boolean found = false;
		  BufferedReader br = null;
	      BufferedWriter bw = null;
	      try {
	         br = new BufferedReader(new FileReader(FILE_NAME));
	         bw = new BufferedWriter(new FileWriter(tmpFileName));
	         String line;
	         while ((line = br.readLine()) != null) {
	            if (line.contains(acc_num))
	               {
	               found = true;
	               line = acc_num + "$" + Integer.toString(a) + "$" + Integer.toString(b);}
	               bw.write(line+"\n");
	               }
	         	
	         if(!found) {
	        	line = acc_num + "$" + Integer.toString(a) + "$" + Integer.toString(b);
	            bw.write(line+"\n");  
			    }
	      } catch (Exception e) {
	         return;
	      } finally {
	    	  
	         try {
	            if(br != null)
	               br.close();
	         } catch (IOException e) {
	            //
	         }
	         try {
	            if(bw != null)
	               bw.close();
	         } catch (IOException e) {
	            //
	         }
	          File oldFile = new File(FILE_NAME);
		      oldFile.delete();
		      File newFile = new File(tmpFileName);
		      newFile.renameTo(oldFile);
	      }
	      
	}
	
//	private static int addToWS(String acc_num, int b) {
//		  String tmpFileName = "tmp";
//		  BufferedReader br = null;
//	      BufferedWriter bw = null;
//	      int change_value = 0;
//	      try {
//	         br = new BufferedReader(new FileReader(FILE_NAME));
//	         bw = new BufferedWriter(new FileWriter(tmpFileName));
//	         String line;
//	         while ((line = br.readLine()) != null) {
//	            if (line.contains(acc_num))
//	               {
//	               String[] parts = line.split("\\$");
//	               change_value = Integer.parseInt(parts[2]) + b;
//	               line = parts[0] + "$" + parts[1] + "$" + Integer.toString(change_value);}
//	               bw.write(line+"\n");
//	               }
//	      } catch (Exception e) {
//	         return change_value;
//	      } finally {
//	    	  
//	         try {
//	            if(br != null)
//	               br.close();
//	         } catch (IOException e) {
//	            //
//	         }
//	         try {
//	            if(bw != null)
//	               bw.close();
//	         } catch (IOException e) {
//	            //
//	         }
//	          File oldFile = new File(FILE_NAME);
//		      oldFile.delete();
//		      File newFile = new File(tmpFileName);
//		      newFile.renameTo(oldFile);
//	      }
//	      return change_value;
//	}
	
	public static boolean check(String acc_num, int dbId) {
		if(getLastModifiedDb(acc_num) > 0) {
			return lastModifiedDb.get(acc_num)[0] == dbId;}
		else {
			Integer[] value = {dbId,0};
			lastModifiedDb.put(acc_num, value);
			return true;
		}
		
	}
	
	public static int getLastModifiedDb(String acc_num) {
		try {	
			return lastModifiedDb.get(acc_num)[0];}
		catch (NullPointerException e) {
			return -1;
		}
	}
	
	public static void setLastModifiedDb(String acc_num, int a, int b) {
		if(a==1 || a==2) {
			Integer[] value = {a,b};
			lastModifiedDb.put(acc_num, value);
			updateConsistency(acc_num, a, b);
		}
	}
	
	public static void doConsistence(String acc_num, int dbId, int amount) throws SQLException {
		amount += lastModifiedDb.get(acc_num)[1];
		System.out.println(amount);
		new Query(dbId).updateBalance(acc_num, amount);
		Integer[] value = {0,0};
		lastModifiedDb.put(acc_num, value);
		updateConsistency(acc_num, 0, 0);
	}
	public static int getWS(String acc_num) {
		int a;
		try {
			a = lastModifiedDb.get(acc_num)[1];
		}
		catch(NullPointerException e) {
			a=0;
		}
		return a;
	}
//	public static int updateConsistence(String acc_num, int amount) {
////		int id = lastModifiedDb.get(acc_num)[0];
////		int bal = lastModifiedDb.get(acc_num)[1];
////		Integer[] value = {id, amount + bal};
////		lastModifiedDb.put(acc_num, value);
//		return addToWS(acc_num, amount);
//	}
//	public static void main(String[] args) {
//		int b = getLastModifiedDb("123");
//		System.out.println(b);
//	}
	
	
}
