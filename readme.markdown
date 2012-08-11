My attempt at solving the sortable challenge. Happy to be trying Scala again after almost 10 months. :-)

## What did I do?

* Downloaded and installed Scala, SBT, IntelliJ IDEA Scala plugin
* Got hello world running from the SBT console
*

## Sortable Challenge

### Overview

The goal is to use text similarity to match product listings from a 3rd party retailer, e.g. “Nikon D90 12.3MP Digital SLR Camera (Body Only)” against a set of known products, e.g. “Nikon D90″.

Read the instructions, download the data files below, code up your solution, post it to your github account and impress us!

### Background

At Sortable we do a lot of data integration. A simplified example: product specifications might come from one data source, product pricing from another, and product reviews from a third. A major challenge in working with all this data is determining when two pieces of information from disparate data sources are actually talking about the same product. In academic circles this problem is sometimes called Record Linkage, Entity Resolution, Reference Reconciliation, or a host of other fancy terms. We describe this problem very technically as “matching.”

### The Challenge

We’ll provide you with a set of products and a set of price listings matching some of those products. The task is to match each listing to the correct product. Precision is critical. We much prefer missed matches (lower recall) over incorrect matches, so try hard to avoid false positives. A single price listing may match at most one product.

Be careful not to tie your logic too tightly to the input data. We will run your solution against both the listings provided in the challenge, and a different set of listings that you don’t get to see ahead of time. No giant if statements tailored exactly to the test data, please!

We’ll evaluate solutions based on the following criteria:

* Compare your results to our results
    * Precision – do you make (m)any false matches?
    * Recall – how many correct matches did you make?
* Code quality (readable, maintainable)
* Efficiency (data structure and algorithm choices)

### How to enter

Complete the challenge in whatever language you like (bonus points for Scala, but not required)
Put the source code for your solution up on your github account
Make it easy for us to run your solution (ideally on Linux)
e.g. “clone my repository and run go.sh”
e.g. If your solution requires Windows, zip up an executable that we can run (one-click, ideally) and email it to us
Email challenge@sortable.com with the location of your github repo and any other relevant info

### Details

The inputs and outputs for the challenge are all text files. Each file has one JSON object per line. The following section describes what those objects look like.

### Data Objects

##### Product

    {
      "product_name": String   // A unique id for the product
      "manufacturer": String
      "family": String         // optional grouping of products
      "model": String
      "announced-date": String // ISO-8601 formatted date string, e.g. 2011-04-28T19:00:00.000-05:00
    }

##### Listing

    {
      "title": String         // description of product for sale
      "manufacturer":  String // who manufactures the product for sale
      "currency": String      // currency code, e.g. USD, CAD, GBP, etc.
      "price": String         // price, e.g. 19.99, 100.00
    }

##### Result

A file full of Result objects is what your solution will be generating. A Result simply associates a Product with a list of matching Listing objects.

    {
      "product_name": String
      "listings": Array[Listing]
    }

##### Input Files

* Products file (products.txt) - Text file with one Product object per line.
* Listings file (listings.txt) - Text file with one Listing object per line.

##### Output File

The output your solution creates should be a text file with one Result object per line.

##### Challenge Data

Download: [challenge_data_20110429.tar.gz](http://blog.snapsort.com/files/challenge_data_20110429.tar.gzs) (370 KB)

Contains two files:

products.txt – Contains around 700 products
listings.txt – Contains about 20,000 product listings