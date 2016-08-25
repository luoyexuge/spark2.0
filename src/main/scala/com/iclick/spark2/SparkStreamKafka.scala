package com.iclick.spark2
import org.apache.spark.sql.SparkSession
import org.apache.log4j.{ Level, Logger }
import org.apache.spark.sql.functions._
object SparkStreamKafka {
   def main(args: Array[String]): Unit = {
      Logger.getLogger("org.apache.spark").setLevel(Level.WARN)
    Logger.getLogger("org.eclipse.jetty.server").setLevel(Level.OFF)
    val spark = SparkSession.builder.
      master("local")
      .appName("example").config("spark.sql.warehouse.dir", "file:///D:/warehouse").getOrCreate()
      spark.conf.set("spark.sql.shuffle.partitions",5)
      import spark.implicits._
      
      
   }
}