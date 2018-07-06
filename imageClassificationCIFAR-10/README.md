## Welcome to a hands-on tutorial on deep learning focusing on image classification with CIFAR-10

### The CIFAR-10 dataset
The CIFAR-10 and CIFAR-100 are labeled subsets of the 80 million tiny images dataset. They were collected by Alex Krizhevsky, Vinod Nair, and Geoffrey Hinton.

The [CIFAR-10 dataset](http://www.cs.toronto.edu/~kriz/cifar.html) consists of 60000 32x32 colour images in 10 classes, with 6000 images per class. There are 50000 training images and 10000 test images.

![imageClassificationCIFAR-10](https://raw.githubusercontent.com/Azure/data-ai-iot/master/imageClassificationCIFAR-10/images/CIFAR-10.png)

## MLADS Tutorial

This tutorial was presented at MLADS (Machine Learning and Data Science), Microsoft's internal machine learning conference, in spring 2018. It is a hands-on tutorial on deep learning that focuses on image classification with CIFAR-10. It has three hands-on components:

1. Preprocessing images using the local Spark instance
2. Building an image classifier using the Microsoft Cognitive Toolkit
3. Deploying the model as a REST API using Microsoft ML Server

You can run this tutorial on your own Ubuntu DSVM. Create a VM using the ARM template in the *setup* folder, *git clone* this repo to the notebooks directory in your home directory, log in to JupyterHub, and navigate to thi directory. The Jupyter notebooks are designed to be easy to follow along.

## Prerequisites

To deploy the Deep Learning Linux GPU-based Data Science VM, you will need:

1. An [Azure account](https://portal.azure.com)
2. Clone these GitHub repositories using Git and the following commands: 

    `git clone https://github.com/Azure/data-ai-iot.git`
    
    `git clone https://github.com/Azure/DataScienceVM.git`

**Note** that you will be deploying one on more GPU VMs into your Azure Subscription when either clicking on the [Deploy to Azure](https://github.com/Azure/DataScienceVM/blob/master/Tutorials/MLADS-spring-2018/setup/README.md) button, or by alternatively deploying this Deep Learning Linux GPU-based Data Science VM is by using an ARM template and parameters file via the Azure CLI.

To check on the availability of GPU VMs you might also want to run the following commands in the Azure CLI to find locations where you have access to GPU VMs (Note more information on using the CLI is found in the `Provisioning using the Azure CLI` section below):
  
  `az login`

  `az account show`

  `az account list-locations`
  
  `az vm list-sizes -l westus`

  `az vm list-usage --location westus2 -o table`

You should get some output that looks like this:

![list-usage](https://raw.githubusercontent.com/Azure/data-ai-iot/master/imageClassificationCIFAR-10/images/list-usage.png)

## Choices for Provisioning a Deep Learning Linux GPU-based Data Science VM

You can provision one or more (you only need one for this tutorial) VMs using the Deploy to Azure button or using the Azure CLI.

``` 
Note as of the time of Ready2018 07/17/2018 the Linux DSVM is using NVIDIA-SMI Driver Version: 390.30 which needs to be updated to Verion 390.46 using the configure_vm.sh

This process causes the deployment of the VM to run 60 minutes plus and then eventually timeout.  The driver update will be pre-done on near future Linux DSVM images and the update logic will be removed from the configure_vm.sh.  At that time the provisioning will complete successfully.

For now just access the VM after 10 minutes and check for 2 things:
1) Enter the nvidia-smi command at the $ prompt.  It should come back as NVIDIA-SMI Driver Version: 390.46
2) The MLADS-spring-2018 folder should be present in Jupyter Hub
```

### Provisioning using the Deploy to Azure button

After you have checked for the availability of Standard NC Family vCPUs in your subscription.

1) Create a Resource Group using the [Azure Portal](https://portal.azure.com) or Azure CLI a command like this:

  `az group create -n cifartutorial -l westus`

2) Click on the [Deploy to Azure](https://github.com/Azure/DataScienceVM/blob/master/Tutorials/MLADS-spring-2018/setup/README.md) button on the README on the DataScienceVM GitHub repository  

### Provisioning using the Azure CLI

1. Download and install the [Azure CLI Installer (MSI) for Windows](https://aka.ms/InstallAzureCliWindows) or [Mac or Linux](https://docs.microsoft.com/en-us/cli/azure/install-azure-cli?view=azure-cli-latest) . Once the installation is complete open the command prompt and run `az login`, then copy the access code returned. In a browser, open a **private tab** and enter the URL `aka.ms/devicelogin`. When prompted, paste in the access code from above. You will be prompted to authenticate using our Azure account.  Go through the appropriate multifaction authenication.
2. In you have never deployed the DSVM before, log into the Azure Portal and click on the Create a resource green + sign in the left panel and search for and select **Data Science Virtual Machine for Linux (Ubuntu)**. On the resources' page, click on the link in the buttom that says <u>Want to deploy programmatically? Get started</u>
Scroll down and make sure that the status is enabled for the intended subscription.
3. From **Windows Explorer** navigate to the DataScienceVM folder `DataScienceVM\Tutorials\MLADS-spring-2018\setup` and from there launch the command prompt by going to the address bar and typing `cmd` (for the Windows command prompt) or `bash` (for the Linux command prompt assuming it is installed already) and type `az --version` to check the installation.  Copy over the `parameters-ldsvm.json` from the data-ai-iot folder `data-ai-iot\imageClassificationCIFAR-10\setup`.  Note this come from the GitHub repos you cloned during the Prerequisites above.  

4. When you logged in to the CLI in step 1 above you will see a json list of all the Azure account you have access to. Run `az account show` to see you current active account.  Run `az account list -o table` if you want to see all of you Azure account in a table. If you would like to switch to another Azure account run `az account set --subscription <your SubscriptionId>` to set the active subcription.  Run `az group create -n cifartutorial -l westus` to create a resource group called `cifartutorial`.

5. Next run the following command to provision the DSVM:
```
az group deployment create -g cifartutorial --template-file azuredeploy.json --parameters @parameters-ldsvm.json
```
Once the provisioning is finished, we can run `az resource list -g cifartutorial -o table` to check what resources were launched. Our listed resources includes a DSVM called `ready2018ldsvm`, which we provided in the `parameters.json` file.  

## Run the Notebooks on Jupyter Hub

After 10 minutes of provisioning:

1. Sign in to Jupyter Hub

Your login will be something like this:
`https://dsvmwz2dgsjsfbiuy.westus.cloudapp.azure.com:8000/hub/login`

`https://<DNSname>:8000/hub/login` or `https://<PublicIPaddress>:8000/hub/login`

![SignIn](https://raw.githubusercontent.com/Azure/data-ai-iot/master/imageClassificationCIFAR-10/images/jupyterHubSignIn.png)

Enter the Username and Password you used during provisioning

2. Check that things are ready to go.  In Juypter click on the New drop-down and choose Terminal (Note you can do this by connecting to the server with any terminal)

  A) Enter the nvidia-smi command at the $ prompt.  It should come back as NVIDIA-SMI Driver Version: 390.46

![jupyterTerminal](https://raw.githubusercontent.com/Azure/data-ai-iot/master/imageClassificationCIFAR-10/images/jupyterTerminal.png)

  B) Is there a `MLADS-spring-2018` folder should be present in Jupyter Hub?

![MLADSfolder](https://raw.githubusercontent.com/Azure/data-ai-iot/master/imageClassificationCIFAR-10/images/MLADSfolder.png)

3. Run the Notebooks on Jupyter Hub

Open up the `1. load_data` notebook and run the cells individually by highlighting the cell and entering `Ctrl-Enter`

The cells are complete when they change from and `*` to a number when they complete.

After you are finished with the first notebook open up the `2. train_model` notebook and run the cells individually by highlighting the cell and entering `Ctrl-Enter`

Note that many of the cells just contain images or markdown for documentation and learning purposes.  Some of the cells like the one below don't run correctly.

```
# Figure 1
Image(url="https://cntk.ai/jup/201/cifar-10.png", width=500, height=500)
```
You can fix these by changing the code a bit like this.  You can also just not run these cells.

```
# Figure 1
from IPython.display import Image as ShowImage
ShowImage(url="https://cntk.ai/jup/201/cifar-10.png", width=500, height=500)
```

7. You can start/stop/restart your VM from the CLI using the corresponding commands below:
```
az vm start -g cifartutorial -n ready2018ldsvm
az vm stop -g cifartutorial -n ready2018ldsvm
az vm restart -g cifartutorial -n ready2018ldsvm
```

## Thank you to member of the DSVM Product team who created this tutorial:
* **Paul Shealy** 
* **Gopi Kumar**
* **Nishank Gupta** 
