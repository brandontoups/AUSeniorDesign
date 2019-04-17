# Document Extraction
The actual extraction of the documents is outlined here. titlesearcher.com is necessary, as we do not have access to all of the documents, and rely on the website for the available counties. 

## BeautifulSoupAPI
We will use [BeautifulSoupAPI.py](/src/BeautifulSoupAPI.py) for extracting the documents. In ```def main()``` we have defined our credentials, but they may need to be altered. 
```java
"userName": "auburnTigers",
"password": "AuburnUniv",
"loginb": "Go"
```

Also, titlesearcher.com is available for other states and counties. Currently it is set for Humphreys County in the state of Tennessee. 

## Virtualenv
Extracting the documents requires [BeautifulSoup4](https://pypi.org/project/beautifulsoup4/) and [requests](http://docs.python-requests.org/en/master/). These dependencies can be
remedied using Pipenv. This can be done in two ways.

First way uses the provided [Pipfile.lock](pipenv/Pipfile.lock). Be in the same directory as Pipfile.lock and do:
```console
$ pipenv sync
$ pipenv shell
$ (virtualenv) python3 BeautifulSoupAPI.py
w 0 0 imgDir pdf 0 172 3 0
```

You can check that the proper dependencies were installed with ```pipenv graph```. 

---

In case dependencies change or more become needed, this is  . Installing each dependency and running the file looks like: 
```console
$ pipenv --three install beautifulsoup4
$ pipenv --three install requests
$ pipenv shell
$ (virtualenv) python3 BeautifulSoupAPI.py
w 0 0 imgDir pdf 0 172 3 0
```
For downloading a post-1993 Warranty Deed, book 172, page 3, as a pdf, into the imgDir local directory.

## Uploading to Box
Upload the extracted PDFs into a service such as Box or Google Drive. We utilized Box during this project. The purpose of this is to keep track of documents that have been uploaded into WKS, while allowing human reviewers to centralize the documents.
