## Instructions

#### Time Series Insights
[Add your user in the Time Series Insights Data Access Pane]({Outputs.dataAccessPaneUrl})

 * Click on **+Add** > **Select user** > **Enter your account** > click **Select**
 * Click on **Select role** > choose **Contributor** > click **Ok** > click **Ok**
 * Click on **Overview**
 * Click on **Go to Environment** or the **Time Series Insights URL**
 
 [Open Time Series Insights](https://insights.timeseries.azure.com/) and check that tweets are streaming in. 

#### Azure Databricks Workspace
[Azure Databricks Overview]({Outputs.databricksOverviewUrl})

* Click **Launch Workspace**

![Launch Workspace](https://raw.githubusercontent.com/Azure/data-ai-iot/master/databricks/assets/LaunchWorkspace.JPG)

* Choose Organization (if applicable) which will land you in the Azure Databricks Workspace

![Azure Databricks Workspace](https://raw.githubusercontent.com/Azure/data-ai-iot/master/databricks/assets/databricksWorkspace.JPG)


#### Create a new Spark Cluster

* Click on **Clusters** on the left navigation bar and then click on **+ Create Cluster**
* Choose the Standard Cluster Type
* Enter a Cluster Name
* Choose **Databricks Runtime Version** 4.0
* Choose **Python Version** 3
* Choose **Driver Type** Same as worker
* Choose **Worker Type** Standard)DS3_v2 (default) or something of your choice
* Enter **Min Workers** 2 and change **Max Workers** to 3 and check **Enable Autoscaling**
* Check Terminate after 120 minutes of inactivity (or what ever time you think appropriate)
* Click **Create Cluster**

![Create Spark Cluster](https://raw.githubusercontent.com/Azure/data-ai-iot/master/databricks/assets/newCluster.JPG)

#### Download and Import Notebook to read tweets from EventHub

[Azure Data Bricks Notebook]({PatternAssetBaseUrl}/Notebooks/ReadTweetsFromEventHub.dbc)

![Import Notebook](https://raw.githubusercontent.com/Azure/data-ai-iot/master/databricks/assets/ImportNotebook.JPG)


#### Get Text Analytics Cognitive Service Endpoint and Key 

[Text Analytics Cognitive Service]({Outputs.textAnalyticsOverviewUrl})

#### Quick links
* [Time Series Insights](https://insights.timeseries.azure.com/)
* [Azure Event Hubs Capture]({Outputs.ehCapture})
* [Azure Databricks Overview]({Outputs.databricksOverviewUrl})
* [Text Analytics Cognitive Service]({Outputs.textAnalyticsOverviewUrl})

**Change Keywords in Function app settings**
[Open the Function App](https://ms.portal.azure.com/?flight=1#blade/WebsitesExtension/FunctionsIFrameBlade/id/%2Fsubscriptions%2F{SubscriptionId}%2FresourceGroups%2F{ProjectName}%2Fproviders%2FMicrosoft.Web%2Fsites%2F{Outputs.functionAppName})

 * Click on **Platform features** > **Application setting** > scroll to find **App settings** and then change the values for **TWITTER_KEYWORDS**
 * Click **Save** at the top of the App settings blade
 * Open the Database in SQL Server Management Studio or Visual Studio and open a **New Query**
 * Enter ```truncate table TweetScore``` and click **Execute** (This will remove all the records from you previous key words)