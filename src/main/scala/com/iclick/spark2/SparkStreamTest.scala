package com.iclick.spark2
import org.apache.spark.sql.SparkSession
import org.apache.log4j.{ Level, Logger }
import org.apache.spark.sql.functions._
import java.sql.Timestamp
object SparkStreamTest {
  def main(args: Array[String]): Unit = {
      Logger.getLogger("org.apache.spark").setLevel(Level.WARN)
    Logger.getLogger("org.eclipse.jetty.server").setLevel(Level.OFF)
    val spark = SparkSession.builder.
      master("local")
      .appName("example").config("spark.sql.warehouse.dir", "file:///D:/warehouse").getOrCreate()
      spark.conf.set("spark.sql.shuffle.partitions",5)
      import spark.implicits._
     val lines=spark.readStream.format("socket").option("includeTimestamp",true)
     .option("host", "localhost").option("port", "520").load()
     .as[(String,Timestamp)]
      
      val words=lines.flatMap(line=>line._1.split(" ").map(word=>(word,line._2))).toDF("word", "timestamp")
      
        val windowedCounts = words.groupBy(window($"timestamp", "3 seconds", "2 seconds"), $"word"
    ).count().orderBy("window")
          
       val query=windowedCounts.writeStream.outputMode("complete").format("console").option("truncate", "false").start()
      
      query.awaitTermination()
  }
  
}