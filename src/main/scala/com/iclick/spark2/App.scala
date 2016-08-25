package com.iclick.spark2
import org.apache.spark.SparkConf
import org.apache.log4j.{ Level, Logger }
import org.apache.spark.SparkContext
import org.apache.spark.sql.{ SparkSession, Row }
import org.apache.spark.sql.types.{ DoubleType, StringType, StructField, StructType }
import org.apache.spark.sql.types.StructField
case class Record(key: Int, value: String)
object App {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org.apache.spark").setLevel(Level.WARN)
    Logger.getLogger("org.eclipse.jetty.server").setLevel(Level.OFF)
    val spark = SparkSession.builder.
      master("local")
      .appName("example").config("spark.sql.warehouse.dir", "file:///D:/warehouse").config("spark.sql.shuffle.partitions", "20")
      .getOrCreate()
      
       val df=spark.read.option("header",true).csv("d:/wilson.zhou/Desktop/question.csv")
      
       df.describe().show()
       
       
       val sc=spark.sparkContext
 
       
       
       
//       df.write.format("csv").save("d:/wilson.zhou/Desktop/sssss.csv")
   

  }
}