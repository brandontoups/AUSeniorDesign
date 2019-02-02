import requests
import urllib
from selenium import webdriver
from bs4 import BeautifulSoup

website = "https://www.titlesearcher.com/"

with requests.Session() as s:

    # Prompt user for username and password.
    username = raw_input("Please enter your username for TitleSearcher.com. ")
    password = raw_input("Please enter your password for TitleSearcher.com. ")

    homePage = s.post(website, data={
        "userName": username,
        "password": password,
        "loginb": "Go"
    })

    soup = BeautifulSoup(homePage.text, 'html.parser')
    county = raw_input("Please enter the county for which you would like to " +
                        "perform a title search. ")

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

    # Prompt user for book number and page number.
    bookNumber = raw_input("Please enter the book number. ")
    pageNumber = raw_input("Please enter the page number. ")

    p = s.post(website + str(bookSearch), data={
        "book": bookNumber,
        "page": pageNumber,
        "executeSearch": "Execute Search"
    })

    # Prompt user for image format (TIFF OR JPEG).
    imageFormat = raw_input("Please enter the image format (either 'TIFF'" +
                            " or 'PDF'). ")

    # Prompt user for directory where image will be saved.
    imageDirectory = raw_input("Please enter the directory - relative to the " +
                            "current one - where the images will be saved. ")

    # Parse HTML code to find image metadata.
    soup = BeautifulSoup(p.text, 'html.parser')
    urlBody = ''
    urlTotal = ''

    i = 0
    for link in soup.find_all('a'):
        if link.has_attr('onclick'):
            onclick = link.get('onclick')
            if (onclick[0:4] == 'load'):
                onclickList = onclick.split("'")
                urlBody = onclickList[1]
                urlTotal = website + 'imgview.php?' + urlBody
                if (imageFormat == 'TIFF'):
                    urlTotal += '&imgtype=tiff'
                elif (imageFormat == 'PDF'):
                    urlTotal += '&imgtype=pdf'
                #urlTotal += '&ACCTID='
                #urlTotal += acctid
                urllib.urlretrieve(urlTotal, imageDirectory + '/image' + str(i))
                i += 1

    #https://www.titlesearcher.com/imgview.php?imgMode=GS&instNum=95016095&year=1995&PHPSESSID=v3h74j4o1lh3ep25ogg0qhg5g5&imgtype=pdf&ACCTID=258305
    #https://www.titlesearcher.com/imgview.php?imgMode=GS&instNum=95016095&year=1995&PHPSESSID=v3h74j4o1lh3ep25ogg0qhg5g5&imgtype=tiff&ACCTID=258305
