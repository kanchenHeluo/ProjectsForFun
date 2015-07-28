# Samples
{
    "$schema": "http://schema.management.azure.com/schemas/2015-01-01/deploymentTemplate.json#",
    "contentVersion": "1.0.0.0",
    "parameters": {
        "gwnetworkLocation": {
            "type": "string",
            "defaultValue": "West US",
            "allowedValues": [
                "East US",
                "West US",
                "West Europe",
                "East Asia",
                "South East Asia"
            ]
        },
        "nicName": {
            "type": "string"
        },
        "numberOfVMs": {
            "type": "int"
        },
        "publicipDnsName": {
            "type": "string"
        },
        "traz1319Name": {
            "type": "string"
        },
        "traz1319Type": {
            "type": "string",
            "defaultValue": "Standard_LRS",
            "allowedValues": [
                "Standard_LRS",
                "Standard_GRS",
                "Standard_ZRS"
            ]
        },
        "vmName": {
            "type": "string"
        },
        "vmAdminUserName": {
            "type": "string"
        },
        "vmAdminPassword": {
            "type": "securestring"
        },
        "vmWindowsOSVersion": {
            "type": "string",
            "defaultValue": "2012-R2-Datacenter",
            "allowedValues": [
                "2008-R2-SP1",
                "2012-Datacenter",
                "2012-R2-Datacenter",
                "Windows-Server-Technical-Preview"
            ]
        }
    },
    "variables": {
        "gwnetworkPrefix": "10.0.0.0/16",
        "gwnetworkSubnet1Name": "Subnet-1",
        "gwnetworkSubnet1Prefix": "10.0.0.0/24",
        "gwnetworkSubnet2Name": "Subnet-2",
        "gwnetworkSubnet2Prefix": "10.0.1.0/24",
        "nicVnetID": "[resourceId('Microsoft.Network/virtualNetworks', 'gwnetwork')]",
        "nicSubnetRef": "[concat(variables('nicVnetID'),'/subnets/', variables('gwnetworkSubnet1Name'))]",
        "numberOfInstances": "[parameters('numberOfVMs')]",
        "vmImagePublisher": "MicrosoftWindowsServer",
        "vmImageOffer": "WindowsServer",
        "vmOSDiskName": "vmOSDisk",
        "vmVmSize": "Standard_D1",
        "vmVnetID": "[resourceId('Microsoft.Network/virtualNetworks', 'gwnetwork')]",
        "vmSubnetRef": "[concat(variables('vmVnetID'), '/subnets/', variables('gwnetworkSubnet1Name'))]",
        "vmStorageAccountContainerName": "vhds"

    },
    "resources": [
        {
            "name": "gwnetwork",
            "type": "Microsoft.Network/virtualNetworks",
            "location": "[parameters('gwnetworkLocation')]",
            "apiVersion": "2015-05-01-preview",
            "dependsOn": [ ],
            "tags": {
                "displayName": "gwnetwork"
            },
            "properties": {
                "addressSpace": {
                    "addressPrefixes": [
                        "[variables('gwnetworkPrefix')]"
                    ]
                },
                "subnets": [
                    {
                        "name": "[variables('gwnetworkSubnet1Name')]",
                        "properties": {
                            "addressPrefix": "[variables('gwnetworkSubnet1Prefix')]"
                        }
                    },
                    {
                        "name": "[variables('gwnetworkSubnet2Name')]",
                        "properties": {
                            "addressPrefix": "[variables('gwnetworkSubnet2Prefix')]"
                        }
                    }
                ]
            }
        },
        {
            "name": "[concat('nic', copyindex())]",
            "copy": {
                "name": "nicLoop",
                "count": "[variables('numberOfInstances')]"
            },
            "type": "Microsoft.Network/networkInterfaces",
            "location": "[parameters('gwnetworkLocation')]",
            "apiVersion": "2015-05-01-preview",
            "dependsOn": [
                "[concat('Microsoft.Network/virtualNetworks/', 'gwnetwork')]",
                "[concat('Microsoft.Network/publicIPAddresses/', 'publicIp', copyIndex())]"
            ],
            "tags": {
                "displayName": "nic"
            },
            "properties": {
                "ipConfigurations": [
                    {
                        "name": "ipconfig1",
                        "properties": {
                            "privateIPAllocationMethod": "Dynamic",
                            "subnet": {
                                "id": "[variables('nicSubnetRef')]"
                            },
                            "publicIPAddress": {
                                "id": "[resourceId('Microsoft.Network/publicIPAddresses', concat('publicIp', copyIndex()))]"
                            }
                        }
                    }
                ]
            }
        },
        {
            "name": "[concat('publicIp', copyIndex())]",
            "copy": {
                "name": "publicipLoop",
                "count": "[variables('numberOfInstances')]"
            },
            "type": "Microsoft.Network/publicIPAddresses",
            "location": "[parameters('gwnetworkLocation')]",
            "apiVersion": "2015-05-01-preview",
            "dependsOn": [ ],
            "tags": {
                "displayName": "publicip"
            },
            "properties": {
                "publicIPAllocationMethod": "Dynamic",
                "dnsSettings": {
                    "domainNameLabel": "[concat(parameters('publicipDnsName'), copyIndex())]"
                }
            }
        },
        {
            "name": "[parameters('traz1319Name')]",
            "type": "Microsoft.Storage/storageAccounts",
            "location": "[parameters('gwnetworkLocation')]",
            "apiVersion": "2015-05-01-preview",
            "dependsOn": [ ],
            "tags": {
                "displayName": "traz1319"
            },
            "properties": {
                "accountType": "[parameters('traz1319Type')]"
            }
        },
        {
            "name": "[concat(parameters('vmName'), copyindex())]",
            "copy": {
                "name": "virtualMachineLoop",
                "count": "[variables('numberOfInstances')]"
            },
            "type": "Microsoft.Compute/virtualMachines",
            "location": "[parameters('gwnetworkLocation')]",
            "apiVersion": "2015-05-01-preview",
            "dependsOn": [
                "[concat('Microsoft.Storage/storageAccounts/', parameters('traz1319Name'))]",
                "[concat('Microsoft.Network/networkInterfaces/','nic',copyindex())]"
            ],
            "tags": {
                "displayName": "vm"
            },
            "properties": {
                "hardwareProfile": {
                    "vmSize": "[variables('vmVmSize')]"
                },
                "osProfile": {
                    "computername": "[parameters('vmName')]",
                    "adminUsername": "[parameters('vmAdminUsername')]",
                    "adminPassword": "[parameters('vmAdminPassword')]"
                },
                "storageProfile": {
                    "imageReference": {
                        "publisher": "[variables('vmImagePublisher')]",
                        "offer": "[variables('vmImageOffer')]",
                        "sku": "[parameters('vmWindowsOSVersion')]",
                        "version": "latest"
                    },
                    "osDisk": {
                        "name": "vmOSDisk",
                        "vhd": {
                            "uri": "[concat('http://', parameters('traz1319Name'), '.blob.core.windows.net/', variables('vmStorageAccountContainerName'), '/', variables('vmOSDiskName'), copyindex(), '.vhd')]"
                        },
                        "caching": "ReadWrite",
                        "createOption": "FromImage"
                    }
                },
                "networkProfile": {
                    "networkInterfaces": [
                        {
                            "id": "[resourceId('Microsoft.Network/networkInterfaces', concat('nic', copyindex()))]"
                        }
                    ]
                }
            }
        }
    ],
    "outputs": {
    }
}
