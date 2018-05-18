### Welcome to the Microsoft AI Solution Template - Tweets using Event Hub, Azure Databricks, and Time Series Insights

[![Deploy to Azure](https://raw.githubusercontent.com/Azure/data-ai-iot/master/images/DeployToAzure.PNG)](https://quickstart.azure.ai/Deployments/new/databricks?source=GitHub)

[View Deployed Solution](https://quickstart.azure.ai/Deployments)

[![Solution Diagram](https://raw.githubusercontent.com/Azure/data-ai-iot/master/databricks/assets/databricksTwitter.JPG)](https://raw.githubusercontent.com/Azure/data-ai-iot/master/databricks/assets/databricksTwitter.JPG)

## Prerequisites

To run the TwitterClient web job, you will need:

1. A [Twitter account](https://twitter.com/login)
2. A [Twitter application](https://apps.twitter.com)
3. Twitter's Streaming API OAuth credentials
  - On the Twitter application page, click on the *Keys and Access Tokens* tab
  - *Consumer Key (API Key)* and *Consumer Secret (API Secret)* can be found under **Application Settings** section
  - Under **Your Access Token** section, click on *Create my access token* to obtain both *Access Token* and *Access Token Secret*

More details on Twitter's Streaming API OAuth access token can be found [here](https://dev.twitter.com/oauth/overview/application-owner-access-tokens).

## Thank you to the following people that inspired, and helped unblock me along the way:
* **Andrew Ivanov** for all the work on this Twitter streaming solution template since the "Project Hudson" days
* **Garren Staubli** (Solution Architect at [Blueprint](https://bpcs.com/)) for all the help with Spark Streaming and Scala
* **Sabee Grewal** for the help with the [Azure Event Hubs Connector for Apache Spark](https://github.com/Azure/azure-event-hubs-spark)
* **Lena Hall** for the scenario code from [Azure.com](https://docs.microsoft.com/en-us/azure/azure-databricks/databricks-sentiment-analysis-cognitive-services)
* **Giuliano Rapoz** for figuring out why the sentiment was not returning from Cognitive Services
* **Jay Desai** for helping me get this Solution Template published