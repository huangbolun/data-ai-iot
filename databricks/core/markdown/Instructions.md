## Instructions

#### Time Series Insights
[Add your user in the Time Series Insights Data Access Pane]({Outputs.dataAccessPaneUrl})

 * Click on **+Add** > **Select user** > **Enter your account** > click **Select**
 * Click on **Select role** > choose **Contributor** > click **Ok** > click **Ok**
 * Click on **Overview**
 * Click on **Go to Environment** or the **Time Series Insights URL**
 
 [Open Time Series Insights](https://insights.timeseries.azure.com/) and check that tweets are streaming in. 

 #### Azure Event Hubs Capture
[Turn on Azure Event Hubs Capture]({Outputs.ehCapture})

 * Click the **On** Capture toggle
 * Use the default for **Time window** and **Size window**
 * Chose **Azure Storage** for the Capture Provider
 * Click the **Select Container** button > choose the **{Outputs.storageAccountName}** storage account
 * Add a container and give it a name like **twitterrows** > choose **Private (no anonymous access)** as the Public access level > click **OK**
 * Select the newly created container > click **Select**
 * Use the default Sample Capture file name formats as **{Namespace}/{EventHub}/{PartitionId}/{Year}/{Month}/{Day}/{Hour}/{Minute}/{Second}**
 * Click **Save changes**

![Capture Settings](https://raw.githubusercontent.com/Azure/data-ai-iot/master/databricks/assets/Capture.JPG)

#### Azure Databricks Workspace
[Azure Databricks Overview]({Outputs.databricksOverviewUrl})

* Click **Launch Workspace**

![Launch Workspace](https://raw.githubusercontent.com/Azure/data-ai-iot/master/databricks/assets/LaunchWorkspace.JPG)

* Choose Organization which will land you in the Azure Databricks Workspace

![Azure Databricks Workspace](https://raw.githubusercontent.com/Azure/data-ai-iot/master/databricks/assets/databricksWorkspace.JPG)

#### Create and attach to a Event Hub Library using the Azure Event Hubs Spark Connector

You can find details here on [Requirements for using the Azure Event Hubs Spark Connector](https://docs.azuredatabricks.net/spark/latest/structured-streaming/streaming-event-hubs.html#requirements)

![Create Maven Library](https://raw.githubusercontent.com/Azure/data-ai-iot/master/databricks/assets/createMavenLibrary.JPG)

![azure-eventhubs-spark](https://raw.githubusercontent.com/Azure/data-ai-iot/master/databricks/assets/azure-eventhubs-spark.JPG)

#### Create a new Spark Cluster

![Create Spark Cluster](https://raw.githubusercontent.com/Azure/data-ai-iot/master/databricks/assets/newCluster.JPG)

#### Attach Library to Spark Cluster

![Attach Spark Cluster](https://raw.githubusercontent.com/Azure/data-ai-iot/master/databricks/assets/attachLibrary.JPG)

#### Create at Notebook to read Tweets from EventHub


#### Quick links
* [Time Series Insights](https://insights.timeseries.azure.com/)
* [Azure Event Hubs Capture]({Outputs.ehCapture})
* [Azure Databricks Overview]({Outputs.databricksOverviewUrl})

**Change Keywords in Function app settings**
[Open the Function App](https://ms.portal.azure.com/?flight=1#blade/WebsitesExtension/FunctionsIFrameBlade/id/%2Fsubscriptions%2F{SubscriptionId}%2FresourceGroups%2F{ProjectName}%2Fproviders%2FMicrosoft.Web%2Fsites%2F{Outputs.functionAppName})

 * Click on **Platform features** > **Application setting** > scroll to find **App settings** and then change the values for **TWITTER_KEYWORDS**
 * Click **Save** at the top of the App settings blade
 * Open the Database in SQL Server Management Studio or Visual Studio and open a **New Query**
 * Enter ```truncate table TweetScore``` and click **Execute** (This will remove all the records from you previous key words)