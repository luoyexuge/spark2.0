package com.iclick.spark2
import org.apache.spark.sql.SparkSession
import org.apache.log4j.{ Level, Logger }
object SparkCataLog {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org.apache.spark").setLevel(Level.WARN)
    Logger.getLogger("org.eclipse.jetty.server").setLevel(Level.OFF)
    val spark = SparkSession.builder.
      master("local")
      .appName("example").config("spark.sql.warehouse.dir", "file:///D:/warehouse").getOrCreate()
      
      spark.conf.set("spark.sql.shuffle.partitions",20)
      
      println(spark.conf.get("spark.sql.shuffle.partitions"))
      
      val df=spark.read.text("d:\\wilson.zhou\\Desktop\\wordcount.txt")
      df.createOrReplaceTempView("iterblog")
      val catalog=spark.catalog
      catalog.listDatabases().select("name").show(false)
      catalog.listTables().select("name").show(false)
     val dfsql= spark.sql("select * from iterblog")
      dfsql.show()
      //选择函数展示
      catalog.listFunctions().select("name","className","isTemporary").show(100,true)

  }

}