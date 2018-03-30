## Instructions

#### Time Series Insights
[Add your user in the Time Series Insights Data Access Pane]({Outputs.dataAccessPaneUrl}).

 * Click on **+Add** > **Select user** > **Enter your account** > click **Select**
 * Click on **Select role** > choose **Contributor** > click **Ok** > click **Ok**
 * Click on **Overview**
 * Click on **Go to Environment** or the **Time Series Insights URL**
 
 [Open Time Series Insights](https://insights.timeseries.azure.com/)

#### Azure Databricks Workspace
[Azure Databricks Overview]({Outputs.databricksOverviewUrl})

#### Quick links
* [Time Series Insights](https://insights.timeseries.azure.com/)
* [Azure Databricks Overview]({Outputs.databricksOverviewUrl})

**Change Keywords in Function app settings**
[Open the Function App](https://ms.portal.azure.com/?flight=1#blade/WebsitesExtension/FunctionsIFrameBlade/id/%2Fsubscriptions%2F{SubscriptionId}%2FresourceGroups%2F{ProjectName}%2Fproviders%2FMicrosoft.Web%2Fsites%2F{Outputs.functionAppName})

 * Click on **Platform features** > **Application setting** > scroll to find **App settings** and then change the values for **TWITTER_KEYWORDS**
 * Click **Save** at the top of the App settings blade
 * Open the Database in SQL Server Management Studio or Visual Studio and open a **New Query**
 * Enter ```truncate table TweetScore``` and click **Execute** (This will remove all the records from you previous key words)