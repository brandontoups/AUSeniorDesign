import requests
import urllib
import sys
import os
from bs4 import BeautifulSoup

website = "https://www.titlesearcher.com/"

class TitleSearcher:

    def __init__(self, bookNum, pageNum, state, county, isDataPreExtracted):
        self.bookNum = bookNum
        self.pageNum = pageNum
        self.state = ''
        self.county = ''
        if (state == '0'):
            self.state = 'tennessee'
        if (county == '0'):
            self.county = 'humphreys'
        self.isDataPreExtracted = isDataPreExtracted

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

    def locateImageURL(self, deedType, soupObject, imageFormat, imageDir, s):
        if (self.isDataPreExtracted == '0'):
            for br in soupObject.find_all('br'):
                for td in br.find_all('td'):
                    if (td.text.strip() == deedType):
                        imgDetails = td.find_previous_sibling('td')
                        imgName = imgDetails.find_all('span')
                        imgName = imgName[1].text.strip()
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
                            urllib.urlretrieve(urlTotal, imageDir + os.sep + imgName + '.pdf')

        elif (self.isDataPreExtracted == '1'):
            for td in soupObject.find_all('td'):
                for iframe in td.find_all('iframe'):
                    newWindow = iframe['src']
                    break

            urlTotal = website + newWindow
            p = s.get(urlTotal)
            soup = BeautifulSoup(p.text, 'html.parser')

            for frame in soup.find_all('frame'):
                urlBody = frame['src']
                with open(imageDir + '/' + deedType + self.bookNum + '-' +  self.pageNum, 'wb') as handle:
                    response = s.get(website + urlBody, stream=True)
                    if not response.ok:
                        print response
                    for block in response.iter_content(1024):
                        if not block:
                            break
                        handle.write(block)
                break

    def locateDetails(deedType, soupObject, s): #TODO: Modify to include cross references
        for br in soupObject.find_all('br'):
            for td in br.find_all('td'):
                if (td.text.strip() == deedType):
                    propInfo = []
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
                                #print(propInfo['date'])
                                propInfoCounter += 1

                                while (propInfoCounter <= 8):
                                    td_2 = td_2.findNext('td')
                                    if (propInfoCounter == 2):
                                        propInfo['tax'] = td_2.text.strip().split('$')[1]
                                        #print(propInfo['tax'])
                                    if (propInfoCounter == 3):
                                        propInfo['transaction'] = td_2.text.strip().split('$')[1]
                                        #print(propInfo['transaction'])
                                    if (propInfoCounter == 6):
                                        propInfo['lien'] = td_2.text.strip().split('$')[1]
                                        #print(propInfo['lien'])
                                    if (propInfoCounter == 7):
                                        propInfo['mort'] = td_2.text.strip().split('$')[1]
                                        #print(propInfo['mort'])
                                    propInfoCounter += 1

                            if (td_2.text.strip() == 'Grantor(s)'):
                                td_2 = td_2.findNext('td')
                                td_2 = td_2.findNext('td')
                                td_2 = td_2.findNext('td')
                                propInfo['grantors'] = td_2.text.strip()
                                #print(propInfo['grantors'])
                                td_2 = td_2.findNext('td')
                                propInfo['grantees'] = td_2.text.strip()
                                #print(propInfo['grantees'])
                                break

    def navigateToSearchPage(self, username, password, s):
        homePage = s.post(website, data={
            "userName": username,
            "password": password,
            "loginb": "Go"
        })
        soup = BeautifulSoup(homePage.text, 'html.parser')
        county = self.county.upper()
        countySearch = ''

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
        if (self.isDataPreExtracted == '0'):
            p = self.searchByBookPageNum(soup, s)
        elif (self.isDataPreExtracted == '1'):
            p = self.searchNonIndexedDocs(soup, s)
        return p

    def GetWarrantyDeed(self, imageDir, imageFormat, p, s):
        soup = BeautifulSoup(p.text, 'html.parser')
        self.locateImageURL('WD', soup, imageFormat, imageDir, s)

    def GetTrustDeed(self, imageDir, imageFormat, p, s):
        soup = BeautifulSoup(p.text, 'html.parser')
        self.locateImageURL('TD', soup, imageFormat, imageDir, s)

    def GetTrustDeedByName():
        pass

    def GetWarrantyDeedByName():
        pass

def main():
    with requests.Session() as s:
        while (1):
            # Example Input: t 18 18 0 0 imageDir pdf 0
            deedArguments = raw_input("Enter the deed arguments with spaces in between each argument. ")
            deedArguments = deedArguments.split(" ")
            deedType = deedArguments[0]
            bookNum = deedArguments[1]
            pageNum = deedArguments[2]
            state = deedArguments[3]
            county = deedArguments[4]
            imageDir = deedArguments[5]
            imageFormat = deedArguments[6]
            isDataPreExtracted = deedArguments[7]
            TitleSearch = TitleSearcher(bookNum, pageNum, state, county, isDataPreExtracted)
            p = TitleSearch.navigateToSearchPage('auburnTigers', 'AuburnUniv', s)
            if (deedType == 't'):
                    TitleSearch.GetTrustDeed(imageDir, imageFormat, p, s)
            elif (deedType == 'w'):
                TitleSearch.GetWarrantyDeed(imageDir, imageFormat, p, s)

if __name__ == "__main__":
    main()
