// Databricks notebook source
// MAGIC %md
// MAGIC Enter the ConnectionStingBuilder Event Hub Endpoint <**IngestConnectionString**> and set the Event Hub Name <**IngestEventHubName**> prior to running the cell below

// COMMAND ----------

import org.apache.spark.eventhubs._

// Build connection string with the above information
val connectionString = ConnectionStringBuilder("<IngestConnectionString>")
// ("Endpoint=sb://test15gjacp5rwplhceservicebusingest.servicebus.windows.net/;SharedAccessKeyName=RootManageSharedAccessKey;SharedAccessKey=Jf5GNGVIIiQ5mLcKvVfu8h8vIm+43RzpOQBb9pdNRMo=")
  .setEventHubName("<IngestEventHubName>")
  .build

val customEventhubParameters =
  EventHubsConf(connectionString)
  .setMaxEventsPerTrigger(5)

val incomingStream = spark.readStream.format("eventhubs").options(customEventhubParameters.toMap).load()

incomingStream.printSchema

// Sending the incoming stream into the console.
// Data comes in batches!
incomingStream.writeStream.outputMode("append").format("console").option("truncate", false).start()

// COMMAND ----------

import org.apache.spark.sql.types._
import org.apache.spark.sql.functions._

// Event Hub message format is JSON and contains "body" field
// Body is binary, so we cast it to string to see the actual content of the message
val messages =
  incomingStream
  .withColumn("Offset", $"offset".cast(LongType))
  .withColumn("Time (readable)", $"enqueuedTime".cast(TimestampType))
  .withColumn("Timestamp", $"enqueuedTime".cast(LongType))
  .withColumn("Body", $"body".cast(StringType))
  .select("Offset", "Time (readable)", "Timestamp", "Body")

messages.printSchema

messages.writeStream.outputMode("append").format("console").option("truncate", false).start()

// COMMAND ----------

import org.apache.spark.sql.{functions=>F}
import org.apache.spark.sql.Encoders
case class LogRecord(lang: String, text: String, time_zone: String, timestamp_ms: String, topic: String, retweet_count: Int, id: String)
val sparkSchema = Encoders.product[LogRecord].schema
val df = messages.select(F.from_json(F.col("body"), sparkSchema).alias("json_parsed"))
  .select("json_parsed.*")
                              

// COMMAND ----------

import java.io._
import java.net._
import java.util._

class Document(var id: String, var text: String, var language: String = "", var sentiment: Double = 0.0) extends Serializable

class Documents(var documents: List[Document] = new ArrayList[Document]()) extends Serializable {

    def add(id: String, text: String, language: String = "") {
        documents.add (new Document(id, text, language))
    }
    def add(doc: Document) {
        documents.add (doc)
    }
}

// COMMAND ----------

class CC[T] extends Serializable { def unapply(a:Any):Option[T] = Some(a.asInstanceOf[T]) }
object M extends CC[scala.collection.immutable.Map[String, Any]]
object L extends CC[scala.collection.immutable.List[Any]]
object S extends CC[String]
object D extends CC[Double]

// COMMAND ----------

// MAGIC %md
// MAGIC Enter the Text Analytics Cognitive Service accessKey <**accessKey**> and host Endpoint <**Endpoint**> prior to running the cell below
// MAGIC 
// MAGIC * Note that for the host value you need to drop the **/text/analytics/v2.0** from the end of the Text Analytics endpoint
// MAGIC * It should look something like this below (your region might be different)
// MAGIC **val host = "https://eastus.api.cognitive.microsoft.com"**

// COMMAND ----------

import javax.net.ssl.HttpsURLConnection
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import scala.util.parsing.json._

object SentimentDetector extends Serializable {

  // Cognitive Services API connection settings
  val accessKey = "<accessKey>"
  val host = "<Endpoint>"
  val languagesPath = "/text/analytics/v2.0/languages"
  val sentimentPath = "/text/analytics/v2.0/sentiment"
  val languagesUrl = new URL(host+languagesPath)
  val sentimenUrl = new URL(host+sentimentPath)

  def getConnection(path: URL): HttpsURLConnection = {
    val connection = path.openConnection().asInstanceOf[HttpsURLConnection]
    connection.setRequestMethod("POST")
    connection.setRequestProperty("Content-Type", "text/json")
    connection.setRequestProperty("Ocp-Apim-Subscription-Key", accessKey)
    connection.setDoOutput(true)
    return connection
  }

  def prettify (json_text: String): String = {
    val parser = new JsonParser()
    val json = parser.parse(json_text).getAsJsonObject()
    val gson = new GsonBuilder().setPrettyPrinting().create()
    return gson.toJson(json)
  }

