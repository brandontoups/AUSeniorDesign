import requests
import pickle
import sys
import os
import re
from bs4 import BeautifulSoup
from os.path import exists

# Example Input 1: t 0 0 imageDir pdf 0 18 18 0
# Example Input 2: w 0 0 imageDir tiff 0 john tinnell 1
# Argument descriptions:
# ['t' for trust deed or 'w' for warrranty deed] ['0' for Tennessee] ['0' for Humphreys county]
# ['example_directory' for images] ['pdf' or 'tiff' for file type] ['0' for post-1993 or '1' for pre-1993]
# ['bookNum' if searching by book or 'firstName' if searching by name]
# ['pageNum' if searching by book or 'lastName' if searching by name]
# ['0' if searching by book or '1' if searching by name]

website = "https://www.titlesearcher.com/"

class TitleSearcher:

    def __init__(self, state, county, isDataPreExtracted, bookNum=None,
                 pageNum=None, firstName=None, lastName=None):
        self.state = ''
        self.county = ''
        self.bookNum = bookNum
        self.pageNum = pageNum
        self.firstName = firstName
        self.lastName = lastName
        if (state == '0'):
            self.state = 'tennessee'
        if (county == '0'):
            self.county = 'humphreys'
        self.isDataPreExtracted = isDataPreExtracted

    def searchByName(self, soupObject, s):
        for a in soupObject.find_all('a'):
            for font in a.find_all('font'):
                if (font.text.strip() == 'Search By Name'):
                    nameSearch = a['href']
                    break
        p = s.post(website + str(nameSearch), data={
            "p1": self.lastName + '+' + self.firstName,
            "nameType": "2",
            "searchType" : "PA",
            "indexType" : "BOTH",
            "itype" : "0",
            "expandAll" : "on",
            "executeSearch": "Execute+Search"
        })
        return p

    def searchByBookPageNum(self, soupObject, s):
        for a in soupObject.find_all('a'):
            for font in a.find_all('font'):
                if (font.text.strip() == 'Search By Book And Page / File #'):
                    bookSearch = a['href']
                    break

        p = s.post(website + str(bookSearch), data={
            "book": self.bookNum,
            "page": self.pageNum,
            "executeSearch": "Execute Search"
        })
        return p

    def searchOldIndexBooks(self, soupObject, s, imageDir):
        for a in soupObject.find_all('a'):
            for font in a.find_all('font'):
                if (font.text.strip() == 'View Old Index Books'):
                    bookSearch = a['href']
                    break

        lastName = self.lastName
        charSearch = lastName[0].lower()
        p = s.get(website + str(bookSearch))
        soup = BeautifulSoup(p.text, 'html.parser')
        for tr in soup.find_all('tr'):
            for td in tr.find_all('td'):
                for div in td.find_all('div'):
                    if (div.text.strip() == 'Index Books'):
                        divNext = div.findNext('div')
        indexBookTypes = []
        for a in divNext.find_all('a'):
            indexBookTypes.append(a.text.strip())

        for bookType in indexBookTypes:
            for tort in range(2):
                if (tort == 0):
                    tortee = "TOR"
                else:
                    tortee = "TEE"
                    if ((bookType == '1968-1978td') | (bookType == '1979-1993td') | (bookType == 'gbi')):
                        break
                if ((bookType == '1810-1940') | (bookType == 'gbi')):
                    if (lastName[0:2].lower() == 'mc'):
                        charSearch = lastName[0:2].lower()
                if ((bookType == 'gbi') & (charSearch == 'x')):
                    break
                elif ((bookType == '1940-1994') | (bookType == '1968-1978td') | (bookType == '1979-1993td')):
                    if (charSearch == 'a' | charSearch == 'b'):
                        charSearch = 'A-B'
                    elif (charSearch == 'c' | charSearch == 'd'):
                        charSearch = 'C-D'
                    elif (charSearch == 'e' | charSearch == 'f' | charSearch == 'g'):
                        charSearch = 'E-G'
                    elif (charSearch == 'i' | charSearch == 'j' | charSearch == 'k'):
                        charSearch = 'I-K'
                    elif (charSearch == 'n' | charSearch == 'o' | charSearch == 'p' | charSearch == 'q'):
                        charSearch = 'N-Q'
                    elif (charSearch == 't' | charSearch == 'u' | charSearch == 'v'):
                        charSearch = 'T-V'
                    elif (charSearch == 'w' | charSearch == 'x' | charSearch == 'y' | charSearch == 'z'):
                        charSearch = 'W-Z'

                p = s.post(website + str(bookSearch), data={
                    "datekey": bookType,
                    "path": charSearch,
                    "bookpath": "0001",
                    "tortee": tortee,
                    "chars": charSearch,
                    "state": "tn",
                    "county": "humphreys"
                })
                soup = BeautifulSoup(p.text, 'html.parser')
                for td in soup.find_all('td'):
                    for div in td.find_all('div'):
                        for div2 in div.find_all('div'):
                            if (div2.text.strip() == 'Index Page Viewer'):
                                iFrame = div.findNext('iframe')
                urlBody = iFrame['src']
                urlTotal = website + urlBody
                with open(imageDir + os.sep + 'IndexDirectory.pdf', 'wb') as handle:
                    response = s.get(urlTotal, stream=True)
                    if not response.ok:
                        print(response)
                    for block in response.iter_content(1024):
                        if not block:
                            break
                        handle.write(block)
                pageNumber = input("Please enter the index book page number. ")\

                p = s.post(website + 'oldIndexPageSearch.php', data={
                        "pageNum": pageNumber,
                        "datekey": bookType,
                        "path": charSearch,
                        "bookpath": "0001",
                        "tortee": tortee,
                        "chars": charSearch,
                        "state": "tn",
                        "county": "humphreys",
                        "submit" : "Look Up Page"
                })

                soup = BeautifulSoup(p.text, 'html.parser')
                for td in soup.find_all('td'):
                    for div in td.find_all('div'):
                        for div2 in div.find_all('div'):
                            if (div2.text.strip() == 'Page View'):
                                iFrame = div.findNext('iframe')

                urlBody = iFrame['src']
                urlTotal = website + urlBody

                with open(imageDir + os.sep + 'IndexPage.pdf', 'wb') as handle:
                    response = s.get(urlTotal, stream=True)
                    if not response.ok:
                        print(response)
                    for block in response.iter_content(1024):
                        if not block:
                            break
                        handle.write(block)

    def searchNonIndexedDocs(self, soupObject, s):
        for a in soupObject.find_all('a'):
            for font in a.find_all('font'):
                if (font.text.strip() == 'View Non-Indexed Documents'):
                    bookSearch = a['href']
                    break

        p = s.post(website + str(bookSearch), data={
            "dir": "1" #1 corresponds to warranty deeds
        })
        soup = BeautifulSoup(p.text, 'html.parser')
        bookNum = 'none'

        #Iterate through book options and see if book number is listed
        for td in soup.find_all('td'):
            for select in td.find_all('select'):
                if (select['name'] == 'book'):
                    for option in select.find_all('option'):
                        #Strip whitespace, leading zeros, and leading 'wd'
                        if (option.text.strip().lstrip('0').lstrip('wd') == self.bookNum):
                            bookNum = option['value']
                            break

        if (bookNum == 'none'):
            print("Book not found in Non-Indexed Documents.")
        else:
            p = s.post(website + str(bookSearch) + '?dir=1', data={
                "book": bookNum
            })
            soup = BeautifulSoup(p.text, 'html.parser')
            pageNum = 'none'

            for td in soup.find_all('td'):
                for select in td.find_all('select'):
                    if (select['name'] == 'page'):
                        for option in select.find_all('option'):
                            #Strip whitespace, leading zeros, any numbers after decimal point
                            if (int(float(option.text.strip().lstrip('0'))) == int(self.pageNum)):
                                pageNum = option['value']
                                break

                        if (pageNum == 'none'):
                            print("Page not found in Book.")
                        else:
                            p = s.post(website + str(bookSearch) + '?dir=1&book='
                                       + bookNum, data={
                                "page": pageNum })
                            soup = BeautifulSoup(p.text, 'html.parser')

                            #Parse max and min
                            for td_2 in soup.find_all('td'):
                                for input in td_2.find_all('input'):
                                    if (input.has_attr('name') == True):
                                        if (input['name'] == 'max'):
                                            max = input['value']
                                            min = input.findNext('input')
                                            min = min['value']
                                            break

                            p = s.get(website + 'ziframe.php?site=deedholdImage&dir=1&book=' +
                                      bookNum + '&page=' + pageNum + '&max=' + max + '&min=' +
                                      min + '&schema=&previoussite=deedhold.php')
                            return p

    def writeImgToDirectory(self, imageDir, imgName, deed, s, urlTotal, deedList):
        with open(imageDir + os.sep + imgName + '.pdf', 'wb') as handle:
                response = s.get(urlTotal, stream=True)
                deed["pdf"] = response.content
                if (self.bookNum != None):
                    deed["firstArg"] = self.bookNum
                    deed["secondArg"] = self.pageNum
                else:
                    deed["firstArg"] = self.firstName
                    deed["secondArg"] = self.lastName
                deedList.append(deed)
                if not response.ok:
                    print(response)
                for block in response.iter_content(1024):
                    if not block:
                        break
                    handle.write(block)
        return deedList

    def locateImageURL(self, deedType, soupObject, imageFormat, imageDir, s):
        deedList = []
        counter = 0
        if (self.isDataPreExtracted == '0'):
            if (self.bookNum != None):
                for br in soupObject.find_all('br'):
                    for td in br.find_all('td'):
                        if ((td.text.strip()[0:2] == deedType) & (len(td.text.strip()) <= 3)):
                            deed = {}
                            imgDetails = td.find_previous_sibling('td')
                            #imgName = imgDetails.find_all('span')
                            #imgName = imgName[1].text.strip()
                            imgName = "WD" + self.bookNum + "-" + self.pageNum
                            if (counter != 0):
                                imgName += ("_" + str(counter))
                            counter += 1
                            aTag = td.findNext('a')

                            while (aTag.has_attr('onclick') != True):
                                td = aTag
                                aTag = td.findNext('a')

                            onclick = aTag.get('onclick')
                            if (onclick[0:4] == 'load'):
                                onclickList = onclick.split("'")
                                urlBody = onclickList[1]
                                urlTotal = website + 'imgview.php?' + urlBody
                                if (imageFormat.upper() == 'TIFF'):
                                    urlTotal += '&imgtype=tiff'
                                elif (imageFormat.upper() == 'PDF'):
                                    urlTotal += '&imgtype=pdf'
                                deedList = self.writeImgToDirectory(imageDir, imgName, deed, s, urlTotal, deedList)

            else:
                #How to distinguish between people when searching by name without additional discerning info?
                #This implementation simply fetches all deeds on the page without selecting a person
                for td in soupObject.find_all('td'):
                    if ((td.text.strip()[0:2] == deedType) & (len(td.text.strip()) <= 3)):
                        imgDetails = td.find_previous_sibling('td')
                        imgName = imgDetails.find_all('span')
                        imgName = imgName[1].text.strip()
                        aTag = td.findNext('a')

                        while (aTag.has_attr('onclick') != True):
                            td = aTag
                            aTag = td.findNext('a')

                        onclick = aTag.get('onclick')
                        if (onclick[0:4] == 'java'):
                            deed = {}
                            onclickList = onclick.split("\"")
                            urlBody = onclickList[1]
                            urlTotal = website + 'imgview.php?' + urlBody
                            if (imageFormat.upper() == 'TIFF'):
                                urlTotal += '&imgtype=tiff'
                            elif (imageFormat.upper() == 'PDF'):
                                urlTotal += '&imgtype=pdf'
                            deedList = self.writeImgToDirectory(imageDir, imgName, deed, s, urlTotal, deedList)

        elif (self.isDataPreExtracted == '1'): #Deeds are from before
            for td in soupObject.find_all('td'):
                for iframe in td.find_all('iframe'):
                    newWindow = iframe['src']
                    break

            urlTotal = website + newWindow
            p = s.get(urlTotal)
            soup = BeautifulSoup(p.text, 'html.parser')

            for frame in soup.find_all('frame'):
                deed = {}
                urlBody = frame['src']
                deedList = self.writeImgToDirectory(imageDir, deedType + self.bookNum + '-' +  self.pageNum, deed, s, website + urlBody, deedList)
                break
        print('LENGTH OF DEEDLIST IS' + str(len(deedList)))
        return deedList

    def locateDetails(self, deedType, soupObject, s, deedList): #TODO: Modify to include cross references
        propInfo = {}
        deedCounter = 0
        for br in soupObject.find_all('br'):
            for td in br.find_all('td'):
                if ((td.text.strip()[0:2] == deedType) & (len(td.text.strip()) <= 3)):
                    td = td.find_previous_sibling('td')
                    details = td.find_previous_sibling('td')
                    details = details.findNext('a')
                    if (details.text.strip() == 'Details'):
                        detailsLink = details['href']
                        propInfoCounter = 0
                        p = s.get(website + str(detailsLink))
                        soup = BeautifulSoup(p.text, 'html.parser')

                        for td_2 in soup.find_all('td'):
                            if (td_2.text.strip()[0:9] == 'File Date'):
                                propInfo['date'] = td_2.text.strip().split(':')[1]
                                deedList[deedCounter]["date"] = td_2.text.strip().split(':')[1]
                                print('Date: ')
                                print(propInfo['date'])
                                propInfoCounter += 1

                                while (propInfoCounter <= 8):
                                    td_2 = td_2.findNext('td')
                                    if (propInfoCounter == 2):
                                        propInfo['tax'] = td_2.text.strip().split('$')[1]
                                        print('Tax: ')
                                        print(propInfo['tax'])
                                    if (propInfoCounter == 3):
                                        propInfo['transaction'] = td_2.text.strip().split('$')[1]
                                        print('Transaction: ')
                                        print(propInfo['transaction'])
                                    if (propInfoCounter == 6):
                                        propInfo['lien'] = td_2.text.strip().split('$')[1]
                                        print('Lien: ')
                                        print(propInfo['lien'])
                                    if (propInfoCounter == 7):
                                        propInfo['mort'] = td_2.text.strip().split('$')[1]
                                        print('Mort: ')
                                        print(propInfo['mort'])
                                    propInfoCounter += 1

                            if (td_2.text.strip() == 'Grantor(s)'):
                                td_2 = td_2.findNext('td')
                                td_2 = td_2.findNext('td')
                                td_2 = td_2.findNext('td')
                                propInfo['grantors'] = td_2.text.strip()
                                deedList[deedCounter]["grantors"] = re.split(r'\s{2,}', td_2.text.strip())
                                print('Grantors: ')
                                print(propInfo['grantors'])
                                td_2 = td_2.findNext('td')
                                propInfo['grantees'] = td_2.text.strip()
                                deedList[deedCounter]["grantees"] = re.split(r'\s{2,}', td_2.text.strip())
                                print('Grantees: ')
                                print(propInfo['grantees'])
                                break
                        deedCounter += 1
        return propInfo

    def navigateToSearchPage(self, s, imageDir):
        homePage = s.get(website)
        soup = BeautifulSoup(homePage.text, 'html.parser')
        county = self.county.upper()
        countySearch = None
        for a in soup.find_all('a'):
            if (a.text.strip() == 'New Search'):
                countySearch = a['href']
                break
        if (countySearch == None): # Check if cookies have expired.
            homePage = s.post(website, data={
                "userName": "auburnTigers",
                "password": "AuburnUniv",
                "loginb": "Go"
            })
            with open('cookies', 'wb') as f:
                pickle.dump(homePage.cookies, f)
            f.close()
            with open('cookies', 'rb') as f:
                s.cookies.update(pickle.load(f))
            soup = BeautifulSoup(homePage.text, 'html.parser')
            for a in soup.find_all('a'):
                if (a.text.strip() == 'New Search'):
                    countySearch = a['href']
                    break

        p = s.get(str(countySearch))
        soup = BeautifulSoup(p.text, 'html.parser')
        countyPage = ''

        for a in soup.find_all('a'):
            if (a.text.strip() == county):
                countyPage = a['href']
                break

        p = s.get(website + str(countyPage))
        soup = BeautifulSoup(p.text, 'html.parser')
        search = ''

        for a in soup.find_all('a'):
            if (a.text.strip() == 'SEARCH ' + county):
                search = a['href']
                break

        p = s.get(website + str(search))
        soup = BeautifulSoup(p.text, 'html.parser')
        bookSearch = ''
        if (self.isDataPreExtracted == '0'): #unsure about the division of if-statement
            if (self.bookNum != None):
                p = self.searchByBookPageNum(soup, s)
            elif (self.firstName != None):
                p = self.searchByName(soup, s)
        elif (self.isDataPreExtracted == '1'):
            if (self.bookNum != None):
                p = self.searchNonIndexedDocs(soup, s)
            elif (self.firstName != None):
                p = self.searchOldIndexBooks(soup, s, imageDir)
        return p

    def GetWarrantyDeed(self, imageDir, imageFormat, p, s):
        soup = BeautifulSoup(p.text, 'html.parser')
        wd = self.locateImageURL('WD', soup, imageFormat, imageDir, s)
        details = self.locateDetails('WD', soup, s, wd)
        return wd

    def GetTrustDeed(self, imageDir, imageFormat, p, s):
        soup = BeautifulSoup(p.text, 'html.parser')
        td = self.locateImageURL('TD', soup, imageFormat, imageDir, s)
        details = self.locateDetails('TD', soup, s, td)
        return td

    def GetTrustDeedByName():
        pass

    def GetWarrantyDeedByName():
        pass

