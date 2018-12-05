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
	
	private static final String FILE_NAME = "consistence.txt";
	private static Map<String, Integer> lastModifiedDb = new HashMap<>();
	
	public static void genConsistency() {
		  BufferedReader br = null;
	      try {
	         br = new BufferedReader(new FileReader(FILE_NAME));
	         String line;
	         while ((line = br.readLine()) != null) {
	            String[] part = line.split(" ");
	            String key = part[0];
	            int value = Integer.parseInt(part[1]);
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
	
	private static void updateConsistency(String acc_num, int a) {
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
	               {found = true;
	               line = acc_num + " " + Integer.toString(a);}
	               bw.write(line+"\n");
	               }
	         	
	         if(!found) {
	        	line = acc_num + " " + Integer.toString(a);
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
	
	public static boolean check(String acc_num, int dbId) {
		if(getLastModifiedDb(acc_num) > 0) {
			return lastModifiedDb.get(acc_num) == dbId;}
		return true;
	}
	
	public static int getLastModifiedDb(String acc_num) {
		try {	
			return lastModifiedDb.get(acc_num);}
		catch (NullPointerException e) {
			return -1;
		}
	}
	
	public static void setLastModifiedDb(String acc_num, int a) {
		if(a==1 || a==2) {
			lastModifiedDb.put(acc_num, a);
			updateConsistency(acc_num, a);
		}
	}
	
	public static void doConsistence(String acc_num, int dbId) throws SQLException {
		Account acc = new Query(lastModifiedDb.get(acc_num)).selectByAccNum(acc_num);
		new Query(dbId).updateAcc(acc);
		lastModifiedDb.put(acc_num, 0);
		updateConsistency(acc_num, 0);
	}
	public static void main(String[] args) {
		int b = getLastModifiedDb("123");
		System.out.println(b);
	}

	
	
}
