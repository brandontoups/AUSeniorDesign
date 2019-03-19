import requests
import pickle
import sys
import os
from bs4 import BeautifulSoup
from os.path import exists

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
            if (self.bookNum != None):
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
                                with open(imageDir + os.sep + imgName + '.pdf', 'wb') as handle:
                                    response = s.get(urlTotal, stream=True)
                                    if not response.ok:
                                        print response
                                    for block in response.iter_content(1024):
                                        if not block:
                                            break
                                        handle.write(block)
            else:
                #How to distinguish between people when searching by name without additional discerning info?
                #This implementation simply fetches all deeds on the page without selecting a person
                for td in soupObject.find_all('td'):
                    if (td.text.strip() == deedType):
                        imgDetails = td.find_previous_sibling('td')
                        imgName = imgDetails.find_all('span')
                        imgName = imgName[1].text.strip()
                        aTag = td.findNext('a')

                        while (aTag.has_attr('onclick') != True):
                            td = aTag
                            aTag = td.findNext('a')

                        onclick = aTag.get('onclick')
                        if (onclick[0:4] == 'java'):
                            onclickList = onclick.split("\"")
                            urlBody = onclickList[1]
                            urlTotal = website + 'imgview.php?' + urlBody
                            if (imageFormat.upper() == 'TIFF'):
                                urlTotal += '&imgtype=tiff'
                            elif (imageFormat.upper() == 'PDF'):
                                urlTotal += '&imgtype=pdf'
                            with open(imageDir + os.sep + imgName + '.pdf', 'wb') as handle:
                                response = s.get(urlTotal, stream=True)
                                if not response.ok:
                                    print response
                                for block in response.iter_content(1024):
                                    if not block:
                                        break
                                    handle.write(block)

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
                with open(imageDir + os.sep + deedType + self.bookNum + '-' +  self.pageNum + '.pdf', 'wb') as handle:
                    response = s.get(website + urlBody, stream=True)
                    if not response.ok:
                        print response
                    for block in response.iter_content(1024):
                        if not block:
                            break
                        handle.write(block)
                break

    def locateDetails(self, deedType, soupObject, s): #TODO: Modify to include cross references
        propInfo = {}
        for br in soupObject.find_all('br'):
            for td in br.find_all('td'):
                if (td.text.strip() == deedType):
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
                                print('Grantors: ')
                                print(propInfo['grantors'])
                                td_2 = td_2.findNext('td')
                                propInfo['grantees'] = td_2.text.strip()
                                print('Grantees: ')
                                print(propInfo['grantees'])
                                break
        return propInfo

    def navigateToSearchPage(self, s):
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
            p = self.searchNonIndexedDocs(soup, s)
        return p

    def GetWarrantyDeed(self, imageDir, imageFormat, p, s):
        soup = BeautifulSoup(p.text, 'html.parser')
        self.locateImageURL('WD', soup, imageFormat, imageDir, s)
        details = self.locateDetails('WD', soup, s)

    def GetTrustDeed(self, imageDir, imageFormat, p, s):
        soup = BeautifulSoup(p.text, 'html.parser')
        self.locateImageURL('TD', soup, imageFormat, imageDir, s)
        details = self.locateDetails('TD', soup, s)

    def GetTrustDeedByName():
        pass

    def GetWarrantyDeedByName():
        pass

def main():
    with requests.Session() as s:
        # Example Input: t 0 0 imageDir pdf 0 18 18 0
        # Example Input: w 0 0 imageDir tiff 0 john tinnell 1
        # w 0 0 imageDir pdf 0 (post 1993) 18 21 0 (search by book and page)
        # w 0 0 imageDir pdf 0 19 34 0
        # w 0 0 imageDir pdf 0 (post 1993) john tinnell 1 (search by first and last name)
        # w 0 0 imageDir pdf 1 (pre 1993) 20 22 0 (search by book and page - only option for pre 1993 non-indexed docs)
        # Old Index Book Documents are uploaded to Box
        if (not exists('cookies')): # Check if cookies file exists.
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

        deedArguments = raw_input("Enter the deed arguments with spaces in between each argument. ")
        deedArguments = deedArguments.split(" ")
        deedType = deedArguments[0]
        state = deedArguments[1]
        county = deedArguments[2]
        imageDir = deedArguments[3]
        imageFormat = deedArguments[4]
        isDataPreExtracted = deedArguments[5]
        searchArg1 = deedArguments[6]
        searchArg2 = deedArguments[7]
        pageOrName = deedArguments[8]
        if (pageOrName == '0'):
            TitleSearch = TitleSearcher(state, county, isDataPreExtracted,
                                        bookNum=searchArg1, pageNum=searchArg2,
                                        firstName=None, lastName=None)
        else:
            TitleSearch = TitleSearcher(state, county, isDataPreExtracted,
                                        bookNum=None, pageNum=None,
                                        firstName=searchArg1, lastName=searchArg2)
        p = TitleSearch.navigateToSearchPage(s)
        if (p == None):
            print('Property document not found. ')
        else:
            if (deedType == 't'):
                TitleSearch.GetTrustDeed(imageDir, imageFormat, p, s)
            elif (deedType == 'w'):
                TitleSearch.GetWarrantyDeed(imageDir, imageFormat, p, s)



if __name__ == "__main__":
    main()