def execute(deedType, state, county, imageDir, imageFormat, isDataPreExtracted, searchArg1, searchArg2, pageOrName):
    with requests.Session() as s:
        if (not exists('cookies')): # Check if cookies file exists.
            homePage = s.post(website, data={
                "userName": "auburnTigers",
                "password": "AuburnUniv",
                "loginb": "Go"
            })
            with open('cookies', 'wb') as f:
                pickle.dump(homePage.cookies, f, protocol=0)
            f.close()

        with open('cookies', 'rb') as f:
            s.cookies.update(pickle.load(f))

        if (pageOrName == '0'):
            TitleSearch = TitleSearcher(state, county, isDataPreExtracted,
                                        bookNum=searchArg1, pageNum=searchArg2,
                                        firstName=None, lastName=None)
        else:
            TitleSearch = TitleSearcher(state, county, isDataPreExtracted,
                                        bookNum=None, pageNum=None,
                                        firstName=searchArg1, lastName=searchArg2)
        p = TitleSearch.navigateToSearchPage(s, imageDir)
        if (p == None):
            print('Property document not found. ')
        else:
            if ((deedType == 't') & (isDataPreExtracted != 1) & (pageOrName != 1)):
                deed = TitleSearch.GetTrustDeed(imageDir, imageFormat, p, s)
            elif ((deedType == 'w') & (isDataPreExtracted != 1) & (pageOrName != 1)):
                deed = TitleSearch.GetWarrantyDeed(imageDir, imageFormat, p, s)
        return deed
