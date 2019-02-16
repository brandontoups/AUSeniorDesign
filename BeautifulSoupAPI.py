import requests
import urllib
import sys
from selenium import webdriver
from bs4 import BeautifulSoup


# Example file call: python BeautifulSoupAPI.py [deedType] [bookNum] [pageNum] [county] [imageDir] [imageType]
#                    python BeautifulSoupAPI.py w 18 41 humphreys imageDir tiff
# deedType: 'w' for warranty deed or 't' for trust deed
# bookNum: warranty or trust deed book number
# pageNum: warranty or trust deed page number
# county: name of county in which to search for deed(s)
# imageDir: directory where images are saved
# imageType: 'tiff' or 'pdf'

website = "https://www.titlesearcher.com/"

def locateImageURL(deedType, soupObject, imageFormat, imageDir):
    urlBody = ''
    urlTotal = ''
    imgName = ''

    for link in soupObject.find_all('br'):
        for link2 in link.find_all('td'):
            if (link2.text.strip() == deedType):
                imgDetails = link2.find_previous_sibling('td')
                imgName = imgDetails.find_all('span')
                imgName = imgName[1].text.strip()
                aTag = link2.findNext('a')
                while (aTag.has_attr('onclick') != True):
                    link2 = aTag
                    aTag = link2.findNext('a')
                onclick = aTag.get('onclick')
                if (onclick[0:4] == 'load'):
                    onclickList = onclick.split("'")
                    urlBody = onclickList[1]
                    urlTotal = website + 'imgview.php?' + urlBody
                    if (imageFormat.upper() == 'TIFF'):
                        urlTotal += '&imgtype=tiff'
                    elif (imageFormat.upper() == 'PDF'):
                        urlTotal += '&imgtype=pdf'
                    urllib.urlretrieve(urlTotal, imageDir + '/' + imgName)

def locateDetails(deedType, soupObject, s):
    for link in soupObject.find_all('br'):
        for link2 in link.find_all('td'):
            if (link2.text.strip() == deedType):
                propertyInfo = {}
                link2 = link2.find_previous_sibling('td')
                details = link2.find_previous_sibling('td')
                details2 = details.findNext('a')
                if (details2.text.strip() == 'Details'):
                    detailsLink = details2['href']
                    listCounter = 0
                    p = s.get(website + str(detailsLink))
                    soup = BeautifulSoup(p.text, 'html.parser')
                    for link3 in soup.find_all('td'):
                        if (link3.text.strip()[0:9] == 'File Date'):
                            propertyInfo['date'] = link3.text.strip().split(':')[1]
                            #print(propertyInfo['date'])
                            listCounter += 1
                            while (listCounter <= 8):
                                link3 = link3.findNext('td')
                                if (listCounter == 2):
                                    propertyInfo['tax'] = link3.text.strip().split('$')[1]
                                    #print(propertyInfo['tax'])
                                if (listCounter == 3):
                                    propertyInfo['transaction'] = link3.text.strip().split('$')[1]
                                    #print(propertyInfo['transaction'])
                                if (listCounter == 6):
                                    propertyInfo['lien'] = link3.text.strip().split('$')[1]
                                    #print(propertyInfo['lien'])
                                if (listCounter == 7):
                                    propertyInfo['mort'] = link3.text.strip().split('$')[1]
                                    #print(propertyInfo['mort'])
                                listCounter += 1
                        if (link3.text.strip() == 'Grantor(s)'):
                            link3 = link3.findNext('td')
                            link3 = link3.findNext('td')
                            link3 = link3.findNext('td')
                            propertyInfo['grantors'] = link3.text.strip()
                            #print(propertyInfo['grantors'])
                            link3 = link3.findNext('td')
                            propertyInfo['grantees'] = link3.text.strip()
                            #print(propertyInfo['grantees'])
                            break

def navigateToSearchPage(username, password, bookNumber, pageNumber, county, s):
    homePage = s.post(website, data={
        "userName": username,
        "password": password,
        "loginb": "Go"
    })
    soup = BeautifulSoup(homePage.text, 'html.parser')
    county = county.upper()
    countySearch = ''

    for link in soup.find_all('a'):
        if (link.text.strip() == 'New Search'):
            countySearch = link['href']
            break

    p = s.get(str(countySearch))
    soup = BeautifulSoup(p.text, 'html.parser')
    countyPage = ''

    for link in soup.find_all('a'):
        if (link.text.strip() == county):
            countyPage = link['href']
            break

    p = s.get(website + str(countyPage))
    soup = BeautifulSoup(p.text, 'html.parser')
    search = ''

    for link in soup.find_all('a'):
        if (link.text.strip() == 'SEARCH ' + county):
            search = link['href']
            break

    p = s.get(website + str(search))
    soup = BeautifulSoup(p.text, 'html.parser')
    bookSearch = ''

    for link in soup.find_all('a'):
        for link2 in link.find_all('font'):
            if (link2.text.strip() == 'Search By Book And Page / File #'):
                bookSearch = link['href']
                break

    p = s.post(website + str(bookSearch), data={
        "book": bookNumber,
        "page": pageNumber,
        "executeSearch": "Execute Search"
    })
    return p

def GetWarrantyDeed(imageDir, imageFormat, p):
    #with requests.Session() as s:
        #p = navigateToSearchPage(username, password, bookNumber, pageNumber, county, s)
    soup = BeautifulSoup(p.text, 'html.parser')
    locateImageURL('WD', soup, imageFormat, imageDir)
    #locateDetails('WD', soup, s)

def GetTrustDeed(imageDir, imageFormat, p):
    #with requests.Session() as s:
        #p = navigateToSearchPage(username, password, bookNumber, pageNumber, county, s)
    soup = BeautifulSoup(p.text, 'html.parser')
    locateImageURL('TD', soup, imageFormat, imageDir)
    #locateDetails('TD', soup, s)

def GetTrustDeedByName(username, password, firstName, lastName, county, imageDir, imageFormat):
    pass

def GetWarrantyDeedByName(username, password, firstName, lastName, countyName, imageDir, imageFormat):
    pass

def main():
    deedType = sys.argv[1]
    bookNum = sys.argv[2]
    pageNum = sys.argv[3]
    county = sys.argv[4]
    imageDir = sys.argv[5]
    imageFormat = sys.argv[6]

    with requests.Session() as s:
        while (1):
            # TODO: Change when searching by name
            p = navigateToSearchPage('auburnTigers', 'AuburnUniv', bookNum, pageNum, county, s)
            if (deedType == 't'):
                GetTrustDeed(imageDir, imageFormat, p)
            elif (deedType == 'w'):
                GetWarrantyDeed(imageDir, imageFormat, p)
            deedArguments = raw_input("Enter the deed arguments with spaces in between each argument. ")
            # Example input: t 18 18 humphreys imageDir pdf
            deedArguments = deedArguments.split(" ")
            deedType = deedArguments[0]
            bookNum = deedArguments[1]
            pageNum = deedArguments[2]
            county = deedArguments[3]
            imageDir = deedArguments[4]
            imageFormat = deedArguments[5]

if __name__ == "__main__":
    main()
