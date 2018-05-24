package com.sf.utils;

import java.io.File;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.helpers.OptionConverter;
import org.apache.log4j.spi.LoggingEvent;

/**
 * 这个类主要实现了log4j，让它既可以设置日志文件最大值，又可以按照一定的时间后缀来配置文件名
 * 唯一不足，并没有实现，日志文件总数量的维护，当日志文件量成千上万，需要自己去删除
 * @author tuzhaoliang
 * @date 2018年3月15日
 * @email 1129830650@qq.com
 */
public class EasyRollingFileAppender extends FileAppender {

  /**
     The date pattern. By default, the pattern is set to
     "'.'yyyy-MM-dd" meaning daily rollover.
   */
  private String datePattern = "'.'yyyy-MM-dd-hh-mm-SS";

  /**
     The log file will be renamed to the value of the
     scheduledFilename variable when the next interval is entered. For
     example, if the rollover period is one hour, the log file will be
     renamed to the value of "scheduledFilename" at the beginning of
     the next hour. 

     The precise time when a rollover occurs depends on logging
     activity. 
  */
//  private String scheduledFilename;
  
  /**
  	The default maximum file size is 10MB.
  */
  public String maxFileSize = "10MB";
  public long maximumFileSize = 10*1024*1024;

  SimpleDateFormat sdf;


  // The gmtTimeZone is used only in computeCheckPeriod() method.
  static final TimeZone gmtTimeZone = TimeZone.getTimeZone("GMT");


  /**
     The default constructor does nothing. */
  public EasyRollingFileAppender() {
  }

  /**
    Instantiate a <code>DailyRollingFileAppender</code> and open the
    file designated by <code>filename</code>. The opened filename will
    become the ouput destination for this appender.

    */
  public EasyRollingFileAppender (Layout layout, String filename,
				   String datePattern) throws IOException {
    super(layout, filename, true);
    this.datePattern = datePattern;
    activateOptions();
  }
  
  public String getMaxFileSize() {
	return maxFileSize;
  }
  

  public void setMaxFileSize(String maxFileSize) {
//	  System.out.println("传入maxFileSize->"+maxFileSize);
	  this.maximumFileSize = OptionConverter.toFileSize(maxFileSize, this.maximumFileSize + 1);
//	  this.maxFileSize = maxFileSize;
//	  System.out.println("this.maxFileSize->"+this.maxFileSize);
//	  System.out.println("this.maximumFileSize->"+this.maximumFileSize);
  }

  /**
     The <b>DatePattern</b> takes a string in the same format as
     expected by {@link SimpleDateFormat}. This options determines the
     rollover schedule.
   */
  public void setDatePattern(String pattern) {
    datePattern = pattern;
  }

  /** Returns the value of the <b>DatePattern</b> option. */
  public String getDatePattern() {
    return datePattern;
  }

  public void activateOptions() {
    super.activateOptions();
    if(datePattern != null && fileName != null) {
      sdf = new SimpleDateFormat(datePattern);
    } else {
      LogLog.error("Either File or DatePattern options are not set for appender ["
		   +name+"].");
    }
  }


  /**
     Rollover the current file to a new file.
  */
  void rollOver() throws IOException {

    /* Compute filename, but only if datePattern is specified */
    if (datePattern == null) {
      errorHandler.error("Missing DatePattern option in rollOver().");
      return;
    }

//    String datedFilename = fileName+sdf.format(now);
    // It is too early to roll over because we are still within the
    // bounds of the current interval. Rollover will occur once the
    // next interval is reached.
//    if (scheduledFilename.equals(datedFilename)) {
//      return;
//    }

    // close current file, and rename it to datedFilename
    this.closeFile();
    File file1 = new File(fileName);
    File target  = new File(fileName+sdf.format(new Date(file1.lastModified())));
    file1 = null;
    System.out.println("target->"+target);
//    if (target.exists()) {
//      target.delete();
//    }

    File file = new File(fileName);
    boolean result = file.renameTo(target);
    if(result){
    	LogLog.debug(fileName +" -> "+ target.getName());
    }else{
    	LogLog.error("Failed to rename ["+fileName+"] to ["+target.getName()+"].");
    }

    try {
      // This will also close the file. This is OK since multiple
      // close operations are safe.
      this.setFile(fileName, true, this.bufferedIO, this.bufferSize);
    }
    catch(IOException e) {
      errorHandler.error("setFile("+fileName+", true) call failed.");
    }
//    scheduledFilename = datedFilename;
  }

  /**
   * This method differentiates DailyRollingFileAppender from its
   * super class.
   *
   * <p>Before actually logging, this method will check whether it is
   * time to do a rollover. If it is, it will schedule the next
   * rollover time and then rollover.
   * */
  protected void subAppend(LoggingEvent event) {
	  File currentFile = new File(fileName);
	  System.out.println("fileName->"+fileName+",fileLength->"+currentFile.length()+",maxFileSize->"+maxFileSize);
	  if(currentFile.length()>this.maximumFileSize){
	      try {
			rollOver();
		      }
		      catch(IOException ioe) {
		          if (ioe instanceof InterruptedIOException) {
		              Thread.currentThread().interrupt();
		          }
			      LogLog.error("rollOver() failed.", ioe);
		      }
	  }
	  super.subAppend(event);
   }
}

