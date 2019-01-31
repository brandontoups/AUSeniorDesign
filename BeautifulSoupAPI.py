import requests
import urllib
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
        if (link.find(text=True).strip() == 'New Search'):
            countySearch = link['href']
            break

    p = s.get(str(countySearch))
    soup = BeautifulSoup(p.text, 'html.parser')
    countyPage = ''
    for link in soup.find_all('a'):
        if (link.find(text=True).strip() == county):
            countyPage = link['href']
            break

    p = s.get(website + str(countyPage))
    soup = BeautifulSoup(p.text, 'html.parser')
    search = ''
    for link in soup.find_all('a'):
        if (link.find(text=True).strip() == 'SEARCH ' + county):
            search = link['href']
            break

    p = s.get(website + str(search))
    soup = BeautifulSoup(p.text, 'html.parser')
    bookSearch = ''
    for link in soup.find_all('a'):
        if (link.find(text=True) == 'Search By Book And Page / File #'):
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

    # Parse html code to find ACCTID and image metadata.
    soup = BeautifulSoup(p.text, 'html.parser')
    imageLink = ''
    for link in soup.find_all('a', attrs = {'onclick' : True}):
        attributes_dictionary = link.attrs
        if ('onclick' in attributes_dictionary):
            print(attributes_dictionary['onclick'])
        #imageLink = link['onclick']
        #Parse onclick attribute




#"imgMode=GS&instNum=95016095&year=1995&PHPSESSID=lb52j6ogs5j0tmro7vk1ftmne5"

#"imgMode=GS&instNum=00042139&year=2000&PHPSESSID=lb52j6ogs5j0tmro7vk1ftmne5"


#"https://www.titlesearcher.com/imgview.php?imgMode=GS&instNum=00042139&year=2000&PHPSESSID=lb52j6ogs5j0tmro7vk1ftmne5&imgtype=tiff&ACCTID=258305"


#"imgMode=GS&instNum=13003358&year=2013&PHPSESSID=lb52j6ogs5j0tmro7vk1ftmne5"


#urllib.urlretrieve("https://www.titlesearcher.com/imgview.php?imgMode=GS&instNum=95016095&year=1995&PHPSESSID=n5ih6i70f66nvr85onudirouv6&imgtype=tiff&ACCTID=258305", "img.tiff")
