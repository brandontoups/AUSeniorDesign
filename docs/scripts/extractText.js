// This whole file is to be pasted into chrome console for extracting the 
// text into a downloadable .txt file based on the input of the pdf searched for.
// This is to limit the time needed for bulk extraction and download of files.
// Workflow:
//     titlesearch.mybluemix.net/Query.jsp
//      -- input first .pdf to extract text from
//      -- this takes you to /FindFile page
//      -- F12 console run now to download text of that file
//      -- Search for new pdf file will reload console
//          -- do not run twice in console before either reloading page or new search query.
function download(strData, strFileName, strMimeType) {
    var D = document,
        A = arguments,
        a = D.createElement("a"),
        d = A[0],
        n = A[1],
        t = A[2] || "text/plain";

    //build download link:
    a.href = "data:" + strMimeType + "charset=utf-8," + escape(strData);


    if (window.MSBlobBuilder) { // IE10
        var bb = new MSBlobBuilder();
        bb.append(strData);
        return navigator.msSaveBlob(bb, strFileName);
    } /* end if(window.MSBlobBuilder) */



    if ('download' in a) { //FF20, CH19
        a.setAttribute("download", n);
        a.innerHTML = "downloading...";
        D.body.appendChild(a);
        setTimeout(function() {
            var e = D.createEvent("MouseEvents");
            e.initMouseEvent("click", true, false, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null);
            a.dispatchEvent(e);
            D.body.removeChild(a);
        }, 66);
        return true;
    }; /* end if('download' in a) */



    //do iframe dataURL download: (older W3)
    var f = D.createElement("iframe");
    D.body.appendChild(f);
    f.src = "data:" + (A[2] ? A[2] : "application/octet-stream") + (window.btoa ? ";base64" : "") + "," + (window.btoa ? window.btoa : escape)(strData);
    setTimeout(function() {
        D.body.removeChild(f);
    }, 333);
    return true;
}

// Table on /FindFile
// |------------------------|
// |        Results         |
// |------------------------|
// | Filename | WD66-12.pdf |   
// |------------------------|
// |      | WHEREAS,Pursuant| 
// | Text | to said advertis|
// |      | ement said land |
// | -----------------------|
// | Keys | ...             |
// |------------------------|

// get table
var table = document.getElementsByTagName("table")[0];
// get second tr
var filenameTr = table.rows[1];
// get second td in tr
var filenameTd = filenameTr.cells[1];
// get text inside td
var filename = filenameTd.innerHTML;
// example text of filename: WD66-12.pdf_ with accidental space at end
filename = filename.substring(0, filename.length - 4);


// var table = document.getElementsByTagName("table")[0];
var textTr = table.rows[2];
var textTd = textTr.cells[1];
// Actual warranty deed text 
var text = textTd.innerHTML;
//console.log(filename)
//console.log(text)

download(text, filename, 'text/plain');