  // Handles the call to Cognitive Services API.
  // Expects Documents as parameters and the address of the API to call.
  // Returns an instance of Documents in response.
  def processUsingApi(inputDocs: Documents, path: URL): String = {
    val docText = new Gson().toJson(inputDocs)
    val encoded_text = docText.getBytes("UTF-8")
    val connection = getConnection(path)
    val wr = new DataOutputStream(connection.getOutputStream())
    wr.write(encoded_text, 0, encoded_text.length)
    wr.flush()
    wr.close()

    val response = new StringBuilder()
    val in = new BufferedReader(new InputStreamReader(connection.getInputStream()))
    var line = in.readLine()
    while (line != null) {
        response.append(line)
        line = in.readLine()
    }
    in.close()
    return response.toString()
  }

  // Calls the language API for specified documents.
  // Returns a documents with language field set.
  def getLanguage (inputDocs: Documents): Documents = {
    try {
      val response = processUsingApi(inputDocs, languagesUrl)
      // In case we need to log the json response somewhere
      val niceResponse = prettify(response)
      val docs = new Documents()
      val result = for {
            // Deserializing the JSON response from the API into Scala types
            Some(M(map)) <- scala.collection.immutable.List(JSON.parseFull(niceResponse))
            L(documents) = map("documents")
            M(document) <- documents
            S(id) = document("id")
            L(detectedLanguages) = document("detectedLanguages")
            M(detectedLanguage) <- detectedLanguages
            S(language) = detectedLanguage("iso6391Name")
      } yield {
            docs.add(new Document(id = id, text = id, language = language))
      }
      return docs
    } catch {
          case e: Exception => return new Documents()
    }
  }

  // Calls the sentiment API for specified documents. Needs a language field to be set for each of them.
  // Returns documents with sentiment field set, taking a value in the range from 0 to 1.
  def getSentiment (inputDocs: Documents): Documents = {
    try {
      val response = processUsingApi(inputDocs, sentimenUrl)
      val niceResponse = prettify(response)
      val docs = new Documents()
      val result = for {
            // Deserializing the JSON response from the API into Scala types
            Some(M(map)) <- scala.collection.immutable.List(JSON.parseFull(niceResponse))
            L(documents) = map("documents")
            M(document) <- documents
            S(id) = document("id")
            D(sentiment) = document("score")
      } yield {
            docs.add(new Document(id = id, text = id, sentiment = sentiment))
      }
      return docs
    } catch {
        case e: Exception => return new Documents()
    }
  }
}

// User Defined Function for processing content of messages to return their sentiment.
val toSentiment = udf((textContent: String) => {
  val inputDocs = new Documents()
  inputDocs.add (textContent, textContent)
  val docsWithLanguage = SentimentDetector.getLanguage(inputDocs)
  val docsWithSentiment = SentimentDetector.getSentiment(docsWithLanguage)
  if (docsWithLanguage.documents.isEmpty) {
    // Placeholder value to display for no score returned by the sentiment API
    (-1).toDouble
  } else {
    docsWithSentiment.documents.get(0).sentiment.toDouble
  }
})

// COMMAND ----------

// Prepare a dataframe with Content and Sentiment columns
val streamingDataFrame = df.withColumn("Sentiment", toSentiment($"text"))

// Display the streaming data with the sentiment
streamingDataFrame.writeStream.outputMode("append").format("console").option("truncate", false).start()

// COMMAND ----------

// Create body from the json

import org.apache.spark.sql.functions._

val df2 = streamingDataFrame 
  .select("lang", "text", "time_zone", "timestamp_ms", "topic", "retweet_count", "id", "Sentiment")   // this selects all the columns
  .withColumn("body", to_json(struct("lang", "text", "time_zone", "timestamp_ms", "topic", "retweet_count", "id", "Sentiment")))
  .select("body")

// COMMAND ----------

// MAGIC %md
// MAGIC Enter the ConnectionStingBuilder Event Hub Endpoint <**DestConnectionString**> and set the Event Hub Name <**DestEventHubName**> prior to running the cell below

// COMMAND ----------

// Connect to destinate Event Hub

import org.apache.spark.eventhubs._

// Build connection string with the above information
val connectionStringd = ConnectionStringBuilder("<DestConnectionString>")
  .setEventHubName("<DestEventHubName>")
  .build

val customEventhubParametersd =
  EventHubsConf(connectionStringd)
  .setMaxEventsPerTrigger(5)

//val incomingStream = spark.readStream.format("eventhubs").options(customEventhubParametersd.toMap).load()


// COMMAND ----------

// Write body data from a DataFrame to EventHubs. Events are distributed across partitions using round-robin model.
val ds = df2
  .select("body")
  .writeStream
  .format("eventhubs")
  .options(customEventhubParametersd.toMap)    // EventHubsConf containing the destination EventHub connection
  .option("checkpointLocation", "/checkpoint/") 
  .start()