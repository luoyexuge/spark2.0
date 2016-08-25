package com.iclick.spark2
import org.apache.spark.sql.SparkSession
import org.apache.log4j.{ Level, Logger }
import org.apache.spark.sql.functions._
import org.apache.spark.sql.DataFrame
object SparkTimeWindow {
  def printWindow(windowDF:DataFrame, aggCol:String) ={
    windowDF.sort("window.start").
    select("window.start","window.end",s"$aggCol").
    show(truncate = false)
}
  
  def main(args: Array[String]): Unit = {
     Logger.getLogger("org.apache.spark").setLevel(Level.WARN)
    Logger.getLogger("org.eclipse.jetty.server").setLevel(Level.OFF)
    val spark = SparkSession.builder.master("local").appName("example").config("spark.sql.warehouse.dir", "file:///D:/warehouse").getOrCreate()
      spark.conf.set("spark.sql.shuffle.partitions",20)
      val  path="d:\\wilson.zhou\\Desktop\\spark资料和计算广告相关\\iteblog_apple.csv"
      
      val  stockDF1=spark.read.option("header", "true").option("inferSchema", "true").csv(path)
      
      val replace=udf((arg:String)=>arg.replaceAll("/", "-"))
      
      val stockDF=stockDF1.withColumn("Date",replace(col("Date")))
      
      stockDF.selectExpr("Close*2").show()
      stockDF.show()
      //找出2016年的交易数据，所以对原始数据进行过滤
      val  stocks2016 =stockDF.filter("year(Date)==2016")
   
      //计算平均值
      val tumblingWindowDS =stocks2016.groupBy(window(stocks2016.col("Date"),"1 week")).agg(avg("Close").as("weekly_average"))
       
      tumblingWindowDS.show()
      tumblingWindowDS.sort("window.start").select("window.start", "window.end","weekly_average").show(truncate=false)

 
 

  }
  
  
}